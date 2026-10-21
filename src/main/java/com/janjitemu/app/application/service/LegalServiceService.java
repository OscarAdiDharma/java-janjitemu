package com.janjitemu.app.application.service;

import com.janjitemu.app.domain.model.ServiceItem;
import com.janjitemu.app.domain.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LegalServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<ServiceItem> getAllServices() {
        return serviceRepository.findAll();
    }

    public Optional<ServiceItem> getServiceById(String id) {
        return serviceRepository.findById(id);
    }

    public ServiceItem createService(String nama, String deskripsi) {
        ServiceItem item = new ServiceItem(nama.trim(), deskripsi != null ? deskripsi.trim() : "");
        return serviceRepository.save(item);
    }

    public boolean updateService(String id, String nama, String deskripsi) {
        Optional<ServiceItem> opt = serviceRepository.findById(id);
        if (opt.isPresent()) {
            ServiceItem item = opt.get();
            if (nama != null) item.setNama(nama.trim());
            if (deskripsi != null) item.setDeskripsi(deskripsi.trim());
            serviceRepository.save(item);
            return true;
        }
        return false;
    }

    public boolean deleteService(String id) {
        if (serviceRepository.existsById(id)) {
            serviceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long countServices() {
        return serviceRepository.count();
    }

    public void saveAll(List<ServiceItem> items) {
        serviceRepository.saveAll(items);
    }
}
