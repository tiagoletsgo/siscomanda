package br.com.siscomanda.repository.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.TypedQuery;

import br.com.siscomanda.model.Preco;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.Tamanho;
import br.com.siscomanda.repository.base.GenericDAO;

public class PrecoDAO extends GenericDAO<Preco> implements Serializable {

	private static final long serialVersionUID = -1326620506933540046L;

	public List<Preco> porProduto(Produto produto) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT preco FROM Preco preco ");
		sql.append("INNER JOIN FETCH preco.produto produto ");
		sql.append("INNER JOIN FETCH preco.tamanho tamanho ");
		sql.append("WHERE produto = :produto AND preco.disponibilizaParaVenda = :disponibilizaParaVenda ");
		sql.append("ORDER BY preco.precoVenda ASC ");
		
		TypedQuery<Preco> query = getEntityManager().createQuery(sql.toString(), Preco.class);
		query.setParameter("produto", produto);
		query.setParameter("disponibilizaParaVenda", true);
		
		List<Preco> precos = query.getResultList();
		return precos;
	}
	
	public List<Preco> porTamanhoProduto(List<Produto> produtos, Tamanho tamanho) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT preco FROM Preco preco ");
		sql.append("INNER JOIN FETCH preco.produto produto ");
		sql.append("INNER JOIN FETCH preco.tamanho tamanho ");
		sql.append("WHERE produto IN (:produto) AND preco.disponibilizaParaVenda = :disponibilizaParaVenda ");
		sql.append("AND preco.tamanho = :tamanho ");
		sql.append("ORDER BY preco.precoVenda ASC ");
		
		TypedQuery<Preco> query = getEntityManager().createQuery(sql.toString(), Preco.class);
		query.setParameter("produto", produtos);
		query.setParameter("tamanho", tamanho);
		query.setParameter("disponibilizaParaVenda", true);
		
		List<Preco> precos = query.getResultList();
		return precos;
	}
}
