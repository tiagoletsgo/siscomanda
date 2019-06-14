package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.enumeration.EStatus;
import br.com.siscomanda.enumeration.ETipoVenda;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.model.Cliente;
import br.com.siscomanda.model.ItemVendaOLD;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.VendaOLD;
import br.com.siscomanda.service.ClienteService;
import br.com.siscomanda.service.VendaMesaComandaOLDService;
import br.com.siscomanda.util.JSFUtil;
import br.com.siscomanda.util.StringUtil;

@Named
@ViewScoped
public class VendaMesaComandaBean extends BaseBean<VendaOLD> implements Serializable {

	private static final long serialVersionUID = -7879816895142165754L;
	
	@Inject
	private VendaMesaComandaOLDService service;
	
	@Inject
	private ClienteService clienteService;
	
	private ItemVendaOLD itemSelecionado;
	
	private Double quantidade;
	
	private List<Integer> mesasComandas;
	
	private List<Adicional> adicionais;
	
	private List<Adicional> selectManyCheckBoxAdicionais;

	private Produto produtoSelecionado;
	
	private Adicional adicionalSelecionado;

	private List<Produto> produtos;
	
	private String filterPesquisar;
	
	private Integer mesaComanda;
	
	private ETipoVenda tipoVenda;
	
	@Override
	protected void init() {
		initValores();
		
		produtos = service.buscaProduto("PIZZA");
		adicionais = service.getAdicionais();
		
		beforeSearch();
	}
	
	private void initValores() {
		getEntity().setIniciado(new Date());
		getEntity().setSubtotal(new Double(0));
		getEntity().setTotal(new Double(0));
		getEntity().setTaxaServico(new Double(0));
		getEntity().setTaxaEntrega(new Double(0));
		getEntity().setDesconto(new Double(0));
		getEntity().setValorPago(new Double(0));
		getEntity().setMesaComanda(new Integer(0));
		setQuantidade(new Double(1));
		
		mesasComandas = service.geraMesasComandas();
		carregaVendaPorParamentro();
	}
	
