package br.com.siscomanda.repository.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import br.com.siscomanda.exception.NapuleException;
import br.com.siscomanda.model.SubCategoria;
import br.com.siscomanda.repository.base.GenericDAO;

public class SubCategoriaDAO extends GenericDAO<SubCategoria> {
	
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
	
	public List<SubCategoria> porDescricao(String descricao) throws NapuleException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("FROM SubCategoria subcategoria WHERE subcategoria.descricao LIKE :descricao");
			TypedQuery<SubCategoria> query = getEntityManager().createQuery(sql.toString(), SubCategoria.class);
			query.setParameter("descricao", "%"+descricao.toUpperCase().trim()+"%");
			return query.getResultList();
		}
		catch(Exception e) {
			throw new NapuleException("Nenhum registro encontrado. " + e.getMessage());
		}
	}
}
