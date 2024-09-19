package br.nagualcode.menuservice.service;

import br.nagualcode.menuservice.model.Plate;
import br.nagualcode.menuservice.repository.PlateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlateService {

    private final PlateRepository plateRepository;

    @Autowired
    public PlateService(PlateRepository plateRepository) {
        this.plateRepository = plateRepository;
    }

    public List<Plate> getAllPlates() {
        return plateRepository.findAll();
    }

    public Optional<Plate> getPlateById(Long id) {
        return plateRepository.findById(id);
    }

    public Plate createPlate(Plate plate) {
        return plateRepository.save(plate);
    }

    public void deletePlate(Long id) {
        plateRepository.deleteById(id);
    }

    public Plate updatePlate(Long id, Plate plateDetails) {
        Optional<Plate> plateOptional = plateRepository.findById(id);
        if (plateOptional.isPresent()) {
            Plate existingPlate = plateOptional.get();
            existingPlate.setDescription(plateDetails.getDescription());
            existingPlate.setPrice(plateDetails.getPrice());
            return plateRepository.save(existingPlate);
        }
        return null; // In production, handle this with proper exception handling
    }
}

