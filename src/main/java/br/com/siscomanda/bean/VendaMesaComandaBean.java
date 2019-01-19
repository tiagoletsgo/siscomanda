package br.com.siscomanda.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.enumeration.EStatus;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.service.VendaMesaComandaService;
import br.com.siscomanda.util.JSFUtil;

@Named
@ViewScoped
public class VendaMesaComandaBean extends BaseBean<Venda> implements Serializable {

	private static final long serialVersionUID = -7879816895142165754L;
	
	@Inject
	private VendaMesaComandaService service;
	
	private ItemVenda itemSelecionado;
	
	private Integer quantidade;
	
	private List<Integer> mesasComandas;
	
	@Override
	protected void init() {
		if(mesasComandas == null || mesasComandas.isEmpty()) {			
			mesasComandas = service.geraMesasComandas();
		}
		quantidade = new BigDecimal(1).intValue();
		getEntity().setStatus(EStatus.EM_ABERTO);
		getEntity().setIniciado(new Date());
	}
		
	public void btnAdicionaItem(Produto produto) {
		ItemVenda item = service.adicionaItemPedidoVenda(produto);
		item.setId(service.setIdTemporarioItem(getEntity().getItens()));
		service.adicionaItem(getEntity().getItens(), item);
		service.ordenarItemMenorParaMaior(getEntity().getItens());
	}
	
	public void btnRemoveItem(ItemVenda itemVenda, Produto produto) {
		try {
			itemVenda = service.clonaItemVenda(itemVenda, produto, getQuantidade());
			service.removeItem(getEntity().getItens(), itemVenda, produto);
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	@Override
	protected void beforeSearch() {
	}
	
	public List<Integer> getQuantidadeMesasComandas() {
		return mesasComandas;
	}

	public ItemVenda getItemSelecionado() {
		getEntity();
		if(itemSelecionado == null) {
			itemSelecionado = new ItemVenda();
		}
		return itemSelecionado;
	}

	public void setItemSelecionado(ItemVenda itemSelecionado) {
		this.itemSelecionado = itemSelecionado;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
}
