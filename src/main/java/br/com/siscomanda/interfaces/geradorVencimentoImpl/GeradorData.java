package br.com.siscomanda.interfaces.geradorVencimentoImpl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import br.com.siscomanda.enumeration.EFreaquencia;

public abstract class GeradorData implements Serializable {

	private static final long serialVersionUID = 7414876083701467819L;
	
	protected Date executarVencimento(Date data, EFreaquencia frequencia) {
		switch(frequencia) {
		case DIARIO:
			return executarVencimentoDiario(data, 1);
		case SEMANAL:
			return executarVencimentoSemanal(data, 7);
		case QUINZENAL:
			return executarVencimentoQuinzenal(data, 15);
		case MENSAL:
			return executarVencimentoMensal(data, 1);
		case ANUAL:
			return executarVencimentoAnual(data, 1);
		}
		return null;
	}
	
	private Date executarVencimentoSemanal(Date data, int semana) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(data);	
		
		semana += calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, semana);
		return calendar.getTime();
	}
	
	private Date executarVencimentoQuinzenal(Date data, int quinzena) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(data);	
		
		quinzena += calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, quinzena);
		return calendar.getTime();
	}
	
	private Date executarVencimentoMensal(Date data, int mes) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(data);
		
		mes += calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, mes);
		return calendar.getTime();
	}
	
	private Date executarVencimentoDiario(Date data, int dia) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(data);	
		
		dia += calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, dia);
		return calendar.getTime();
	}
	
	private Date executarVencimentoAnual(Date data, int ano) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(data);	
		
		ano += calendar.get(Calendar.YEAR);
		calendar.set(Calendar.YEAR, ano);
		return calendar.getTime();
	}
}
