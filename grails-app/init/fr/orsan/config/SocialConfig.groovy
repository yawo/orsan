package fr.orsan.config
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.neo4j.server.Neo4jServer
import org.springframework.security.crypto.encrypt.Encryptors
import org.springframework.social.UserIdSource
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer
import org.springframework.social.config.annotation.SocialConfigurer
import org.springframework.social.connect.ConnectionFactoryLocator
import org.springframework.social.connect.UsersConnectionRepository
import org.springframework.social.connect.neo4j.repositories.impl.Neo4jUsersConnectionRepository
import org.springframework.social.facebook.connect.FacebookConnectionFactory
import org.springframework.social.security.AuthenticationNameUserIdSource
import org.springframework.social.twitter.connect.TwitterConnectionFactory
/**
 * Created by yawo on 22/11/15.
 */
@Configuration
class SocialConfig implements SocialConfigurer {
    @Autowired
    Neo4jServer neo4jServer

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
        return new Neo4jUsersConnectionRepository(neo4jServer.url(), connectionFactoryLocator, Encryptors.queryableText("coco","wawa"));

    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }
}