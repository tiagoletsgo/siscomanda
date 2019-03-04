package br.com.siscomanda.repository.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.repository.base.GenericDAO;

public class VendaMesaComandaDAO extends GenericDAO<Venda> implements Serializable {

	private static final long serialVersionUID = 5272635544808053392L;
	
	public List<Venda> buscaPor(Venda venda) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT venda FROM Venda venda JOIN FETCH venda.itens WHERE 1 = 1 ");
		sql.append(venda.getId() != null ? "AND venda = :venda " : "");
		sql.append(venda.getStatus() != null ? "AND venda.status = :status " : "");
		sql.append(venda.getSistema() != null && venda.getSistema() > 0 ? "AND venda.sistema = :sistema" : "");
		TypedQuery<Venda> query = getEntityManager().createQuery(sql.toString(), Venda.class);
		
		if(venda.getId() != null) {			
			query.setParameter("venda", venda);
		}
		
		if(venda.getStatus() != null) {
			query.setParameter("status", venda.getStatus());
		}
		
		if(venda.getSistema() != null && venda.getSistema() > 0) {
			query.setParameter("sistema", venda.getSistema());
		}
		
		List<Venda> vendas  = query.getResultList();
		return vendas;
	}
	
	@SuppressWarnings("unchecked")
	public List<Venda> vendasNaoPagasDiaCorrente() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT v.* FROM venda v WHERE (CAST (v.data_iniciado AS DATE)) = current_date AND v.pago = false");
		Query query = getEntityManager().createNativeQuery(sql.toString(), Venda.class);
		List<Venda> vendas  = query.getResultList();
		return vendas;
	}
	
	public Venda porCodigo(Long codigo) throws SiscomandaException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("FROM DISTINCT Venda venda JOIN FETCH venda.itens WHERE venda.id = :codigo ");
			TypedQuery<Venda> query = getEntityManager().createQuery(sql.toString(), Venda.class);
			query.setParameter("codigo", codigo);
			Venda venda  = query.getSingleResult();
			return venda;
		}
		catch(Exception e) {
			throw new SiscomandaException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Adicional> buscaAdicionalVenda(ItemVenda item) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT adicional.* FROM adicional adicional ");
		sql.append("INNER JOIN item_venda_adicional iva ON iva.adicional_id = adicional.id ");
		sql.append("INNER JOIN venda venda ON venda.id = iva.venda_id ");
		sql.append("WHERE venda.id = :venda AND iva.item_venda_id = :item ");
		Query query = getEntityManager().createNativeQuery(sql.toString(), Adicional.class);
		query.setParameter("venda", item.getVenda().getId());
		query.setParameter("item", item.getId());
		
		List<Adicional> adicionais = query.getResultList();
		return adicionais;
	}
}
