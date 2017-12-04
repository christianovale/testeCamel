package br.com.christianovale.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class RotaPedidosChris {

	public static void main(String[] args) throws Exception {

		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {

			@Override
			public void configure() throws Exception {

				/*from("file:pedidos?delay=5s&noop=true").routeId("rota-pedidos")
						.multicast().to("direct:soap").to("direct:http");

				from("direct:soap").routeId("rota-soap")
						.log("chamando servico soap ${body}").to("mock:soap");

				from("direct:http")
						.routeId("rota-http")
						.setProperty("pedidoId", xpath("/pedido/id/text()"))
						.setProperty("email",
								xpath("/pedido/pagamento/email-titular/text()"))
						.split()
						.xpath("/pedido/itens/item")
						.filter()
						.xpath("/item/formato[text()='EBOOK']")
						.setProperty("ebookId",
								xpath("/item/livro/codigo/text()"))
						.setHeader(
								Exchange.HTTP_QUERY,
								simple("clienteId=${property.email}&pedidoId=${property.pedidoId}&ebookId=${property.ebookId}"))
						.to("http4://localhost:8080/webservices/ebook/item");*/
				
			/*from("file:pedidos?delay=5s&noop=true").
		    routeId("rota-pedidos").
		    multicast().
		        to("direct:soap").
		        to("direct:http");

			from("direct:soap").
			    routeId("rota-soap").
			    log("chamando servico soap ${body}").
			to("mock:soap");

			from("direct:http").
			    routeId("rota-http").
			    setProperty("pedidoId", xpath("/pedido/id/text()")).
			    setProperty("email", xpath("/pedido/pagamento/email-titular/text()")).
			    split().xpath("/pedido/itens/item").
			    filter().xpath("/item/formato[text()='EBOOK']").
			    setProperty("ebookId", xpath("/item/livro/codigo/text()")).
			    setHeader(Exchange.HTTP_QUERY, simple("clienteId=${property.email}&pedidoId=${property.pedidoId}&ebookId=${property.ebookId}")).
			to("http4://localhost:8080/webservices/ebook/item");
			}*/
			
			/**
			 * H� uma alternativa ao direct e multicast. Na rota e sub-rotas podemos aplicar algo chamado de Staged event-driven architecture 
			 * ou simplesmente SEDA.

				A ideia do SEDA � que cada rota (e sub-rota) possua a sua fila dedicada de entrada e as rotas enviam mensagens para essas 
				filas para se comunicar. Dentro dessa arquitetura as mensagens s�o chamadas de eventos. A rota fica ent�o consumindo as 
				mensagens/eventos dessa fila, tudo em paralelo.
				
				Enquanto o direct usa o processamento s�ncrono, o seda usa ass�ncrono.
				� importante mencionar que seda n�o implementar qualquer tipo de persist�ncia ou a recupera��o, tudo � processado dentro da JVM. 
				Se voc� precisa de persist�ncia, confiabilidade ou processamento distribu�do, JMS � a melhor escolha.

				Para usar SEDA basta substituir a palavra direct por seda. O multicast n�o � mais necess�rio:
			 */
		from("file:pedidos?delay=5s&noop=true").
	    routeId("rota-pedidos").
	    to("seda:soap").
	    to("seda:http");

		from("seda:soap").
		    routeId("rota-soap").
		    log("chamando servico soap ${body}").
		to("mock:soap");

		from("seda:http").
		    routeId("rota-http").
		    setProperty("pedidoId", xpath("/pedido/id/text()")).
		    setProperty("email", xpath("/pedido/pagamento/email-titular/text()")).
		    split().
		        xpath("/pedido/itens/item").
		    filter().
		        xpath("/item/formato[text()='EBOOK']").
		    setProperty("ebookId", xpath("/item/livro/codigo/text()")).
		    setHeader(Exchange.HTTP_QUERY,
		            simple("clienteId=${property.email}&pedidoId=${property.pedidoId}&ebookId=${property.ebookId}")).
		to("http4://localhost:8080/webservices/ebook/item");

		});

		context.start();
		Thread.sleep(20000);
		context.stop();
	}
}
