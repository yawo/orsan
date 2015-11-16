package fr.orsan.domain

import org.neo4j.ogm.annotation.NodeEntity

@NodeEntity
class Address {
    Long id

    static constraints = {
    }
}
