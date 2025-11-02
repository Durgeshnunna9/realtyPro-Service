package com.realtypro.service;

import com.realtypro.repository.AgentRepository;
import com.realtypro.repository.ManagerRepository;
import com.realtypro.repository.PropertyRepository;
import com.realtypro.repository.SaleRepository;
import com.realtypro.schema.Agent;
import com.realtypro.schema.Manager;
import com.realtypro.schema.Property;
import com.realtypro.schema.Sale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private PropertyRepository propertyRepository;


    // ✅ Create Sale
    public Sale createSale(Sale sale) {
        Optional<Agent> agentOpt = agentRepository.findById(sale.getAgent().getAgentId());
        if (agentOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid Agent ID: " + sale.getAgent().getAgentId());
        }

        Optional<Manager> managerOpt = managerRepository.findById(sale.getManager().getManagerId());
        if (managerOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid Manager ID: " + sale.getManager().getManagerId());
        }

        Optional<Property> propertyOpt = propertyRepository.findById(sale.getProperty().getPropertyId());
        if (propertyOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid Property ID: " + sale.getProperty().getPropertyId());
        }

        sale.setAgent(agentOpt.get());
        sale.setManager(managerOpt.get());
        sale.setProperty(propertyOpt.get());

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

            // Update agent
            if (updatedSale.getAgent() != null && updatedSale.getAgent().getAgentId() != null) {
                agentRepository.findById(updatedSale.getAgent().getAgentId())
                        .ifPresent(existingSale::setAgent);
            }

            // Update manager
            if (updatedSale.getManager() != null && updatedSale.getManager().getManagerId() != null) {
                managerRepository.findById(updatedSale.getManager().getManagerId())
                        .ifPresent(existingSale::setManager);
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
