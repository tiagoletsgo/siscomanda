package br.com.siscomanda.repository.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.TypedQuery;

import br.com.siscomanda.model.Tamanho;
import br.com.siscomanda.model.Tipo;
import br.com.siscomanda.repository.base.GenericDAO;

public class TamanhoDAO extends GenericDAO<Tamanho> implements Serializable {

	private static final long serialVersionUID = 4442893572100855041L;
	
	public List<Tamanho> tamanhoPorTipo(Tipo tipo) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT tamanho FROM Tamanho tamanho ");
		sql.append("LEFT JOIN FETCH tamanho.tipo tipo ");
		sql.append("WHERE tamanho.tipo = :tipo ");
		
		TypedQuery<Tamanho> query = getEntityManager().createQuery(sql.toString(), Tamanho.class);
		query.setParameter("tipo", tipo);
		
		List<Tamanho> tamanhos = query.getResultList();
		return tamanhos;
	}
}
