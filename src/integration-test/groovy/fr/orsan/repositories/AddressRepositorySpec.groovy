package fr.orsan.repositories
import fr.orsan.domain.Address
import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.template.Neo4jOperations
import spock.lang.Specification

@Rollback
@Integration
class AddressRepositorySpec extends Specification {
    static Logger logger = LoggerFactory.getLogger(AddressRepositorySpec.class)

    @Autowired AddressRepository addressRepository
    @Autowired Neo4jOperations neo4jTemplate

    Address center          = null
    List<Address> addresses = null
    //to delete : MATCH (n:Address)    DETACH DELETE n
    def createAdresses(){
        addresses = []

        Address paris = new Address()
        paris.setLat(48.8534100)
        paris.setLon(2.3488000)
        neo4jTemplate.save(paris)
        addresses.add(paris)

        Address london = new Address()
        london.setLat(51.5085300)
        london.setLon(-0.1257400)
        neo4jTemplate.save(london)
        addresses.add(london)


        Address insideParis = new Address()
        insideParis.setLat(48.8634100)
        insideParis.setLon(2.3400000)
        neo4jTemplate.save(insideParis)
        addresses.add(insideParis)

        paris
    }

    def setup() { center = createAdresses()  }

    def cleanup() { addresses.each { neo4jTemplate.delete(it) } }

    void "test neo4j addressRepository"() {
        given:
            Set<Address> founds = addressRepository.withinRadiumKm(center.lat, center.lon, 100.0)
            logger.info "n addresses around 100 km: ${founds.each {println it}}"


        expect: founds.size() > 0
    }
}
