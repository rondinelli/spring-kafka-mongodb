package br.com.template.generalbusiness.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.template.generalbusiness.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

    public Token findByToken(String token);

}
