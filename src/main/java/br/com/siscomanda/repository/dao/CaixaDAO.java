package br.com.siscomanda.repository.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.siscomanda.model.Caixa;
import br.com.siscomanda.model.PagamentoVenda;
import br.com.siscomanda.repository.base.GenericDAO;
import br.com.siscomanda.util.DateUtil;

public class CaixaDAO extends GenericDAO<Caixa> implements Serializable {

	private static final long serialVersionUID = -7623759446618936499L;
	
	@SuppressWarnings("unchecked")
	public List<PagamentoVenda> lancamentos() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT pg.* FROM pagamento_venda pg ");
		sql.append("INNER JOIN venda v on pg.venda_id = v.id ");
		sql.append("INNER JOIN caixa c on c.id = v.caixa_id ");
		sql.append("LEFT OUTER JOIN forma_pagamento fp on fp.id = pg.forma_pagamento_id ");
		sql.append("LEFT OUTER JOIN bandeira b on b.id = pg.bandeira_id ");
		sql.append("WHERE 1 = 1 ");
		sql.append("  AND c.caixa_aberto ");
		sql.append("   OR not v.pago ");
		sql.append("ORDER BY v.id ASC ");
		
		Query query = getEntityManager().createNativeQuery(sql.toString(), PagamentoVenda.class);
		List<PagamentoVenda> lancamentos = query.getResultList();
		return lancamentos;
	}
	
	public Caixa temCaixaAberto() {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT caixa FROM Caixa caixa ");
			sql.append("JOIN FETCH caixa.lancamentos ");
			sql.append("WHERE caixa.caixaAberto = true ");
			Query query = getEntityManager().createQuery(sql.toString(), Caixa.class);
			Caixa caixa = (Caixa)query.getSingleResult();
			return caixa;
		} catch(Exception e) {
			return null;
		}
	}
	
	public Caixa temCaixaAberto(Caixa caixa) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT caixa FROM Caixa caixa ");
			sql.append("JOIN FETCH caixa.lancamentos ");
			sql.append("WHERE caixa.caixaAberto = true ");
			sql.append("AND caixa = :caixa ");
			Query query = getEntityManager().createQuery(sql.toString(), Caixa.class);
			query.setParameter("caixa", caixa);
			caixa = (Caixa)query.getSingleResult();
			return caixa;
		} catch(Exception e) {
			return caixa;
		}
	}
	
	public List<Caixa> maisRecente(int dia) {
		StringBuilder sql = new StringBuilder();
		sql.append("FROM Caixa caixa ");
		sql.append("WHERE 1 = 1 ");
		sql.append("AND caixa.dataHoraAbertura BETWEEN :primeiroDiaMesCorrente AND :data ");
		sql.append("ORDER BY caixa.id DESC ");
		
		TypedQuery<Caixa> query = getEntityManager().createQuery(sql.toString(), Caixa.class);
		query.setParameter("primeiroDiaMesCorrente", DateUtil.diaAtual());
		query.setParameter("data", DateUtil.data(dia, 23, 59, 59));
		List<Caixa> caixas = query.getResultList();
		return caixas;
	}
	
	public List<Caixa> porPeriodo(Date dataInicial, Date dataFinal) {
		StringBuilder sql = new StringBuilder();
		sql.append("FROM Caixa caixa ");
		sql.append("WHERE 1 = 1 ");
		sql.append("AND caixa.dataHoraAbertura BETWEEN :dataInicial AND :dataFinal ");
		sql.append("ORDER BY caixa.id DESC ");
		
		TypedQuery<Caixa> query = getEntityManager().createQuery(sql.toString(), Caixa.class);
		query.setParameter("dataInicial", DateUtil.data(dataInicial, 0, 0, 0));
		query.setParameter("dataFinal", DateUtil.data(dataFinal, 23, 59, 59));
		List<Caixa> caixas = query.getResultList();
		return caixas;
	}
	
	public Caixa detalhar(Caixa caixa) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT caixa FROM Caixa caixa ");
			sql.append("JOIN FETCH caixa.lancamentos ");
			sql.append("WHERE 1 = 1 ");
			sql.append(" AND caixa = :caixa ");
			
			TypedQuery<Caixa> query = getEntityManager().createQuery(sql.toString(), Caixa.class);
			query.setParameter("caixa", caixa);
			
			caixa = query.getSingleResult();
			return caixa;
		}
		catch(Exception e) {
			return null;
		}
	}
}