	private void carregaVendaPorParamentro() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext external = context.getExternalContext();
		String id = external.getRequestParameterMap().get("codigo");
		if(id != null) {
			Long codigo = Long.parseLong(id);
			getEntity().setId(codigo);
			setEntity(service.porFiltro(getEntity(), true).get(0));
			mesaComanda = getEntity().getMesaComanda();
			
			for(ItemVendaOLD item : getEntity().getItens()) {
				for(Adicional adicional : service.carregaAdicionais(item)) {
					adicional.setQuantidade(new Double(1));
					item.getAdicionais().add(adicional);
				}
			}
			
			getEstadoViewBean().setCurrentView(true, false, false, false);
		}
	}
	
	private void setMesaComanda() {
		getEntity().setMesaComanda(mesaComanda == null ? getEntity().getMesaComanda() : mesaComanda);
	}
	
	public void btnAtualizaHeaderPedido() {
		try {
			
			if(getTipoVenda() == null) {
				JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao atualizar capa do pedido. É necessário informar o tipo de venda.");
				btnNovo();
				return;
			}
			
			mesaComanda = 999999;
			Double taxaEntrega = new Double(0);
			Cliente cliente = null;

			if(getEntity().getCliente() != null) {
				cliente = clienteService.porCodigo(getEntity().getCliente());
				taxaEntrega = cliente.getServico().getValor();
			}
			
			getEntity().setMesaComanda(mesaComanda);
			getEntity().setCliente(cliente);
			getEntity().setTaxaEntrega(taxaEntrega);
			getEntity().setTipoVenda(getTipoVenda());
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao atualizar capa do pedido. " + e.getMessage());
		}
		
	}
	
	public void btnSalvar() {		
		try {
			setMesaComanda();
			
			getEntity().setDataVenda(new Date());
			service.validaQuantidadeTotalItens(getEntity().getItens());
			setEntity(service.salvar(getEntity()));
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar. " + e.getMessage());
		}
	}
	
	public void btnExcluir() {
		
		try {
			service.remover(getEntity());
			
			setEntity(new VendaOLD());
			initValores();
			
			getEstadoViewBean().setCurrentView(false, false, false, true);
			beforeSearch();
			
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro removido com sucesso.");
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover. " + e.getMessage());
		}
	}
	
	public void btnAdicionaItem() {
		try {
			
			setMesaComanda();
			
			service.incluirItem(getEntity(), getSelectManyCheckBoxAdicionais(), getItemSelecionado(), getProdutoSelecionado(), getQuantidade());
			afterAction();
			
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso.");
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar. " + e.getMessage());
		}
	}
		
	public void btnRemoveItem() {
		try {
			ItemVendaOLD item = service.clonar(produtoSelecionado, getSelectManyCheckBoxAdicionais());
			
			if(item == null) {
				item = service.clonar(itemSelecionado.getProduto(), itemSelecionado.getAdicionais());
				item.setId(itemSelecionado.getId());
				item.setQuantidade(itemSelecionado.getQuantidade());
			}
			
			List<ItemVendaOLD> itens = service.removeItem(getEntity().getItens(), item, quantidade);
			getEntity().setItens(itens);
			afterAction();
			
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro removido com sucesso.");
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover. " + e.getMessage());
		}
	}
	
	public void btnRemoveAdicional() {
		itemSelecionado.getAdicionais().remove(adicionalSelecionado);
		afterAction();
		
		JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro removido com sucesso.");
	}
	
	public void btnPesquisar() {
		setElements(service.porFiltro(getEntity(), true));
	}
	
	@Override
	public void btnNovo() {
		setEntity(new VendaOLD()); 
		
		initValores();
		getEntity().setStatus(EStatus.EM_ABERTO);
		getEntity().setTipoVenda(ETipoVenda.MESA_COMANDA);
		getEstadoViewBean().setCurrentView(true, false, false, false);
	}
	
	@Override
	public void btnCancelar() {
		try {
			setMesaComanda();
			
			setEntity(service.cancelar(getEntity()));
			
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro cancelado com sucesso.");
		}
		catch(SiscomandaException e) {
			JSFUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao cancelar. " + e.getMessage());
		}
	}
	
	public void editar(VendaOLD venda) {
		
		setEntity(service.porFiltro(venda, true).get(0));
		mesaComanda = venda.getMesaComanda();
		for(ItemVendaOLD item : getEntity().getItens()) {
			for(Adicional adicional : service.carregaAdicionais(item)) {
				adicional.setQuantidade(new Double(1));
				item.getAdicionais().add(adicional);
			}
		}
		
		getEstadoViewBean().setCurrentView(true, false, false, false);
	}
	
	public void ajaxPesquisaAdicional() {
		adicionais = service.buscaAdicionalPor(filterPesquisar);
	}
		
	private void afterAction() {
		getEntity().setSubtotal(service.calculaSubtotal(getEntity().getItens()));
		getEntity().setTaxaServico(getEntity().getSubtotal() * service.getTaxaServico());		
		getEntity().setTotal(service.calculaTotal(getEntity()));
		setQuantidade(new Double(1));
		
		filterPesquisar = null;
		itemSelecionado = null;
		produtoSelecionado = null;
		selectManyCheckBoxAdicionais = new ArrayList<>();
	}
	
	public EStatus[] getStatusVenda() {
		return EStatus.values();
	}
	
	public Double calculaSubTotalItem(ItemVendaOLD item) {
		return service.calculaSubTotalItem(item);
	}
	
	@Override
	protected void beforeSearch() {
		if(getEstadoViewBean().getSearch()) {
			setElements(service.porFiltro(getEntity(), false));
		}
	}
	
	public List<Integer> getQuantidadeMesasComandas() {
		return mesasComandas;
	}

	public ItemVendaOLD getItemSelecionado() {
		getEntity();
		if(itemSelecionado == null) {
			itemSelecionado = new ItemVendaOLD();
		}
		return itemSelecionado;
	}
	
	public String formatMoeda(Double valor) {
		return StringUtil.parseDouble(valor);
	}
	
	public String converter(Double valor) {
		return StringUtil.converterDouble(valor);
	}

	public void setItemSelecionado(ItemVendaOLD itemSelecionado) {
		this.itemSelecionado = itemSelecionado;
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}

	public Double getQuantidade() {
		return quantidade;
	}
	
	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public List<Adicional> getSelectManyCheckBoxAdicionais() {
		return selectManyCheckBoxAdicionais;
	}

	public void setSelectManyCheckBoxAdicionais(List<Adicional> selectManyCheckBoxAdicionais) {
		this.selectManyCheckBoxAdicionais = selectManyCheckBoxAdicionais;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}
	
	public List<Adicional> getAdicionais() {
		return adicionais;
	}

	public String getFilterPesquisar() {
		return filterPesquisar;
	}

	public void setFilterPesquisar(String filterPesquisar) {
		this.filterPesquisar = filterPesquisar;
	}

	public Adicional getAdicionalSelecionado() {
		return adicionalSelecionado;
	}

	public void setAdicionalSelecionado(Adicional adicionalSelecionado) {
		this.adicionalSelecionado = adicionalSelecionado;
	}
	
	public List<ETipoVenda> getTipoVendas() {
		List<ETipoVenda> tipoVendas = new ArrayList<>();
		if(getEstadoViewBean().getSearch()) {
			tipoVendas.add(ETipoVenda.MESA_COMANDA);
		}
		tipoVendas.add(ETipoVenda.BALCAO);
		tipoVendas.add(ETipoVenda.DELIVERY);		
		return tipoVendas;
	}

	public void setTipoVenda(ETipoVenda tipoVenda) {
		this.tipoVenda = tipoVenda;
	}
	
	public ETipoVenda getTipoVenda() {
		return tipoVenda;
	}
}
