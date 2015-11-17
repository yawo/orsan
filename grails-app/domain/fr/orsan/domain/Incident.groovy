package fr.orsan.domain

import org.neo4j.ogm.annotation.NodeEntity

@NodeEntity
class Incident {
    Long id
    String name
    String description
    List<IncidentPositionRelationship> incidentPositions

    static constraints = {
    }
}
