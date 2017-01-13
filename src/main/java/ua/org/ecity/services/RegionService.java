package ua.org.ecity.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.org.ecity.entities.Region;
import ua.org.ecity.repository.RegionRepository;

import java.util.List;

@Service
public class RegionService {

    @Autowired
    RegionRepository regionRepository;

    public List<Region> getRegions() {
        return regionRepository.findAll();
    }

    public void saveRegion(Region region) {
        regionRepository.save(region);
    }

    public void deleteRegion(Integer id) {
        regionRepository.delete(id);
    }

    public Region getRegionByID(int id) {
        return regionRepository.getById(id);
    }

    public List<Region> getRegionByName(String name) {
        return regionRepository.findByName(name);
    }

    public Region getRegion(String name) {
        return regionRepository.findByName(name).get(0);
    }

    public boolean checkIfRegionInDataBase(String name) {
        return regionRepository.countValues(name) > 0;
    }

}