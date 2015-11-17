package fr.orsan.utils.config

import fr.orsan.domain.Address
import fr.orsan.domain.Person
import grails.core.GrailsApplication
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.neo4j.ogm.session.request.DefaultRequest
import org.neo4j.ogm.session.request.Neo4jRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

/**
 * Created by ykpotufe on 17/11/2015.
 */
@Component("orsanIndexer")
public class OrsanIndexer {
    @Autowired
    GrailsApplication grailsApplication

    final CloseableHttpClient httpClient    = HttpClients.createDefault();
    Neo4jRequest<String> neo4jRequest       = new DefaultRequest(httpClient);
    String baseUrl                          = grailsApplication.config.getProperty('neo4j.url')
    String indexUrl                         = baseUrl+"/db/data/index/node"
    String nodeUrl                          = baseUrl+"/db/data/node"
    String relationshipUrl                  = baseUrl+"/db/data/relationship"
    String spatialBaseUrl                   = baseUrl+"/db/data/ext/SpatialPlugin/graphdb"
    String personIndexName                  = 'person_fulltext'

    @PostConstruct
    def createIndexes(){
        //Create personIndexName fulltext index config
        neo4jRequest.execute(indexUrl,"""{
                                          "name" : "${personIndexName}",
                                          "config" : {
                                            "type" : "fulltext",
                                            "provider" : "lucene"
                                          }
                                        }""")
        //Create layer
        neo4jRequest.execute(spatialBaseUrl+"/addSimplePointLayer",'''{
                                          "layer" : "geom",
                                          "lat" : "lat",
                                          "lon" : "lon"
                                        }''')
        //Create spatial index config
        neo4jRequest.execute(indexUrl,'''{
                                          "name" : "geom",
                                          "config" : {
                                            "provider" : "spatial",
                                            "geometry_type" : "point",
                                            "lat" : "lat",
                                            "lon" : "lon"
                                          }
                                        }''')
    }


    def indexPerson(Person person){
        neo4jRequest.execute(indexUrl,"""{
                                          "value" : "${person.firstName}_${person.lastName}_${person.age}_${person.email}_${person.ssn}",
                                          "uri"   : "${nodeUrl}/${person.id},
                                          "key    : "firstName_lastName_age_email_ssn}"
                                        }""")
    }
    def indexAddress(Address address){
        //Create layer
        neo4jRequest.execute(spatialBaseUrl+"/addNodeToLayer","""{
                                          "layer" : "geom",
                                          "node" : "${nodeUrl}/${address.id}"
                                        }""")
    }

    def indexNode(Object node){
        //TODO: sort of pattern matching
        if(node instanceof Person) {
            indexPerson((Person)node)
        }
        if(node instanceof Address) {
            indexAddress((Address)node)
        }

    }

}
