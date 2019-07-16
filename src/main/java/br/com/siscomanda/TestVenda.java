package br.com.siscomanda;

import java.util.Date;

import br.com.siscomanda.builder.VendaBuilder;
import br.com.siscomanda.enumeration.EStatus;
import br.com.siscomanda.model.Venda;

public class TestVenda {
	public static void main(String[] args) {
		VendaBuilder builder = new VendaBuilder();
		builder.comDataHora(new Date())
		.comDesconto(new Double(0))
		.comNumeroPedido(new Integer(001))
		.comOperador("administrador")
		.comStatus(EStatus.EM_ABERTO)
		.comTaxaServico(new Double(0))
		.comDesconto(new Double(0));
//		.comItem(new ItemVenda(builder.constroi(), new Produto(), new Double(100), new Double(1), "observacao"))
//		.comItem(new ItemVenda(builder.constroi(), new Produto(), new Double(200), new Double(0), "observacao"))
//		.comItem(new ItemVenda(builder.constroi(), new Produto(), new Double(300), new Double(1), "observacao"))
//		.comItem(new ItemVenda(builder.constroi(), new Produto(), new Double(400), new Double(1), "observacao"));
		
		Venda venda = builder.constroi();
		System.out.println(venda.getTotal());
	}
}
