package fr.orsan.utils.config
import fr.orsan.domain.Address
import fr.orsan.domain.Incident
import fr.orsan.domain.Person
import grails.core.GrailsApplication
import org.apache.commons.codec.binary.Base64
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
import org.neo4j.ogm.authentication.UsernamePasswordCredentials
import org.neo4j.ogm.session.request.Neo4jRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
    static Logger logger = LoggerFactory.getLogger(OrsanIndexer.class)

    CloseableHttpClient httpClient          = null
    Header header                           = new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8")
    Header acceptHeader                     = new BasicHeader("Accept", "application/json;charset=UTF-8")
    Header userAgentHeader                  = new BasicHeader("User-Agent", "neo4j-ogm.java/1.0")
    Header authHeader                       = null



    Neo4jRequest<String> neo4jRequest       = null
    String baseUrl                          = null;
    String indexUrl                         = null;
    String nodeUrl                          = null;
    String relationshipUrl                  = null;
    String spatialBaseUrl                   = null;
    String personIndexName                  = null;
    String incidentIndexName                = null;
    String personIndexUrl                   = null;
    String incidentIndexUrl                 = null;
    String addressIndexUrl                  = null;
    UsernamePasswordCredentials credentials = null

    @PostConstruct
    def createIndexes(){
        PoolingHttpClientConnectionManager   connectionManager  = new PoolingHttpClientConnectionManager()
        connectionManager.setDefaultMaxPerRoute(10)
        httpClient                                              = HttpClients.custom().setConnectionManager(connectionManager).build();
        credentials                                             = new UsernamePasswordCredentials(grailsApplication.config.getProperty('neo4j.username'),grailsApplication.config.getProperty('neo4j.password'))
        String usernamePassword                                 = grailsApplication.config.getProperty('neo4j.username').concat(":").concat(grailsApplication.config.getProperty('neo4j.password'))
        authHeader                                              = new BasicHeader("Authorization", "Basic "+Base64.encodeBase64String(usernamePassword.getBytes()))
        //neo4jRequest                                          = new DefaultRequest(httpClient,credentials);
        baseUrl                                                 = grailsApplication.config.getProperty('neo4j.url')
        indexUrl                                                = baseUrl+"/db/data/index/node"
        nodeUrl                                                 = baseUrl+"/db/data/node"
        relationshipUrl                                         = baseUrl+"/db/data/relationship"
        spatialBaseUrl                                          = baseUrl+"/db/data/ext/SpatialPlugin/graphdb"
        personIndexName                                         = 'person_fulltext'
        incidentIndexName                                       = 'incident_fulltext'
        personIndexUrl                                          = baseUrl+"/db/data/index/node/"+personIndexName
        incidentIndexUrl                                        = baseUrl+"/db/data/index/node/"+incidentIndexName
        addressIndexUrl                                         = baseUrl+"/db/data/index/node/geom"

        //Create layer
        execute(spatialBaseUrl+"/addSimplePointLayer",'{"layer":"geom","lat":"lat","lon":"lon"}')
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
        //Create incidentIndexName fulltext index config
        execute(indexUrl,"""{
                                          "name" : "${incidentIndexName}",
                                          "config" : {
                                            "type" : "fulltext",
                                            "provider" : "lucene"
                                          }
                                        }""")
        logger.info("indexes created...")
    }


    def indexPerson(Person person){
        execute(personIndexUrl,"""{
                                          "value" : "${person.firstName}_${person.lastName}_${person.age}_${person.email}_${person.ssn}",
                                          "uri"   : "${nodeUrl}/${person.id},
                                          "key    : "firstName_lastName_age_email_ssn}"
                                        }""")
    }

    def indexIncident(Incident incident){
        execute(incidentIndexUrl,"""{
                                          "value" : "${incident.name}",
                                          "uri"   : "${nodeUrl}/${incident.id},
                                          "key    : "firstName_lastName_age_email_ssn}"
                                        }""")
    }

    def indexAddress(Address address){
        execute(addressIndexUrl,"""{
                                          "value" : "${address.name}",
                                          "uri"   : "${nodeUrl}/${address.id}",
                                          "key"   : "name"
                                        }""")
        /*execute(spatialBaseUrl+"/addNodeToLayer","""{
                                          "layer" : "geom",
                                          "node" : "${nodeUrl}/${address.id}"
                                        }""")*/
    }

    def indexNode(Object node){
        switch (node?.getClass()){
            case Person.class:
                indexPerson((Person)node)
                break
            case Address.class:
                indexAddress((Address)node)
                break
            case Incident.class:
                indexIncident((Incident)node)
                break
            default:
                true //do nothing
        }
    }

    def execute(url,query){
        HttpPost request = new HttpPost(url);
        HttpEntity entity = new StringEntity(query,"UTF-8");
        request.setHeader(header);
        request.setHeader(acceptHeader);
        request.setHeader(authHeader);
        // http://tools.ietf.org/html/rfc7231#section-5.5.3
        request.setHeader(userAgentHeader);
        request.setEntity(entity);
        CloseableHttpResponse response = httpClient.execute(request); //todo: do sth with response, for God sake .
        //JsonResponse jsonResponse = new JsonResponse(response);
        logger.info("${url}:: response status code = ${response.getStatusLine().getStatusCode()} #####################")
        response.close()
    }
}

