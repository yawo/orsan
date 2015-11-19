package fr.orsan.repositories

import fr.orsan.domain.Person
import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.template.Neo4jOperations
import org.springframework.data.neo4j.template.Neo4jTemplate
import spock.lang.Specification

@Rollback
@Integration
class PersonRepositorySpec extends Specification {
    static Logger logger = LoggerFactory.getLogger(PersonRepositorySpec.class)

    @Autowired
    PersonRepository personRepository

    @Autowired
    Neo4jOperations neo4jTemplate

    def setup() {
        //personRepository = (PersonRepository)Holders.applicationContext.getBean("personRepository")
    }

    def cleanup() {
    }

    void "test neo4j personRepository"() {
        given:
            Person person = new Person()
            person.setEmail('mcguy2008@gmail.com')
            person = neo4jTemplate.save(person)
            Person found = personRepository.findOne(person.id)
            logger.info "person: ${found.id}, ${found.email}"
            //neo4jTemplate.delete(person)

        expect:
            found?.id != null //todo: check this form
    }
}