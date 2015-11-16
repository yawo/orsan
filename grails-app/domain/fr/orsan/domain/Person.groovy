package fr.orsan.domain

import fr.orsan.utils.PrivacyStatus
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.annotation.typeconversion.DateString

@NodeEntity
class Person {
    //System id
    Long id
    String email

    //Privacy
    PrivacyStatus   privacyStatus
    //Safety status

    //Identity
    String          firstName
    String          lastName
    String          race
    String          eyes
    String          height
    String          bio
    //Sensitive
    String          ssn
    List<String>    pictures
    List<String>    phones
    @DateString("yy-MM-dd")
    Date            birthday
    String          nationality


    //Addresses
    @Relationship(type="COMMON_ADDRESS", direction=Relationship.OUTGOING)
    List<Address> commonAddresses

    PersonPositionRelationship lastKnowAddress

    //Friends
    @Relationship(type="FRIENDS", direction=Relationship.UNDIRECTED)
    List<Person>    friends

    //Relatives (those can activate a search or make declaration)
    @Relationship(type="WATCHERS", direction=Relationship.OUTGOING)
    List<Person>    watchers


    static constraints = {
    }
}
