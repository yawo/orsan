package orsan
import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.springframework.context.annotation.ComponentScan

@ComponentScan(basePackages = "fr.orsan")
class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        println "Running App"
        GrailsApp.run(Application, args)
    }
}