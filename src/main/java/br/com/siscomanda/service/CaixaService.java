package br.com.siscomanda.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.siscomanda.config.jpa.Transactional;
import br.com.siscomanda.enumeration.ETipoOperacao;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.interfaces.CalculaLancamento;
import br.com.siscomanda.interfaces.lancamentoImpl.LancamentoEntradaService;
import br.com.siscomanda.interfaces.lancamentoImpl.LancamentoSaidaService;
import br.com.siscomanda.model.Caixa;
import br.com.siscomanda.model.CaixaLancamento;
import br.com.siscomanda.model.FormaPagamento;
import br.com.siscomanda.model.PagamentoVenda;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.repository.dao.CaixaDAO;
import br.com.siscomanda.repository.dao.CaixaLancamentoDAO;
import br.com.siscomanda.repository.dao.FormaPagamentoDAO;
import br.com.siscomanda.repository.dao.VendaDAO;
import br.com.siscomanda.util.JSFUtil;
import br.com.siscomanda.util.StringUtil;

public class CaixaService implements Serializable {

	private static final long serialVersionUID = -2451209455360556487L;
	
	private static final String SALDO_INICIAL = "SALDO INICIAL";
	
	@Inject
	private CaixaDAO caixaDAO;
	
	@Inject
	private CaixaLancamentoDAO caixaLancamentoDAO;
	
	@Inject
	private FormaPagamentoDAO formaPagamentoDAO;
	
	@Inject
	private VendaDAO vendaDAO;
	
	public Caixa temCaixaAberto(Caixa caixa) {
		return caixaDAO.temCaixaAberto(caixa);
	}
	
	public Caixa temCaixaAberto() {
		Caixa caixa = caixaDAO.temCaixaAberto();
		if(Objects.isNull(caixa)) {
			return initCaixa();
		}
		return caixa;
	}
	
	public List<Caixa> pesquisar(Integer dia, Date dataInicial, Date dataFinal) throws SiscomandaException {
		List<Caixa> caixas = new ArrayList<>();
		
		if(Objects.isNull(dia) && Objects.isNull(dataInicial) && Objects.isNull(dataFinal)) {
			throw new SiscomandaException("Selecione uma das opçoes de pesquisa.");
		}
		
		if(dia != null) {
			caixas = caixaDAO.maisRecente(dia);
		}
		else if(dataInicial != null && dataFinal != null) {
			caixas = caixaDAO.porPeriodo(dataInicial, dataFinal);
		}
		
		if(caixas.isEmpty()) {
			throw new SiscomandaException("A pesquisa não retornou nenhum resultado para o período informado.");
		}
		
		return caixas;
	}
	
	@Transactional
	public void removeLancamento(CaixaLancamento lancamento) throws SiscomandaException {
		if(lancamento.getTipoOperacao().equals(ETipoOperacao.VENDA)) {
			throw new SiscomandaException("Lançamentos gerados a partir de uma venda não pode ser excluido, pelo fechamento de caixa.");
		}
		
		if(lancamento.getDescricao().equals(SALDO_INICIAL)) {
			throw new SiscomandaException("Este lançamento não pode ser removido, por ser o primeiro suprimento do caixa.");
		}
		
		caixaLancamentoDAO.remover(CaixaLancamento.class, lancamento.getId());
	}
	
	public List<FormaPagamento> buscaFormaPagamentoPorDescricao(String descricao) {
		try {
			return formaPagamentoDAO.porDescricao(descricao, FormaPagamento.class);
		} catch (SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Forma de pagamento não localizada.");
			return null;
		}
	}
	
	public Caixa initCaixa() {
		Caixa caixa = new Caixa();
		caixa.setCaixaAberto(false);
		caixa.setSaldoInicial(new Double(0));
		caixa.setTotalEntrada(new Double(0));
		caixa.setTotalSaida(new Double(0));
		return caixa;
	}
	
	public Map<String, Object> calculaTotalizador(List<CaixaLancamento> lancamentos) {
		Map<String, Object> totalizadores = new HashMap<>();
		Double totalEntrada = new Double(0);
		Double totalSaida = new Double(0);
		Double totalDinheiro = new Double(0);
		
		for(CaixaLancamento lancamento : lancamentos) {
			totalEntrada += lancamento.getValorEntrada() == null ? new Double(0) : lancamento.getValorEntrada();
			totalSaida += lancamento.getValorSaida() == null ? new Double(0) : lancamento.getValorSaida();
			if(lancamento.getFormaPagamento().getDescricao().equals("DINHEIRO")) {
				Double entrada = lancamento.getValorEntrada() == null ? new Double(0) : lancamento.getValorEntrada();
				Double saida = lancamento.getValorSaida() == null ? new Double(0) : lancamento.getValorSaida();
				totalDinheiro += (entrada - saida);
			}
		}
		
		totalizadores.put("TOTAL_ENTRADA", totalEntrada);
		totalizadores.put("TOTAL_SAIDA", totalSaida);
		totalizadores.put("TOTAL_DINHEIRO", totalDinheiro);
		
		return totalizadores;
	}
	
