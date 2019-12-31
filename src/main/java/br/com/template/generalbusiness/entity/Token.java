package br.com.template.generalbusiness.entity;


import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Token {

    private static final long TOKEN_EXPIRATION_TIME = 30L; // Minutes

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @NotBlank
    @Column
    private String token;

    @NotNull
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Token() {
        this.renew();
    }

    public static long getTokenExpirationTime() {
        return TOKEN_EXPIRATION_TIME;
    }

    @JsonIgnore
    public boolean isValid() {
        final long min = 1000 * 60;
        return this.token != null && this.createdAt != null &&  (new Date().getTime() - this.createdAt.getTime()) / min <= TOKEN_EXPIRATION_TIME;
    }

    public void renew() {
        UUID random = UUID.randomUUID();
        this.token = random.toString();
        this.createdAt = new Date();
    }

    public void expire() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.createdAt);
        calendar.add(Calendar.MINUTE, (int) -TOKEN_EXPIRATION_TIME);
        calendar.add(Calendar.MINUTE, -1);
        this.createdAt = calendar.getTime();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
