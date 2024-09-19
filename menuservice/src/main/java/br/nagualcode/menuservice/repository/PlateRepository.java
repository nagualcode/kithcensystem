package br.nagualcode.menuservice.repository;

import br.nagualcode.menuservice.model.Plate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlateRepository extends JpaRepository<Plate, Long> {
    // Additional query methods can be defined here if needed
}
