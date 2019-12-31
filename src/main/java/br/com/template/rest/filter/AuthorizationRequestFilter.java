package br.com.template.rest.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.template.generalbusiness.entity.User;
import br.com.template.generalbusiness.repository.UserRepository;


@Component(value = "authorizationRequestFilter")
public class AuthorizationRequestFilter implements Filter {

    private static final String[] WHITELISTED_PATHS = {
            "/login",
            "/mongo"
           
    };

    @Autowired
    private UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        if (req.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name()) || !this.isAuthenticationRequired(req)) {

            chain.doFilter(request, response);

        } else {

            // Checking the sent token
            String tokenStr = (((HttpServletRequest) request).getHeader("token"));
            if (tokenStr == null) {
                HttpServletResponse res = (HttpServletResponse) response;
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            // Loading user
            User user = this.userRepository.findByRawToken(tokenStr);
            //User user = this.userRepository.findOne(4l);
            if (user == null || !user.getToken().isValid()) {
                HttpServletResponse res = (HttpServletResponse) response;
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            Authentication auth = new UsernamePasswordAuthenticationToken(user, null, getAuthorities(user));
            SecurityContextHolder.getContext().setAuthentication(auth);
            chain.doFilter(request, response);


        }

    }

    public Collection<GrantedAuthority> getAuthorities(User user) {
    	 return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString()));
    }

    private boolean isAuthenticationRequired(HttpServletRequest request) {
        for (String path : WHITELISTED_PATHS) {
            if (request.getRequestURL().toString().contains(path)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
