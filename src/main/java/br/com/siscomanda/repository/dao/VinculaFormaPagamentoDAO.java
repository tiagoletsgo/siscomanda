package br.com.siscomanda.repository.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import br.com.siscomanda.model.FormaPagamento;
import br.com.siscomanda.model.VinculaFormaPagamento;
import br.com.siscomanda.repository.base.GenericDAO;

public class VinculaFormaPagamentoDAO extends GenericDAO<VinculaFormaPagamento> implements Serializable {
	
	private static final long serialVersionUID = 8641733346691230065L;

	public List<VinculaFormaPagamento> porFormaPagamento(FormaPagamento formaPagamento) {
		
		if(formaPagamento == null) {
			return new ArrayList<>();
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("FROM VinculaFormaPagamento vinculaFormaPagamento ");
		sql.append("LEFT JOIN FETCH vinculaFormaPagamento.bandeira ");
		sql.append("LEFT JOIN FETCH vinculaFormaPagamento.formaPagamento ");
		sql.append("WHERE vinculaFormaPagamento.formaPagamento = :formaPagamento ");
		
		TypedQuery<VinculaFormaPagamento> query = getEntityManager().createQuery(sql.toString(), VinculaFormaPagamento.class);
		query.setParameter("formaPagamento", formaPagamento);
		
		List<VinculaFormaPagamento> vinculaFormasPagamento = query.getResultList();
		
		return vinculaFormasPagamento;
	}
}
