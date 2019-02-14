package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.siscomanda.base.service.VendaService;
import br.com.siscomanda.exception.SiscomandaRuntimeException;
import br.com.siscomanda.model.Adicional;
import br.com.siscomanda.model.Produto;
import br.com.siscomanda.model.SubCategoria;
import br.com.siscomanda.repository.dao.AdicionalDAO;
import br.com.siscomanda.repository.dao.ProdutoDAO;
import br.com.siscomanda.repository.dao.VendaMesaComandaDAO;
import br.com.siscomanda.util.StringUtil;

public class VendaMesaComandaService extends VendaService implements Serializable {

	private static final long serialVersionUID = -7365230528253341931L;
	
	@Inject
	private VendaMesaComandaDAO vendaDAO;
	
	@Inject
	private ProdutoDAO produtoDAO;
	
	@Inject
	private AdicionalDAO adicionalDAO;
	
	public List<Adicional> getAdicionais() {
		List<Adicional> adicionais = adicionalDAO.buscaPor(null);
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
	
	public List<Produto> buscaProduto(String descricao, SubCategoria subCategoria) {
		try {			
			return produtoDAO.buscaPorSubCategoria(descricao, subCategoria);
		}
		catch(Exception e) {
			throw new SiscomandaRuntimeException(e.getMessage());
		}
	}
}
