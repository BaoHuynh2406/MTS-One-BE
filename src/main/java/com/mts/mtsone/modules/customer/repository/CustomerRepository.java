package com.mts.mtsone.modules.customer.repository;

import com.mts.mtsone.modules.auth.entity.User;
import com.mts.mtsone.modules.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);    @Query("SELECT c FROM Customer c WHERE " +
            "(:searchKey IS NULL OR :searchKey = '' OR " +
            "CAST(c.id AS string) LIKE CONCAT('%', :searchKey, '%') OR " +
            "LOWER(c.address) LIKE LOWER(CONCAT('%', :searchKey, '%')) OR " +
            "LOWER(c.fullName) LIKE LOWER(CONCAT('%', :searchKey, '%')) OR " +
            "LOWER(c.phoneNumber) LIKE LOWER(CONCAT('%', :searchKey, '%')))")
    Page<Customer> findAllWithFilters(
            @Param("searchKey") String searchKey,
            Pageable pageable
    );
}
