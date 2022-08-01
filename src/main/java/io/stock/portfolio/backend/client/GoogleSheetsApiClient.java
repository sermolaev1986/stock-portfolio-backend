package io.stock.portfolio.backend.client;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;

@Component
public class GoogleSheetsApiClient {
    private static String SPREADSHEET_ID = "1MBtHoA9FOXtG4GfVIdLr1JXHw7JXkdoK0ZCFmfR-U5I";

    @Value("${sheets-credentials}")
    private String credentials;

    private Sheets getSheetsService() throws GeneralSecurityException, IOException {
        var keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(credentials));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privKey = kf.generatePrivate(keySpec);

        Credential credential = new GoogleCredential.Builder()
                .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                .setJsonFactory(JacksonFactory.getDefaultInstance())
                .setServiceAccountId("portfolio-backend-20@spheric-ray-108018.iam.gserviceaccount.com")
                .setServiceAccountPrivateKey(privKey)
                .setServiceAccountScopes(List.of(SheetsScopes.SPREADSHEETS_READONLY))
                .build();
        credential.refreshToken();


        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("123")
                .build();
    }

    public List<List<Object>> getDataForRange(String range) {
        try {
            Sheets sheetsService = getSheetsService();

            var response = sheetsService.spreadsheets().values()
                    .get(SPREADSHEET_ID, range)
                    .execute();

            return response.getValues();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
