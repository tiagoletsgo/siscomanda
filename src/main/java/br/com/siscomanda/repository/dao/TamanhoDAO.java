package br.com.siscomanda.repository.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.TypedQuery;

import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.SubCategoria;
import br.com.siscomanda.model.Tamanho;
import br.com.siscomanda.repository.base.GenericDAO;

public class TamanhoDAO extends GenericDAO<Tamanho> implements Serializable {

	private static final long serialVersionUID = 4442893572100855041L;
	
	public List<Tamanho> porSubCategoria(SubCategoria subCategoria) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT tamanho FROM Tamanho tamanho ");
		sql.append("LEFT JOIN FETCH tamanho.subCategoria sub ");
		sql.append("WHERE tamanho.subCategoria = :subcategoria ");
		
		TypedQuery<Tamanho> query = getEntityManager().createQuery(sql.toString(), Tamanho.class);
		query.setParameter("subcategoria", subCategoria);
		
		List<Tamanho> tamanhos = query.getResultList();
		return tamanhos;
	}
	
	public Tamanho porTamanho(Tamanho tamanho) throws SiscomandaException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT tamanho FROM Tamanho tamanho ");
			sql.append("LEFT JOIN FETCH tamanho.subCategoria subcategoria ");
			sql.append("WHERE tamanho = :tamanho ");
			
			TypedQuery<Tamanho> query = getEntityManager().createQuery(sql.toString(), Tamanho.class);
			query.setParameter("tamanho", tamanho);
			tamanho = query.getSingleResult();
			
			return tamanho;
		}
		catch(Exception e) {
			throw new SiscomandaException("Nenhum registro encontrado. " + e.getMessage());
		}
	}
}
