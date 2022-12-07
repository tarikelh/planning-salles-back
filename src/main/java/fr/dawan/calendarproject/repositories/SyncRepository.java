package fr.dawan.calendarproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.SyncReport;

@Repository
public interface SyncRepository extends JpaRepository<SyncReport, Long> {
}
