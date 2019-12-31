package br.com.template.generalbusiness.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.template.generalbusiness.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    User findByLoginAndPassword(String email, String password);
    User findByLogin(String login);

    
    @Query("select count(u) > 0 from User u where u.resetPasswordToken = :resetPasswordToken")
    boolean existsWithResetPasswordToken(@Param("resetPasswordToken") String token);

    @Query("select count(u) > 0 from User u where u.email = :email")
    boolean exists(@Param("email") String email);

    @Query("SELECT u from Token t inner join t.user u where t.token = :token")
    User findByRawToken(@Param("token") String token);

}
