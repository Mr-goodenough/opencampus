package yang.opencampus.opencampusback.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import yang.opencampus.opencampusback.entity.Rooter;

public interface RooterRepository extends JpaRepository<Rooter,Integer>{
    Optional<Rooter> findByName(String name);
    public boolean existsByName(String name);
    public Rooter findById(int Id);
}
