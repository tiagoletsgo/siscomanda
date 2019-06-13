package br.com.siscomanda.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.siscomanda.config.jpa.Transactional;
import br.com.siscomanda.enumeration.EFreaquencia;
import br.com.siscomanda.enumeration.ETipoOperacao;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.interfaces.geradorVencimentoImpl.GeraDataAnual;
import br.com.siscomanda.interfaces.geradorVencimentoImpl.GeraDataDiario;
import br.com.siscomanda.interfaces.geradorVencimentoImpl.GeraDataMensal;
import br.com.siscomanda.interfaces.geradorVencimentoImpl.GeraDataQuinzenal;
import br.com.siscomanda.interfaces.geradorVencimentoImpl.GeraDataSemanal;
import br.com.siscomanda.model.ContaPagar;
import br.com.siscomanda.repository.dao.ContaPagarDAO;
import br.com.siscomanda.util.JSFUtil;

public class ContaPagarService implements Serializable {

	private static final long serialVersionUID = -7605329514615722286L;

	@Inject
	private ContaPagarDAO dao;
	
	public List<ContaPagar> buscaPorFiltro(Map<String, Object> filter) throws SiscomandaException {
		return dao.porFiltro(filter);
	}
	
	@Transactional
	public void salvar(ContaPagar conta, List<Date> vencimentos, boolean pagarConta) throws SiscomandaException {
		
		if(conta.getDescricao().isEmpty()) {
			throw new SiscomandaException("Informe uma descrição.");
		}
		
		if(Objects.isNull(conta.getDataVencimento())) {
			throw new SiscomandaException("Informe um vencimento.");
		}
		
		if(Objects.isNull(conta.getValor())) {
			throw new SiscomandaException("Informe um valor.");
		}
		
		if(conta.getTotalPago() < BigDecimal.ZERO.doubleValue()) {
			throw new SiscomandaException("Não é permitido valores menores que zero.");
		}
		
		if(pagarConta && Objects.isNull(conta.getDataPagamento())) {
			throw new SiscomandaException("Informe a data de pagamento.");
		}
		
		if(conta.isPaga()) {
			throw new SiscomandaException("Conta paga não pode ser excluída ou alterada.");
		}
		
		if(Objects.isNull(conta.getTipoOperacao())) {
			throw new SiscomandaException("O tipo de operação não foi informada. Para este problema entre em contato com o administrador do sistema.");
		}
		
		conta.setPago(pagarConta);
		if(conta.isNovo()) {
			if(Objects.nonNull(vencimentos) && !vencimentos.isEmpty()) {
				int parcela = 1;
				String descricao = conta.getDescricao();
				vencimentos.add(conta.getDataVencimento());
				for(Date vencimento : vencimentos) {
					conta.setDescricao(descricao + "(" + parcela + ")");
					conta.setDataVencimento(vencimento);
					parcela++;
					
					dao.salvar(conta);
					conta.setDescricao(null);
				}
				JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Conta salva com sucesso.");
				return;
			}
			dao.salvar(conta);
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Conta salva com sucesso.");
			return;
		}
		
		dao.salvar(conta);
		JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Conta alterada com sucesso.");
	}
	
	@Transactional
	public void remover(List<ContaPagar> contas) throws SiscomandaException {
		if(contas == null || contas.isEmpty()) {
			throw new SiscomandaException("Selecione pelo menos 1 registro para ser excluído.!");
		}
		
		try {
			for(ContaPagar conta : contas) {
				if(conta.getTipoOperacao().equals(ETipoOperacao.DESPESA)) {
					throw new SiscomandaException("Não é possível excluir conta a pagar a partir de uma saída lançada em caixa. Alterações devem ser realizadas diretamente no módulo caixa. ");
				}
				if(conta.isPaga()) {
					throw new SiscomandaException("Conta paga não pode ser removida.");
				}
				dao.remover(ContaPagar.class, conta.getId());
			}
		}
		catch(SiscomandaException e) {
			throw new SiscomandaException(e.getMessage(), e);
		}
	}
	
	public List<ContaPagar> todos() {
		return dao.todos(ContaPagar.class);
	}
	
	public List<Date> gerarVencimento(EFreaquencia frequencia, Date data, Integer quantidadeRepeticao) throws SiscomandaException {
		
		if(Objects.isNull(frequencia)) {
			throw new SiscomandaException("Informe a frequência.");
		}

		if(Objects.isNull(data)) {
			throw new SiscomandaException("Informe data de vencimento.");
		}
		
		if(Objects.isNull(quantidadeRepeticao)) {
			throw new SiscomandaException("Informe a quantidade de repetições.");
		}
		
		switch (frequencia) {
			case MENSAL:
				return new GeraDataMensal().frequencia(data, quantidadeRepeticao);
			case SEMANAL:
				return new GeraDataSemanal().frequencia(data, quantidadeRepeticao);
			case DIARIO:
				return new GeraDataDiario().frequencia(data, quantidadeRepeticao);
			case ANUAL:
				return new GeraDataAnual().frequencia(data, quantidadeRepeticao);
			case QUINZENAL:
				return new GeraDataQuinzenal().frequencia(data, quantidadeRepeticao);
			default:
				break;
		}
		
		return null;
	}
	
}