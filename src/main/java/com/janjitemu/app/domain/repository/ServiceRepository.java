package com.janjitemu.app.domain.repository;

import com.janjitemu.app.domain.model.ServiceItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends MongoRepository<ServiceItem, String> {
}
