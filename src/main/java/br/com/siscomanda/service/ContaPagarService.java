package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import br.com.siscomanda.config.jpa.Transactional;
import br.com.siscomanda.enumeration.EFreaquencia;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.interfaces.geradorVencimentoImpl.GeraDataAnualService;
import br.com.siscomanda.interfaces.geradorVencimentoImpl.GeraDataDiarioService;
import br.com.siscomanda.interfaces.geradorVencimentoImpl.GeraDataMensalService;
import br.com.siscomanda.interfaces.geradorVencimentoImpl.GeraDataQuinzenalService;
import br.com.siscomanda.interfaces.geradorVencimentoImpl.GeraDataSemanalService;
import br.com.siscomanda.model.ContaPagar;
import br.com.siscomanda.repository.dao.ContaPagarDAO;

public class ContaPagarService implements Serializable {

	private static final long serialVersionUID = -7605329514615722286L;

	@Inject
	private ContaPagarDAO dao;
	
	@Transactional
	public void salvar(ContaPagar conta, List<Date> vencimentos) throws SiscomandaException {
		
		if(conta.getDescricao().isEmpty()) {
			throw new SiscomandaException("Informe uma descrição.");
		}
		
		if(Objects.isNull(conta.getDataVencimento())) {
			throw new SiscomandaException("Informe um vencimento.");
		}
		
		if(Objects.isNull(conta.getValor())) {
			throw new SiscomandaException("Informe um valor.");
		}
		
		if(Objects.nonNull(vencimentos) && !vencimentos.isEmpty()) {
			int parcela = 1;
			String descricao = conta.getDescricao();
			for(Date vencimento : vencimentos) {
				conta.setDescricao(descricao + "(" + parcela + ")");
				conta.setDataVencimento(vencimento);
				parcela++;
				
				dao.salvar(conta);
				conta.setDescricao(null);
			}
			return;
		}
		
		dao.salvar(conta);
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
				return new GeraDataMensalService().frequencia(data, quantidadeRepeticao);
			case SEMANAL:
				return new GeraDataSemanalService().frequencia(data, quantidadeRepeticao);
			case DIARIO:
				return new GeraDataDiarioService().frequencia(data, quantidadeRepeticao);
			case ANUAL:
				return new GeraDataAnualService().frequencia(data, quantidadeRepeticao);
			case QUINZENAL:
				return new GeraDataQuinzenalService().frequencia(data, quantidadeRepeticao);
			default:
				break;
		}
		
		return null;
	}
	
}