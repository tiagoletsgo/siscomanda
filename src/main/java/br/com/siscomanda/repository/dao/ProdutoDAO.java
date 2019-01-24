package br.com.siscomanda.repository.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.SubCategoria;
import br.com.siscomanda.repository.base.GenericDAO;
import br.com.siscomanda.util.StringUtil;

public class ProdutoDAO extends GenericDAO<Produto> {
	
	public Produto porCodigo(Produto produto) throws SiscomandaException {
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
			throw new SiscomandaException("Não encontrou nenhum resultado na pesquisa.");
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
	
	public List<Produto> buscaPorSubCategoria(String descricaoProduto) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT produto Produto produto ");
		sql.append("INNER JOIN FETCH produto.subCategoria subCategoria ");
		sql.append("INNER JOIN FETCH subCategoria.categoria categoria ");
		sql.append("WHERE 1 = 1 ");
		sql.append("  AND produto.descricao LIKE :descricao ");
		sql.append("  AND produto.codigoEan LIKE :codigoEan ");
		sql.append("  AND subCategoria = :subcategoria ");
		
		TypedQuery<Produto> query = getEntityManager().createQuery(sql.toString(), Produto.class);
		query.setParameter("descricao", "%" + descricaoProduto + "%");
		query.setParameter("codigoEan", "%" + descricaoProduto + "%");
		query.setParameter("subcategoria", new SubCategoria(5L));
		
		List<Produto> produtos = query.getResultList();
		return produtos;
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
