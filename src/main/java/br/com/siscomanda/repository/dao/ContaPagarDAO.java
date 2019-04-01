package br.com.siscomanda.repository.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.TypedQuery;

import br.com.siscomanda.enumeration.ESituacaoConta;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.ContaPagar;
import br.com.siscomanda.repository.base.GenericDAO;
import br.com.siscomanda.util.DateUtil;

public class ContaPagarDAO extends GenericDAO<ContaPagar> implements Serializable {

	private static final long serialVersionUID = -8884258810493593100L;
	
	private Date dataPagamentoInicial;
	private Date dataPagamentoFinal;
	private Date dataVencimentoInicial;
	private Date dataVencimentoFinal;
	private ESituacaoConta status;
	private String descricaoConta;
	private Double valor;
	
	private void initParameter(Map<String, Object> filter) {
		this.dataPagamentoInicial = (Date)filter.get("dataPagamentoInicial");
		this.dataPagamentoFinal = (Date)filter.get("dataPagamentoFinal");
		this.dataVencimentoInicial = (Date)filter.get("dataVencimentoInicial");
		this.dataVencimentoFinal = (Date)filter.get("dataVencimentoFinal");
		this.status = (ESituacaoConta)filter.get("status");
		this.descricaoConta = (String)filter.get("descricaoConta");
		this.valor = (Double)filter.get("valor");
	}
	
	public List<ContaPagar> porFiltro(Map<String, Object> filter) throws SiscomandaException {
		initParameter(filter);
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT conta FROM ContaPagar conta ");
		sql.append("WHERE 1 = 1 ");
		predicate(sql, filter);
		sql.append("ORDER BY conta.dataVencimento ASC ");
		
		TypedQuery<ContaPagar> query = getEntityManager().createQuery(sql.toString(), ContaPagar.class);
		parameter(query, filter);
		
		List<ContaPagar> contas = query.getResultList();
		return contas;
	}
	
	private void parameter(TypedQuery<ContaPagar> query, Map<String, Object> filter) {
		if(Objects.nonNull(dataVencimentoInicial) && Objects.nonNull(dataVencimentoFinal)) {
			query.setParameter("vencimentoInicial", dataVencimentoInicial);
			query.setParameter("vencimentoFinal", dataVencimentoFinal);
		}
		
		if(Objects.nonNull(dataVencimentoInicial) && Objects.isNull(dataVencimentoFinal)) {
			query.setParameter("vencimentoInicial", dataVencimentoInicial);
		}
		
		if(Objects.isNull(dataVencimentoInicial) && Objects.nonNull(dataVencimentoFinal)) {
			query.setParameter("vencimentoFinal", dataVencimentoFinal);
		}
		
		if(Objects.nonNull(dataPagamentoInicial) && Objects.nonNull(dataPagamentoFinal)) {
			query.setParameter("pagamentoInicial", dataPagamentoInicial);
			query.setParameter("pagamentoFinal", dataPagamentoFinal);
		}	
		
		if(Objects.nonNull(dataPagamentoInicial) && Objects.isNull(dataPagamentoFinal)) {
			query.setParameter("pagamentoInicial", dataPagamentoInicial);
		}
		
		if(Objects.isNull(dataPagamentoInicial) && Objects.nonNull(dataPagamentoFinal)) {
			query.setParameter("pagamentoFinal", dataPagamentoFinal);
		}
		
		if(Objects.nonNull(descricaoConta)) {
			query.setParameter("descricaoConta", "%" + descricaoConta.toUpperCase() + "%");
		}
		
		if(Objects.nonNull(status) && status.equals(ESituacaoConta.CONTA_VENCIDA)
				|| Objects.nonNull(status) && status.equals(ESituacaoConta.CONTA_A_PAGAR)) {
			query.setParameter("dataCorrente", new Date());
		}
		
		if(Objects.nonNull(status) && status.equals(ESituacaoConta.CONTA_PAGA)) {
			query.setParameter("pago", true);
		}
		
		if(Objects.nonNull(valor)) {
			query.setParameter("valor", valor);
		}
	}
	
	private void predicate(StringBuilder sql, Map<String, Object> filter) throws SiscomandaException {
		if(Objects.nonNull(dataVencimentoInicial) && Objects.nonNull(dataVencimentoFinal)) {
			if(DateUtil.isDepois(dataVencimentoInicial, dataVencimentoFinal)) {
				throw new SiscomandaException("período inicial não pode ser maior que o período final");
			}
			sql.append(" AND conta.dataVencimento BETWEEN :vencimentoInicial AND :vencimentoFinal ");
		}
		
		if(Objects.nonNull(dataVencimentoInicial) && Objects.isNull(dataVencimentoFinal)) {
			sql.append(" AND conta.dataVencimento = :vencimentoInicial ");
		}
		
		if(Objects.isNull(dataVencimentoInicial) && Objects.nonNull(dataVencimentoFinal)) {
			sql.append(" AND conta.dataVencimento = :vencimentoFinal ");
		}
		
		if(Objects.nonNull(dataPagamentoInicial) && Objects.nonNull(dataPagamentoFinal)) {
			if(DateUtil.isDepois(dataPagamentoInicial, dataPagamentoFinal)) {
				throw new SiscomandaException("período inicial não pode ser maior que o período final");
			}
			sql.append(" AND conta.dataPagamento BETWEEN :pagamentoInicial AND :pagamentoFinal ");
		}
		
		if(Objects.nonNull(dataPagamentoInicial) && Objects.isNull(dataPagamentoFinal)) {
			sql.append(" AND conta.dataPagamento = :pagamentoInicial ");
		}
		
		if(Objects.isNull(dataPagamentoInicial) && Objects.nonNull(dataPagamentoFinal)) {
			sql.append(" AND conta.dataPagamento = :pagamentoFinal ");
		}
		
		if(Objects.nonNull(descricaoConta)) {
			sql.append(" AND conta.descricao LIKE :descricaoConta ");
		}
		
		if(Objects.nonNull(status) && status.equals(ESituacaoConta.CONTA_VENCIDA)) {
			sql.append(" AND conta.dataVencimento < :dataCorrente ");
		}
		
		if(Objects.nonNull(status) && status.equals(ESituacaoConta.CONTA_A_PAGAR)) {
			sql.append(" AND conta.dataVencimento >= :dataCorrente ");
			sql.append(" AND conta.pago = false ");
		}
		
		if(Objects.nonNull(status) && status.equals(ESituacaoConta.CONTA_PAGA)) {
			sql.append(" AND conta.pago = :pago ");
		}
		
		if(Objects.nonNull(valor)) {
			sql.append(" AND conta.valor = :valor ");
		}
	}
}
