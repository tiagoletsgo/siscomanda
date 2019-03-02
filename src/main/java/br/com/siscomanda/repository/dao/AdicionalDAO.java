package br.com.siscomanda.repository.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.model.Categoria;
import br.com.siscomanda.repository.base.GenericDAO;

public class AdicionalDAO extends GenericDAO<Adicional> implements Serializable {

	private static final long serialVersionUID = 6275901924828170817L;
	
	@SuppressWarnings("unchecked")
	public List<Adicional> buscaPor(String filter) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT adicional.* FROM adicional adicional ");
		sql.append("INNER JOIN adicional_categoria ad_categoria ON ad_categoria.adicional_id = adicional.id ");
		sql.append("LEFT OUTER JOIN categoria categoria ON categoria.id = ad_categoria.categoria_id ");
		sql.append(filter != null ? "WHERE 1 = 1 AND adicional.descricao LIKE :descricao " : "");
		Query query = getEntityManager().createNativeQuery(sql.toString(), Adicional.class);
		
		if(filter != null) {
			query.setParameter("descricao", "%" + filter.toUpperCase() + "%");
		}
		
		List<Adicional> adicionais = new ArrayList<>();
		for(Adicional adicional : (List<Adicional>)query.getResultList()) {
			if(!adicionais.contains(adicional)) {
				adicional.setDescricaoCategoria(join(adicional.getCategorias(), ", "));
				adicional.setCategorias(porDescricao(adicional.getDescricao()));
				adicionais.add(adicional);
			}
		}
		
		return adicionais;
	}
	
	public void removeAdicionalPorCodigo(Long codigo) throws SiscomandaException {
		try {			
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM adicional adicional WHERE adicional.id = :codigo ");
			Query query = getEntityManager().createNativeQuery(sql.toString());
			query.setParameter("codigo", codigo);
			query.executeUpdate();
		}
		catch(Exception e) {
			throw new SiscomandaException("Erro ao remover adicional. " + e.getMessage());
		}
	}
	
	public void removeAdicionalCategoriaPorCodigo(Long codigo) throws SiscomandaException {
		try {			
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM adicional_categoria ad_categoria WHERE ad_categoria.adicional_id = :codigo ");
			Query query = getEntityManager().createNativeQuery(sql.toString());
			query.setParameter("codigo", codigo);
			query.executeUpdate();
		}
		catch(Exception e) {
			throw new SiscomandaException("Erro ao remover adicional categoria. " + e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Categoria> porDescricao(String descricao) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT categoria.* FROM categoria categoria ");
		sql.append("INNER JOIN adicional_categoria ad_categoria ON ad_categoria.categoria_id = categoria.id ");
		sql.append("INNER JOIN adicional adicional ON adicional.id = ad_categoria.adicional_id ");
		sql.append("WHERE adicional.descricao LIKE :descricao ");
		
		Query query = getEntityManager().createNativeQuery(sql.toString(), Categoria.class);
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		List<Categoria> categorias = query.getResultList();
		return categorias;
	}
	
	private String join(List<Categoria> categorias, String delimiter) {
		List<String> descricoes = new ArrayList<>();
		for(Categoria categoria : categorias) {
			descricoes.add(categoria.getDescricao());
		}
		return String.join(delimiter, descricoes);
	}
}
