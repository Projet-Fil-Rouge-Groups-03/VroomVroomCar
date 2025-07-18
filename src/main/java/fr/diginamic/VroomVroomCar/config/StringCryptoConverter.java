package fr.diginamic.VroomVroomCar.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

/**
 * Convertisseur pour chiffrer et déchiffrer les chaînes de caractères.
 * Ce convertisseur est utilisé pour chiffrer les données sensibles avant de les stocker en base de données.
 */
@Converter
public class StringCryptoConverter implements AttributeConverter<String, String> {

    /**
     * Algorithme de chiffrement utilisé.
     */
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * Clé secrète utilisée pour le chiffrement.
     */
    private final Key key;

    /**
     * Vecteur d'initialisation utilisé pour le chiffrement.
     */
    private final IvParameterSpec iv;

    /**
     * Constructeur du convertisseur de chiffrement.
     *
     * @param secretKey La clé secrète pour le chiffrement, doit faire 32 caractères.
     * @param ivKey    La clé d'initialisation pour le chiffrement, doit faire 16 caractères.
     * @throws IllegalArgumentException Si les clés ne respectent pas les longueurs requises.
     */
    public StringCryptoConverter(@Value("${app.crypto.secret-key}") String secretKey, @Value("${app.crypto.iv-key}") String ivKey) {
        if (secretKey == null || secretKey.length() != 32) {
            throw new IllegalArgumentException("La clé secrète 'app.crypto.secret-key' doit faire 32 caractères.");
        }
        if (ivKey == null || ivKey.length() != 16) {
            throw new IllegalArgumentException("La clé d'initialisation 'app.crypto.iv-key' doit faire 16 caractères.");
        }

        this.key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
        this.iv = new IvParameterSpec(ivKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Chiffre une chaîne de caractères pour la stocker en base de données.
     *
     * @param attribute La chaîne de caractères à chiffrer.
     * @return La chaîne de caractères chiffrée.
     * @throws IllegalStateException Si une erreur se produit lors du chiffrement.
     */
    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, this.key, this.iv);
            byte[] encryptedBytes = cipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new IllegalStateException("Erreur lors du chiffrement de la donnée.", e);
        }
    }

    /**
     * Déchiffre une chaîne de caractères provenant de la base de données.
     *
     * @param dbData La chaîne de caractères chiffrée à déchiffrer.
     * @return La chaîne de caractères déchiffrée.
     * @throws IllegalStateException Si une erreur se produit lors du déchiffrement.
     */
    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, this.key, this.iv);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(dbData));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Erreur lors du déchiffrement de la donnée. La clé a-t-elle changé ?", e);
        }
    }
}