package fr.orsan.repositories
import fr.orsan.domain.Address
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.stereotype.Repository

@Repository
public interface AddressRepository extends GraphRepository<Address> {
    // MATCH (a:Address {firstName={0}}) RETURN a
    @Query("START node = node:geom('withinDistance:[ {0} , {1} , {2} ]') return node")
    List<Address> findWithinRadiumKm(String centerLat, String centerLon, String radiusKm);
}
