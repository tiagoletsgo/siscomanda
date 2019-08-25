package br.com.siscomanda.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import br.com.siscomanda.exception.SiscomandaRuntimeException;

public class DateUtil {
	
	private static TimeZone timeZone = TimeZone.getTimeZone("America/Sao_Paulo");
	private static Locale locale = new Locale("pt", "BR");
	
	public DateUtil() {
		TimeZone.setDefault(timeZone);
	}
	
	public static Date primeiroDiaDoMesAtual() {
		Calendar calendar = new GregorianCalendar(timeZone);
		calendar.setTime(new Date());
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	
	public static int extrairDia(Date date) {
		Calendar calendar = new GregorianCalendar(timeZone);
		calendar.setTime(date);
		return calendar.get(GregorianCalendar.DAY_OF_MONTH);
	}
	
	public static Date ultimoDiaDoMesAtual() {
		Calendar calendar = new GregorianCalendar(timeZone);
		calendar.setTime(new Date());
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	
	public static Date data(int dia, int hora, int minuto, int segundo) {
		Calendar calendar = new GregorianCalendar(timeZone);
		calendar.setTime(new Date());
		calendar.set(Calendar.DATE, dia);
		calendar.set(Calendar.HOUR_OF_DAY, hora);
		calendar.set(Calendar.MINUTE, minuto);
		calendar.set(Calendar.SECOND, segundo);
		return calendar.getTime();
	}
	
	public static Date data(Date data, int hora, int minuto, int segundo) {
		Calendar calendar = new GregorianCalendar(timeZone);
		calendar.setTime(data);
		calendar.set(Calendar.HOUR_OF_DAY, hora);
		calendar.set(Calendar.MINUTE, minuto);
		calendar.set(Calendar.SECOND, segundo);
		return calendar.getTime();
	}
	
	public static boolean isDepois(Date dataInicial, Date dataFinal) {
		return dataInicial.after(dataFinal);
	}
	
	public static boolean isAntes(Date dataInicial, Date dataFinal) {
		return dataInicial.before(dataFinal);
	}
	
	public static int totalDiasDoMes(Date data) {
		Calendar calendar = Calendar.getInstance(timeZone);
		calendar.setTime(data);
		
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public static Date parseDate(String data) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			sdf.setTimeZone(timeZone);
			Date dt = sdf.parse(data);
			return dt;
		}
		catch(Exception e) {
			throw new SiscomandaRuntimeException(e.getMessage());
		}
	}
	
	public static String extrairMesPorExtenso(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMMMM", locale);
		sdf.setTimeZone(timeZone);
		String nomePorExtenso = sdf.format(data);
		return nomePorExtenso.toUpperCase();
	}
}
