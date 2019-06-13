package br.com.siscomanda.repository.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.TypedQuery;

import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Categoria;
import br.com.siscomanda.model.SubCategoria;
import br.com.siscomanda.repository.base.GenericDAO;

public class SubCategoriaDAO extends GenericDAO<SubCategoria> implements Serializable {
	
	private static final long serialVersionUID = -846964487147811788L;

	public boolean isExists(SubCategoria subCategoria) {
		StringBuilder sql = new StringBuilder();
		sql.append("FROM SubCategoria subcategoria WHERE subcategoria.descricao = :descricao");
		TypedQuery<SubCategoria> query = getEntityManager().createQuery(sql.toString(), SubCategoria.class);
		query.setParameter("descricao", subCategoria.getDescricao().toUpperCase());
		
		List<SubCategoria> categorias = query.getResultList();
		
		if(categorias.size() > 0) {
			return true;
		}
		
		return false;
	}
	
	public List<SubCategoria> porDescricao(String descricao) throws SiscomandaException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("FROM SubCategoria subcategoria WHERE subcategoria.descricao LIKE :descricao");
			TypedQuery<SubCategoria> query = getEntityManager().createQuery(sql.toString(), SubCategoria.class);
			query.setParameter("descricao", "%"+descricao.toUpperCase().trim()+"%");
			return query.getResultList();
		}
		catch(Exception e) {
			throw new SiscomandaException("Nenhum registro encontrado. " + e.getMessage());
		}
	}
	
	public List<SubCategoria> porCategoria(Categoria categoria) throws SiscomandaException {
		try {
			StringBuilder sql = new StringBuilder(); 
			sql.append("SELECT sc FROM SubCategoria sc ");
			sql.append("INNER JOIN FETCH sc.categoria c ");
			sql.append("WHERE c.id = :categoria ");
			
			TypedQuery<SubCategoria> query = getEntityManager().createQuery(sql.toString(), SubCategoria.class);
			query.setParameter("categoria", categoria.getId());
			
			List<SubCategoria> subCategorias = query.getResultList();
			return subCategorias; 
		}
		catch(Exception e) {
			throw new SiscomandaException("Nenhum registro encontrado. " + e.getMessage());
		}
	}
}
