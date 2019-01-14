package br.com.napule.repository.base;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.napule.base.model.BaseEntity;
import br.com.napule.exception.NapuleException;

public abstract class GenericDAO<T extends BaseEntity> {
	
	@Inject
	private EntityManager entityManager;
	
	private T entity;
	
	public T salvar(T entity) {
		return entityManager.merge(entity);
	}
		
	public void remover(Class<T> typeClazz, Long codigo) throws NapuleException {
		try {			
			this.entity = porCodigo(codigo, typeClazz);
			entityManager.remove(entity);
			entityManager.flush();
		}
		catch(Exception e) {
			throw new NapuleException(e.getMessage(), e);
		}
	}
	
	public boolean isExists(String descricao, Class<T> typeClazz) {
		StringBuilder sql = new StringBuilder();
		sql.append("FROM " + typeClazz.getSimpleName() + " entity WHERE entity.descricao = :descricao ");
		TypedQuery<T> query = getEntityManager().createQuery(sql.toString(), typeClazz);
		query.setParameter("descricao", descricao.toUpperCase());
		
		List<T> entitys = query.getResultList();
		
		if(entitys.size() > 0) {
			return true;
		}
		
		return false;
	}
	
	public List<T> porDescricao(String descricao, Class<T> typeClazz) throws NapuleException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("FROM " + typeClazz.getSimpleName() + " entity WHERE entity.descricao LIKE :descricao ");
			TypedQuery<T> query = getEntityManager().createQuery(sql.toString(), typeClazz);
			query.setParameter("descricao", "%"+descricao.toUpperCase()+"%");
			return query.getResultList();
		}
		catch(Exception e) {
			throw new NapuleException("Nenhum registro encontrado. " + e.getMessage());
		}
	}
	
	public T porCodigo(Long codigo, Class<T> typeClazz) {
		return entityManager.find(typeClazz, codigo);
	}
	
	public List<T> todos(Class<T> typeClazz) {
		return entityManager.createQuery(" FROM " + typeClazz.getSimpleName(), typeClazz).getResultList();
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
}
