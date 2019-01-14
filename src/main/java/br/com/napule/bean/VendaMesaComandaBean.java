package br.com.napule.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.napule.base.bean.BaseBean;
import br.com.napule.config.jsf.JSFUtil;
import br.com.napule.model.DefinicaoGeral;
import br.com.napule.model.ItemVenda;
import br.com.napule.model.Produto;
import br.com.napule.model.Venda;
import br.com.napule.service.DefinicaoGeralService;
import br.com.napule.service.VendaMesaComandaService;

@Named
@ViewScoped
public class VendaMesaComandaBean extends BaseBean<Venda> implements Serializable {

	private static final long serialVersionUID = -7879816895142165754L;
	
	@Inject
	private DefinicaoGeralService definicaoGeralService;
	
	@Inject
	private VendaMesaComandaService service;
	
	private List<Integer> mesasComandas;
	
	@Override
	protected void init() {
		DefinicaoGeral definicao = definicaoGeralService.carregaDefinicaoSistema();
		System.out.println("teste...");
//		int qtdMesasComandas = definicaoGeralService.carregaDefinicaoSistema().getQtdMesaComanda();
//		for(int i = 0; i < qtdMesasComandas; i++) {
//			mesasComandas.add(i + 1);
//		}
	}
	
	public void carregaMesasComandas() {
		System.out.println("teste...");
	}
	
	public void btnAdicionaItem(Produto produto) {
		ItemVenda item = service.adicionaItemPedidoVenda(produto);
		item.setId(service.setIdTemporarioItem(getEntity().getItens()));
		getEntity().getItens().add(item);
		
		service.ordenarItemMenorParaMaior(getEntity().getItens());
	}
	
	public void btnRemoveItem(ItemVenda item) {
		getEntity().getItens().remove(item);
		JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro removido com sucesso.");
	}
	
	@Override
	protected void beforeSearch() {
	}
	
	public List<Integer> getQuantidadeMesasComandas() {
		return mesasComandas;
	}
}
