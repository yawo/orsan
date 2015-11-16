package fr.orsan.utils.domain

import fr.orsan.domain.Address
import org.neo4j.ogm.annotation.EndNode
import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.typeconversion.DateString

/**
 * Created by ykpotufe on 16/11/2015.
 */
trait PositionRelationship extends Commentable{
    @GraphId                            Long relationshipId
    @EndNode                            Address address
    @DateString("yy-MM-dd HH:mm:ss")    Date at;
}