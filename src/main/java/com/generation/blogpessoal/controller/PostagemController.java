package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostagemController {
	//Criando um novo objeto de injeção
	@Autowired
	private PostagemRepository postagemRepository;
	
	@Autowired
	private TemaRepository temaRepository;
	
	@GetMapping
	public ResponseEntity<List<Postagem>> getAll(){
		return ResponseEntity.ok(postagemRepository.findAll());
	}// MESMA COISA QUE SELECT * FROM tb_postagens;
	
	@GetMapping("/{id}")
	public ResponseEntity<Postagem> getById( @PathVariable Long id){ //PathvARIABLE PEGA O ID DA LINHA 21
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
	}// SELECT ) FROM tb_postagens WHERE i = ?;
	
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getAllByTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
	}// MESMA COISA QUE SELECT * FROM tb_postagens WHEERE titulo LIKE "%?%";

	@PostMapping
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem){
		if(temaRepository.existsById(postagem.getTema().getId() )) {
			postagem.setId(null);//Fazemos isso - porque o front vai eenviar o id 0, mas no spring o BD acha q é consulta
			
			return ResponseEntity.status(HttpStatus.CREATED)
				.body(postagemRepository.save(postagem));
		// INSERT INTO tb_postagens (titulo, texto) VALUES ({titulo}, {texto});  (SQL) <- Isso é o que o método save() faz, ele insere um novo registro na tabela tb_postagens com os valores informados
	} else {
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O tema não existe!", null);
	}
		}//INSERT INTO tb_postagens(titulo,texto) VALUES(?,?)
	
	@PutMapping
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem){
		
		if(postagemRepository.existsById(postagem.getId())) {
			
			if (temaRepository.existsById(postagem.getTema().getId())) {
				return ResponseEntity.ok(postagemRepository.save(postagem));
				// UPDATE tb_postagens SET titulo = ?, texto = ? WHERE id = ?;
			}
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O Tema não existe!", null);
			
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete( @PathVariable Long id){ //PathvARIABLE PEGA O ID DA LINHA 21
		Optional<Postagem> postagem = postagemRepository.findById(id);
		
		if(postagem.isEmpty()) 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		postagemRepository.deleteById(id);
	}// DELETE ) FROM tb_postagens WHERE i = ?;
	
	
}
