package com.leaguebuilds.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseInitializer {
    @Value("${FIREBASE.CONFIG.FILE.PATH.PROD}")
    private String FIREBASE_CONFIG_FILE_PATH;

    @PostConstruct
    public void init() {
        try {
            FileInputStream serviceAccount = new FileInputStream(FIREBASE_CONFIG_FILE_PATH);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
