package br.com.siscomanda.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import br.com.siscomanda.enumeration.ETipoRelatorio;
import br.com.siscomanda.exception.SiscomandaException;
import br.com.siscomanda.util.JSFUtil;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

@SuppressWarnings("deprecation")
public abstract class Relatorio {
	
	private HttpServletResponse response;
	private ByteArrayOutputStream baos;
	private InputStream stream;
	private JasperPrint print;
	private String extensao;
	private String contentType;
	
	public abstract String pathJasper();
	public abstract String nomeRelatorio();
	public abstract List<?> colecaoParaGerarRelatorio();
	public abstract List<?> colecaoDeDadosSelecionados();
	public abstract Map<String, Object> parametros();
	
	private void preparaRelatorio() throws SiscomandaException {
		try {
			validador();
			
			stream = getClass().getResourceAsStream(pathJasper());
			JasperReport report = (JasperReport) JRLoader.loadObject(stream);
			
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(fontDeDados());
			
			Map<String, Object> parametros = getParametros();
			parametros.put("LIST_DATA_SOURCE", dataSource);
			parametros.put("DATA_TEST", new Date());
			
			print = JasperFillManager.fillReport(report, parametros, new JREmptyDataSource());
			baos = new ByteArrayOutputStream();
		}
		catch(JRException e) {
			throw new SiscomandaException(e.getMessage());
		}
	}
	
	private List<?> fontDeDados() {
		if(colecaoDeDadosSelecionados() != null && !colecaoDeDadosSelecionados().isEmpty()) {
			return colecaoDeDadosSelecionados();
		}
		return colecaoParaGerarRelatorio();
	}
	
	private void validador() throws SiscomandaException {
		if(pathJasper() == null || pathJasper().isEmpty()) {
			throw new SiscomandaException("É necessário informar o diretorio onde esta o arquivo jasper.");
		}
		if(colecaoParaGerarRelatorio() == null || colecaoParaGerarRelatorio().isEmpty()) {
			throw new SiscomandaException("É necessário informar a lista ou coleção de dados.");
		}
		if(nomeRelatorio() == null || nomeRelatorio().isEmpty()) {
			throw new SiscomandaException("É necessário informar o nome do relatório.");
		}
	}
	
	private Map<String, Object> getParametros() {
		return parametros() == null ? new HashMap<String, Object>() : parametros();
	}
	
	private void respostaCompleta() throws SiscomandaException  {
		try {
			
			response = (HttpServletResponse)JSFUtil.getContext().getExternalContext().getResponse();			
			response.reset();
			
			response.setContentType(contentType);
			response.setHeader("content-disposition", "attachment; filename="+ nomeRelatorio() +"." + extensao);
			
			response.setContentLength(baos.size());
			response.getOutputStream().write(baos.toByteArray());
			response.getOutputStream().flush();
			response.getOutputStream().close();
			
			JSFUtil.getContext().responseComplete();
		}
		catch(IOException e) {
			throw new SiscomandaException(e.getMessage());
		}
	}
	
	public void gerarRelatorio(ETipoRelatorio tipoRelatorio) throws SiscomandaException {
		switch (tipoRelatorio.name()) {
			case "PDF": 
				gerarPDF();
				break;
			case "XLS": 
				gerarXLS();
				break;
			default:
				break;
		}
	}
	
	private void gerarPDF() throws SiscomandaException {
		try {
			extensao = "pdf";
			contentType = "application/pdf";
			
			preparaRelatorio();
			JasperExportManager.exportReportToPdfStream(print, baos);
			respostaCompleta();
		}
		catch(JRException e) {
			throw new SiscomandaException(e.getMessage());
		}
	}
	
	private void gerarXLS() throws SiscomandaException {
		try {
			extensao = "xls";
			contentType = "application/xls";
			
			preparaRelatorio();
			JRXlsExporter xlsExporter = new JRXlsExporter();
			xlsExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, print);
			xlsExporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
			xlsExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			xlsExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			xlsExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			xlsExporter.exportReport();
			respostaCompleta();
		}
		catch(JRException e) {
			throw new SiscomandaException(e.getMessage());
		}
	}
	
	protected boolean isRelatorioDisponivel() {
		return (colecaoParaGerarRelatorio() != null && colecaoParaGerarRelatorio().size() > 0);
	}
	
	protected boolean isRelatorioNaoDisponivel() {
//		return !isRelatorioDisponivel();
		return true;
	}
}
