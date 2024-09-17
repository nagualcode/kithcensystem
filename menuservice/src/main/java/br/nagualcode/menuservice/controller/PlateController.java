package br.nagualcode.menuservice.controller;

import br.nagualcode.menuservice.model.Plate;
import br.nagualcode.menuservice.service.PlateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/plates")
public class PlateController {

    private final PlateService plateService;

    @Autowired
    public PlateController(PlateService plateService) {
        this.plateService = plateService;
    }

    @GetMapping
    public List<Plate> getAllPlates() {
        return plateService.getAllPlates();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plate> getPlateById(@PathVariable Long id) {
        Optional<Plate> plate = plateService.getPlateById(id);
        return plate.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Plate> createPlate(@RequestBody Plate plate) {
        Plate createdPlate = plateService.createPlate(plate);
        return ResponseEntity.ok(createdPlate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Plate> updatePlate(@PathVariable Long id, @RequestBody Plate plateDetails) {
        Plate updatedPlate = plateService.updatePlate(id, plateDetails);
        if (updatedPlate != null) {
            return ResponseEntity.ok(updatedPlate);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlate(@PathVariable Long id) {
        plateService.deletePlate(id);
        return ResponseEntity.noContent().build();
    }
}
