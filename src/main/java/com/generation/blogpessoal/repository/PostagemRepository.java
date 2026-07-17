package com.generation.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.blogpessoal.model.Postagem;

public interface PostagemRepository extends JpaRepository<Postagem, Long>{ //Herança nessa linha
	
	public List<Postagem> findAllByTituloContainingIgnoreCase(String titulo);
	// SELECT * FROM tb_postagens WHERE titulo LIKE "%?%" onde esta o ?  na variavel titulo
}
