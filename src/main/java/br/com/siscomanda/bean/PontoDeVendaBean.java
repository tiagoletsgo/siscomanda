package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.enumeration.EStatus;
import br.com.siscomanda.enumeration.ETipoVenda;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.Preco;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.service.AdicionalService;
import br.com.siscomanda.service.PontoDeVendaService;
import br.com.siscomanda.service.PrecoService;
import br.com.siscomanda.service.ProdutoService;
import br.com.siscomanda.util.JSFUtil;

@Named
@ViewScoped
public class PontoDeVendaBean extends BaseBean<Venda> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean novoItem;
	
	private boolean personalizar;
	
	private Produto produtoSelecionado;
	
	private List<Preco> precos;
	
	private List<Adicional> adicionais;
	
	private List<Adicional> adicionaisSelecionados;
	
	private List<Produto> produtos;
	
	private List<Produto> produtosSelecionados;
	
	@Inject
	private PrecoService precoService;
	
	@Inject
	private AdicionalService adicionalService;
	
	@Inject
	private ProdutoService produtoService;
	
	@Inject
	private PontoDeVendaService pontoDeVendaService;
	
	private ItemVenda item;
	
	private ItemVenda itemSelecionado;
	
	private List<ItemVenda> itensMeioAmeio;
	
	private Preco preco;
	
	private boolean personalizaTemMaisDeUmItem;
	
	private String descricaoProduto;
			
	@Override
	protected void init() {
		getEntity().setTipoVenda(ETipoVenda.COMANDA);
		getEntity().setStatus(EStatus.EM_ABERTO);
		getEntity().setDataHora(new Date());
		getEntity().setSubtotal(new Double(0));
		getEntity().setTaxaEntrega(new Double(0));
		getEntity().setTaxaServico(new Double(0));
		getEntity().setDesconto(new Double(0));
		getEntity().setTotal(new Double(0));
		
		setItem(new ItemVenda());
		setItensMeioAmeio(new ArrayList<ItemVenda>());
	}
	
	public void btnNovoItem(boolean novoItem) {
		if(novoItem) {
			this.novoItem = novoItem;
			getEstadoViewBean().setCurrentView(false, false, false, false);
		}
	}
	
	public void btnIncluir(boolean incluir, Produto produto) {
		try {
			if(incluir) {
				this.novoItem = false;
				this.personalizar = incluir;
				this.descricaoProduto = produto.getDescricao();
				this.precos = precoService.porProduto(produto);
				this.item = pontoDeVendaService.item(getEntity(), produto, item.getValor(), item.getQuantidade());

				setProdutoSelecionado(produto);
				getEstadoViewBean().setCurrentView(false, false, false, false);
			}
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void btnPersonalizar() {
		try {
			List<Preco> precos = precoService.porTamanhoProdutos(produtosSelecionados, getPreco().getTamanho());
			this.item.setValor(getPreco().getPrecoVenda());
			getItensMeioAmeio().add(this.item);
			long id = 1;
			for(Preco preco : precos) {
				ItemVenda item = new ItemVenda(getEntity(), preco.getProduto(), preco.getPrecoVenda(), this.item.getQuantidade(), "");
				item.setId(id);
				getItensMeioAmeio().add(item);
				id++;
			}
			
			if(getItensMeioAmeio().size() > 1) {
				setDescricaoProduto("PIZZA PERSONALIZADA (" + getPreco().getTamanho().getSigla().toUpperCase() + ")");
				this.personalizaTemMaisDeUmItem = true;
			}
			
			atualizarValorTotalItem();
			getProdutosSelecionados().clear();
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void btnRemoverItemPersonalizado(ItemVenda item) {
		if(this.item.getProduto().equals(item.getProduto()) || getItensMeioAmeio().size() == 2) {
			setDescricaoProduto(getProdutoSelecionado().getDescricao());
			getItensMeioAmeio().clear();
			atualizarValorTotalItem();
			return;
		}
		
		getItensMeioAmeio().remove(item);
		atualizarValorTotalItem();
		JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Item removido com sucesso.");
	}
	
	public void atualizarValorTotalItem() {
		try {
			double soma = 0.0;
			
			this.item.setTotal(preco.getPrecoVenda());
			
			if(getItensMeioAmeio().isEmpty()) {
				soma += item.getTotal();
			}
			
			if(getItensMeioAmeio().size() > 1) {
				for(ItemVenda item : getItensMeioAmeio()) {
					getProdutosSelecionados().add(item.getProduto());
				}
				getItensMeioAmeio().clear();
				List<Preco> precos = precoService.porTamanhoProdutos(getProdutosSelecionados(), getPreco().getTamanho());
				long id = 1;
				for(Preco preco : precos) {
					ItemVenda item = new ItemVenda(getEntity(), preco.getProduto(), preco.getPrecoVenda(), this.item.getQuantidade(), "");
					item.setId(id);
					
					soma += item.getTotal();
					getItensMeioAmeio().add(item);
					id++;
				}
			}
			
			item.setTotal(soma);
			if(this.adicionais == null) {
				this.adicionais = adicionalService.todos();
			}
			
			if(this.produtos == null) {
				this.produtos = produtoService.todos();
				this.produtos.remove(produtoSelecionado);
			}
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public String converterValorMonetario(Double valor) {
		return JSFUtil.converterParaValorMonetario(valor);
	}
	
	@Override
	protected void beforeSearch() {
	}

	public boolean isNovoItem() {
		return novoItem;
	}
	
	public boolean isPersonalizar() {
		return personalizar;
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}
	
	public List<Preco> getPrecos() {
		return precos;
	}

	public ItemVenda getItem() {
		return item;
	}

	public void setItem(ItemVenda item) {
		this.item = item;
	}

	public Preco getPreco() {
		return preco;
	}

	public void setPreco(Preco preco) {
		this.preco = preco;
	}
	
	public List<Adicional> getAdicionais() {
		return adicionais;
	}

	public List<Adicional> getAdicionaisSelecionados() {
		return adicionaisSelecionados;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setAdicionaisSelecionados(List<Adicional> adicionaisSelecionados) {
		this.adicionaisSelecionados = adicionaisSelecionados;
	}

	public List<Produto> getProdutosSelecionados() {
		return produtosSelecionados;
	}
	
	public void setProdutosSelecionados(List<Produto> produtosSelecionados) {
		this.produtosSelecionados = produtosSelecionados;
	}
	
	public ItemVenda getItemSelecionado() {
		return itemSelecionado;
	}

	public void setItemSelecionado(ItemVenda itemSelecionado) {
		this.itemSelecionado = itemSelecionado;
	}

	public List<ItemVenda> getItensMeioAmeio() {
		return itensMeioAmeio;
	}

	public void setItensMeioAmeio(List<ItemVenda> itensMeioAmeio) {
		this.itensMeioAmeio = itensMeioAmeio;
	}
	
	public boolean isPersonalizaTemMaisDeUmItem() {
		return personalizaTemMaisDeUmItem;
	}

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}
}
