package fr.orsan.domain

import fr.orsan.utils.SafetyStatus
import org.neo4j.ogm.annotation.*
import org.neo4j.ogm.annotation.typeconversion.DateString

@RelationshipEntity(type="PLAYED_IN")
class IncidentPositionRelationship {
    @GraphId                            Long relationshipId;
    @StartNode                          Person person;
    @EndNode                            Address address;
    @DateString("yy-MM-dd HH:mm:ss")    Date at;
    @Property                           SafetyStatus safetyStatus
    @Property                           List<String> comments
    static constraints = {
    }
}
