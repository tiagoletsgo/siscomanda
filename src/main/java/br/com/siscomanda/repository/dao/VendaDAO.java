package br.com.siscomanda.repository.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.siscomanda.enumeration.EStatus;
import br.com.siscomanda.exception.SiscomandaException;
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
	
	public List<Venda> porData(Date dataInicial, Date dataFinal) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT venda FROM Venda venda ");
		sql.append("WHERE 1 = 1 ");
		sql.append("AND venda.dataVenda BETWEEN :dataInicial AND :dataFinal ");
		
		TypedQuery<Venda> query = getEntityManager().createQuery(sql.toString(), Venda.class);
		query.setParameter("dataInicial", dataInicial);
		query.setParameter("dataFinal", dataFinal);
		
		List<Venda> vendas = query.getResultList();
		return vendas;
	}
	
	public List<Venda> buscarPor(Map<String, Object> filter) {
		Venda venda = (Venda) filter.get("venda");
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT venda FROM Venda venda ");
		sql.append("INNER JOIN FETCH venda.itens itens ");
		sql.append("LEFT JOIN FETCH venda.cliente cliente ");
		sql.append("LEFT JOIN FETCH cliente.servico servico ");
		sql.append("LEFT JOIN FETCH venda.caixa caixa ");
		sql.append("LEFT JOIN FETCH venda.operador operador ");
		sql.append("WHERE 1 = 1 ");
		sql.append(Objects.nonNull(venda.getId()) ? "AND venda.id = :numeroVenda " : "");
		sql.append(Objects.nonNull(venda.getStatus()) ? "AND venda.status = :status " : "");
		sql.append(Objects.nonNull(venda.getControle()) ? "AND venda.controle = :controle " : "");
		sql.append(Objects.nonNull(venda.getTipoVenda()) ? "AND venda.tipoVenda = :tipoVenda " : "");
		sql.append("ORDER BY venda.id ASC ");
		TypedQuery<Venda> query = getEntityManager().createQuery(sql.toString(), Venda.class);
		
		if(Objects.nonNull(venda.getId())) {
			query.setParameter("numeroVenda", venda.getId());
		}
		
		if(Objects.nonNull(venda.getStatus())) {
			query.setParameter("status", venda.getStatus());
		}
		
		if(Objects.nonNull(venda.getControle())) {
			query.setParameter("controle", venda.getControle());
		}
		
		if(Objects.nonNull(venda.getTipoVenda())) {
			query.setParameter("tipoVenda", venda.getTipoVenda());
		}
		
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
			sql.append("SELECT venda FROM Venda venda  ");
			sql.append("INNER JOIN FETCH venda.itens item ");
			sql.append("INNER JOIN FETCH venda.operador operador ");
			sql.append("INNER JOIN FETCH item.tamanho tamanho ");
			sql.append("INNER JOIN FETCH item.produto produto ");
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
}
