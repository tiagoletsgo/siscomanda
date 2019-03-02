package br.com.siscomanda.repository.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.TypedQuery;

import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.repository.base.GenericDAO;

public class VendaMesaComandaDAO extends GenericDAO<Venda> implements Serializable {

	private static final long serialVersionUID = 5272635544808053392L;
	
	public Venda porCodigo(Long codigo) throws SiscomandaException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("FROM Venda venda JOIN FETCH venda.itens WHERE venda.id = :codigo ");
			TypedQuery<Venda> query = getEntityManager().createQuery(sql.toString(), Venda.class);
			query.setParameter("codigo", codigo);
			Venda venda  = query.getSingleResult();
			return venda;
		}
		catch(Exception e) {
			throw new SiscomandaException(e.getMessage());
		}
	}
	
	public List<ItemVenda> itensDaVenda(Venda venda) {
		StringBuilder sql = new StringBuilder();
		sql.append("FROM ItemVenda item JOIN FETCH item.produto WHERE item.venda = :venda ");
		TypedQuery<ItemVenda> query = getEntityManager().createQuery(sql.toString(), ItemVenda.class);
		query.setParameter("venda", venda);
		
		List<ItemVenda> itens = query.getResultList();
		return itens;
	}
	
	public void removeItemVenda(Venda venda) {
		
	}
}
