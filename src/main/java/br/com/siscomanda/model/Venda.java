package br.com.siscomanda.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.siscomanda.base.model.BaseEntity;
import br.com.siscomanda.enumeration.EStatus;

@Entity
@Table(name = "venda")
public class Venda extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 2583564472683970706L;
		
	@OneToMany(mappedBy = "venda", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PagamentoVenda> pagamentos = new ArrayList<>();
	
	@OneToMany(mappedBy = "venda", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemVenda> itens = new ArrayList<>();
	
	@Column(name = "pago", nullable = false)
	private boolean pago;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private EStatus status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cliente_id", nullable = true)
	private Cliente cliente;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_iniciado", nullable = false)
	private Date iniciado;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_finalizado", nullable = true)
	private Date finalizado;
	
	@Column(name = "subtotal", nullable = false)
	private Double subtotal;
	
	@Column(name = "taxa_servico", nullable = false)
	private Double taxaServico;
	
	@Column(name = "taxa_entrega", nullable = false)
	private Double taxaEntrega;
	
	@Column(name = "desconto", nullable = false)
	private Double desconto;

//	private Usuario operador
	
	@Column(name = "total", nullable = false)
	private Double total;
	
	@Column(name = "valor_pago", nullable = false)
	private Double valorPago;
	
	@Column(name = "mesa_comanda", nullable = false)
	private Integer mesaComanda;
	
	public Venda() {
	}
	
	public Venda(Long id) {
		setId(id);
	}

	public List<PagamentoVenda> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<PagamentoVenda> pagamentos) {
		this.pagamentos = pagamentos;
	}

	public List<ItemVenda> getItens() {
		return itens;
	}

	public void setItens(List<ItemVenda> itens) {
		this.itens = itens;
	}

	public boolean isPago() {
		return pago;
	}

	public void setPago(boolean pago) {
		this.pago = pago;
	}

	public EStatus getStatus() {
		return status;
	}

	public void setStatus(EStatus status) {
		this.status = status;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Double getTaxaServico() {
		return taxaServico;
	}

	public void setTaxaServico(Double taxaServico) {
		this.taxaServico = taxaServico;
	}

	public Double getDesconto() {
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getTaxaEntrega() {
		return taxaEntrega;
	}

	public void setTaxaEntrega(Double taxaEntrega) {
		this.taxaEntrega = taxaEntrega;
	}

	public Double getValorPago() {
		return valorPago;
	}

	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}

	public Integer getMesaComanda() {
		return mesaComanda;
	}

	public void setMesaComanda(Integer mesaComanda) {
		this.mesaComanda = mesaComanda;
	}
	
	@Transient
	public boolean isExcluivel() {
		return !isNovo() && getStatus().equals(EStatus.CANCELADO)
				|| !isNovo() && getStatus().equals(EStatus.EM_ABERTO);
	}
	
	@Transient
	public boolean isNotExcluivel() {
		return !isExcluivel();
	}
	
	@Transient
	public boolean isCancelavel() {
		return !isNovo() && getStatus().equals(EStatus.EM_ABERTO);
	}
	
	@Transient
	public boolean isNotCancelavel() {
		return !isCancelavel();
	}
	
	@Transient
	public boolean isNotPago() {
		return !isPago();
	}
	
	@Transient
	public boolean isEditavel() {
		return !isNovo() && getStatus().equals(EStatus.EM_ABERTO)
				|| isNovo() && getStatus().equals(EStatus.EM_ABERTO);
	}
	
	@Transient
	public boolean isNotEditavel() {
		return !isEditavel();
	}
	
	@Transient
	public boolean isPagavel() {
		return !isNovo() && !getStatus().equals(EStatus.PAGO)
				&& !getStatus().equals(EStatus.PAGO_PARCIAL) && !getStatus().equals(EStatus.CANCELADO);
	}
	
	@Transient
	public boolean isNotPagavel() {
		return !isPagavel();
	}
}
