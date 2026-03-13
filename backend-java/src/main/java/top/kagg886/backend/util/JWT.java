package top.kagg886.backend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.hmac.HMACSigner;
import io.fusionauth.jwt.hmac.HMACVerifier;
import lombok.SneakyThrows;
import top.kagg886.backend.entity.User;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
public class JWT {
    private static final String KEY = "cdd7c8de76524db58c27a442c1d209a6";
    private static final Long EXPIRE_TIME = Duration.of(30, ChronoUnit.DAYS).getSeconds();

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final Signer signer = HMACSigner.newSHA512Signer(KEY);
    private static final Verifier verifier = HMACVerifier.newVerifier(KEY);
    @SneakyThrows
    public static String publish(User u) {
        io.fusionauth.jwt.domain.JWT jwt = new io.fusionauth.jwt.domain.JWT();
        jwt.setIssuer("kagg886");
        jwt.setIssuedAt(ZonedDateTime.now());
        jwt.setExpiration(ZonedDateTime.now().plusSeconds(EXPIRE_TIME));
        jwt.setSubject(Base64.getEncoder().encodeToString(mapper.writeValueAsString(u).getBytes(StandardCharsets.UTF_8)));

        return io.fusionauth.jwt.domain.JWT.getEncoder().encode(
                jwt,
                signer
        );
    }

    @SneakyThrows
    public static User verify(String token) {
        io.fusionauth.jwt.domain.JWT jwt = io.fusionauth.jwt.domain.JWT.getDecoder().decode(token, verifier);

        return mapper.readValue(
                new String(Base64.getDecoder().decode(jwt.subject)), User.class
        );
    }
}
