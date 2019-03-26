package br.com.siscomanda.interfaces.geradorVencimentoImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.siscomanda.interfaces.GerarVencimento;

public class GeraDataMensalService implements GerarVencimento {

	@Override
	public List<Date> frequencia(Date data, int quantidadeRepeticao) {
		List<Date> vencimento = new ArrayList<>();
		for(int i = 0; i < quantidadeRepeticao; i++) {
			data = executarVencimento(data, 1);
			vencimento.add(data);
		}
		return vencimento;
	}
	
	private Date executarVencimento(Date data, int mes) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(data);
		
		mes += calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, mes);
		return calendar.getTime();
	}
}
