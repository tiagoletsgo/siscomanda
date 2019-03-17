package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.siscomanda.base.service.VendaService;
import br.com.siscomanda.config.jpa.Transactional;
import br.com.siscomanda.enumeration.EStatus;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.exception.SiscomandaRuntimeException;
import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.ItemVendaAdicional;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.SubCategoria;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.repository.dao.AdicionalDAO;
import br.com.siscomanda.repository.dao.ItemVendaAdicionalDAO;
import br.com.siscomanda.repository.dao.ProdutoDAO;
import br.com.siscomanda.repository.dao.VendaDAO;
import br.com.siscomanda.util.JSFUtil;

public class VendaMesaComandaService extends VendaService implements Serializable {

	private static final long serialVersionUID = -7365230528253341931L;
	
	@Inject
	private VendaDAO vendaDAO;
	
	@Inject
	private ProdutoDAO produtoDAO;
	
	@Inject
	private AdicionalDAO adicionalDAO;
	
	@Inject
	private ItemVendaAdicionalDAO itemVendaAdicionalDAO;
	
	@Inject
	private DefinicaoGeralService definicaoGeralService;
		
	public List<Venda> porFiltro(Venda venda, boolean editando) {
		return vendaDAO.buscaPor(venda, editando);
	}
		
	public List<Adicional> carregaAdicionais(ItemVenda item) {
		return vendaDAO.buscaAdicionalVenda(item);
	}
	
	public List<Integer> geraMesasComandas() {
		List<Integer> mesas = new ArrayList<>();
		int qtdMesasComandas = definicaoGeralService.carregaDefinicaoSistema().getQtdMesaComanda();
		for(int i = 1; i <= qtdMesasComandas; i++) {
			mesas.add(i);
		}
		
		List<Venda> vendas = vendaDAO.vendasNaoPagasDiaCorrente();
		for(Venda venda : vendas) {
			mesas.remove(new Integer(venda.getMesaComanda()));
		}
		
		return mesas;
	}
	
	@Transactional
	public Venda salvar(Venda venda) throws SiscomandaException {
		List<ItemVenda> itens = venda.getItens();

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
			venda = vendaDAO.salvar(venda);	
			salvarAdicionais(itens, venda);
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso.");
			return venda;
		}
		
		if(!venda.isNovo()) {			
			venda = vendaDAO.salvar(venda);
			itemVendaAdicionalDAO.remove(venda);
			salvarAdicionais(itens, venda);
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro alterado com sucesso.");
		}
		
		return venda;
	}
	
	@Transactional
	public Venda cancelar(Venda venda) throws SiscomandaException {
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
		venda = vendaDAO.salvar(venda);
		
		return venda;
	}
	
	@Transactional
	public void remover(Venda venda) throws SiscomandaException {
		
		if(venda.getStatus().equals(EStatus.PAGO)) {
			throw new SiscomandaException("Pedido com status pago não pode ser excluído.");
		}
		
		if(venda.getStatus().equals(EStatus.PAGO_PARCIAL)) {
			throw new SiscomandaException("Pedido com status pago parcial não pode ser excluído.");
		}
		
		itemVendaAdicionalDAO.remove(venda);
		vendaDAO.remover(Venda.class, venda.getId());
	}
	
	@Transactional
	private void salvarAdicionais(List<ItemVenda> itens, Venda venda) {
		
		List<ItemVenda> temp = venda.getItens();
		
		for(ItemVenda item : itens) {
			for(ItemVenda item2 : temp) {
				if(item.getProduto().equals(item2.getProduto()) && item.getProduto().isPermiteAdicional()) {
					item2.setAdicionais(item.getAdicionais());						
					break;
				}
			}
		}
		
		for(ItemVenda item : temp) {
			for(Adicional adicional : item.getAdicionais()) {
				ItemVendaAdicional itemAdicional = new ItemVendaAdicional();
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
