package fr.orsan.domain

import org.neo4j.ogm.annotation.NodeEntity
@NodeEntity
class Address {
    Long id
    String name
    Double lat
    Double lon

    static constraints = {
    }
}
