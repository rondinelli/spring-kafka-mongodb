package br.com.template.generalbusiness.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.template.generalbusiness.entity.SampleMongo;

public interface SampleMongoRepository extends MongoRepository<SampleMongo, String> {
    // 
}