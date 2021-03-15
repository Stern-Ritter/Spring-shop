package com.geekbrains.springbootproject.services;

import com.geekbrains.springbootproject.entities.DeliveryAddress;
import com.geekbrains.springbootproject.repositories.DeliveryAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryAddressService {
    private DeliveryAddressRepository deliveryAddressRepository;

    @Autowired
    public void setDeliveryAddressRepository(DeliveryAddressRepository deliveryAddressRepository) {
        this.deliveryAddressRepository = deliveryAddressRepository;
    }

    public DeliveryAddress getUserAddress(Long userId) {
        return deliveryAddressRepository.findFirstByUser_Id(userId);
    }

    public void save(DeliveryAddress deliveryAddress){
        deliveryAddressRepository.save(deliveryAddress);
    }
}
