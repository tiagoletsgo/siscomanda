package br.com.siscomanda.repository.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Query;

import br.com.siscomanda.model.Caixa;
import br.com.siscomanda.model.PagamentoVenda;
import br.com.siscomanda.repository.base.GenericDAO;

public class CaixaDAO extends GenericDAO<Caixa> implements Serializable {

	private static final long serialVersionUID = -7623759446618936499L;
	
	@SuppressWarnings("unchecked")
	public List<PagamentoVenda> lancamentos() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT pg.* FROM pagamento_venda pg ");
		sql.append("INNER JOIN venda v on pg.venda_id = v.id ");
		sql.append("LEFT OUTER JOIN forma_pagamento fp on fp.id = pg.forma_pagamento_id ");
		sql.append("LEFT OUTER JOIN bandeira b on b.id = pg.bandeira_id ");
		sql.append("WHERE 1 = 1 ");
		sql.append("  AND (CAST (v.data_iniciado AS DATE)) = CURRENT_DATE ");
		sql.append("   OR not v.pago ");
		sql.append("ORDER BY v.id ASC ");
		
		Query query = getEntityManager().createNativeQuery(sql.toString(), PagamentoVenda.class);
		List<PagamentoVenda> lancamentos = query.getResultList();
		return lancamentos;
	}
	
	public Caixa temCaixaAberto() {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT c.* FROM caixa c WHERE c.caixa_aberto ");
			Query query = getEntityManager().createNativeQuery(sql.toString(), Caixa.class);
			return (Caixa)query.getSingleResult();
		} catch(Exception e) {
			return null;
		}
	}
}
