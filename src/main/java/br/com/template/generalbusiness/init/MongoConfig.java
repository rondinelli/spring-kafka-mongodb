package br.com.template.generalbusiness.init;

import java.util.Arrays;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
@PropertySource("classpath:application.properties")
@EnableMongoRepositories(basePackages = {"br.com.template.generalbusiness.repository"})
public class MongoConfig extends AbstractMongoConfiguration {
  
	

    @Override
    protected String getDatabaseName() {
    	return "template";
    }

   
	@Override
	public MongoClient mongoClient() {
		// TODO Auto-generated method stub
		return new MongoClient("127.0.0.1", 27017);
	}
	

}