package br.com.siscomanda.vo;

import java.io.Serializable;
import java.util.Date;

import br.com.siscomanda.enumeration.EStatus;
import br.com.siscomanda.enumeration.ETipoVenda;

public class HistoricoVendaVO implements Serializable {

	private static final long serialVersionUID = -7702363206766002825L;

	private Long numeroVenda;
	private String nomeCliente;
	private String nomeFuncionario;
	private ETipoVenda tipoVenda;
	private Date iniciado;
	private Date finalizado;
	private double servico;
	private double entrega;
	private double total;
	private EStatus status;
	
	public HistoricoVendaVO() {
	}
	
	public HistoricoVendaVO(Long numeroVenda, String nomeCliente, String nomeFuncionario, ETipoVenda tipoVenda,
			Date iniciado, Date finalizado, double servico, double entrega, double total, EStatus status) {
		this.numeroVenda = numeroVenda;
		this.nomeCliente = nomeCliente;
		this.nomeFuncionario = nomeFuncionario;
		this.tipoVenda = tipoVenda;
		this.iniciado = iniciado;
		this.finalizado = finalizado;
		this.servico = servico;
		this.entrega = entrega;
		this.total = total;
		this.status = status;
	}



	public Long getNumeroVenda() {
		return numeroVenda;
	}

	public void setNumeroVenda(Long numeroVenda) {
		this.numeroVenda = numeroVenda;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getNomeFuncionario() {
		return nomeFuncionario;
	}

	public void setNomeFuncionario(String nomeFuncionario) {
		this.nomeFuncionario = nomeFuncionario;
	}

	public ETipoVenda getTipoVenda() {
		return tipoVenda;
	}

	public void setTipoVenda(ETipoVenda tipoVenda) {
		this.tipoVenda = tipoVenda;
	}

	public Date getIniciado() {
		return iniciado;
	}

	public void setIniciado(Date iniciado) {
		this.iniciado = iniciado;
	}

	public Date getFinalizado() {
		return finalizado;
	}

	public void setFinalizado(Date finalizado) {
		this.finalizado = finalizado;
	}

	public double getServico() {
		return servico;
	}

	public void setServico(double servico) {
		this.servico = servico;
	}

	public double getEntrega() {
		return entrega;
	}

	public void setEntrega(double entrega) {
		this.entrega = entrega;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public EStatus getStatus() {
		return status;
	}

	public void setStatus(EStatus status) {
		this.status = status;
	}
}
