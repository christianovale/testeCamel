package br.com.christianovale.camel;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file:pedidos").
        to("activemq:queue:pedidos");
    }
	
	@Bean
	public RoutesBuilder rota() { 
	    return new RouteBuilder() {
	        @Override
	        public void configure() throws Exception {
	            from("file:pedidos").
	            to("activemq:queue:pedidos");
	        } 
	    };
	}
}
