package fr.diginamic.VroomVroomCar.service;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtAuthentificationService implements IJwtAuthentificationService {
    @Value("${jwt.expires_in}")
    private Integer EXPIRES_IN;

    @Value("${jwt.cookie}")
    private String TOKEN_COOKIE;

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    public ResponseCookie generateToken(String mail, String role) {
        String jwt = Jwts.builder()
                .setSubject(mail)
                .claim("roles", role)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRES_IN))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
        return ResponseCookie.from(TOKEN_COOKIE, jwt)
                .httpOnly(true)
                .maxAge(EXPIRES_IN)
                .path("/")
                .build();
    }

    public void invalidateToken(HttpServletResponse http) {
        Cookie cookie = new Cookie(TOKEN_COOKIE, "");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            cookie.setPath("/");
        http.addCookie(cookie);
    }

    public String getSubject(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().getSubject();
    };

    public Boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token)
                    .getBody();

            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expiré");
            return false;
        } catch (UnsupportedJwtException e) {
            System.out.println("Format du token non supporté");
            return false;
        } catch (MalformedJwtException e) {
            System.out.println("Token malformé");
            return false;
        } catch (SignatureException e) {
            System.out.println("Signature invalide");
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("Token vide ou null");
            return false;
        }catch (Exception e) {
            System.out.println("C'est pas normal ça....");
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRoleFromToken(String token) {
        return (String) Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .get("roles");
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
