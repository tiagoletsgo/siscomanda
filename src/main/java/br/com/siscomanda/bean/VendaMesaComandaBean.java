package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.service.VendaMesaComandaService;

@Named
@ViewScoped
public class VendaMesaComandaBean extends BaseBean<Venda> implements Serializable {

	private static final long serialVersionUID = -7879816895142165754L;
	
	@Inject
	private VendaMesaComandaService service;
	
	private ItemVenda itemSelecionado;
	
	private List<Integer> mesasComandas;
	
	@Override
	protected void init() {
		mesasComandas = service.geraMesasComandas();
	}
		
	public void btnAdicionaItem(Produto produto) {
		ItemVenda item = service.adicionaItemPedidoVenda(produto);
		item.setId(service.setIdTemporarioItem(getEntity().getItens()));
		service.adicionaItem(getEntity().getItens(), item);
		service.ordenarItemMenorParaMaior(getEntity().getItens());
	}
	
	public void btnRemoveItem(ItemVenda itemVenda, Produto produto) {
		service.removeItem(getEntity().getItens(), itemVenda, produto);
	}
	
	@Override
	protected void beforeSearch() {
	}
	
	public List<Integer> getQuantidadeMesasComandas() {
		return mesasComandas;
	}

	public ItemVenda getItemSelecionado() {
		return itemSelecionado;
	}

	public void setItemSelecionado(ItemVenda itemSelecionado) {
		this.itemSelecionado = itemSelecionado;
	}
}
