package br.com.napule.repository.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import br.com.napule.model.FormaPagamento;
import br.com.napule.model.VinculaFormaPagamento;
import br.com.napule.repository.base.GenericDAO;

public class VinculaFormaPagamentoDAO extends GenericDAO<VinculaFormaPagamento> {
	
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
