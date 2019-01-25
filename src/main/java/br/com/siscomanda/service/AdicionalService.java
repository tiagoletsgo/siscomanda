package br.com.siscomanda.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.siscomanda.repository.dao.AdicionalDAO;

public class AdicionalService implements Serializable {

	private static final long serialVersionUID = 6750595631115213547L;
	
	@Inject
	private AdicionalDAO dao;
}
