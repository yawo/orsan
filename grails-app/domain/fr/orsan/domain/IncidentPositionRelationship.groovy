package fr.orsan.domain

import fr.orsan.utils.domain.PositionRelationship
import org.neo4j.ogm.annotation.*

@RelationshipEntity(type="PLAYED_IN")
class IncidentPositionRelationship implements PositionRelationship {

    @StartNode                          Incident incident;
    static constraints = {
    }
}
