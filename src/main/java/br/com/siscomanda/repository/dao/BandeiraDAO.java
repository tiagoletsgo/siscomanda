package br.com.siscomanda.repository.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.TypedQuery;

import br.com.siscomanda.model.Bandeira;
import br.com.siscomanda.model.FormaPagamento;
import br.com.siscomanda.repository.base.GenericDAO;

public class BandeiraDAO extends GenericDAO<Bandeira> implements Serializable {

	private static final long serialVersionUID = -379371030149574454L;
	
	public List<Bandeira> porFormaPagamento(FormaPagamento formaPagamento, Boolean vincula) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT bandeira FROM VinculaFormaPagamento vinculaFormaPagamento ");
		sql.append("LEFT JOIN vinculaFormaPagamento.bandeira bandeira ");
		sql.append("LEFT JOIN vinculaFormaPagamento.formaPagamento formaPagamento ");
		sql.append("WHERE formaPagamento.descricao = :formaPagamento AND vinculaFormaPagamento.vincular = :vincula ");
		
		TypedQuery<Bandeira> query = getEntityManager().createQuery(sql.toString(), Bandeira.class);
		query.setParameter("formaPagamento", formaPagamento.getDescricao());
		query.setParameter("vincula", vincula);
		
		List<Bandeira> bandeiras = query.getResultList();
		return bandeiras;
	}
}
