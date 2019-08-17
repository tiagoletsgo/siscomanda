package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.siscomanda.builder.VendaBuilder;
import br.com.siscomanda.config.jpa.Transactional;
import br.com.siscomanda.enumeration.EStatus;
import br.com.siscomanda.enumeration.ETipoVenda;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.model.ConfiguracaoGeral;
import br.com.siscomanda.model.ItemVenda;
import br.com.siscomanda.model.Preco;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.Tamanho;
import br.com.siscomanda.model.Venda;
import br.com.siscomanda.repository.dao.PrecoDAO;
import br.com.siscomanda.repository.dao.ProdutoDAO;
import br.com.siscomanda.repository.dao.VendaDAO;
import br.com.siscomanda.util.JSFUtil;

public class PontoDeVendaService implements Serializable {

	private static final long serialVersionUID = -8887278588654970978L;
	
	@Inject
	private PrecoDAO precoDAO;
	
	@Inject
	private ProdutoDAO produtoDAO;
	
	@Inject
	private VendaDAO vendaDAO;
	
	private List<Produto> produtosTemp = new ArrayList<Produto>();
	
	public void validaSituacaoVenda(Venda venda) throws SiscomandaException {
		if(venda.getItens().isEmpty()) {
			throw new SiscomandaException("Não é permitido salvar uma venda sem itens.");
		}
		
		if(venda.getTotal() < new Double(0)) {
			throw new SiscomandaException("Não é permitido salvar uma venda com valor negativo.");
		}
		
		if(venda.getStatus() == null) {
			throw new SiscomandaException("Não é permitido salvar uma venda com status em branco.");
		}
		
		if(!venda.isNovo() && venda.getStatus().equals(EStatus.PAGO)) {
			throw new SiscomandaException("Venda com status pago não pode ser alterado/excluído.");
		}
		
		if(!venda.isNovo() && venda.getStatus().equals(EStatus.PAGO_PARCIAL)) {
			throw new SiscomandaException("Venda com status pago parcial não pode ser excluído.");
		}
		
		if(!venda.isNovo() && venda.getStatus().equals(EStatus.CANCELADO)) {
			throw new SiscomandaException("Venda com status cancelado não pode ser excluído/alterado.");
		}
	}
	
	@Transactional
	public void cancelar(Venda venda) throws SiscomandaException {
		try {
			validaSituacaoVenda(venda);
			venda.setStatus(EStatus.CANCELADO);
			vendaDAO.salvar(venda);
			
		} catch (SiscomandaException e) {
			throw new SiscomandaException(e.getMessage());
		}
	}
	
	@Transactional
	public void remover(Venda venda) throws SiscomandaException {
		try {
			validaSituacaoVenda(venda);
			
			if(Objects.isNull(venda.getId())) {
				throw new SiscomandaException("Erro ao remover, Venda sem código de identificação.");
			}
			
			vendaDAO.remover(Venda.class, venda.getId());
			
		} catch (SiscomandaException e) {
			throw new SiscomandaException(e.getMessage());
		}
	}
	
	public List<Venda> buscarPor(Map<String, Object> filter) {
		return vendaDAO.buscarPor(filter);
	}
	
	public List<Venda> vendasNaoPagas() {
		return vendaDAO.naoPagas();
	}
	
	public Venda porCodigo(Long codigo) throws SiscomandaException {
		return vendaDAO.porCodigo(codigo);
	}
	
