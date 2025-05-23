package com.mts.mtsone.modules.customer.service;

import com.mts.mtsone.modules.customer.dto.CreateCustomerDTO;
import com.mts.mtsone.modules.customer.dto.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    CustomerDTO createCustomer(CreateCustomerDTO createCustomerDTO);
    CustomerDTO updateCustomer(Long id, CreateCustomerDTO customerDTO);
    void deleteCustomer(Long id);
    CustomerDTO getCustomerById(Long id);
    Page<CustomerDTO> getAllCustomers(Pageable pageable);
}
