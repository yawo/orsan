package fr.orsan.domain

import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.typeconversion.DateString

@NodeEntity
class Incident {
    Long id
    String name
    @DateString("yy-MM-dd HH:mm:ss")
    Date at

    static constraints = {
    }
}
