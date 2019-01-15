package br.com.siscomanda.converter;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.siscomanda.base.model.BaseEntity;

@FacesConverter(value = "entityConverter", forClass = BaseEntity.class)
public class EntityConverter implements Converter {
	
	private Map<String, Object> getAttributesFrom(UIComponent component) {
		return component.getAttributes();
	}
	
	private void addAttributes(UIComponent component, BaseEntity baseEntity) {
		this.getAttributesFrom(component).put(baseEntity.getId().toString(), baseEntity);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if(value != null) {
			return getAttributesFrom(component).get(value);
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if(obj != null && !"".equals(obj)) {
			BaseEntity baseEntity = (BaseEntity) obj;
			if(baseEntity.getId() != null) {
				this.addAttributes(component, baseEntity);
				
				if(baseEntity.getId() != null) {
					return String.valueOf(baseEntity.getId());
				}
				
				return (String) obj;
			}
		}
		return "";
	}
}