	@Transactional
	public Venda salvar(Venda venda) throws SiscomandaException {
		validaSituacaoVenda(venda);
		if(venda.isNovo()) {
			venda = vendaDAO.salvar(venda);
			JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso.");
			return venda;
		}
		
		JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, "Registro alterado com sucesso.");
		return vendaDAO.salvar(venda);
	}
			
	public ItemVenda paraItemVenda(Produto produto) {
		return new ItemVenda(produto, 0D, 1D, "");
	}
	
	public List<Adicional> desmarcarListaDeComplementos(List<Adicional> complementos, List<ItemVenda> itens, ItemVenda item) {
		for(Adicional complemento : complementos) {
			complemento.setSelecionado(false);
		}
		
		if(itens.isEmpty()) {
			for(Adicional complemento : complementos) {
				if(item.getAdicionais().contains(complemento)) {
					complemento.setSelecionado(true);
				}
			}
		}
		
		return complementos;
	}
	
	public List<ItemVenda> atualizaListaItemMeioAmeio(List<ItemVenda> itens, Tamanho tamanho) throws SiscomandaException {
		List<Produto> produtos = new ArrayList<Produto>();
		double quantidadeItens = new Double(1) / new Double(itens.size());
		if(!itens.isEmpty()) {
			for(ItemVenda item : itens) {
				produtos.add(item.getProduto());
			}
			
			List<Preco> precos = precoDAO.porTamanhoProduto(produtos, tamanho);
			
			if(precos.isEmpty()) {
				throw new SiscomandaException("Não foi possivel atualizar a lista de itens meio a meio.");
			}
			
			for(Preco preco : precos) {
				for(ItemVenda item : itens) {
					if(item.getProduto().equals(preco.getProduto())) {							
						item.setValor(preco.getPrecoVenda());
						item.setTotal(item.getValor() * quantidadeItens);
					}
				}
			}			
		}
				
		return itens;
	}
	
	public Double calcularValorTotal(List<ItemVenda> itens, ItemVenda item) {
		double total = itens.isEmpty() ? item.getValor() : 0;
		double totalComplemento = 0;
		double quantidadeItens = !itens.isEmpty() ? (new Double(1) / new Double(itens.size())) : 1;
		
		if(!itens.isEmpty()) {
			for(ItemVenda iten : itens) {
				if(!iten.getAdicionais().isEmpty()) {
					for(Adicional complemento : iten.getAdicionais()) {
						totalComplemento += complemento.getPrecoVenda();
					}
				}
				total += iten.getValor();
			}
		}
		else 
			if(itens.isEmpty()) {
				for(Adicional complemento : item.getAdicionais()) {
					totalComplemento += complemento.getPrecoVenda();
				}
		}
		
		if(item.getQuantidade() == null || item.getQuantidade().equals(new Double(0))) {
			item.setQuantidade(1D);
		}
		
		total = (total * quantidadeItens) * item.getQuantidade();
		return total + totalComplemento;
	}
	
	public List<ItemVenda> removerItem(List<ItemVenda> itens, ItemVenda item, Produto produto) throws SiscomandaException {
		if(!itens.contains(item)) {
			throw new SiscomandaException("Este item não foi encontrado na lista.");
		}
		
		if(itens.size() == 2 || item.getProduto().equals(produto)) {
			itens.clear();
		}
		else {			
			itens.remove(item);
		}
		
		return itens;
	}
	
	public List<Adicional> atualizaListaDeComplementos(ItemVenda itemSelecionado, ItemVenda itemPrincipal, List<Adicional> complementos) {
		if(itemSelecionado == null) {
			itemSelecionado = itemPrincipal;
		}
		
		if(!itemSelecionado.getAdicionais().isEmpty()) {
			for(Adicional complemento : complementos) {
				if(itemSelecionado.getAdicionais().contains(complemento)) {
					complemento.setSelecionado(true);
				}
				else {
					complemento.setSelecionado(false);
				}
			}
		}
		else {
			for(Adicional complement : complementos) {
				complement.setSelecionado(false);
			}
		}
		
		return complementos;
	}
	
	public List<ItemVenda> adicionaComplemento(ItemVenda itemSelecionado, ItemVenda itemPrincipal, Adicional complemento, List<Adicional> complementos, List<ItemVenda> itens) throws SiscomandaException {
		if(itemSelecionado == null && !itens.isEmpty()) {
			for(Adicional adicional : complementos) {
				adicional.setSelecionado(false);
			}
			throw new SiscomandaException("Antes de incluir um complemento por gentileza, selecione um item da lista de itens meio a meio.");
		}
		
		if(itemSelecionado == null && complemento.isSelecionado()) {
			itemSelecionado = itemPrincipal;
			itens.add(itemSelecionado);
		}
		
		if(complemento.isSelecionado()) {
			for(Adicional adicional: complementos) {
				if(adicional.isSelecionado()) {
					if(!itemSelecionado.getAdicionais().contains(adicional)) {					
						itemSelecionado.getAdicionais().add(adicional);
						adicional.setSelecionado(false);
					}
				}
			}
			
			for(ItemVenda iten : itens) {
				if(iten.equals(itemSelecionado)) {
					iten = itemSelecionado;
				}
			}
		}
		else if(itemSelecionado != null) {
			itemSelecionado.getAdicionais().remove(complemento);
		}
		else {
			itemPrincipal.getAdicionais().remove(complemento);
			itens.add(itemPrincipal);
		}
		
		return itens;
	}
	
	public List<ItemVenda> personalizar(List<Produto> produtos, List<ItemVenda> itens, ItemVenda item) throws SiscomandaException {
		
		if(produtos != null) {
			for(Produto produto : produtos) {
				for(ItemVenda iten : itens) {
					if(iten.getProduto().equals(produto)) {
						throw new SiscomandaException("Este(s) produto (s) já se encontra(m) incluido(s).");
					}
				}
			}
			
			Map<ItemVenda, List<Adicional>> adicionais = new HashMap<ItemVenda, List<Adicional>>();
			Map<ItemVenda, String> observacoes = new HashMap<ItemVenda, String>();
			
			if(itens.isEmpty()) {
				itens.add(item);
			}
			
			// para manter dados que ja existam
			for(ItemVenda iten : itens) {
				produtos.add(iten.getProduto());
				adicionais.put(iten, iten.getAdicionais());
				observacoes.put(iten, iten.getObservacao());
			}
			
			itens.clear();
			
			if(!produtos.contains(item.getProduto())) {					
				produtos.add(item.getProduto());
			}
			
			List<Preco> precos = precoDAO.porTamanhoProduto(produtos, item.getTamanho());
			Double quantidade = new Double(1) / new Double(produtos.size());
			
			Tamanho tamanho = item.getTamanho();
			item = new ItemVenda(item.getProduto(), item.getValor(), quantidade, "");
			itens.add(item);
			
			for(Preco preco : precos) {
				for(Produto produto : produtos) {
					if(produto.equals(preco.getProduto())) {
						ItemVenda itemFilho = new ItemVenda(produto, (preco.getPrecoVenda()), quantidade, "");
						
						item.setTamanho(tamanho);
						itemFilho.setTamanho(tamanho);

						if(!item.getProduto().equals(itemFilho.getProduto())) {
							item.adicionaItemFilho(itemFilho);
							itens.add(itemFilho);
						}
					}
				}
			}
			
			// recupera os adicionais
			for(Map.Entry<ItemVenda, List<Adicional>> entry : adicionais.entrySet()) {
				for(ItemVenda iten : itens) {
					if(iten.getProduto().equals(entry.getKey().getProduto())) {
						iten.setAdicionais(entry.getValue());
					}
				}
			}
			
			// recupera as observaçoes
			for(Map.Entry<ItemVenda, String> entry : observacoes.entrySet()) {
				for(ItemVenda iten : itens) {
					if(iten.getProduto().equals(entry.getKey().getProduto())) {
						iten.setObservacao(entry.getValue());
					}
				}
			}
		}
		
		return itens;
	}
	
	public List<ItemVenda> listaItemMeioAmeioTiverUmRegistroRemovaTudo(List<ItemVenda> itens) {
		if(itens.size() == 1) {
			itens.clear();
		}
		return itens;
	}
	
	public List<ItemVenda> incluirObservacao(List<ItemVenda> itens, ItemVenda itemSelecionado, String observacao) {
		if(!itens.isEmpty()) {
			for(ItemVenda item : itens) {
				if(item.equals(itemSelecionado)) {
					item.setObservacao(observacao);
					break;
				}
			}
		}
		
		return itens;
	}
	
	public String observacao(List<ItemVenda> itens, ItemVenda item) {
		String observacao = "";
		
		if(itens.isEmpty()) {
			observacao = item.getObservacao();
		}
		else {
			for(ItemVenda itemm : itens) {
				if(itemm.getProduto().equals(item.getProduto())) {
					observacao = itemm.getObservacao();
				}
			}
		}
		
		return observacao;
	}
	
	public List<Produto> localizaProdutoParaPersonalizar(List<Produto> produtos, Produto produto, String valorParaPesquisar) {
		List<Produto> produtosLocalizado = new ArrayList<Produto>();
		
		if(!valorParaPesquisar.isEmpty()) {
			for(Produto prod : produtos) {
				if(prod.getDescricao().contains(valorParaPesquisar.toUpperCase())) {
					produtosLocalizado.add(prod);
				}
			}
		}
		
		if(!produtosLocalizado.isEmpty()) {
			return produtosLocalizado;
		}
		
		produtos = produtoDAO.todos(true);
		produtos.remove(produto);
		
		return produtos;
	}
	
	public List<Produto> atualizaListaProdutoSelecionado(List<Produto> listaProdutoPrincipal, List<Produto> listProdutoSelecionado) {
		if(listProdutoSelecionado != null) {	
			for(Produto produto : listProdutoSelecionado) {
				if(!listaProdutoPrincipal.contains(produto)) {
					listaProdutoPrincipal.add(produto);
				}
			}
		}
		
		return listaProdutoPrincipal;
	}
	
	public List<Produto> validaQuantidadePermitida(List<Produto> produtos, ConfiguracaoGeral configuracao) throws SiscomandaException {
		Produto produtoCheck = new Produto();
		
		if(produtos.size() < produtosTemp.size()) {
			produtosTemp.clear();
			produtosTemp.addAll(produtos);
		}
		
		if(produtos.size() > produtosTemp.size()) {
			for(Produto produto : produtos) {
				if(!produtosTemp.contains(produto)) {
					produtoCheck = produto;
					produtosTemp.add(produtoCheck);
					break;
				}
			}
		}
		
		if(produtos.size() > configuracao.getPermiteQuantoSabores()) {
			produtosTemp.remove(produtoCheck);
			produtos.remove(produtoCheck);
 			throw new SiscomandaException("A quantidade de sabores selecionado excede o limite configurado.");
		}
		
		return produtos;
	}
	
	public List<ETipoVenda> tiposDeVenda(ConfiguracaoGeral configuracao) {
		List<ETipoVenda> tiposDeVenda = new ArrayList<ETipoVenda>();
		tiposDeVenda.add(ETipoVenda.DELIVERY);
		tiposDeVenda.add(ETipoVenda.BALCAO);
		
		if(configuracao != null && configuracao.getSistema() != null) {
			if(configuracao.getSistema().getDescricao().toUpperCase().equals(ETipoVenda.COMANDA.toString())) {
				tiposDeVenda.add(ETipoVenda.COMANDA);
			}
			else {
				tiposDeVenda.add(ETipoVenda.MESA);
			}
		}
		
		return tiposDeVenda;
	}
	
	public List<Integer> gerarControladores(ConfiguracaoGeral configuracao) {
		List<Integer> controladores = new ArrayList<Integer>();
		
		for(int i = 1; i <= configuracao.getQtdMesaComanda(); i++) {
			controladores.add(new Integer(i));
		}
		
		return controladores;
	}
	
	public VendaBuilder confirmarItem(VendaBuilder builder, List<ItemVenda> itens, ItemVenda item, boolean atualizar) {
		List<ItemVenda> listItens = itens.isEmpty() ? Arrays.asList(item) : itens;
		
		if(!atualizar) {
			builder.comItens(listItens);
		}
		
		atualizar = false;
		itens = new ArrayList<ItemVenda>();
		
		return builder;
	}
	
	public List<Adicional> carregarItensComplementares(List<Adicional> adicionais, ItemVenda item) {
		List<Adicional> complementos = new ArrayList<Adicional>();
		
		if(item.getTamanho().isPermiteMeioAmeio()) {
			for(Adicional adicional : item.getAdicionais()) {
				adicional.setSelecionado(true);
			}
			
			for(Adicional adicional : adicionais) {
				for(Adicional complemento : item.getAdicionais()) {
					if(adicional.equals(complemento) && complemento.isSelecionado()) {
						adicional.setSelecionado(true);
					}
				}
				complementos.add(adicional);
			}
		}
		
		return complementos;
	}
	
	public Venda salvarTipoVenda(Venda venda) throws SiscomandaException {
		
		if(venda.getTipoVenda() == null) {
			throw new SiscomandaException("Para continuar é necessário informar o tipo venda.");
		}
		
		if(venda.getTipoVenda().equals(ETipoVenda.BALCAO)
				|| venda.getTipoVenda().equals(ETipoVenda.DELIVERY)) {
			venda.setControle(new Integer(999999));
		}
		
		if(venda.getTipoVenda().equals(ETipoVenda.BALCAO)
				|| venda.getTipoVenda().equals(ETipoVenda.MESA)) {
			venda.setCliente(null);
			venda.setTaxaEntrega(0D);
		}
		
		if(Objects.nonNull(venda.getCliente())
				&& Objects.nonNull(venda.getCliente().getServico())) {
			venda.setTaxaEntrega(venda.getCliente().getServico().getValor());
		}
		
		if(venda.getTipoVenda().equals(ETipoVenda.DELIVERY)	&& venda.getCliente() == null) {
			throw new SiscomandaException("Para o tipo de venda Delivery é necessário informar o cliente. Por gentileza informe o cliente, para continuar.");
		}
		
		return venda;
	}
}
