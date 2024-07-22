package com.tada.darajab2c.repository;

import com.tada.darajab2c.entity.PaymentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentStatusRepository extends MongoRepository<PaymentStatus, String> {
}
