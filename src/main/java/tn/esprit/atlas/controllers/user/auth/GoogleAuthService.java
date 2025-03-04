package tn.esprit.atlas.controllers.user.auth;

import com.google.api.client.auth.oauth2.*;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;


import com.sun.net.httpserver.HttpServer;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.List;

public class GoogleAuthService {

    private static final String CLIENT_SECRET_FILE = "/credentials.json";
    private static final List<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/userinfo.email",
            "https://www.googleapis.com/auth/userinfo.profile");

    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    public static Credential getCredentials() throws IOException {
        InputStream in = GoogleAuthService.class.getResourceAsStream(CLIENT_SECRET_FILE);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CLIENT_SECRET_FILE);
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setAccessType("offline")
                .build();

        int port = 8888;
        final Credential[] credential = new Credential[1];

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/callback", exchange -> {
            URI requestUri = exchange.getRequestURI();
            String query = requestUri.getQuery();
            String code = query.split("=")[1];

            String response = "Authorization successful! You can close this window.";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            exchange.close();

            server.stop(0);

            try {
                TokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri("http://localhost:" + port + "/callback").execute();
                credential[0] = flow.createAndStoreCredential(tokenResponse, "user");
                System.out.println("Credential obtained: " + credential[0].getAccessToken());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        server.start();

        String authUrl = flow.newAuthorizationUrl().setRedirectUri("http://localhost:" + port + "/callback").build();
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(URI.create(authUrl));
        } else {
            System.out.println("Open this URL manually: " + authUrl);
        }

        while (credential[0] == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return credential[0];
    }

    public static String fetchUserInfo(Credential credential) throws IOException {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        GenericUrl url = new GenericUrl("https://www.googleapis.com/oauth2/v2/userinfo");
        HttpRequest request = requestFactory.buildGetRequest(url);
        request.getHeaders().setAuthorization("Bearer " + credential.getAccessToken());

        return request.execute().parseAsString();
    }
}