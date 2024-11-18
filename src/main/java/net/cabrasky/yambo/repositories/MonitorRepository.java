package net.cabrasky.yambo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.cabrasky.yambo.models.Monitor;
import java.util.List;
import java.util.Optional;

public interface MonitorRepository extends JpaRepository<Monitor, Long> {
    Optional<Monitor> findByName(String name);

    @Query("SELECT m FROM Monitor m JOIN m.groups g WHERE g.name = :groupName")
    List<Monitor> findByGroupName(@Param("groupName") String groupName);
}