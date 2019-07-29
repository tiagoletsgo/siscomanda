package br.com.siscomanda.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.siscomanda.model.Usuario;
import br.com.siscomanda.repository.dao.UsuarioDAO;

public class UsuarioService implements Serializable {

	private static final long serialVersionUID = -7508320203271260198L;
	
	@Inject
	private UsuarioDAO usuarioDAO;
	
	public Usuario porCodigo(Long codigo) {
		return usuarioDAO.porCodigo(codigo, Usuario.class);
	}
}
