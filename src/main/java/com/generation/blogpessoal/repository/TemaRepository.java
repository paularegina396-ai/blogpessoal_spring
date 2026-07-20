package com.generation.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.model.Tema;

public interface TemaRepository extends JpaRepository<Tema, Long>{
	public List<Tema> findAllByDescricaoContainingIgnoreCase(String descricao);
	//Observe que foi criada uma Query Method para buscar temas pela descrição, conforme detalhado abaixo:
	// SELECT * FROM tb_temas WHERE descrição LIKE "%?%" onde esta o ?  na variavel descrição

}
