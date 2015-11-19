package fr.orsan.repositories

import fr.orsan.domain.Address
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
class AddressRepositorySpec extends Specification {
    static Logger logger = LoggerFactory.getLogger(AddressRepositorySpec.class)

    @Autowired
    AddressRepository addressRepository

    @Autowired
    Neo4jOperations neo4jTemplate

    Address center = null
    //to delete : MATCH (n:Address)    DETACH DELETE n
    def createAdresses(){
        Address paris = new Address()
        paris.setLat(48.8534100)
        paris.setLon(2.3488000)
        neo4jTemplate.save(paris)

        Address london = new Address()
        london.setLat(51.5085300)
        london.setLon(-0.1257400)
        neo4jTemplate.save(london)
        Address insideParis = new Address()
        insideParis.setLat(48.8634100)
        insideParis.setLon(2.3400000)
        neo4jTemplate.save(insideParis)
        paris
    }

    def setup() {
        center = createAdresses()
    }

    def cleanup() {
    }

    void "test neo4j addressRepository"() {
        given:
            List<Address> founds = addressRepository.findWithinRadiumKm(center.lat, center.lon, 10)
            logger.info "n addresses around 10 km: ${founds.size()}"

        expect:
            !founds.empty
    }
}