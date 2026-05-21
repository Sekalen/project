package com.example.final_project.service;

import com.example.final_project.entity.Product;
import com.example.final_project.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TelegramBot telegramBot;

    public List<Product> getAll() {
        log.info("Запрос списка продуктов");
        return productRepository.findAll();
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public Product create(Product product) {
        Product saved = productRepository.save(product);
        log.info("Создан продукт: {}", saved.getName());
        telegramBot.sendMessage("✅ Новый продукт: " + saved.getName());
        return saved;
    }

    public Product update(Long id, Product product) {
        Product existing = getById(id);
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setQuantity(product.getQuantity());
        existing.setDescription(product.getDescription());
        log.info("Обновлён продукт: {}", existing.getName());
        telegramBot.sendMessage("📝 Обновлён продукт: " + existing.getName());
        return productRepository.save(existing);
    }

    public void delete(Long id) {
        Product product = getById(id);
        productRepository.deleteById(id);
        log.info("Удалён продукт: {}", product.getName());
        telegramBot.sendMessage("🗑️ Удалён продукт: " + product.getName());
    }
}