	@Transactional
	public CaixaLancamento adicionaEntradaSaida(CaixaLancamento lancamento, Double valor, Double totalDinheiro) throws SiscomandaException {
		lancamento.setDataHora(new Date());
		
		if(lancamento.getCaixa().isNovo() && lancamento.getCaixa().isFechado()) {
			throw new SiscomandaException("Não foi realizado a abertura do caixa.");
		}
		
		if(!lancamento.getTipoOperacao().equals(ETipoOperacao.ENTRADA) && valor > totalDinheiro) {
			throw new SiscomandaException("Não há saldo suficiente em dinheiro para esta operação.");
		}
		
		if(valor == null || valor.equals(BigDecimal.ZERO.doubleValue())) {
			throw new SiscomandaException("Não é permitido adicionar um lançamento com valor zerado.");
		}
		
		if(lancamento.getTipoOperacao() == null) {
			throw new SiscomandaException("Não é permitido incluir um lançamento sem informar o tipo de operação.");
		}
		
		if(lancamento.getFormaPagamento() == null) {
			throw new SiscomandaException("Não é permitido incluir um lançamento sem uma forma de pagamento.");
		}
		
		if(StringUtil.isEmpty(lancamento.getDescricao())) {
			throw new SiscomandaException("Não é permitido incluir um lançamento sem informar uma descrição.");
		}
		
		CalculaLancamento calcula = new LancamentoEntradaService();
		lancamento = calcula.executaCalculo(lancamento, lancamento.getTipoOperacao(), valor);
		
		calcula = new LancamentoSaidaService();
		lancamento = calcula.executaCalculo(lancamento, lancamento.getTipoOperacao(), valor);
		
		caixaLancamentoDAO.salvar(lancamento);
		
		return lancamento;
	}
	
	@Transactional
	public void abrirCaixa(Caixa caixa) throws SiscomandaException {
		
		if(caixaDAO.temCaixaAberto() != null) {
			throw new SiscomandaException("Não é permitido ter mais de um caixa aberto. Por gentileza finalize o caixa anterior e depois realiza a abertura novamente.");
		}
		
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
		lancamento.setDescricao(SALDO_INICIAL);
		lancamento.setValorEntrada(caixa.getSaldoInicial());
		lancamento.setValorSaida(new Double(0));
		lancamento.setCaixa(caixa);
		lancamento.setTipoOperacao(ETipoOperacao.ENTRADA);
		
		caixa.setDataHoraAbertura(new Date());
		
		String observacao = "Aberto às " + new SimpleDateFormat("HH:mm:ss").format(caixa.getDataHoraAbertura()) + " por Administrador";
		
		caixa.setObservacao(observacao.toUpperCase());
		caixa.setTotalEntrada(lancamento.getValorEntrada());
		caixa.setCaixaAberto(true);

		caixa.getLancamentos().add(lancamento);
		caixaDAO.salvar(caixa);
	}
	
	@Transactional
	public void fecharCaixa(Caixa caixa) throws SiscomandaException {
		
		List<Venda> vendasEmAberto = vendaDAO.vendasNaoPagas();
		if(!vendasEmAberto.isEmpty()) {
			throw new SiscomandaException("Existem ( " + vendasEmAberto.size() + " ) vendas não finalizadas.");
		}
		
		caixa.setDataHoraFechamento(new Date());
		caixa.setCaixaAberto(false);
		caixaDAO.salvar(caixa);
	}
	
	public List<CaixaLancamento> lancamentos(Caixa caixa) {
		
		if(Objects.isNull(caixa)) {
			caixa = initCaixa();
			return new ArrayList<>();
		}
		
		List<CaixaLancamento> lancamentos = new ArrayList<>();
		List<PagamentoVenda> pagamentos = caixaDAO.lancamentos();
		
		lancamentos.addAll(caixa.getLancamentos());
		for(PagamentoVenda pagamento : pagamentos) {
			if(pagamento.getCaixa().equals(caixa)) {
				CaixaLancamento lancamento = new CaixaLancamento();
				lancamento.setId(pagamento.getId());
				lancamento.setDataHora(pagamento.getVenda().getIniciado());
				lancamento.setFormaPagamento(pagamento.getFormaPagamento());
				lancamento.setDescricao("PEDIDO Nº " + pagamento.getVenda().getId());
				lancamento.setValorEntrada(pagamento.getValorRecebido() - pagamento.getValorTroco());
				lancamento.setValorSaida(new Double(0));
				lancamento.setBandeira(pagamento.getBandeira());
				lancamento.setTipoOperacao(ETipoOperacao.VENDA);
				lancamento.setCaixa(caixa);
				
				if(!lancamentos.contains(lancamento)) {
					lancamentos.add(lancamento);
				}
			}
		}
		
		return lancamentos;
	}
	
	public Caixa detalhar(Caixa caixa) {
		return caixaDAO.detalhar(caixa);
	}
	
	public List<ETipoOperacao> operacoes() {
		List<ETipoOperacao> operacoes = new ArrayList<>();
		operacoes.add(ETipoOperacao.DESPESA);
		operacoes.add(ETipoOperacao.SAIDA);
		operacoes.add(ETipoOperacao.ENTRADA);
		return operacoes;
	}
}
