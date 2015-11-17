package fr.orsan.domain

import org.neo4j.ogm.annotation.NodeEntity
//create Index directly in DB. and use after save
@NodeEntity
class Address {
    Long id
    Double lat
    Double lon



    static constraints = {
    }
}
