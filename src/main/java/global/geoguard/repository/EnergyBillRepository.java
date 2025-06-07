package global.geoguard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import global.geoguard.model.EnergyBill;

public interface EnergyBillRepository extends JpaRepository<EnergyBill, Long> {

    Page<EnergyBill> findByOwnerId(Long userId, Pageable pageable);

}
