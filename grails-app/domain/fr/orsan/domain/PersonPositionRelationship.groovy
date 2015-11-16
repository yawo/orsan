package fr.orsan.domain

import fr.orsan.utils.domain.PositionRelationship
import fr.orsan.utils.SafetyStatus
import org.neo4j.ogm.annotation.*

@RelationshipEntity(type="PLAYED_IN")
class PersonPositionRelationship implements PositionRelationship{
    @StartNode                          Person person;
    @Property                           SafetyStatus safetyStatus
    static constraints = {
    }
}
