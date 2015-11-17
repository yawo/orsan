/**
 * Created by yawo on 16/11/15.
 */
package fr.orsan.utils.config
import grails.config.Config
import grails.core.GrailsApplication
import org.neo4j.ogm.session.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.neo4j.config.Neo4jConfiguration
import org.springframework.data.neo4j.server.Neo4jServer
import org.springframework.data.neo4j.server.RemoteServer

@Configuration
class BaseOrsanConfiguration extends Neo4jConfiguration{
    @Autowired
    GrailsApplication grailsApplication


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
}
