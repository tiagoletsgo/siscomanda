package br.com.napule.repository.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.napule.exception.NapuleException;
import br.com.napule.model.Produto;
import br.com.napule.repository.base.GenericDAO;
import br.com.napule.util.StringUtil;

public class ProdutoDAO extends GenericDAO<Produto> {
	
	public Produto porCodigo(Produto produto) throws NapuleException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("FROM Produto produto ");
			sql.append("LEFT JOIN FETCH produto.categoria ");
			sql.append("LEFT JOIN FETCH produto.embalagem ");
			sql.append("LEFT JOIN FETCH produto.subCategoria ");
			sql.append("LEFT JOIN FETCH produto.fornecedor ");
			sql.append("WHERE produto = :produto ");
			
			TypedQuery<Produto> query = getEntityManager().createQuery(sql.toString(), Produto.class);
			query.setParameter("produto", produto);
			
			produto = query.getSingleResult();
			return produto;
			
		}
		catch(NoResultException e) {
			throw new NapuleException("Não encontrou nenhum resultado na pesquisa.");
		}
	}
	
	public boolean isExists(Produto produto) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("FROM Produto produto WHERE produto.codigoEan = :codigoEan ");
			TypedQuery<Produto> query = getEntityManager().createQuery(sql.toString(), Produto.class);
			query.setParameter("codigoEan", produto.getCodigoEan());
			
			produto = query.getSingleResult();
			if(!produto.isNovo()) {
				return true;
			}
			
			return false;
		}
		catch(NoResultException e) {
			return false;
		}
	}
	
	public List<Produto> buscaPor(Produto produto) {
		StringBuilder sql = new StringBuilder();
		sql.append("FROM Produto produto WHERE 1 = 1 ");
		sql.append(StringUtil.isNotEmpty(produto.getCodigoEan()) ? " AND produto.codigoEan = :codigoEan " : "");
		sql.append(StringUtil.isNotEmpty(produto.getDescricao()) ? " AND produto.descricao LIKE :descricao " : "");
		TypedQuery<Produto> query = getEntityManager().createQuery(sql.toString(), Produto.class);
		
		if(StringUtil.isNotEmpty(produto.getCodigoEan())) {
			query.setParameter("codigoEan", produto.getCodigoEan().toUpperCase().trim());
		}
		
		if(StringUtil.isNotEmpty(produto.getDescricao())) {
			query.setParameter("descricao", "%"+produto.getDescricao().toUpperCase().trim()+"%");
		}
		
		List<Produto> produtos = query.getResultList();
		return produtos;
		
	}
}
