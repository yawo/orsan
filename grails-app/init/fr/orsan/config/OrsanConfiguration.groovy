/**
 * Created by yawo on 16/11/15.
 */
package fr.orsan.config
import fr.orsan.utils.config.BaseOrsanConfiguration
import org.neo4j.ogm.session.Session
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.core.annotation.Order
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableNeo4jRepositories(basePackages = "fr.orsan.repositories")
@EnableTransactionManagement
@Profile(["dev","production"])
@Order(1)
class OrsanConfiguration extends BaseOrsanConfiguration{

    // needed for session in view in web-applications
    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
    public Session getSession() throws Exception {
        return super.getSession();
    }
}
