# java-rest-api

> Simple REST API built with pure Java — no Spring, no frameworks

A minimal HTTP server using Java's built-in `com.sun.net.httpserver`, demonstrating REST patterns, in-memory data, and JSON serialization without external dependencies.

## Endpoints

| Method | Route | Description |
|--------|-------|-------------|
| GET | /health | Health check |
| GET | /api/products | List all products |
| GET | /api/products/:id | Get product by ID |

## Run

```bash
# Compile
javac -d out src/main/java/dev/kaua/**/*.java src/main/java/dev/kaua/Main.java

# Run
java -cp out dev.kaua.Main

# Test
curl http://localhost:8080/api/products
curl http://localhost:8080/api/products/1
```

## Why no Spring?

This project intentionally uses only the Java standard library to demonstrate a clear understanding of HTTP, routing, and JSON at the lowest level before bringing in frameworks.
