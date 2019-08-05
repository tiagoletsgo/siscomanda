package br.com.siscomanda.repository.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.siscomanda.enumeration.EStatus;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.model.ItemVendaOLD;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.repository.base.GenericDAO;

public class VendaDAO extends GenericDAO<Venda> implements Serializable {

	private static final long serialVersionUID = 5272635544808053392L;
	
	public List<Venda> todos() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT venda FROM Venda venda ");
		TypedQuery<Venda> query = getEntityManager().createQuery(sql.toString(), Venda.class);
		List<Venda> vendas = query.getResultList();
		return vendas;
	}
	
	@SuppressWarnings("unchecked")
	public List<Venda> buscaPor(Venda venda, boolean editando) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT venda.* FROM venda venda ");
		sql.append("INNER JOIN item_venda item ON venda.id = item.venda_id ");
		sql.append("LEFT OUTER JOIN cliente cliente ON venda.cliente_id = cliente.id ");
		sql.append("LEFT OUTER JOIN servico servico ON servico.id = cliente.servico_id ");
		sql.append("LEFT OUTER JOIN caixa caixa ON caixa.id = venda.caixa_id ");
		sql.append("WHERE 1 = 1 ");
		sql.append(editando == false ? " AND caixa.caixa_aberto OR NOT venda.pago " : "");
		sql.append(venda.getId() != null ? "AND venda.id = :venda " : "");
		sql.append(venda.getStatus() != null ? "AND venda.status = :status " : "");
		sql.append(venda.getControle() != null && venda.getControle() > 0 ? "AND venda.mesa_comanda = :mesaComanda " : "");
		sql.append(venda.getTipoVenda() != null ? "AND venda.tipo_venda = :tipoVenda " : "");
		sql.append("ORDER BY venda.id ASC ");
		Query query = getEntityManager().createNativeQuery(sql.toString(), Venda.class);
		
		if(venda.getId() != null) {			
			query.setParameter("venda", venda.getId());
		}
		
		if(venda.getStatus() != null) {
			query.setParameter("status", venda.getStatus().name());
		}
		
		if(venda.getControle() != null && venda.getControle() > 0) {
			query.setParameter("mesaComanda", venda.getControle());
		}
		
		if(venda.getTipoVenda() != null) {
			query.setParameter("tipoVenda", venda.getTipoVenda().name());
		}
		
		List<Venda> vendas  = query.getResultList();
		return vendas;
	}
	
//	@SuppressWarnings("unchecked")
//	public List<VendaOLD> vendasNaoPagasDiaCorrente() {
//		StringBuilder sql = new StringBuilder();
//		sql.append("SELECT v.* FROM venda v WHERE (CAST (v.data_iniciado AS DATE)) = current_date AND v.pago = false AND NOT v.status = 'CANCELADO' ");
//		Query query = getEntityManager().createNativeQuery(sql.toString(), VendaOLD.class);
//		List<VendaOLD> vendas  = query.getResultList();
//		return vendas;
//	}
	
//	@Deprecated
//	@SuppressWarnings("unchecked")
//	public List<VendaOLD> vendasNaoPagas() {
//		StringBuilder sql = new StringBuilder();
//		sql.append("SELECT v.* FROM venda v WHERE v.pago = false AND NOT v.status = 'CANCELADO' ");
//		Query query = getEntityManager().createNativeQuery(sql.toString(), VendaOLD.class);
//		List<VendaOLD> vendas  = query.getResultList();
//		return vendas;
//	}
	
	public List<Venda> buscarPor(Map<String, Object> filter) {
		Venda venda = (Venda) filter.get("venda");
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT venda FROM Venda venda ");
		sql.append("INNER JOIN FETCH venda.itens itens ");
		sql.append("LEFT JOIN FETCH venda.cliente cliente ");
		sql.append("LEFT JOIN FETCH cliente.servico servico ");
		sql.append("LEFT JOIN FETCH venda.caixa caixa ");
		sql.append("WHERE 1 = 1");
		sql.append("ORDER BY venda.id ASC ");
		TypedQuery<Venda> query = getEntityManager().createQuery(sql.toString(), Venda.class);
		List<Venda> vendas = query.getResultList();
		return vendas;
	}
	
	public List<Venda> naoPagaDiaCorrente() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT venda FROM Venda venda ");
		sql.append("WHERE venda.pago = :pago ");
		sql.append("AND NOT venda.status = :status ");
		sql.append("AND venda.dataVenda = :data ");
		
		TypedQuery<Venda> query = getEntityManager().createQuery(sql.toString(), Venda.class);
		query.setParameter("pago", false);
		query.setParameter("status", EStatus.CANCELADO);
		query.setParameter("data", new Date());
		List<Venda> vendas = query.getResultList();
		return vendas;
	}
	
	public List<Venda> naoPagas() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT venda FROM Venda venda ");
		sql.append("WHERE venda.pago = :pago AND NOT venda.status = :status ");
		sql.append("ORDER BY venda.id ASC ");
		
		TypedQuery<Venda> query = getEntityManager().createQuery(sql.toString(), Venda.class);
		query.setParameter("pago", false);
		query.setParameter("status", EStatus.CANCELADO);
		List<Venda> vendas = query.getResultList();
		return vendas;
	}
	
	public Venda porCodigo(Long codigo) throws SiscomandaException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT venda FROM Venda venda ");
			sql.append("INNER JOIN FETCH venda.itens itens ");
			sql.append("INNER JOIN FETCH venda.operador operador ");
			sql.append("INNER JOIN FETCH itens.tamanho tamanho ");
			sql.append("INNER JOIN FETCH itens.produto produto ");
			sql.append("WHERE venda.id = :codigo ");
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
	public List<Adicional> buscaAdicionalVenda(ItemVendaOLD item) {
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