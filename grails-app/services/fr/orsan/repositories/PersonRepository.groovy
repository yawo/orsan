package fr.orsan.repositories

import fr.orsan.domain.Person
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.stereotype.Repository


@Repository
public interface PersonRepository extends GraphRepository<Person> {
    // MATCH (person:Person {name={0}}) RETURN person
    Person findByName(String name);

}
