# siscomanda
Sistema para gerenciamento de pequenos estabelecimentos. Voltado para bares, restaurantes e lanchonetes.

# prefences config

Para o funcionamento do projeto é necessário alterar o webContent do contexto da aplicação para src/main/webapp
como realizar essa alteração selecione o projeto
com o projeto selecionado siga o seguinte passo.:
Project ->
	Preferences ->
		Project Facets.: Desmarcar a opção Dynamic Web Module e JavaServer Faces e clicar no botão Apply.
		                 Marcar a opção Dynamic Web Module e clicar no link "Funther configuration available..."
		                 no campo content directory incluir o caminho src/main/webapp e marcar Generate web.xml deployment descriptor clicar no botão OK e depois Apply.
		                 Marcar a opção JavaServer Faces no link "Funther configuration available..."  adicionar a url-pattern como /*.xhtml.
