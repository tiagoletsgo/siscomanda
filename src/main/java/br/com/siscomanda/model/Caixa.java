package br.com.siscomanda.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.siscomanda.base.model.BaseEntity;

@Entity
@Table(name = "caixa")
public class Caixa extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 822247632597440611L;
	
	@Column(name = "observacao", nullable = false)
	private String observacao;
	
	@Column(name = "saldo_inicial", nullable = false)
	private Double saldoInicial;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_hora_abertura", nullable = false)
	private Date dataHoraAbertura;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_hora_fechamento", nullable = true)
	private Date dataHoraFechamento;
	
	@Column(name = "caixa_aberto")
	private Boolean caixaAberto = false;
	
	@Column(name = "total_entrada", nullable = false)
	private Double totalEntrada;
	
	@Column(name = "total_saida", nullable = false)
	private Double totalSaida;
	
	@OneToMany(mappedBy = "caixa", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Lancamento> lancamentos = new ArrayList<>();
	
	public Caixa() {
	}
	
	public Caixa(Long id) {
		setId(id);
	}
	
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Double getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoInicial(Double saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

	public Date getDataHoraAbertura() {
		return dataHoraAbertura;
	}

	public void setDataHoraAbertura(Date dataHoraAbertura) {
		this.dataHoraAbertura = dataHoraAbertura;
	}

	public Date getDataHoraFechamento() {
		return dataHoraFechamento;
	}

	public void setDataHoraFechamento(Date dataHoraFechamento) {
		this.dataHoraFechamento = dataHoraFechamento;
	}

	public Boolean getCaixaAberto() {
		return caixaAberto;
	}

	public void setCaixaAberto(Boolean caixaAberto) {
		this.caixaAberto = caixaAberto;
	}

	public Double getTotalEntrada() {
		return totalEntrada;
	}

	public void setTotalEntrada(Double totalEntrada) {
		this.totalEntrada = totalEntrada;
	}

	public Double getTotalSaida() {
		return totalSaida;
	}

	public void setTotalSaida(Double totalSaida) {
		this.totalSaida = totalSaida;
	}

	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}
	
	@Transient
	public boolean isAberto() {
		return isNovo() && !getCaixaAberto();
	}
	
	@Transient
	public boolean isFechado() {
		return !isAberto();
	}
}
