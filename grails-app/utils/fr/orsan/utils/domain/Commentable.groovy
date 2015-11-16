package fr.orsan.utils.domain

import org.neo4j.ogm.annotation.Property

/**
 * Created by ykpotufe on 16/11/2015.
 */
trait Commentable {
    @Property                           List<String> comments
}