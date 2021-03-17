package br.com.loja.resource;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.thoughtworks.xstream.XStream;

import br.com.loja.dao.CarrinhoDAO;
import br.com.loja.modelo.Carrinho;
import br.com.loja.modelo.Produto;

@Path("/carrinho")
public class CarrinhoResource {
	
	//@GET
	//@Produces(MediaType.APPLICATION_XML)
	public String busca() {
		Carrinho carrinho = new CarrinhoDAO().busca(1l);
		return carrinho.toXML();
	}
	
	/**
	 * http://localhost:8081/carrinho?id=1
	 * */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String buscaTodos() {
		List<Carrinho> carrinhos = new CarrinhoDAO().buscaTodos();
		return new XStream().toXML(carrinhos);
	}
	
	/**
	 * http://localhost:8081/carrinho/1
	 * */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	public String buscaPorId(@PathParam("id") Long id) {
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		return carrinho.toXML();
	}
	
//	@GET
//	@Path("{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public String buscaPorId(@PathParam("id") Long id) {
//		Carrinho carrinho = new CarrinhoDAO().busca(id);
//		return carrinho.toJson();
//	}
	
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public Response adiciona(String dados) {
		Carrinho carrinho = (Carrinho) new XStream().fromXML(dados);
		new CarrinhoDAO().adiciona(carrinho);
		URI uri = URI.create("/carrinho/" + carrinho.getId());
		
		return Response.created(uri).build();
	}
	
	@DELETE
	@Path("{id}/produtos/{produtoId}")
	public Response removeProduto(@PathParam("id") Long id, @PathParam("produtoId") Long produtoId) {
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		carrinho.remove(produtoId);

		return Response.ok().build();
	}
	
	@PUT
	@Path("{id}/produtos/{produtoId}/quantidade")
	public Response atualizaProduto(String conteudo, @PathParam("id") Long id, @PathParam("produtoId") Long produtoId) {
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		Produto produto = (Produto) new XStream().fromXML(conteudo);
		carrinho.troca(produto);
		
		return Response.ok().build();
	}
}












