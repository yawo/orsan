package fr.orsan.domain

import org.neo4j.ogm.annotation.NodeEntity
@NodeEntity
class Address {
    Long id
    String name
    Double lat
    Double lon

    Address(Long id, String name, Double lat, Double lon) {
        this.id = id
        this.name = name
        this.lat = lat
        this.lon = lon
    }

    static constraints = {
    }
}
