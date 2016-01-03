package fr.orsan.utils.config
import fr.orsan.domain.Person
import fr.orsan.services.AddressService
import fr.orsan.utils.OrsanRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.template.Neo4jOperations
import org.springframework.social.connect.Connection
import org.springframework.social.connect.ConnectionSignUp
import org.springframework.social.facebook.api.Facebook
import org.springframework.social.facebook.api.User
import org.springframework.social.google.api.Google
import org.springframework.social.google.api.plus.PlusOperations
import org.springframework.social.twitter.api.Twitter
import org.springframework.social.twitter.api.TwitterProfile
import org.springframework.stereotype.Component
/**
 * Created by yawo on 28/11/15.
 */
@Component("orsanConnectionSignup")
class OrsanConnectionSignup implements ConnectionSignUp{
    @Autowired Neo4jOperations neo4jTemplate
    @Autowired AddressService addressService

    @Override
    String execute(Connection<?> connection) {
        Person person = new Person()
        person.setAuthorities(Arrays.asList(OrsanRole.ROLE_CUSTOMER))
        neo4jTemplate.save(user2Person(person,connection.api)).userId
    }

/*    public  <T> String executeHelper(Connection<T> connection) {
        String providerId = connection.getKey().providerId;
        Person person = null
        switch (providerId){
            case "facebook" : person = user2Person((Facebook) connection.api);break
            case "twitter"  : person = user2Person((Twitter) connection.api);break
            case "google"   : person = user2Person((Google) connection.api);break
            default         : throw UnsupportedOperationException("${providerId} providerId not yet supported")
        }
        neo4jTemplate.save(person)
        person.id
    }
*/
    protected Person user2Person(Person person, Facebook api) {
        org.springframework.social.facebook.api.UserOperations operations = api.userOperations()
        User user = operations.userProfile
        person.setUserId(user.email)
        person.setProfileImageUrl(user.cover.source)
        person.setBirthDate(Date.parse("MM/dd/yyyy", user.birthday))
        person.setFirstName(user.firstName)
        person.setLastName(user.lastName)
        person.setBio("${user.bio} \n ${user.about}")
        person.setHometown(user.hometown?.name)
        person.setGender(user.gender)
        person.setCommonAddresses(Arrays.asList(addressService.geocodeAndSave(user.getLocation()?.name)))
        person.setProviderId("facebook")
        return person
    }

    protected Person user2Person(Person person, Twitter api) {
        org.springframework.social.twitter.api.UserOperations operations = api.userOperations()
        TwitterProfile user = operations.userProfile;
        person.setUserId(user.screenName)
        person.setProfileImageUrl(user.profileImageUrl)
        person.setBirthDate(null)
        String[] nameParts = user.name.split(" ",2)+["",""];
        person.setFirstName(nameParts[0])
        person.setLastName(nameParts[1])
        person.setBio(user.description)
        person.setHometown(null)
        person.setGender(null)
        person.setCommonAddresses(Arrays.asList(addressService.geocodeAndSave(user.location)))
        person.setProviderId("twitter")
        return person
    }

    protected Person user2Person(Person person, Google api) {
        PlusOperations operations = api.plusOperations()
        org.springframework.social.google.api.plus.Person user = operations.googleProfile
        person.setUserId(user.accountEmail)
        person.setProfileImageUrl(user.imageUrl)
        person.setBirthDate(user.birthday)
        person.setFirstName(user.givenName)
        person.setLastName(user.familyName)
        person.setBio(user.aboutMe)
        person.setHometown(null)
        person.setGender(user.gender)
        person.setCommonAddresses(Arrays.asList(addressService.geocodeAndSave( (user.placesLived?.find {it.value}) )))//find the principal google address
        person.setProviderId("google")
        return person
    }

}
