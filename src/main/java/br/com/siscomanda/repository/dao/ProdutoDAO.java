package br.com.siscomanda.repository.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.SubCategoria;
import br.com.siscomanda.repository.base.GenericDAO;
import br.com.siscomanda.util.StringUtil;

public class ProdutoDAO extends GenericDAO<Produto> implements Serializable {

	private static final long serialVersionUID = 5453891974123756212L;
	
	@SuppressWarnings("unchecked")
	public List<Produto> porDescricaoSubCategoria(String descricao) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT produto.* FROM produto produto WHERE produto.subcategoria_id = ");
		sql.append("(SELECT subcategoria.id FROM subcategoria subcategoria WHERE subcategoria.descricao LIKE :descricao) ");
		sql.append("AND produto.permite_meio_a_meio");
		
		Query query = getEntityManager().createNativeQuery(sql.toString(), Produto.class);
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return query.getResultList();
	}
	
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
	
	public List<Produto> buscaPorSubCategoria(String descricaoProduto, SubCategoria subCategoria) throws SiscomandaException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT produto FROM Produto produto ");
		sql.append("INNER JOIN FETCH produto.subCategoria subCategoria ");
		sql.append("INNER JOIN FETCH subCategoria.categoria categoria ");
		sql.append("WHERE 1 = 1 ");
		sql.append(descricaoProduto != null ? "  AND produto.descricao LIKE :descricao " : "");
		sql.append("  AND subCategoria = :subcategoria ");
		sql.append("  AND produto.permiteMeioAmeio = :permiteMeioAmeio ");
		sql.append(descricaoProduto != null ? "   OR produto.codigoEan LIKE :codigoEan " : "");
		sql.append("  AND subCategoria = :subcategoria ");
		sql.append("  AND produto.permiteMeioAmeio = :permiteMeioAmeio ");
		
		TypedQuery<Produto> query = getEntityManager().createQuery(sql.toString(), Produto.class);
		if(descricaoProduto != null) {
			query.setParameter("descricao", "%" + descricaoProduto.toUpperCase() + "%");
			query.setParameter("codigoEan", "%" + descricaoProduto.toUpperCase() + "%");
		}
		
		query.setParameter("subcategoria", subCategoria);
		query.setParameter("permiteMeioAmeio", true);
		List<Produto> produtos = query.getResultList();
		
		if(produtos.isEmpty() || produtos == null) {
			throw new SiscomandaException("Busca não retornou nenhum resultado.");
		}
		
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
