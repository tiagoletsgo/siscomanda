/**
 * Author: Carlos Tiago
 * Data: 30/11/2018
 * Version: 1.0.1
 */
function init() {
	var div = document.getElementById('coluna_direita');
	var attr = document.createAttribute('style');
	attr.value = 'background: red';
	div.setAttributeNode(attr);
}

function maiuscula(obj) {
	var valor = obj.value.toUpperCase();
	obj.value = valor;
	obj.value = removeAcentuacao(obj.value);	
}

function removeAcentuacao(text) {
    text = text.replace(new RegExp('[ÁÀÂÃ]','gi'), 'a');
    text = text.replace(new RegExp('[ÉÈÊ]','gi'), 'e');
    text = text.replace(new RegExp('[ÍÌÎ]','gi'), 'i');
    text = text.replace(new RegExp('[ÓÒÔÕ]','gi'), 'o');
    text = text.replace(new RegExp('[ÚÙÛ]','gi'), 'u');
    text = text.replace(new RegExp('[Ç]','gi'), 'c');
    text = text.toUpperCase();
    return text;
}