package com.realtypro.service;

import com.realtypro.schema.Sale;
import com.realtypro.schema.User;
import com.realtypro.schema.Property;
import com.realtypro.repository.SaleRepository;
import com.realtypro.repository.UserRepository;
import com.realtypro.repository.PropertyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    // ✅ Create Sale
    public Sale createSale(Sale sale) {
        if (sale.getUser() == null || sale.getUser().getUserId() == null) {
            throw new IllegalArgumentException("User reference is required");
        }

        User user = userRepository.findById(sale.getUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + sale.getUser().getUserId()));

        if (user.getRole() == null) {
            throw new IllegalArgumentException("User role is not defined");
        }

        Property property = propertyRepository.findById(sale.getProperty().getPropertyId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Property ID: " + sale.getProperty().getPropertyId()));

        sale.setUser(user);
        sale.setProperty(property);

        return saleRepository.save(sale);
    }

    // ✅ Get all sales
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    // ✅ Get sale by ID
    public Optional<Sale> getSaleById(Long id) {
        return saleRepository.findById(id);
    }

    // ✅ Update sale
    public Sale updateSale(Long id, Sale updatedSale) {
        return saleRepository.findById(id).map(existingSale -> {

            existingSale.setSaleAmount(updatedSale.getSaleAmount());
            existingSale.setCommission(updatedSale.getCommission());

            // Update user
            if (updatedSale.getUser() != null && updatedSale.getUser().getUserId() != null) {
                userRepository.findById(updatedSale.getUser().getUserId())
                        .ifPresent(existingSale::setUser);
            }

            // Update property
            if (updatedSale.getProperty() != null && updatedSale.getProperty().getPropertyId() != null) {
                propertyRepository.findById(updatedSale.getProperty().getPropertyId())
                        .ifPresent(existingSale::setProperty);
            }

            return saleRepository.save(existingSale);
        }).orElseThrow(() -> new IllegalArgumentException("Sale not found with ID: " + id));
    }

    // ✅ Delete sale
    public void deleteSale(Long id) {
        if (!saleRepository.existsById(id)) {
            throw new IllegalArgumentException("Sale not found with ID: " + id);
        }
        saleRepository.deleteById(id);
    }
}
