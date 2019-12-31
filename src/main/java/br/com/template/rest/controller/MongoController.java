package br.com.template.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.template.generalbusiness.entity.SampleMongo;
import br.com.template.generalbusiness.repository.SampleMongoRepository;

@RequestMapping(value = "/mongo")
@RestController
public class MongoController {
	
	@Autowired
	private SampleMongoRepository sampleMongoRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> index() {
		
		SampleMongo sample = new SampleMongo();
		sample.setName("Albert"); 
		sample.setTeste("Teste 1");
		sampleMongoRepository.save(sample);
		
		
		return ResponseEntity.ok(sampleMongoRepository.findAll());
	}

}
