package fr.diginamic.VroomVroomCar.security;

import fr.diginamic.VroomVroomCar.service.JwtAuthentificationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtAuthentificationService jwtService;
    @Value("${jwt.cookie}")
    private String TOKEN_COOKIE;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getCookies() != null) {
            Stream.of(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(TOKEN_COOKIE))
                    .map(Cookie::getValue)
                    .forEach(token -> {
                        try {
                            if (jwtService.isTokenValid(token)) {
                                String email = jwtService.getEmailFromToken(token);
                                String role = jwtService.getRoleFromToken(token);

                                if (!"ROLE_BANNI".equals(role)) {
                                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                                    authorities.add(new SimpleGrantedAuthority(role));

                                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                            email, null, authorities
                                    );
                                    SecurityContextHolder.getContext().setAuthentication(auth);
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("Erreur lors du parsing du JWT : " + e.getMessage());
                        }
                    });
        }
        filterChain.doFilter(request, response);
    }
}