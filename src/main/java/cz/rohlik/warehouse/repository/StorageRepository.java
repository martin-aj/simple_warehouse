package cz.rohlik.warehouse.repository;

import cz.rohlik.warehouse.domain.Storage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends CrudRepository<Storage, Long> {

}
