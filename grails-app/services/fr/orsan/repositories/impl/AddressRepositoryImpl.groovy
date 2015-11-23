package fr.orsan.repositories.impl

import fr.orsan.domain.Address
import fr.orsan.repositories.AddressRepositoryCustom
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.template.Neo4jOperations
import org.springframework.stereotype.Component

/**
 * Created by yawo on 22/11/15.
 */
@Component
public class AddressRepositoryImpl implements AddressRepositoryCustom{
    @Autowired Neo4jOperations neo4jTemplate;

    @Override
    Set<Address> withinRadiumKm(double lat, double lon, double radiusKm) {
        neo4jTemplate.queryForObjects(Address.class,
                "START node = node:geom('withinDistance: [ ${lat} , ${lon} , ${radiusKm} ]') return node",
                Collections.emptyMap()
        )
    }
}
