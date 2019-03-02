package br.com.siscomanda.repository.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.ItemVendaAdicional;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.repository.base.GenericDAO;

public class ItemVendaAdicionalDAO extends GenericDAO<ItemVendaAdicional> implements Serializable {

	private static final long serialVersionUID = 8977258210759414153L;
	
	public List<ItemVendaAdicional> porVenda(Venda venda) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT itemAdicional FROM ItemVendaAdicional itemAdicional  ");
		sql.append("JOIN FETCH itemAdicional.produto ");
		sql.append("JOIN FETCH itemAdicional.adicional ");
		sql.append("JOIN FETCH itemAdicional.venda ");
		sql.append("JOIN FETCH itemAdicional.itemVenda ");
		sql.append("WHERE itemAdicional.venda = :venda ");
		
		TypedQuery<ItemVendaAdicional> query = getEntityManager().createQuery(sql.toString(), ItemVendaAdicional.class);
		query.setParameter("venda", venda);
		List<ItemVendaAdicional> itens = query.getResultList();
		return itens;
	}
	
	public void remove(Venda venda) throws SiscomandaException {
		try {			
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM item_venda_adicional itemAdicional WHERE itemAdicional.venda_id = :venda ");
			Query query = getEntityManager().createNativeQuery(sql.toString());
			query.setParameter("venda", venda.getId());
			query.executeUpdate();
		}
		catch(Exception e) {
			throw new SiscomandaException("Erro ao remover item da venda. " + e.getMessage());
		}
	}
}
