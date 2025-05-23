package com.mts.mtsone.modules.customer.mapper;

import com.mts.mtsone.modules.customer.dto.CreateCustomerDTO;
import com.mts.mtsone.modules.customer.dto.CustomerDTO;
import com.mts.mtsone.modules.customer.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDTO toDTO(Customer customer);
    Customer toEntity(CreateCustomerDTO createCustomerDTO);
    void updateCustomerFromDTO(CreateCustomerDTO dto, @MappingTarget Customer customer);
}
