package dev.kaua.repository;

import dev.kaua.model.Product;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ProductRepository {
    private final Map<Integer, Product> store = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public ProductRepository() {
        save(new Product(idCounter.getAndIncrement(), "Laptop Pro", 4999.00, "Electronics"));
        save(new Product(idCounter.getAndIncrement(), "Mechanical Keyboard", 349.90, "Electronics"));
        save(new Product(idCounter.getAndIncrement(), "Standing Desk", 1299.00, "Furniture"));
    }

    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }

    public List<Product> findByCategory(String category) {
        return store.values().stream()
            .filter(p -> p.getCategory().equalsIgnoreCase(category))
            .collect(Collectors.toList());
    }

    public Optional<Product> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    public Product save(Product product) {
        if (product.getId() == 0) product.setId(idCounter.getAndIncrement());
        store.put(product.getId(), product);
        return product;
    }

    public boolean deleteById(int id) {
        return store.remove(id) != null;
    }
}
