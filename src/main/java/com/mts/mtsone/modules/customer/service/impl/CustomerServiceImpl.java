package com.mts.mtsone.modules.customer.service.impl;

import com.mts.mtsone.common.exception.BusinessException;
import com.mts.mtsone.modules.customer.dto.CreateCustomerDTO;
import com.mts.mtsone.modules.customer.dto.CustomerDTO;
import com.mts.mtsone.modules.customer.entity.Customer;
import com.mts.mtsone.modules.customer.mapper.CustomerMapper;
import com.mts.mtsone.modules.customer.repository.CustomerRepository;
import com.mts.mtsone.modules.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerDTO createCustomer(CreateCustomerDTO createCustomerDTO) {
        if (customerRepository.existsByPhoneNumber(createCustomerDTO.getPhoneNumber())) {
            throw new BusinessException("PHONE_NUMBER_EXISTS", "Số điện thoại đã tồn tại");
        }

        Customer customer = customerMapper.toEntity(createCustomerDTO);
        customer = customerRepository.save(customer);
        return customerMapper.toDTO(customer);
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomer(Long id, CreateCustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("CUSTOMER_NOT_FOUND", "Không tìm thấy khách hàng"));

        if (customerRepository.existsByPhoneNumberAndIdNot(customerDTO.getPhoneNumber(), id)) {
            throw new BusinessException("PHONE_NUMBER_EXISTS", "Số điện thoại đã tồn tại");
        }

        customerMapper.updateCustomerFromDTO(customerDTO, customer);
        customer = customerRepository.save(customer);
        return customerMapper.toDTO(customer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new BusinessException("CUSTOMER_NOT_FOUND", "Không tìm thấy khách hàng");
        }
        customerRepository.deleteById(id);
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("CUSTOMER_NOT_FOUND", "Không tìm thấy khách hàng"));
        return customerMapper.toDTO(customer);
    }    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDTO> getAllCustomers(int page, int size, String searchKey) {
        if(page < 0 || size < 0) {
            page = 0;
            size = 10;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        String normalizedSearchKey = searchKey != null ? searchKey.trim() : "";
        return customerRepository.findAllWithFilters(normalizedSearchKey, pageable)
                .map(customerMapper::toDTO);
    }
}
