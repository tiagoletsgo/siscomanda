package br.com.siscomanda.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
import br.com.siscomanda.enumeration.ETipoVenda;
import br.com.siscomanda.exception.SiscomandaRuntimeException;

@Entity
@Table(name = "venda")
public class Venda extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1323965645259119303L;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_venda", nullable = false)
	private ETipoVenda tipoVenda;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private EStatus status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario operador;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_hora", nullable = false)
	private Date dataHora;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data_venda", nullable = false)
	private Date dataVenda;
	
	@Column(name = "subtotal", nullable = false)
	private Double subtotal;
	
	@Column(name = "taxa_servico", nullable = false)
	private Double taxaServico;
	
	@Column(name = "taxa_entrega", nullable = false)
	private Double taxaEntrega;
	
	@Column(name = "desconto", nullable = false)
	private Double desconto;
	
	@Column(name = "total", nullable = false)
	private Double total;
	
	@Column(name = "controle", nullable = false)
	private Integer controle;
	
	@Column(name = "valor_pago", nullable = false)
	private Double valorPago;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cliente_id", nullable = true)
	private Cliente cliente;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "caixa_id", nullable = true)
	private Caixa caixa;
	
	@Column(name = "pago")
	private boolean pago;
	
	@OneToMany(mappedBy = "venda", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemVenda> itens = new ArrayList<ItemVenda>();
	
	@OneToMany(mappedBy = "venda", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Pagamento> pagamentos = new ArrayList<>();
	
	@Transient
	private Double fatorCalculoTaxaServico;
	
	public Venda() {	}
	
	public Venda(Long id) {
		setId(id);
	}

	public ETipoVenda getTipoVenda() {
		return tipoVenda;
	}

	public void setTipoVenda(ETipoVenda tipoVenda) {
		this.tipoVenda = tipoVenda;
	}

	public EStatus getStatus() {
		return status;
	}

	public void setStatus(EStatus status) {
		this.status = status;
	}

	public Usuario getOperador() {
		return operador;
	}

	public void setOperador(Usuario operador) {
		this.operador = operador;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
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

	public Double getTaxaEntrega() {
		return taxaEntrega;
	}

	public void setTaxaEntrega(Double taxaEntrega) {
		this.taxaEntrega = taxaEntrega;
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

	public Integer getControle() {
		return controle;
	}

	public void setControle(Integer controle) {
		this.controle = controle;
	}

	public List<ItemVenda> getItens() {
		return Collections.unmodifiableList(itens);
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

	public List<Pagamento> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<Pagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}
	
	public Double getValorPago() {
		return valorPago == null ? new Double(0) : valorPago;
	}

	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}

	public Date getDataVenda() {
		return dataVenda;
	}

	public void setDataVenda(Date dataVenda) {
		this.dataVenda = dataVenda;
	}
	
	public void setFatorCalculoTaxaServico(Double fatorCalculoTaxaServico) {
		this.fatorCalculoTaxaServico = fatorCalculoTaxaServico;
	}
	
	public void setPago(boolean pago) {
		this.pago = pago;
	}

	public boolean isPago() {
		return valorPago >= total && getDiferenca().equals(0D)
				&& getStatus().equals(EStatus.PAGO);
	}

	@Transient
	public ItemVenda getItem(int index) {
		return this.itens.get(index);
	}
	
	public void adicionaItem(ItemVenda item) {
		itens.add(item);
		item.setVenda(this);
		
		if(item.getItemPai() != null) {
			item.getItemPai().setVenda(this);
		}
	}
	
	public void removerItem(int index, ItemVenda itemPai) {
		try {
			itemPai.setVenda(null);
			
			int proximoIndex = index + 1;
			
			for(ItemVenda itemFilho : itemPai.getItensFilhos()) {
				itemFilho.setItemPai(null);
				this.itens.remove(proximoIndex);
			}
			
			this.itens.remove(index);
			calculaValorTotalDaVenda();
		}
		catch(SiscomandaRuntimeException e) {
			new SiscomandaRuntimeException("Error ao tentar remover. " + e.getMessage());
		}
	}
	
	public void calculaValorTotalDaVenda() {
		this.total = 0D;
		this.subtotal = 0D;
		
		for(ItemVenda item : itens) {
			this.subtotal += item.getValor() * item.getQuantidade();
		}
		
		calculaValorTotalItensComplementares();
		this.taxaServico = (this.fatorCalculoTaxaServico * this.subtotal) / 100D;
		this.total = (this.subtotal + this.taxaEntrega + this.taxaServico) - this.desconto;
		Collections.sort(itens);
	}
	
	private void calculaValorTotalItensComplementares() {
		for(ItemVenda item : itens) {
			if(item.getAdicionais().isEmpty() == false) {
				for(Adicional complemento : item.getAdicionais()) {					
					this.subtotal += complemento.getPrecoVenda();
				}
			}
		}
	}
	
	@Transient
	public boolean isBloqueiaVendaMesaOuComanda() {
		
		if(Objects.nonNull(getTipoVenda()) && getTipoVenda().equals(ETipoVenda.MESA) || 
				Objects.nonNull(getTipoVenda()) && getTipoVenda().equals(ETipoVenda.COMANDA)) {
			return true;
		}
		
		return false;
	}
	
	@Transient
	public boolean isNotBloqueiaVendaMesaOuComanda() {
		return !isBloqueiaVendaMesaOuComanda();
	}
	
	@Transient
	public boolean isBloqueiaVendaDelivery() {
		
		if(Objects.nonNull(getTipoVenda()) && getTipoVenda().equals(ETipoVenda.DELIVERY)) {
			return true;
		}
		
		return false;
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
	public boolean isNotBloqueiaVendaDelivery() {
		return !isBloqueiaVendaDelivery();
	}
	
	@Transient
	public boolean isCancelavel() {
		return !isNovo() && getStatus().equals(EStatus.EM_ABERTO)
				|| !isNovo() && getStatus().equals(EStatus.PAGO_PARCIAL);
	}
	
	@Transient
	public boolean isNotCancelavel() {
		return !isCancelavel();
	}
	
	@Transient
	public boolean isEditavel() {
		return !isNovo() && getStatus().equals(EStatus.EM_ABERTO)
				|| isNovo() && getStatus().equals(EStatus.EM_ABERTO)
				|| !isNovo() && getStatus().equals(EStatus.PAGO_PARCIAL);
	}
	
	@Transient
	public boolean isNotEditavel() {
		return !isEditavel();
	}
	
	@Transient
	public boolean isNotPago() {
		return !isPago();
	}
	
	@Transient
	public Double getDiferenca() {
		Double pago = getValorPago() == null ? new Double(0) : getValorPago(); 
		Double total = getTotal() == null ? new Double(0) : getTotal();
		Double diferenca = total - pago; 
		if(diferenca < BigDecimal.ZERO.doubleValue()) {
			diferenca = BigDecimal.ZERO.doubleValue();
		}
		
		return diferenca;
	}
	
	@Transient
	public boolean isPagavel() {
		return !isNovo() && !getStatus().equals(EStatus.PAGO) && !getStatus().equals(EStatus.CANCELADO)
				&& isNotPago();
	}
	
	@Transient
	public boolean isNotPagavel() {
		return !isPagavel();
	}
}
