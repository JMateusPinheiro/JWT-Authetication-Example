package com.tests.jm.jwtauthetication.security;

import com.tests.jm.jwtauthetication.dto.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.tests.jm.jwtauthetication.utils.Constants.HEADER_STRING;
import static com.tests.jm.jwtauthetication.utils.Constants.TOKEN_PREFIX;

@Component
public class JWTAuthenticationFilter extends GenericFilterBean {
    private JwtTokenCodec jwtTokenCodec;

    @Autowired
    public JWTAuthenticationFilter(JwtTokenCodec jwtTokenCodec) {
        this.jwtTokenCodec = jwtTokenCodec;
    }

    // PROBLEM, work without bearer type

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;

        final String header = httpRequest.getHeader(HEADER_STRING);

        final SecurityContext context = SecurityContextHolder.getContext();

        if (header != null && context.getAuthentication() == null) {

            final String tokenStr = header.substring(TOKEN_PREFIX.length());

            final JwtToken token = jwtTokenCodec.decodeToken(tokenStr);

            if (header.contains(TOKEN_PREFIX) && !token.isExpired()) {
                final PreAuthenticatedAuthenticationToken authentication =
                        new PreAuthenticatedAuthenticationToken(token, "n/a", token.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                context.setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
