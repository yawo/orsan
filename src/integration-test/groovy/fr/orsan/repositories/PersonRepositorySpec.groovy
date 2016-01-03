package fr.orsan.repositories

import fr.orsan.domain.Person
import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.template.Neo4jOperations
import spock.lang.Specification

@Rollback
@Integration
class PersonRepositorySpec extends Specification {
    static Logger logger = LoggerFactory.getLogger(PersonRepositorySpec.class)

    @Autowired PersonRepository personRepository
    @Autowired Neo4jOperations neo4jTemplate

    Person person

    def setup() {
        person = new Person()
        person.setUserId('mcguy2008@gmail.com')
        person = neo4jTemplate.save(person)
        person
    }

    def cleanup() {neo4jTemplate.delete(person)}

    void "test neo4j personRepository"() {
        given:
            Person found = personRepository.findOne(person.id)
            logger.info "person: ${found.userId}"

        expect:  found?.id != null //todo: check this form
    }
}