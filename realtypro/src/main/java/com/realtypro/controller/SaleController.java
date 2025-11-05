package com.realtypro.controller;

import com.realtypro.schema.Sale;
import com.realtypro.schema.Property;
import com.realtypro.schema.User;
import com.realtypro.repository.SaleRepository;
import com.realtypro.repository.UserRepository;
import com.realtypro.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    // ✅ CREATE SALE
    @PostMapping("/create")
    public ResponseEntity<?> createSale(@RequestBody Sale sale) {
        try {
            // Validate Agent (User with role = AGENT)
            Optional<User> agentOpt = userRepository.findById(sale.getAgent().getUserId());
            if (agentOpt.isEmpty() || !"AGENT".equalsIgnoreCase(agentOpt.get().getRole().name())) {
                return ResponseEntity.badRequest().body("Invalid Agent ID or Role");
            }

            // Validate Manager (User with role = MANAGER)
            Optional<User> managerOpt = userRepository.findById(sale.getManager().getUserId());
            if (managerOpt.isEmpty() || !"MANAGER".equalsIgnoreCase(managerOpt.get().getRole().name())) {
                return ResponseEntity.badRequest().body("Invalid Manager ID or Role");
            }

            // Validate Property
            Optional<Property> propertyOpt = propertyRepository.findById(sale.getProperty().getPropertyId());
            if (propertyOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid Property ID");
            }

            sale.setAgent(agentOpt.get());
            sale.setManager(managerOpt.get());
            sale.setProperty(propertyOpt.get());

            Sale savedSale = saleRepository.save(sale);
            return ResponseEntity.ok(savedSale);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Error saving sale: " + e.getMessage());
        }
    }

    // ✅ READ ALL SALES
    @GetMapping("/all")
    public ResponseEntity<List<Sale>> getAllSales() {
        List<Sale> sales = saleRepository.findAll();
        if (sales.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(sales);
    }

    // ✅ READ SALE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getSaleById(@PathVariable Long id) {
        return saleRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ UPDATE SALE
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSale(@PathVariable Long id, @RequestBody Sale updatedSale) {
        return saleRepository.findById(id).map(existingSale -> {
            existingSale.setSaleAmount(updatedSale.getSaleAmount());
            existingSale.setCommission(updatedSale.getCommission());

            if (updatedSale.getAgent() != null && updatedSale.getAgent().getUserId() != null) {
                userRepository.findById(updatedSale.getAgent().getUserId())
                        .ifPresent(existingSale::setAgent);
            }
            if (updatedSale.getManager() != null && updatedSale.getManager().getUserId() != null) {
                userRepository.findById(updatedSale.getManager().getUserId())
                        .ifPresent(existingSale::setManager);
            }
            if (updatedSale.getProperty() != null && updatedSale.getProperty().getPropertyId() != null) {
                propertyRepository.findById(updatedSale.getProperty().getPropertyId())
                        .ifPresent(existingSale::setProperty);
            }

            Sale saved = saleRepository.save(existingSale);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ DELETE SALE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSale(@PathVariable Long id) {
        if (!saleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        saleRepository.deleteById(id);
        return ResponseEntity.ok("Sale deleted successfully.");
    }
}
