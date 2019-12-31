package br.com.template.generalbusiness.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

public class SampleMongo {

	@Id
	private String id;

	private String name;

	private String teste;

	public SampleMongo() {

	}

	public SampleMongo(String name, String teste) {
		super();
		this.name = name;
		this.teste = teste;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTeste() {
		return teste;
	}

	public void setTeste(String teste) {
		this.teste = teste;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
