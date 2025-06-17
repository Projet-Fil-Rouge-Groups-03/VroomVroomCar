package fr.diginamic.VroomVroomCar.service;

import io.jsonwebtoken.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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

    public ResponseCookie generateToken(String username) {
        String jwt = Jwts.builder().setSubject(username).setExpiration(new Date(System.currentTimeMillis() + EXPIRES_IN)).signWith(SignatureAlgorithm.HS256, JWT_SECRET).compact();
        return ResponseCookie.from(TOKEN_COOKIE, jwt)
                .httpOnly(true)
                .maxAge(EXPIRES_IN).path("/").build();
    }

    public ResponseCookie invalidateToken() {
        return ResponseCookie.from(TOKEN_COOKIE, "")
                .httpOnly(true)
                .maxAge(0) // Expire immédiatement
                .path("/")
                .build();
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
}
