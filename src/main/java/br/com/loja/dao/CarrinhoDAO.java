package br.com.loja.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import br.com.loja.modelo.Carrinho;
import br.com.loja.modelo.Produto;

public class CarrinhoDAO {
	private static Map<Long, Carrinho> banco = new HashMap<Long, Carrinho>();
	private static AtomicLong contador = new AtomicLong(1);
	
	static {
		Produto esporte = new Produto(3467, "Jogo de Esporte", 60, 2);
		Produto videogame = new Produto(6237, "Videogame 4", 4000, 1);
		
		Carrinho carrinho = new Carrinho()
				.adiciona(esporte)
				.adiciona(videogame)
				.para("Rua Vergueiro 3185, 8 andar", "São Paulo")
				.setId(1l);
		
		banco.put(1l, carrinho);
	}
	
	public void adiciona(Carrinho carrinho) {
		long id = contador.incrementAndGet();
		carrinho.setId(id);
		banco.put(id, carrinho);
	}
	
	public Carrinho busca(Long id) {
		return banco.get(id);
	}
	
	public Carrinho remove(Long id) {
		return banco.remove(id);
	}

	public List<Carrinho> buscaTodos() {
		return new ArrayList<>(banco.values());
	}
}
