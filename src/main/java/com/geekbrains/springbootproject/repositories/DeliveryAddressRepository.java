package com.geekbrains.springbootproject.repositories;

import com.geekbrains.springbootproject.entities.DeliveryAddress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryAddressRepository extends CrudRepository<DeliveryAddress, Long> {
    DeliveryAddress findFirstByUser_Id(Long userId);
}
