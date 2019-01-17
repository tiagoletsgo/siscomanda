package br.com.siscomanda.repository.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Cliente;
import br.com.siscomanda.repository.base.GenericDAO;
import br.com.siscomanda.util.StringUtil;

public class ClienteDAO extends GenericDAO<Cliente> {
	
	public Cliente porCodigo(Cliente cliente) throws SiscomandaException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("FROM Cliente cliente ");
			sql.append("LEFT JOIN FETCH cliente.servico ");
			sql.append("WHERE cliente = :cliente");
			
			TypedQuery<Cliente> query = getEntityManager().createQuery(sql.toString(), Cliente.class);
			query.setParameter("cliente", cliente);
			cliente = query.getSingleResult();
			return cliente;
		}
		catch(NoResultException e) {
			throw new SiscomandaException("Não encontrou nenhum resultado na pesquisa.");
		}
	}
	
	public boolean isExists(Cliente cliente) {
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM Cliente cli ");
		sql.append(" WHERE 1 = 1 ");
		sql.append(StringUtil.isNotEmpty(cliente.getCpf()) ? " AND cli.cpf = :cpf " : "");
		sql.append(StringUtil.isNotEmpty(cliente.getRg()) ? " AND cli.rg = :rg " : "");
		sql.append(StringUtil.isNotEmpty(cliente.getTelefoneCelular()) ? " AND cli.telefoneCelular = :celular" : "");
		
		TypedQuery<Cliente> query = getEntityManager().createQuery(sql.toString(), Cliente.class);
		if(StringUtil.isNotEmpty(cliente.getCpf())) {
			query.setParameter("cpf", StringUtil.somenteAlfanumericoSemEspaco(cliente.getCpf()));
		}
		
		if(StringUtil.isNotEmpty(cliente.getRg())) {
			query.setParameter("rg", StringUtil.somenteAlfanumericoSemEspaco(cliente.getRg()));
		}
		
		if(StringUtil.isNotEmpty(cliente.getTelefoneCelular())) {
			query.setParameter("celular", StringUtil.somenteAlfanumericoSemEspaco(cliente.getTelefoneCelular()));
		}
		
		List<Cliente> clientes = query.getResultList();
		
		if(clientes.size() > 0) {
			return true;
		}
		
		return false;
	}
	
	public List<Cliente> buscaPor(Cliente cliente) {
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM Cliente cli ");
		sql.append(" WHERE 1 = 1 ");
		sql.append(StringUtil.isNotEmpty(cliente.getNomeCompleto()) ? "AND cli.nomeCompleto LIKE :nome " : "");
		sql.append(StringUtil.isNotEmpty(cliente.getRg()) ? "AND cli.rg = :rg " : "");
		sql.append(StringUtil.isNotEmpty(cliente.getCpf()) ? "AND cli.cpf = :cpf " : "");
		sql.append(StringUtil.isNotEmpty(cliente.getTelefoneCelular()) ? "AND cli.telefoneCelular = :celular " : "");
		
		TypedQuery<Cliente> query = getEntityManager().createQuery(sql.toString(), Cliente.class);
		if(StringUtil.isNotEmpty(cliente.getNomeCompleto())) {
			query.setParameter("nome", "%"+cliente.getNomeCompleto().toUpperCase().trim()+"%");
		}
		
		if(StringUtil.isNotEmpty(cliente.getRg())) {
			query.setParameter("rg", StringUtil.somenteAlfanumericoSemEspaco(cliente.getRg()));
		}
		
		if(StringUtil.isNotEmpty(cliente.getCpf()))  {
			query.setParameter("cpf", StringUtil.somenteAlfanumericoSemEspaco(cliente.getCpf()));
		}
		
		if(StringUtil.isNotEmpty(cliente.getTelefoneCelular())) {
			query.setParameter("celular", StringUtil.somenteAlfanumericoSemEspaco(cliente.getTelefoneCelular()));
		}
		
		List<Cliente> clientes = query.getResultList();
		return clientes;
	}
	
	public Cliente porNome(Cliente cliente) throws SiscomandaException {
		try {			
			StringBuilder sql = new StringBuilder();
			sql.append(" FROM Cliente cli ");
			sql.append(" WHERE cli.nomeCompleto LIKE :nome ");
			
			TypedQuery<Cliente> query = getEntityManager().createQuery(sql.toString(), Cliente.class);
			query.setParameter("nome", "%"+cliente.getNomeCompleto().toUpperCase().trim()+"%");
			return query.getSingleResult();
		}
		catch(Exception e) {
			throw new SiscomandaException("Nenhum registro encontrado. " + e.getMessage());
		}
	}
	
	public Cliente porRG(Cliente cliente) throws SiscomandaException {
		try {			
			StringBuilder sql = new StringBuilder();
			sql.append(" FROM Cliente cli ");
			sql.append(" WHERE cli.rg = :rg ");
			
			TypedQuery<Cliente> query = getEntityManager().createQuery(sql.toString(), Cliente.class);
			query.setParameter("rg", StringUtil.somenteAlfanumerico(cliente.getRg()));
			return query.getSingleResult();
		}
		catch(Exception e) {
			throw new SiscomandaException("Nenhum registro encontrado. " + e.getMessage());
		}
	}
	
	public Cliente porCPF(Cliente cliente) throws SiscomandaException {
		try {			
			StringBuilder sql = new StringBuilder();
			sql.append(" FROM Cliente cli ");
			sql.append(" WHERE cli.cpf = :cpf ");
			
			TypedQuery<Cliente> query = getEntityManager().createQuery(sql.toString(), Cliente.class);
			query.setParameter("cpf", StringUtil.somenteAlfanumerico(cliente.getCpf()));
			return query.getSingleResult();
		}
		catch(Exception e) {
			throw new SiscomandaException("Nenhum registro encontrado. " + e.getMessage());
		}
	}
	
	public Cliente porCelular(Cliente cliente) throws SiscomandaException {
		try {			
			StringBuilder sql = new StringBuilder();
			sql.append(" FROM Cliente cli ");
			sql.append(" WHERE cli.telefoneCelular = :celular ");
			
			TypedQuery<Cliente> query = getEntityManager().createQuery(sql.toString(), Cliente.class);
			query.setParameter("celular", StringUtil.somenteAlfanumerico(cliente.getTelefoneCelular()));
			return query.getSingleResult();
		}
		catch(Exception e) {
			throw new SiscomandaException("Nenhum registro encontrado. " + e.getMessage());
		}
	}
}
