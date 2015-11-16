/**
 * Created by yawo on 16/11/15.
 */
package fr.orsan.config

import grails.config.Config
import grails.core.GrailsApplication
import org.neo4j.ogm.session.Session
import org.neo4j.ogm.session.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.data.neo4j.config.Neo4jConfiguration
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.data.neo4j.server.Neo4jServer
import org.springframework.data.neo4j.server.RemoteServer
import org.springframework.transaction.annotation.EnableTransactionManagement


@Configuration
@EnableNeo4jRepositories(basePackages = "fr.orsan.repository")
@EnableTransactionManagement
class OrsanConfiguration extends Neo4jConfiguration{
    @Autowired
    GrailsApplication grailsApplication
    @Bean
    public Neo4jServer neo4jServer() {
        Config config = grailsApplication.config
        new RemoteServer(config.get('neo4j.url'),config.get('neo4j.username'),config.get('neo4j.password'));
    }

    @Bean
    public SessionFactory getSessionFactory() {
        // with domain entity base package(s)
        new SessionFactory("fr.orsan.domain");
    }

    // needed for session in view in web-applications
    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Session getSession() throws Exception {
        return super.getSession();
    }
}
