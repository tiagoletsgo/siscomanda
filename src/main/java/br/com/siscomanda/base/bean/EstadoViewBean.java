package br.com.siscomanda.base.bean;

import java.io.Serializable;

import br.com.siscomanda.enumeration.EStateView;

public class EstadoViewBean implements Serializable {

	private static final long serialVersionUID = -876338639656095129L;

	private Boolean update = false;
	private Boolean insert = false;
	private Boolean delete = false;
	private Boolean search = false;
	
	public EstadoViewBean() {
	}
	
	public EstadoViewBean(String stateView) {
		if("insert".equals(stateView)) {
			setInsert(true);
		}
		else if("delete".equals(stateView)) {
			setDelete(true);
		}
		else if("update".equals(stateView)) {
			setUpdate(true);
		}
		else if("search".equals(stateView)) {
			setSearch(true);
		}
	}
	
	public void setCurrentView(Boolean isInsert, Boolean isUpdate, Boolean isDelete, Boolean isSearch) {
		setSearch(isSearch);
		setUpdate(isUpdate);
		setDelete(isDelete);
		setInsert(isInsert);
	}
	
	public void setCurrentView(EStateView state) {
		if(state == null) {
			setCurrentView(false, false, false, false);
		}
		else if(state.equals(EStateView.INSERT)) {
			setCurrentView(true, false, false, false);
		}
		else if(state.equals(EStateView.UPDATE)) {
			setCurrentView(false, true, false, false);
		}
		else if(state.equals(EStateView.DELETE)) {
			setCurrentView(false, false, true, false);
		}
		else if(state.equals(EStateView.SEARCH)) {
			setCurrentView(false, false, false, true);
		}
	}

	public Boolean getUpdate() {
		return update;
	}

	public void setUpdate(Boolean update) {
		this.update = update;
	}

	public Boolean getInsert() {
		return insert;
	}

	public void setInsert(Boolean insert) {
		this.insert = insert;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

	public Boolean getSearch() {
		return search;
	}

	public void setSearch(Boolean search) {
		this.search = search;
	}

}
