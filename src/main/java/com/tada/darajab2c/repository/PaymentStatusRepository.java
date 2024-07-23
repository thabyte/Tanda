package com.tada.darajab2c.repository;

import com.tada.darajab2c.entity.PaymentStatusEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentStatusRepository extends MongoRepository<PaymentStatusEntity, String> {
}
