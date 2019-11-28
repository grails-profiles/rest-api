@artifact.package@

import grails.testing.mixin.integration.Integration
import grails.testing.spock.OnceBefore
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification


@Integration
class @artifact.name@Spec extends Specification {

    @Shared
    @AutoCleanup
    HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:\$serverPort"
        this.client  = HttpClient.create(new URL(baseUrl))
    }

    void "Test the homepage"() {
        when:"The home page is requested"
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/"), Map)

        then:"The response is correct"
        response.status == HttpStatus.OK
        response.header(HttpHeaders.CONTENT_TYPE) == 'application/json;charset=UTF-8'
        response.body().message == 'Welcome to Grails!'
    }
}
