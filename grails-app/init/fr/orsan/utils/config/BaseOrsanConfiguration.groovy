/**
 * Created by yawo on 16/11/15.
 */
package fr.orsan.utils.config
import fr.orsan.domain.Person
import grails.config.Config
import grails.core.GrailsApplication
import org.neo4j.ogm.session.SessionFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.dao.DataAccessException
import org.springframework.data.neo4j.config.Neo4jConfiguration
import org.springframework.data.neo4j.event.AfterSaveEvent
import org.springframework.data.neo4j.event.BeforeDeleteEvent
import org.springframework.data.neo4j.server.Neo4jServer
import org.springframework.data.neo4j.server.RemoteServer
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.social.security.SocialUserDetails
import org.springframework.social.security.SocialUserDetailsService

@Configuration
class BaseOrsanConfiguration extends Neo4jConfiguration{
    @Autowired GrailsApplication grailsApplication

    @Autowired OrsanIndexer orsanIndexer

    static Logger logger = LoggerFactory.getLogger(BaseOrsanConfiguration.class)

    @Bean
    public Neo4jServer neo4jServer() {
        Config config = grailsApplication.config
        new RemoteServer(config.getProperty('neo4j.url'),config.getProperty('neo4j.username'),config.getProperty('neo4j.password'));
    }

    @Bean
    public SessionFactory getSessionFactory() {
        // with domain entity base package(s)
        new SessionFactory("fr.orsan.domain","org.springframework.social.connect.neo4j.domain");
    }

    @Bean
    ApplicationListener<BeforeDeleteEvent> beforeDeleteEventApplicationListener() {
        return new ApplicationListener<BeforeDeleteEvent>() {
            @Override
            public void onApplicationEvent(BeforeDeleteEvent event) {
                orsanIndexer.unIndexNode(event.getEntity())
                logger.info("un-indexing on delete "+event.getEntity().getClass())
            }
        };
    }

    @Bean
    ApplicationListener<AfterSaveEvent> afterSaveEventApplicationListener() {
        return new ApplicationListener<AfterSaveEvent>() {
            @Override
            public void onApplicationEvent(AfterSaveEvent event) {
               orsanIndexer.indexNode(event.getEntity())
                logger.info("indexing on save "+event.getEntity().getClass())
            }
        };
    }

    @Bean
    UserDetailsService getUserDetailsService(){
        return new UserDetailsService() {
            @Override
            UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                neo4jTemplate().queryForObject(Person.class,"MATCH (person:Person {userId:'${username}'}) RETURN person",Collections.emptyMap())
            }
        }
    }

    @Bean
    SocialUserDetailsService getSocialUserDetailsService(){
        return new SocialUserDetailsService() {
            @Override
            SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException,DataAccessException {
                neo4jTemplate().queryForObject(Person.class,"MATCH (person:Person {userId:'${userId}'}) RETURN person",Collections.emptyMap())
            }
        }
    }
}
