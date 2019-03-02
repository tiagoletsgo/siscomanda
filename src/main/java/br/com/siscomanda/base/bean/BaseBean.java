package br.com.siscomanda.base.bean;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public abstract class BaseBean<T> implements Serializable {
	
	private static final long serialVersionUID = -7037880362401250167L;

	private static String PACKAGE_MODEL = "br.com.siscomanda.model";
	
	private static String VIEW = "view";

	private T entity;
		
	private EstadoViewBean estadoViewBean;
	
	private List<T> elements;
	
	
	@SuppressWarnings({ 
		"rawtypes", 
		"unchecked" 
	})
	public BaseBean() {
		try {
			HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			estadoViewBean = new EstadoViewBean(request.getParameter(VIEW));
			
			if(getClass().getGenericSuperclass().toString().contains(PACKAGE_MODEL)) {
				Class clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
				entity = (T) clazz.newInstance();				
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
		
	@PostConstruct
	protected abstract void init();
	
	protected abstract void beforeSearch();
		
	protected void setEntity(T entity) {
		this.entity = entity;
	}
	
	public T getEntity() {
		return entity;
	}
	
	public void setElements(List<T> elements) {
		this.elements = elements;
	}
	
	public List<T> getElements() {
		return elements;
	}
	
	public void btnNovo() {
		estadoViewBean.setCurrentView(true, false, false, false);
	}
	
	public void btnConsultar() {
		estadoViewBean.setCurrentView(false, false, false, true);
		beforeSearch();
	}
	
	public void btnEditar(T entity) {
		setEntity(entity);
		estadoViewBean.setCurrentView(false, true, false, true);
	}
	
	public void btnCancelar() {
		if(!estadoViewBean.getInsert()) {			
			estadoViewBean.setCurrentView(false, false, false, true);
		}
		beforeSearch();
	}
	
	public void setEstadoViewBean(EstadoViewBean estadoViewBean) {
		this.estadoViewBean = estadoViewBean;
	}
	
	public EstadoViewBean getEstadoViewBean() {
		return estadoViewBean;
	}
}
