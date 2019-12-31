package br.com.template.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.template.generalbusiness.entity.Token;
import br.com.template.generalbusiness.entity.User;
import br.com.template.generalbusiness.helper.EncryptPassword;
import br.com.template.generalbusiness.repository.TokenRepository;
import br.com.template.generalbusiness.repository.UserRepository;
import br.com.template.rest.token.Credentials;
import br.com.template.rest.token.UtilSecurity;


@RestController
public class LoginController {

    private static final String AUTHORIZATION_PROPERTY = "Authorization";

    private static final String TOKEN_PROPERTY = "token";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<?> login(HttpServletRequest request) {

        final String authorization = request.getHeader(AUTHORIZATION_PROPERTY);
        if (authorization == null || authorization.length() == 0) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Credentials credentials = UtilSecurity.parseHeader(authorization);

        User user = userRepository.findByLoginAndPassword(credentials.getUsername(), EncryptPassword.getSha(credentials.getPassword()));

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }else if(!user.isEnabled()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        // Create new token
        user.setOrRenewToken();
        this.userRepository.saveAndFlush(user);

        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.PUT, value = "/renew_token")
    public ResponseEntity<?> renewToken(HttpServletRequest request) {

        // Checking if the header was sent
        final String authorization = request.getHeader(AUTHORIZATION_PROPERTY);
        if (authorization == null || authorization.length() == 0) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Credentials credentials = UtilSecurity.parseHeader(authorization);

        // Checking the user existence
        User user = this.userRepository.findByEmail(credentials.getUsername());
        if(user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Checking the token existence
        Token token = this.tokenRepository.findByToken(credentials.getPassword());
        if(token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Checking if the user is associated with the token
        if(user.getToken() == null || !user.getToken().getToken().equals(credentials.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Renewing the token
        user.setOrRenewToken();
        this.userRepository.saveAndFlush(user);

        return ResponseEntity.ok(user);
    }

}