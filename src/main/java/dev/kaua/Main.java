package dev.kaua;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import dev.kaua.model.Product;
import dev.kaua.repository.ProductRepository;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {
    static final ProductRepository repo = new ProductRepository();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/products", new ProductHandler());
        server.createContext("/health", exchange -> respond(exchange, 200, "{\"status\":\"ok\"}"));
        server.start();
        System.out.println("Server running on http://localhost:8080");
    }

    static void respond(HttpExchange ex, int code, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(code, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.getResponseBody().close();
    }

    static class ProductHandler implements HttpHandler {
        public void handle(HttpExchange ex) throws IOException {
            String method = ex.getRequestMethod();
            String path = ex.getRequestURI().getPath();

            try {
                if ("GET".equals(method) && path.equals("/api/products")) {
                    List<Product> products = repo.findAll();
                    respond(ex, 200, toJson(products));
                } else if ("GET".equals(method) && path.startsWith("/api/products/")) {
                    int id = Integer.parseInt(path.split("/")[3]);
                    var product = repo.findById(id);
                    if (product.isEmpty()) { respond(ex, 404, "{\"error\":\"Not found\"}"); return; }
                    respond(ex, 200, toJson(product.get()));
                } else {
                    respond(ex, 405, "{\"error\":\"Method not allowed\"}");
                }
            } catch (Exception e) {
                respond(ex, 500, "{\"error\":\"" + e.getMessage() + "\"}");
            }
        }

        String toJson(List<Product> list) {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(toJson(list.get(i)));
            }
            return sb.append("]").toString();
        }

        String toJson(Product p) {
            return String.format(
                "{\"id\":%d,\"name\":\"%s\",\"price\":%.2f,\"category\":\"%s\",\"active\":%b}",
                p.getId(), p.getName(), p.getPrice(), p.getCategory(), p.isActive()
            );
        }
    }
}
