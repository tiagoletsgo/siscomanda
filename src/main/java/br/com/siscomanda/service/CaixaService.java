package br.com.siscomanda.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import br.com.siscomanda.config.jpa.Transactional;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Caixa;
import br.com.siscomanda.model.CaixaLancamento;
import br.com.siscomanda.model.FormaPagamento;
import br.com.siscomanda.model.PagamentoVenda;
import br.com.siscomanda.repository.dao.CaixaDAO;
import br.com.siscomanda.repository.dao.FormaPagamentoDAO;

public class CaixaService implements Serializable {

	private static final long serialVersionUID = -2451209455360556487L;
	
	@Inject
	private CaixaDAO caixaDAO;
	
	@Inject
	private FormaPagamentoDAO formaPagamentoDAO;
	
	public Caixa initCaixa() {
		Caixa caixa = caixaDAO.temCaixaAberto();
		if(caixa == null) {
			caixa = new Caixa();
			caixa.setCaixaAberto(false);
			caixa.setSaldoInicial(new Double(0));
			caixa.setTotalEntrada(new Double(0));
			caixa.setTotalSaida(new Double(0));
			return caixa;
		}
		
		caixa.getLancamentos().addAll(lancamentos());
		Map<String, Object> totalizador = calculaTotalizador(caixa.getLancamentos());
		caixa.setTotalEntrada((Double)totalizador.get("TOTAL_ENTRADA"));
		caixa.setTotalSaida((Double)totalizador.get("TOTAL_SAIDA"));
		
		return caixa;
	}
	
	public Map<String, Object> calculaTotalizador(List<CaixaLancamento> lancamentos) {
		Map<String, Object> totalizadores = new HashMap<>();
		Double totalEntrada = new Double(0);
		Double totalSaida = new Double(0);
		
		for(CaixaLancamento lancamento : lancamentos) {
			totalEntrada += lancamento.getValorEntrada() == null ? new Double(0) : lancamento.getValorEntrada();
			totalSaida += lancamento.getValorSaida() == null ? new Double(0) : lancamento.getValorSaida();
		}
		
		totalizadores.put("TOTAL_ENTRADA", totalEntrada);
		totalizadores.put("TOTAL_SAIDA", totalSaida);
		
		return totalizadores;
	}
	
	@Transactional
	public void abrirCaixa(Caixa caixa) throws SiscomandaException {
		
		if(caixa.getSaldoInicial() < BigDecimal.ZERO.doubleValue()) {
			throw new SiscomandaException("Não é possível abrir o caixa com saldo negativo.");
		}
		
		if(caixa.getSaldoInicial().equals(BigDecimal.ZERO.doubleValue())) {
			throw new SiscomandaException("Não é possível abrir o caixa com saldo zerado.");
		}
		
		CaixaLancamento lancamento = new CaixaLancamento();
		lancamento.setDataHora(new Date());
		FormaPagamento formaPagamento = formaPagamentoDAO.porDescricao("DINHEIRO", FormaPagamento.class).get(0);
		lancamento.setFormaPagamento(formaPagamento);
		lancamento.setDescricao("SALDO INICIAL");
		lancamento.setValorEntrada(caixa.getSaldoInicial());
		lancamento.setValorSaida(new Double(0));
		lancamento.setCaixa(caixa);
		
		caixa.setDataHoraAbertura(new Date());
		caixa.setObservacao("Aberto às " + new SimpleDateFormat("HH:mm:ss").format(caixa.getDataHoraAbertura()) + " por Administrador");
		caixa.setTotalEntrada(lancamento.getValorEntrada());
		caixa.setCaixaAberto(true);

		caixa.getLancamentos().add(lancamento);
		caixaDAO.salvar(caixa);
	}
	
	public void fecharCaixa(Caixa caixa) throws SiscomandaException {
		
	}
	
	public List<CaixaLancamento> lancamentos() {
		List<CaixaLancamento> lancamentos = new ArrayList<>();
		List<PagamentoVenda> pagamentos = caixaDAO.lancamentos();
				
		for(PagamentoVenda pagamento : pagamentos) {
			CaixaLancamento lancamento = new CaixaLancamento();
			lancamento.setDataHora(pagamento.getVenda().getIniciado());
			lancamento.setFormaPagamento(pagamento.getFormaPagamento());
			lancamento.setDescricao("Pedido nº " + pagamento.getVenda().getId());
			lancamento.setValorEntrada(pagamento.getValorRecebido() - pagamento.getValorTroco());
			lancamento.setValorSaida(new Double(0));
			lancamento.setBandeira(pagamento.getBandeira());
			lancamentos.add(lancamento);
		}
		
		return lancamentos;
	}
}
