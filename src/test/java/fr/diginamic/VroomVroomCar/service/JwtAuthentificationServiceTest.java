package fr.diginamic.VroomVroomCar.service;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseCookie;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JwtAuthentificationServiceTest {

    @InjectMocks
    private JwtAuthentificationService jwtService;

    @BeforeEach
    void setup() throws Exception {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());

        setPrivateField("EXPIRES_IN", 3600000);
        setPrivateField("TOKEN_COOKIE", "jwt_token");
        setPrivateField("JWT_SECRET", base64Key);

        jwtService.init();
    }

    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = JwtAuthentificationService.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(jwtService, value);
    }

    @Test
    void testGenerateToken() {
        ResponseCookie cookie = jwtService.generateToken("user1", "ROLE_ACTIF");

        assertEquals("jwt_token", cookie.getName());
        assertNotNull(cookie.getValue());
        assertTrue(cookie.isHttpOnly());
        assertEquals("/", cookie.getPath());
        assertEquals(3600000, cookie.getMaxAge().getSeconds());
    }

    @Test
    void testInvalidateToken() {
        HttpServletResponse response = mock(HttpServletResponse.class);

        jwtService.invalidateToken(response);

        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());

        Cookie cookie = cookieCaptor.getValue();
        assertEquals("jwt_token", cookie.getName());
        assertEquals("", cookie.getValue());
        assertEquals(0, cookie.getMaxAge());
        assertTrue(cookie.isHttpOnly());
        assertEquals("/", cookie.getPath());
    }

    @Test
    void testGetSubject() {
        String token = jwtService.generateToken("user1", "ROLE_ACTIF").getValue();

        String subject = jwtService.getSubject(token);

        assertEquals("user1", subject);
    }

    @Test
    void testValidateToken_Valid() {
        String token = jwtService.generateToken("user1", "ROLE_ACTIF").getValue();

        assertTrue(jwtService.validateToken(token));
    }

    @Test
    void testValidateToken_Invalid() {
        assertFalse(jwtService.validateToken("bad.token.value"));
    }
}

