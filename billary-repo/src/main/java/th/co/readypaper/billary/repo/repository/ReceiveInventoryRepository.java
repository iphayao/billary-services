package th.co.readypaper.billary.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.readypaper.billary.repo.entity.receive.ReceiveInventory;

import java.util.List;
import java.util.UUID;

public interface ReceiveInventoryRepository extends JpaRepository<ReceiveInventory, UUID> {

    List<ReceiveInventory> findByOrderByDocumentIdDesc();
    List<ReceiveInventory> findByOrderByDocumentId();

}
