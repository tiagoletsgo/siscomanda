package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.siscomanda.base.service.VendaOLDService;
import br.com.siscomanda.config.jpa.Transactional;
import br.com.siscomanda.enumeration.EStatus;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.exception.SiscomandaRuntimeException;
import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.model.ItemVendaAdicionalOLD;
import br.com.siscomanda.model.ItemVendaOLD;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.SubCategoria;
import br.com.siscomanda.model.VendaOLD;
import br.com.siscomanda.repository.dao.AdicionalDAO;
import br.com.siscomanda.repository.dao.ItemVendaAdicionalOLDDAO;
import br.com.siscomanda.repository.dao.ProdutoDAO;
import br.com.siscomanda.repository.dao.VendaDAO;
import br.com.siscomanda.util.JSFUtil;

public class VendaMesaComandaOLDService extends VendaOLDService implements Serializable {

	private static final long serialVersionUID = -7365230528253341931L;
	
	@Inject
	private VendaDAO vendaDAO;
	
	@Inject
	private ProdutoDAO produtoDAO;
	
	@Inject
	private AdicionalDAO adicionalDAO;
	
	@Inject
	private ItemVendaAdicionalOLDDAO itemVendaAdicionalDAO;
	
	@Inject
	private ConfiguracaoGeralService definicaoGeralService;
		
	public List<VendaOLD> porFiltro(VendaOLD venda, boolean editando) {
//		return vendaDAO.buscaPor(venda, editando);
		return null;
	}
		
	public List<Adicional> carregaAdicionais(ItemVendaOLD item) {
		return vendaDAO.buscaAdicionalVenda(item);
	}
	
	public List<Integer> geraMesasComandas() {
		List<Integer> mesas = new ArrayList<>();
		int qtdMesasComandas = definicaoGeralService.definicaoSistema().getQtdMesaComanda();
		for(int i = 1; i <= qtdMesasComandas; i++) {
			mesas.add(i);
		}
		
//		List<VendaOLD> vendas = vendaDAO.vendasNaoPagasDiaCorrente();
//		for(VendaOLD venda : vendas) {
//			mesas.remove(new Integer(venda.getMesaComanda()));
//		}
		
		return mesas;
	}
	
	@Transactional
	public VendaOLD salvar(VendaOLD venda) throws SiscomandaException {
		List<ItemVendaOLD> itens = venda.getItens();

		if(venda.getItens().isEmpty()) {
			throw new SiscomandaException("Não é permitido salvar pedido sem itens.");
		}
		
		if(venda.getTotal() < new Double(0)) {
			throw new SiscomandaException("Não é permitido salvar pedido com valor negativo.");
		}
		
		if(venda.getStatus() == null) {
			throw new SiscomandaException("Não é permitido salvar pedido com status em branco.");
		}
		
		if(!venda.isNovo() && venda.getStatus().equals(EStatus.PAGO)) {
			throw new SiscomandaException("Pedido com status pago não pode ser excluído.");
		}
		
		if(!venda.isNovo() && venda.getStatus().equals(EStatus.PAGO_PARCIAL)) {
			throw new SiscomandaException("Pedido com status pago parcial não pode ser excluído.");
		}
		
		if(!venda.isNovo() && venda.getStatus().equals(EStatus.CANCELADO)) {
			throw new SiscomandaException("Pedido com status cancelado não pode ser excluído/alterado.");
		}
				
		if(venda.isNovo()) {
			removeIdTemporario(itens);
//			venda = vendaDAO.salvar(venda);	
			salvarAdicionais(itens, venda);
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso.");
			return venda;
		}
		
//		if(!venda.isNovo()) {			
//			venda = vendaDAO.salvar(venda);
//			itemVendaAdicionalDAO.remove(venda);
//			salvarAdicionais(itens, venda);
//			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro alterado com sucesso.");
//		}
		
//		return venda;
		return null;
	}
	
	@Transactional
	public VendaOLD cancelar(VendaOLD venda) throws SiscomandaException {
		if(venda.getItens().isEmpty()) {
			throw new SiscomandaException("Não é permitido salvar pedido sem itens.");
		}
		
		if(venda.getTotal() < new Double(0)) {
			throw new SiscomandaException("Não é permitido salvar pedido com valor negativo.");
		}
		
		if(venda.getStatus() == null) {
			throw new SiscomandaException("Não é permitido salvar pedido com status em branco.");
		}
		
		if(!venda.isNovo() && venda.getStatus().equals(EStatus.PAGO)) {
			throw new SiscomandaException("Pedido com status pago não pode ser excluído.");
		}
		
		if(!venda.isNovo() && venda.getStatus().equals(EStatus.PAGO_PARCIAL)) {
			throw new SiscomandaException("Pedido com status pago parcial não pode ser excluído.");
		}
		
		venda.setStatus(EStatus.CANCELADO);
//		venda = vendaDAO.salvar(venda);
		
		return venda;
	}
	
	@Transactional
	public void remover(VendaOLD venda) throws SiscomandaException {
		
		if(venda.getStatus().equals(EStatus.PAGO)) {
			throw new SiscomandaException("Pedido com status pago não pode ser excluído.");
		}
		
		if(venda.getStatus().equals(EStatus.PAGO_PARCIAL)) {
			throw new SiscomandaException("Pedido com status pago parcial não pode ser excluído.");
		}
		
		itemVendaAdicionalDAO.remove(venda);
//		vendaDAO.remover(VendaOLD.class, venda.getId());
	}
	
	@Transactional
	private void salvarAdicionais(List<ItemVendaOLD> itens, VendaOLD venda) {
		
		List<ItemVendaOLD> temp = venda.getItens();
		
//		for(ItemVendaOLD item : itens) {
//			for(ItemVendaOLD item2 : temp) {
//				if(item.getProduto().equals(item2.getProduto()) && item.getProduto().isPermiteAdicional()) {
//					item2.setAdicionais(item.getAdicionais());						
//					break;
//				}
//			}
//		}
		
		for(ItemVendaOLD item : temp) {
			for(Adicional adicional : item.getAdicionais()) {
				ItemVendaAdicionalOLD itemAdicional = new ItemVendaAdicionalOLD();
				itemAdicional.setAdicional(adicional);
				itemAdicional.setItemVenda(item);
				itemAdicional.setVenda(venda);
				itemAdicional.setProduto(item.getProduto());
				
				itemVendaAdicionalDAO.salvar(itemAdicional);
			}
		}
	}
		
	public List<Adicional> getAdicionais() {
		List<Adicional> adicionais = adicionalDAO.buscaPor(null);
		return adicionais;
	}
	
	public List<Adicional> buscaAdicionalPor(String descricao) {
		List<Adicional> adicionais = adicionalDAO.buscaPor(descricao);
		return adicionais;
	}
	
	public Produto buscaProduto(Produto produto) {
		try {			
			return produtoDAO.porCodigo(produto);
		}
		catch(Exception e) {
			throw new SiscomandaRuntimeException(e.getMessage());
		}
	}
	
	public List<Produto> buscaProduto(String nomeSubCategoria) {
		return produtoDAO.porDescricaoSubCategoria(nomeSubCategoria);
	}
	
	public List<Produto> buscaProduto(String descricao, SubCategoria subCategoria) {
		try {			
			return produtoDAO.buscaPorSubCategoria(descricao, subCategoria);
		}
		catch(Exception e) {
			throw new SiscomandaRuntimeException(e.getMessage());
		}
	}
}
