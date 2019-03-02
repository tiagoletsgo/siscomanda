package br.com.siscomanda.repository.dao;

import java.io.Serializable;

import javax.persistence.Query;

import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.repository.base.GenericDAO;

public class ItemVendaDAO extends GenericDAO<ItemVenda> implements Serializable {

	private static final long serialVersionUID = -1787959695395248516L;
	
	public void remover(Venda venda) throws SiscomandaException {
		try {			
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM item_venda item WHERE item.venda_id = :venda ");
			Query query = getEntityManager().createNativeQuery(sql.toString());
			query.setParameter("venda", venda.getId());
			query.executeUpdate();
		}
		catch(Exception e) {
			throw new SiscomandaException("Erro ao remover itens da venda. " + e.getMessage());
		}
	}
}
