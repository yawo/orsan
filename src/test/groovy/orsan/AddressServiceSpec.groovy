package orsan

import fr.orsan.services.AddressService
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(AddressService)
class AddressServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "pass"() {
        expect:"passExpect"
            true
    }
}
