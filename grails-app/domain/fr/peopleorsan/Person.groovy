package fr.peopleorsan

import org.bson.types.ObjectId

class Person {
    ObjectId id
    String firstName
    String lastName
    String bio
    String ssn

    static constraints = {
    }
    def setId(){
        id = new ObjectId()
    }
    static mapWith = "mongo"
}
