package br.com.siscomanda.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import br.com.siscomanda.model.Venda;
import br.com.siscomanda.repository.dao.VendaDAO;
import br.com.siscomanda.vo.TotalizadorVO;

public class DashboardService implements Serializable {
	
	private static final long serialVersionUID = -1681105702788568103L;
	
	@Inject
	private VendaDAO vendaDAO;
	
	public TotalizadorVO totalizador() {
		List<Venda> vendas = vendaDAO.porData(new Date(), new Date());
		return new TotalizadorVO(vendas);
	}
	
	
}
