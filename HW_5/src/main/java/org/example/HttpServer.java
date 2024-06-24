package org.example;

import com.sun.net.httpserver.*;
import io.jsonwebtoken.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;

public class HttpServer {
    private static final String SECRET_KEY = "46961CCABB9613EA1003C57F879680D18ABAFB8C5313C61391238C984E286ED0"; // Replace with your own secret key
    private static final String HEADER_STRING = "Authorization";

    public static void main(String[] args) throws Exception {
        com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(8765), 0);

        server.createContext("/login", new LoginHandler());
        server.createContext("/api/good", new GoodsHandler());
        server.createContext("/api/good/", new GoodsHandler());

        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();
    }

    static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                JSONObject json = new JSONObject(body);

                String login = json.getString("login");
                String password = json.getString("password"); // Assume password is already MD5 encoded

                System.out.println("Login successful: " + login + " " + password);

                if (authenticate(login, password)) {
                    String token = createJWT(login, 3600000); // 1 hour expiration
                    JSONObject responseJson = new JSONObject();
                    responseJson.put("token", token);
                    byte[] responseBytes = responseJson.toString().getBytes(StandardCharsets.UTF_8);

                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, responseBytes.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(responseBytes);
                    os.close();
                } else {
                    exchange.sendResponseHeaders(401, -1);
                }
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }

        private boolean authenticate(String login, String password) {

            //TODO change to request to database

            return "user".equals(login) && "5f4dcc3b5aa765d61d8327deb882cf99".equals(password); // "password" MD5
        }

        private String createJWT(String subject, long ttl_ms) {
            long now_ms = System.currentTimeMillis();

            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

            byte[] apiKeySecretBytes = Base64.getDecoder().decode(SECRET_KEY);
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

            JwtBuilder builder = Jwts.builder().setIssuedAt(new Date(now_ms)).setSubject(subject).signWith(signingKey, signatureAlgorithm);

            if (ttl_ms >= 0) {
                long expMillis = now_ms + ttl_ms;

                builder.setExpiration(new Date(expMillis));
            }

            return builder.compact();
        }
    }

    static class GoodsHandler implements HttpHandler {
        private final Map<String, JSONObject> goods = new HashMap<>();

        public GoodsHandler() {
            // Sample data
            goods.put("1", new JSONObject().put("id", 1).put("name", "Good 1").put("price", 100));
            goods.put("2", new JSONObject().put("id", 2).put("name", "Good 2").put("price", 200));
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Headers headers = exchange.getRequestHeaders();
            String token = headers.getFirst(HEADER_STRING);

            if (token == null) {
                System.out.println("No token provided");
                exchange.sendResponseHeaders(403, -1);
                return;
            }

            if (!isTokenValid(token)) {
                System.out.println("Invalid token: " + token);
                exchange.sendResponseHeaders(403, -1);
                return;
            }

            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String id = path.substring(path.lastIndexOf('/') + 1);

            switch (method) {
                case "GET":
                    System.out.println("GET " + id);
                    handleGet(exchange, id);
                    break;
                case "PUT":
                    System.out.println("PUT " + id);
                    handlePut(exchange);
                    break;
                case "POST":
                    System.out.println("POST " + id);
                    handlePost(exchange, id);
                    break;
                case "DELETE":
                    System.out.println("DELETE " + id);
                    handleDelete(exchange, id);
                    break;
                default:
                    exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }

        private void handleGet(HttpExchange exchange, String id) throws IOException {
            //TODO change to call to database
            if (goods.containsKey(id)) {
                JSONObject response = goods.get(id);
                byte[] bytes = response.toString().getBytes(StandardCharsets.UTF_8);

                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, bytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(bytes);
                os.close();
            } else {
                exchange.sendResponseHeaders(404, -1); // 404 Not Found
            }
        }

        private void handlePut(HttpExchange exchange) throws IOException {

            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(body);

            if (json.has("id") && json.has("name") && json.has("price")) {
                int price = json.getInt("price");
                if (price < 0) {
                    exchange.sendResponseHeaders(409, -1); // Conflict
                    return;
                }

                String id = String.valueOf(json.getInt("id"));
                goods.put(id, json);

                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(201, 0); // 201 Created
                exchange.getResponseBody().close();
            } else {
                exchange.sendResponseHeaders(409, -1); // Conflict
            }
        }

        private void handlePost(HttpExchange exchange, String id) throws IOException {
            if (!goods.containsKey(id)) {
                exchange.sendResponseHeaders(404, -1); // 404 Not Found
                return;
            }

            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(body);

            if (json.has("price") && json.getInt("price") < 0) {
                exchange.sendResponseHeaders(409, -1); // Conflict
                return;
            }

            JSONObject existingGood = goods.get(id);
            for (String key : json.keySet()) {
                existingGood.put(key, json.get(key));
            }

            exchange.sendResponseHeaders(204, -1); // 204 No Content
        }

        private void handleDelete(HttpExchange exchange, String id) throws IOException {
            if (goods.remove(id) != null) {
                exchange.sendResponseHeaders(204, -1); // 204 No Content
            } else {
                exchange.sendResponseHeaders(404, -1); // 404 Not Found
            }
        }

        private boolean isTokenValid(String token) {
            try {
                Jwts.parserBuilder()
                        .setSigningKey(Base64.getDecoder().decode(SECRET_KEY))
                        .build()
                        .parseClaimsJws(token);
                return true;
            } catch (JwtException | IllegalArgumentException e) {
                return false;
            }
        }
    }
}
