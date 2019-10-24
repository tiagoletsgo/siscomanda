package br.com.siscomanda.bean;

import java.io.Serializable;

import javax.faces.bean.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class LoginBean implements Serializable {
	
	private static final long serialVersionUID = -1363990616805484138L;

	public String login() {
		
		try {
			Thread.sleep(600000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "/pages/index.xhtml?faces-redirect=true";
	}
}
