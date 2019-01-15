package br.com.siscomanda.bean;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.siscomanda.base.bean.BaseBean;
import br.com.siscomanda.model.DefinicaoGeral;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.service.DefinicaoGeralService;
import br.com.siscomanda.service.VendaMesaComandaService;
import br.com.siscomanda.util.JSFUtil;

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
		int qtdMesasComandas = definicaoGeralService.carregaDefinicaoSistema().getQtdMesaComanda();
		for(int i = 0; i < qtdMesasComandas; i++) {
			mesasComandas.add(i + 1);
		}
		Collections.sort(mesasComandas);
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
