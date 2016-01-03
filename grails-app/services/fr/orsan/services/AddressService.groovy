package fr.orsan.services

import fr.orsan.domain.Address
import grails.core.GrailsApplication
import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional
import org.grails.web.json.JSONObject

@Transactional
class AddressService {
    def neo4jTemplate
    GrailsApplication grailsApplication
    RestBuilder rest = new RestBuilder()

    def Address geocode(String addressString) {
        if (addressString==null || "".equals(addressString.trim())){ return null }
        def resp = rest.get("https://maps.googleapis.com/maps/api/geocode/json?key=${grailsApplication.config.getProperty('google.key')}&address=${URLEncoder.encode(addressString,"UTF-8")}")
        resp.json instanceof JSONObject
        JSONObject resAddr = resp.json.results[0]
        JSONObject location = resAddr.geometry.location
        new Address(null,resAddr.formatted_address,location.lat,location.lng)
    }

    def Address geocodeAndSave(String addressString) { neo4jTemplate.save(geocode(addressString)) }

}
