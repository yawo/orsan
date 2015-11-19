package fr.orsan.repositories

import fr.orsan.domain.Incident
import fr.orsan.domain.Person
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.stereotype.Repository


@Repository
public interface IncidentRepository extends GraphRepository<Incident> {
    Incident findByName(String name);
}
