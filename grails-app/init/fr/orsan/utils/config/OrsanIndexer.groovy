package fr.orsan.utils.config

import fr.orsan.domain.Address
import fr.orsan.domain.Person
import grails.core.GrailsApplication
import org.apache.http.Header
import org.apache.http.HttpEntity
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.message.BasicHeader
import org.apache.http.protocol.HTTP
import org.neo4j.ogm.authentication.HttpRequestAuthorization
import org.neo4j.ogm.authentication.UsernamePasswordCredentials
import org.neo4j.ogm.session.request.DefaultRequest
import org.neo4j.ogm.session.request.Neo4jRequest
import org.neo4j.ogm.session.response.JsonResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.template.Neo4jTemplate
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

/**
 * Created by ykpotufe on 17/11/2015.
 */
@Component("orsanIndexer")
public class OrsanIndexer {
    @Autowired
    GrailsApplication grailsApplication
    static Logger logger = LoggerFactory.getLogger(OrsanIndexer.class)

    CloseableHttpClient httpClient          = null
    Header header                           = new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8")
    Header header1                          = new BasicHeader("Accept", "application/json;charset=UTF-8")
    Header header2                          = new BasicHeader("User-Agent", "neo4j-ogm.java/1.0")


    Neo4jRequest<String> neo4jRequest       = null
    String baseUrl                          = null;
    String indexUrl                         = null;
    String nodeUrl                          = null;
    String relationshipUrl                  = null;
    String spatialBaseUrl                   = null;
    String personIndexName                  = null;
    UsernamePasswordCredentials credentials = null

    @PostConstruct
    def createIndexes(){
        PoolingHttpClientConnectionManager   connectionManager  = new PoolingHttpClientConnectionManager()
        connectionManager.setDefaultMaxPerRoute(10)
        httpClient                                              = HttpClients.custom().setConnectionManager(connectionManager).build();
        //credentials                                           = new UsernamePasswordCredentials(grailsApplication.config.getProperty('neo4j.username'),grailsApplication.config.getProperty('neo4j.password'))
        //neo4jRequest                                          = new DefaultRequest(httpClient,credentials);
        baseUrl                                                 = grailsApplication.config.getProperty('neo4j.urlWithAuth')
        indexUrl                                                = baseUrl+"/db/data/index/node"
        nodeUrl                                                 = baseUrl+"/db/data/node"
        relationshipUrl                                         = baseUrl+"/db/data/relationship"
        spatialBaseUrl                                          = baseUrl+"/db/data/ext/SpatialPlugin/graphdb"
        personIndexName                                         = 'person_fulltext'

       /*



        */
        //Create layer
        execute(spatialBaseUrl+"/addSimplePointLayer",'''{
                                          "layer" : "geom",
                                          "lat" : "lat",
                                          "lon" : "lon"
                                        }''')
        //Create spatial index config

        execute(indexUrl,'{"name":"geom","config":{"provider":"spatial","geometry_type":"point","lat":"lat","lon":"lon"}}')
        //Create personIndexName fulltext index config
        execute(indexUrl,"""{
                                          "name" : "${personIndexName}",
                                          "config" : {
                                            "type" : "fulltext",
                                            "provider" : "lucene"
                                          }
                                        }""")
        logger.info("indexes createdIndex...")

    }


    def indexPerson(Person person){
        execute(indexUrl,"""{
                                          "value" : "${person.firstName}_${person.lastName}_${person.age}_${person.email}_${person.ssn}",
                                          "uri"   : "${nodeUrl}/${person.id},
                                          "key    : "firstName_lastName_age_email_ssn}"
                                        }""")
    }
    def indexAddress(Address address){
        //Create layer
        execute(spatialBaseUrl+"/addNodeToLayer","""{
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

    def execute(url,query){
        HttpPost request = new HttpPost(url);
        HttpEntity entity = new StringEntity(query,"UTF-8");
        request.setHeader(header);
        request.setHeader(header1);
        // http://tools.ietf.org/html/rfc7231#section-5.5.3
        request.setHeader(header2);
        request.setEntity(entity);
        CloseableHttpResponse response = httpClient.execute(request); //todo: do sth with response, for God sake .
        //JsonResponse jsonResponse = new JsonResponse(response);
        //logger.info(" response status code = "+ response.getStatusLine().getStatusCode()+" ; "+jsonResponse.properties)
        response.close()
    }
}
