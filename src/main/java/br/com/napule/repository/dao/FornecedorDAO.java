package br.com.napule.repository.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.napule.model.Fornecedor;
import br.com.napule.repository.base.GenericDAO;
import br.com.napule.util.StringUtil;

public class FornecedorDAO extends GenericDAO<Fornecedor> {
	
	public List<Fornecedor> buscaPor(Fornecedor fornecedor) {
		StringBuilder sql = new StringBuilder();
		sql.append("FROM Fornecedor fornecedor WHERE 1 = 1  ");
		sql.append(StringUtil.isNotEmpty(fornecedor.getCpfCnpj()) ? " AND fornecedor.cpfCnpj = :cpfCnpj " : "");
		sql.append(StringUtil.isNotEmpty(fornecedor.getRazaoSocial()) ? " AND fornecedor.razaoSocial LIKE :razaoSocial " : "");	
		sql.append(StringUtil.isNotEmpty(fornecedor.getNomeFantasia()) ? " AND fornecedor.nomeFantasia LIKE :nomeFantasia " : "");
		
		TypedQuery<Fornecedor> query = getEntityManager().createQuery(sql.toString(), Fornecedor.class);
		if(!StringUtil.isEmpty(fornecedor.getCpfCnpj())) {
			query.setParameter("cpfCnpj", StringUtil.somenteAlfanumericoSemEspaco(fornecedor.getCpfCnpj()));
		}
		
		if(!StringUtil.isEmpty(fornecedor.getRazaoSocial())) {
			query.setParameter("razaoSocial", "%"+fornecedor.getRazaoSocial()+"%");
		}
		
		if(!StringUtil.isEmpty(fornecedor.getNomeFantasia())) {
			query.setParameter("nomeFantasia", "%"+fornecedor.getNomeFantasia()+"%");
		}
		
		List<Fornecedor> fornecedores = query.getResultList();
		return fornecedores;
	}
	
	public boolean isExists(Fornecedor fornecedor) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("FROM Fornecedor fornecedor WHERE fornecedor.cpfCnpj = :cpfCnpj ");
			TypedQuery<Fornecedor> query = getEntityManager().createQuery(sql.toString(), Fornecedor.class);
			query.setParameter("cpfCnpj", fornecedor.getCpfCnpj());
			fornecedor = query.getSingleResult();
			
			if(!fornecedor.isNovo()) {
				return false;
			}
			
			return true;
		}
		catch(NoResultException e) {
			return false;
		}
	}
}
