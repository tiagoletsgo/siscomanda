package br.com.siscomanda.repository.dao;

import java.io.Serializable;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.siscomanda.model.Embalagem;
import br.com.siscomanda.repository.base.GenericDAO;

public class EmbalagemDAO extends GenericDAO<Embalagem> implements Serializable {
	
	private static final long serialVersionUID = 8796936221641740958L;

	public boolean isExists(Embalagem embalagem) {
		try {			
			StringBuilder sql = new StringBuilder();
			sql.append("FROM Embalagem embalagem WHERE embalagem.descricao = :descricao ");
			sql.append("  AND embalagem.sigla = :sigla ");
			sql.append("  AND embalagem.quantidade = :quantidade ");
			
			TypedQuery<Embalagem> query = getEntityManager().createQuery(sql.toString(), Embalagem.class);
			query.setParameter("descricao", embalagem.getDescricao());
			query.setParameter("sigla", embalagem.getSigla());
			query.setParameter("quantidade", embalagem.getQuantidade());
			
			embalagem = query.getSingleResult();
			if(!embalagem.isNovo()) {
				return true;
			}
			
			return false;
		}
		catch(NoResultException e) {
			return false;
		}
	}
}
