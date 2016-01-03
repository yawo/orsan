package fr.orsan.domain
import fr.orsan.utils.OrsanRole
import fr.orsan.utils.PrivacyStatus
import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.annotation.typeconversion.DateString
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.social.security.SocialUserDetails

@NodeEntity
class Person implements UserDetails, SocialUserDetails {
    private static final long serialVersionUID = 1

    transient springSecurityService
    @GraphId
    Long id //system id

    String userId //(email or screenName if providerId is twitter.)

    //Privacy
    PrivacyStatus   privacyStatus
    //Safety status

    //Identity
    String          firstName
    String          lastName
    String          race
    Integer         age
    String          gender
    String          height
    String          providerId
    String          bio
    //Sensitive
    String          ssn

    //TODO Images: https://bitbucket.org/sbuettner/grails-cloudinary
    List<String>    pictureUrls
    String          profileImageUrl
    List<String>    phones
    @DateString("yy-MM-dd")
    Date            birthDate
    String          hometown


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
    boolean accountNonExpired = true
    boolean accountNonLocked = true
    boolean credentialsNonExpired = true
    boolean enabled = true
    List<OrsanRole> authorities



    static constraints = {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;
        if (id == null) return super.equals(o);
        return id.equals(person.id);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }

    @Override
    String getUsername() {
        return userId
    }

}
