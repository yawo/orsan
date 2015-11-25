package fr.orsan.domain

import fr.orsan.utils.OrsanRole
import fr.orsan.utils.PrivacyStatus
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.annotation.typeconversion.DateString
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.social.security.SocialUserDetails

@NodeEntity
class Person implements UserDetails, SocialUserDetails {
    private static final long serialVersionUID = 1

    transient springSecurityService
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
    Integer         age
    String          eyes
    String          height
    String          bio
    //Sensitive
    String          ssn

    //TODO Images: https://bitbucket.org/sbuettner/grails-cloudinary
    List<String>    pictures
    List<String>    phones
    @DateString("yy-MM-dd")
    Date            birthDate
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

    //Technical
    private Date lastModified;

    //UserDetails Security
    String password
    String username
    String userId
    Boolean accountNonExpired = Boolean.TRUE
    Boolean accountNonLocked = Boolean.TRUE
    Boolean credentialsNonExpired = Boolean.FALSE
    Boolean enabled = Boolean.TRUE
    List<OrsanRole> authorities



    static constraints = {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;
        if (nodeId == null) return super.equals(o);
        return nodeId.equals(person.nodeId);

    }

    @Override
    public int hashCode() {
        return nodeId != null ? nodeId.hashCode() : super.hashCode();
    }

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return null
    }

    @Override
    String getPassword() {
        return null
    }

    @Override
    String getUsername() {
        return null
    }

    @Override
    boolean isAccountNonExpired() {
        return false
    }

    @Override
    boolean isAccountNonLocked() {
        return false
    }

    @Override
    boolean isCredentialsNonExpired() {
        return false
    }

    @Override
    boolean isEnabled() {
        return false
    }
}
