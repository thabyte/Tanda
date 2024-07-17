package com.tada.darajab2c.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tada.darajab2c.dto.TokenInfo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class OAuthTokenGenerator {

    // TODO stpre credentials i env
    private static final String CLIENT_ID = "NBHVhlW39hP16J2pdqHgIHzd6w6G0aqGfMeKpKCZBSTGN9aX";
    private static final String CLIENT_SECRET = "Fr6M3oukh44hQsflXG9eVZI3UMoGrpKqfXlps9KOPIXB58AMiq0wzC9Vnk1UhJXJ";


    public static String getOAuthToken() throws IOException {
        String basicAuth = "Basic " + getBase64(CLIENT_ID, CLIENT_SECRET);
        OkHttpClient client = getOkHttpClient();

        Request request = new Request.Builder()
                .get()
                .url("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
                .addHeader("Authorization", basicAuth)
                .addHeader("Content-Type", "application/json")
                .build();

        log.info("Request parameters to Safaricom: \nMethod: {} \nURL: {} \nHeaders: {}",
                request.method(), request.url(), request.headers().toString());

        Call call = client.newCall(request);
        Response response = call.execute();

        if (response.isSuccessful()) {
            String responseBody = response.body().string();
            log.info("Safaricom Token: {}", responseBody);

            ObjectMapper mapper = new ObjectMapper();
            TokenInfo tokenInfo = mapper.readValue(responseBody, TokenInfo.class);

            return tokenInfo.getAccessToken();
        } else {
            String errorBody = response.body().string();
            log.info("Mpesa Response \n URL: {} \n Headers: {} \n Body: {}",
                    response.code(), response.headers(), errorBody);

            ObjectMapper mapper = new ObjectMapper();
            MpesaErrorResponse errorResponse = mapper.readValue(errorBody, MpesaErrorResponse.class);
            log.error(errorResponse.getErrorMessage());

            throw new IOException("Failed to get token: " + errorBody);
        }
    }

    public static String generateSecurityCredential(String plainTextPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashInBytes = md.digest(plainTextPassword.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }

            return Base64.getEncoder().encodeToString(sb.toString().getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private static OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(log::debug);
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        return new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(logging)
                .build();
    }

    private static String getBase64(String clientId, String clientSecret) {
        String authString = clientId + ":" + clientSecret;
        return Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.ISO_8859_1));
    }

    static class MpesaErrorResponse {
        private String errorMessage;

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
