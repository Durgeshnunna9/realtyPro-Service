package com.realtypro.controller;

import com.realtypro.repository.AgentRepository;
import com.realtypro.repository.ManagerRepository;
import com.realtypro.repository.PropertyRepository;
import com.realtypro.repository.SaleRepository;
import com.realtypro.schema.Agent;
import com.realtypro.schema.Manager;
import com.realtypro.schema.Property;
import com.realtypro.schema.Sale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/sales")
public class SaleController {
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    // ✅ CREATE SALE
    @PostMapping("/create")
    public ResponseEntity<?> createSale(@RequestBody Sale sale) {
        try {
            // Validate agent
            Optional<Agent> agentOpt = agentRepository.findById(sale.getAgent().getAgentId());
            if (agentOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid Agent ID");
            }

            // Validate manager
            Optional<Manager> managerOpt = managerRepository.findById(sale.getManager().getManagerId());
            if (managerOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid Manager ID");
            }

            // Validate property
            Optional<Property> propertyOpt = propertyRepository.findById(sale.getProperty().getPropertyId());
            if (propertyOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid Property ID");
            }

            // Set validated references
            sale.setAgent(agentOpt.get());
            sale.setManager(managerOpt.get());
            sale.setProperty(propertyOpt.get());

            Sale savedSale = saleRepository.save(sale);
            return ResponseEntity.ok(savedSale);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error saving sale: " + e.getMessage());
        }
    }

    // ✅ READ ALL SALES
    @GetMapping("/all")
    public ResponseEntity<List<Sale>> getAllSales() {
        List<Sale> sales = saleRepository.findAll();
        if (sales.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sales);
    }

    // ✅ READ SINGLE SALE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getSaleById(@PathVariable Long id) {
        Optional<Sale> sale = saleRepository.findById(id);
        if (sale.isPresent()) {
            return ResponseEntity.ok(sale.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ UPDATE SALE
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSale(@PathVariable Long id, @RequestBody Sale updatedSale) {
        return saleRepository.findById(id).map(existingSale -> {
            existingSale.setSaleAmount(updatedSale.getSaleAmount());
            existingSale.setCommission(updatedSale.getCommission());

            // Optionally update relationships
            if (updatedSale.getAgent() != null) {
                agentRepository.findById(updatedSale.getAgent().getAgentId())
                        .ifPresent(existingSale::setAgent);
            }
            if (updatedSale.getManager() != null) {
                managerRepository.findById(updatedSale.getManager().getManagerId())
                        .ifPresent(existingSale::setManager);
            }
            if (updatedSale.getProperty() != null) {
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
