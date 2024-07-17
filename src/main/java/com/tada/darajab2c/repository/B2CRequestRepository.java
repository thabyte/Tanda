package com.tada.darajab2c.repository;

import com.tada.darajab2c.dto.B2CRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface B2CRequestRepository extends MongoRepository<B2CRequest, String> {
}

