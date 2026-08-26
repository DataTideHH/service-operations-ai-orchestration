package de.datatidehh.serviceoperations.evaluation;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ResourceFingerprintService {

    public Map<String, String> sha256(List<String> classpathResources) {
        Map<String, String> fingerprints = new LinkedHashMap<>();
        classpathResources.forEach(path -> fingerprints.put(path, sha256(path)));
        return Map.copyOf(fingerprints);
    }

    String sha256(String classpathResource) {
        try (var input = new ClassPathResource(classpathResource).getInputStream()) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            input.transferTo(new java.security.DigestOutputStream(java.io.OutputStream.nullOutputStream(), digest));
            return HexFormat.of().formatHex(digest.digest());
        }
        catch (IOException exception) {
            throw new UncheckedIOException("Could not fingerprint classpath resource: " + classpathResource, exception);
        }
        catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }
}