/**#### SETUP
 # 1.) Create a simple point layer
 curl -X POST -d '{"layer":"geom","lat":"lat","lon":"lon"}' --header "Content-Type:application/json" http://neo4j:0rs%40np%40ssw0RD@localhost:7474/db/data/ext/SpatialPlugin/graphdb/addSimplePointLayer

 # 2.) Add a spatial index
 curl -X POST -d '{"name":"geom","config":{"provider":"spatial","geometry_type":"point","lat":"lat","lon":"lon"}}' --header "Content-Type:application/json" http://neo4j:0rs%40np%40ssw0RD@localhost:7474/db/data/index/node/



 #### CREATE DATA
 # 3.) Create a sample node with lat and lon data (you can change the name of the properties in step 2)
 curl -v -X POST -d '{"query":"CREATE (n {name:\"Strandbar Hermann 2\",lon:16.385539770126344,lat:48.21198395790515}) RETURN n;"}' --header "Content-Type:application/json" http://neo4j:0rs%40np%40ssw0RD@localhost:7474/db/data/cypher

 # 4.) Add this node to the previously created "geom" index (step 2)
 # This should be done by an auto indexer: https://github.com/neo4j/neo4j/issues/2048
 curl -X POST -d '{"key":"name","value":"Strandbar Hermann 2","uri":"http://neo4j:0rs%40np%40ssw0RD@localhost:7575/db/data/node/8"}' --header "Content-Type:application/json" http://neo4j:0rs%40np%40ssw0RD@localhost:7474/db/data/index/node/geom

 # 5.) Add the node to the PointLayer (This is not necessary. Adding it to the spatial index will handle this too)
 #curl -X POST -d '{"layer":"geom","node":"http://neo4j:0rs%40np%40ssw0RD@localhost:7575/db/data/node/8"}' --header "Content-Type:application/json" http://neo4j:0rs%40np%40ssw0RD@localhost:7474/db/data/ext/SpatialPlugin/graphdb/addNodeToLayer



 #### QUERY FOR DATA
 # 6.) Get nodes within distance via REST API
 curl -v -X POST -d '{"layer":"geom","pointX":16.3,"pointY":48.2,"distanceInKm":100.0}' --header "Content-Type:application/json" http://neo4j:0rs%40np%40ssw0RD@localhost:7474/db/data/ext/SpatialPlugin/graphdb/findGeometriesWithinDistance
 ## Returns the "Strandbar Hermann" node

 # 7.) Get nodes within Distance via cypher
 curl -X POST -d "{\"query\":\"START node=node:geom('withinDistance:[48.2,16.3,100.0]') return node\"}" --header "Content-Type:application/json" http://neo4j:0rs%40np%40ssw0RD@localhost:7474/db/data/cypher
 ## Returns the "Strandbar Hermann" node



 */