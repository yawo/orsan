package fr.orsan.domain

import org.neo4j.ogm.annotation.NodeEntity

@NodeEntity
class Person {
    Long id
    String firstName
    String email
    String lastName

}
