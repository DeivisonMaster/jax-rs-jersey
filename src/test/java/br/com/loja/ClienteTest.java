package br.com.loja;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.loja.modelo.Carrinho;
import br.com.loja.modelo.Produto;
import junit.framework.Assert;

public class ClienteTest {
	Client client = null;
	WebTarget target = null;
	HttpServer servidor = null;
	
	@Before
	public void startServidor() {
		servidor = Servidor.inicializaServidor();
		
		// registrando log http no console
		ClientConfig config = new ClientConfig();
		config.register(new LoggingFilter());
		
		client = ClientBuilder.newClient(config);
		target = client.target("http://localhost:8081");
		System.out.println(servidor.isStarted());
	}
	
	@After
	public void encerraServidor() {
		servidor.stop();
		System.out.println(servidor.isStarted());
	}
	
	@Test
	public void testaConexaoComOServidorFunciona() {
		target = client.target("http://www.mocky.io");
		String dados = target.path("/v2/52aaf5deee7ba8c70329fb7d").request().get(String.class);
		System.out.println(dados);
		
		Assert.assertTrue(dados.contains("<rua>Rua Vergueiro 3185"));
	}
	
	@Test
	public void testaQueBuscarUmCarrinhoTrazOCarrinhoEsperado() {
		String dados = target.path("/carrinho/1").request().get(String.class);
		System.out.println(dados);
		
		Carrinho carrinho = (Carrinho) new XStream().fromXML(dados);
		
		Assert.assertEquals(carrinho.getRua(), "Rua Vergueiro 3185, 8 andar");
	}
	
	@Test
	public void testaQueSuportaNovosCarrinhos() {
		Carrinho carrinho = new Carrinho();
		carrinho.adiciona(new Produto(315, "Microfone", 37, 1));
		carrinho.setRua("Rua Tietê");
		carrinho.setCidade("Cuiabá");
		String carrinhoXML = carrinho.toXML();
		
		Entity<String> entity = Entity.entity(carrinhoXML, MediaType.APPLICATION_XML);
		
		Response response = target.path("/carrinho").request().post(entity);
		Assert.assertEquals(201, response.getStatus());
		
		String location = response.getHeaderString("Location");
		String dados = client.target(location).request().get(String.class);
		Assert.assertTrue(dados.contains("Microfone"));
		
	}
	
}
