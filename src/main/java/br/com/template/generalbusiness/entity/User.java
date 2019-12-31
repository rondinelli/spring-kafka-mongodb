package br.com.template.generalbusiness.entity;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.template.generalbusiness.helper.EncryptPassword;
import br.com.template.rest.enumeration.UserRole;


@Entity
@Table (name = "users")
public class User {
	
	@Transient
    private int minutes = 120;

    public static final Long RESET_PASSWORD_TOKEN_LIFETIME = 7L;                      // Days

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (unique = true, nullable = false)
    private Long             id;

    @Email
    @NotBlank (message = "Email Obrigatório.")
    private String           email;

    @JsonProperty (access = JsonProperty.Access.WRITE_ONLY)
    private String           password;

    private String           login;

    private String           empresa;

    @JsonProperty
    @NotNull
    private UserRole         role;

    @Column
    private boolean          enabled                       = false;

    @Column
    private String           resetPasswordToken;

    @Column
    @Temporal (TemporalType.TIMESTAMP)
    private Date             resetPassowordSentAt;

    private Calendar         created                       = Calendar.getInstance ();

    @JsonIgnore
    @OneToOne (cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user", orphanRemoval = true)
    private Token            token;
    
        @JsonProperty (value = "token")
    public String rawToken () {
        return this.token != null ? this.token.getToken () : null;
    }

    
    public User () {
        super ();
    }

    public User (String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setNewResetPasswordToken () {
        this.resetPasswordToken = UUID.randomUUID ().toString ();
        this.resetPassowordSentAt = new Date ();
    }

    // Object Override

    @Override
    public String toString () {
        return "User{" + "id=" + this.id + '\'' + ", email='" + this.email + '\'' + ", password='" + this.password + '\'' + ", created=" + this.created + ", enabled=" + this.enabled + '}';
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( (this.created == null) ? 0 : this.created.hashCode ());
        result = prime * result + ( (this.email == null) ? 0 : this.email.hashCode ());
        result = prime * result + ( (this.password == null) ? 0 : this.password.hashCode ());
        return result;
    }

    @Override
    public boolean equals (Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass () != obj.getClass ()) {
            return false;
        }
        User other = (User) obj;
        if (this.created == null) {
            if (other.created != null) {
                return false;
            }
        }
        else if (!this.created.equals (other.created)) {
            return false;
        }
        if (this.email == null) {
            if (other.email != null) {
                return false;
            }
        }
        else if (!this.email.equals (other.email)) {
            return false;
        }
        if (this.password == null) {
            if (other.password != null) {
                return false;
            }
        }
        else if (!this.password.equals (other.password)) {
            return false;
        }
        return true;
    }

    // Password Helpers

    public void encodeAndSetBase64Password () {
        try {
            byte[] decodedBytes = Base64.encodeBase64 (this.password.getBytes ());
            this.password = new String (decodedBytes, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace ();
        }
    }

    public void decodeAndSetBase64Password () {
        try {
            byte[] decodedBytes = Base64.decodeBase64 (this.password.getBytes ());
            this.password = new String (decodedBytes, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace ();
        }
    }

    public void encryptAndSetPassword () {
        this.password = EncryptPassword.getSha (this.password);
    }

    // Token Helpers

    public void setNewToken () {
        this.token = new Token ();
        this.token.setUser (this);
    }

    public void renewToken () {
        this.token.renew ();
    }

    public void setOrRenewToken () {
        if (this.token == null) {
            this.setNewToken ();
        }
        else {
            this.renewToken ();
        }
    }


    // Other Helpers

    public User clone () {
        User user = new User ();
        user.id = this.id;
        user.email = this.email;
        return user;
    }

    public static User loggedUser () {
        Object obj = SecurityContextHolder.getContext ().getAuthentication ().getPrincipal ();
        if (obj != null && obj instanceof User) {
            return (User) obj;
        }
        return null;
    }

    // Getters and Setters

    public Long getId () {
        return this.id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public Calendar getCreated () {
        return this.created;
    }

    public void setCreated (Calendar created) {
        this.created = created;
    }

    public String getEmail () {
        return this.email;
    }

    public void setEmail (final String email) {

        if (email != null) {
            email.toLowerCase ();
        }

        this.email = email;
    }

    public String getPassword () {
        return this.password;
    }

    public void setPassword (final String password) {
        this.password = password;
    }

    public void setEnabled (boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled () {
        return this.enabled;
    }

    public String getLogin () {
        return login;
    }

    public void setLogin (String login) {
        this.login = login;
    }

    public String getEmpresa () {
        return empresa;
    }

    public void setEmpresa (String empresa) {
        this.empresa = empresa;
    }

    public UserRole getRole () {
        return role;
    }

    public void setRole (UserRole role) {
        this.role = role;
    }

    public HashMap<String, String> jsonHash () {
        HashMap<String, String> hash = new HashMap<> ();
        hash.put ("email", this.email);
        hash.put ("password", this.password);
        return hash;
    }

    public String getResetPasswordToken () {
        return resetPasswordToken;
    }

    public void setResetPasswordToken (String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public Date getResetPassowordSentAt () {
        return resetPassowordSentAt;
    }

    public void setResetPassowordSentAt (Date resetPassowordSentAt) {
        this.resetPassowordSentAt = resetPassowordSentAt;
    }

    public Token getToken () {
        return token;
    }

    public void setToken (Token token) {
        this.token = token;
    }


	

    
}
