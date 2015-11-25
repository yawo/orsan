package fr.orsan.config
import fr.orsan.utils.config.OrsanNeo4jUsersConnectionRepository
import grails.core.GrailsApplication
import org.neo4j.ogm.session.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment
import org.springframework.data.neo4j.server.Neo4jServer
import org.springframework.security.crypto.encrypt.Encryptors
import org.springframework.social.UserIdSource
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer
import org.springframework.social.config.annotation.EnableSocial
import org.springframework.social.config.annotation.SocialConfigurer
import org.springframework.social.connect.Connection
import org.springframework.social.connect.ConnectionFactoryLocator
import org.springframework.social.connect.ConnectionRepository
import org.springframework.social.connect.UsersConnectionRepository
import org.springframework.social.connect.web.ConnectController
import org.springframework.social.connect.web.ReconnectFilter
import org.springframework.social.facebook.api.Facebook
import org.springframework.social.facebook.connect.FacebookConnectionFactory
import org.springframework.social.security.AuthenticationNameUserIdSource
import org.springframework.social.twitter.connect.TwitterConnectionFactory
/**
 * Created by yawo on 22/11/15.
 */
@Configuration
@EnableSocial
@Order(2)
class SocialConfig implements SocialConfigurer {
    @Autowired Neo4jServer neo4jServer
    @Autowired Session session
    @Autowired GrailsApplication grailsApplication


    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
        cfConfig.addConnectionFactory(new FacebookConnectionFactory(
                env.getProperty("facebook.clientId"),
                env.getProperty("facebook.clientSecret")));
        cfConfig.addConnectionFactory(new TwitterConnectionFactory(
                env.getProperty("twitter.consumerKey"),
                env.getProperty("twitter.consumerSecret")));
    }


    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return new OrsanNeo4jUsersConnectionRepository(session, connectionFactoryLocator,
                Encryptors.queryableText("password",grailsApplication.config.getProperty("grails.security.salt")))

    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Bean
    public ConnectController connectController(
            ConnectionFactoryLocator connectionFactoryLocator,
            ConnectionRepository connectionRepository) {
        return new ConnectController(connectionFactoryLocator, connectionRepository);
    }


    @Bean
    public ReconnectFilter apiExceptionHandler(UsersConnectionRepository usersConnectionRepository, UserIdSource userIdSource) {
        return new ReconnectFilter(usersConnectionRepository, userIdSource);
    }

    @Bean
    @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
    public Facebook facebook(ConnectionRepository repository) {
        Connection<Facebook> connection = repository.findPrimaryConnection(Facebook.class);
        return connection != null ? connection.getApi() : null;
    }
}