/**
 * Created by yawo on 16/11/15.
 */
package fr.orsan.utils.config

import fr.orsan.domain.Person
import grails.config.Config
import grails.core.GrailsApplication
import org.neo4j.ogm.session.SessionFactory
import org.neo4j.ogm.session.request.DefaultRequest
import org.neo4j.ogm.session.request.Neo4jRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.neo4j.config.Neo4jConfiguration
import org.springframework.data.neo4j.event.AfterSaveEvent
import org.springframework.data.neo4j.server.Neo4jServer
import org.springframework.data.neo4j.server.RemoteServer

@Configuration
class BaseOrsanConfiguration extends Neo4jConfiguration{
    @Autowired GrailsApplication grailsApplication

    @Autowired OrsanIndexer orsanIndexer


    @Bean
    public Neo4jServer neo4jServer() {
        Config config = grailsApplication.config
        new RemoteServer(config.getProperty('neo4j.url'),config.getProperty('neo4j.username'),config.getProperty('neo4j.password'));
    }

    @Bean
    //@Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SessionFactory getSessionFactory() {
        // with domain entity base package(s)
        new SessionFactory("fr.orsan.domain");
    }

    @Bean
    ApplicationListener<AfterSaveEvent> afterSaveEventApplicationListener() {
        return new ApplicationListener<AfterSaveEvent>() {
            @Override
            public void onApplicationEvent(AfterSaveEvent event) {
               orsanIndexer.indexNode(event.getEntity())
            }
        };
    }
}
