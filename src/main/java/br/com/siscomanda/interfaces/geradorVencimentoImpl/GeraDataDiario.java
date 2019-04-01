package br.com.siscomanda.interfaces.geradorVencimentoImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.siscomanda.interfaces.GerarVencimento;

public class GeraDataDiario implements GerarVencimento {

	@Override
	public List<Date> frequencia(Date data, int quantidadeRepeticao) {
		List<Date> vencimento = new ArrayList<>();
		for(int i = 0; i < quantidadeRepeticao; i++) {
			data = executarVencimento(data, 1);
			vencimento.add(data);
		}
		return vencimento;
	}
	
	private Date executarVencimento(Date data, int dia) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(data);	
		
		dia += calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, dia);
		return calendar.getTime();
	}
}
