package com.janjitemu.app.repository;

import com.janjitemu.app.model.ServiceItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends MongoRepository<ServiceItem, String> {
}
