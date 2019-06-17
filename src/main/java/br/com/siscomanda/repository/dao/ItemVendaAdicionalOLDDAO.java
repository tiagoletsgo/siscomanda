package br.com.siscomanda.repository.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.ItemVendaAdicionalOLD;
import br.com.siscomanda.model.VendaOLD;
import br.com.siscomanda.repository.base.GenericDAO;

public class ItemVendaAdicionalOLDDAO extends GenericDAO<ItemVendaAdicionalOLD> implements Serializable {

	private static final long serialVersionUID = 8977258210759414153L;
	
	public List<ItemVendaAdicionalOLD> porVenda(VendaOLD venda) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT itemAdicional FROM ItemVendaAdicional itemAdicional  ");
		sql.append("JOIN FETCH itemAdicional.produto ");
		sql.append("JOIN FETCH itemAdicional.adicional ");
		sql.append("JOIN FETCH itemAdicional.venda ");
		sql.append("JOIN FETCH itemAdicional.itemVenda ");
		sql.append("WHERE itemAdicional.venda = :venda ");
		
		TypedQuery<ItemVendaAdicionalOLD> query = getEntityManager().createQuery(sql.toString(), ItemVendaAdicionalOLD.class);
		query.setParameter("venda", venda);
		List<ItemVendaAdicionalOLD> itens = query.getResultList();
		return itens;
	}
	
	public void remove(VendaOLD venda) throws SiscomandaException {
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
