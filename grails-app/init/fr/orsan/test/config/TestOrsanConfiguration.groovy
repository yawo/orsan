/**
 * Created by yawo on 16/11/15.
 */
package fr.orsan.test.config
import fr.orsan.utils.config.BaseOrsanConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableNeo4jRepositories(basePackages = "fr.orsan.repositories")
@EnableTransactionManagement
@Profile("test")
class TestOrsanConfiguration extends BaseOrsanConfiguration{

}
