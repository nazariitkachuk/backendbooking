package com.hotelapp

import com.hotelapp.HotelappApplication
import groovy.sql.Sql
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import spock.lang.Shared
import spock.lang.Specification

import javax.sql.DataSource
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = HotelappApplication)
class MainSpec extends Specification {

    @Autowired
    //  @Shared
    TestRestTemplate template//= new TestRestTemplate()


    //  @Value("#{spring.datasource.url}")
    String url = 'jdbc:postgresql://35.198.99.97/booking-database?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory&sslmode=require'
    //  @Value ("#{spring.datasource.username}")
    @Shared
    String username = 'postgres'

    //  @Value("#{spring.datasource.password}")
    @Shared
    String password = 'admin'

    @Shared
     Sql sql = Sql.newInstance('jdbc:postgresql://35.198.99.97/booking-database?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory&sslmode=require'
            , 'postgres', 'admin')

    def setupSpec() {

    }



    List<Integer> executeFromFile(String serviceName) {

        String fileName = serviceName + ".sql"

       String query = Files.lines(Paths.get("src/test/groovy/com/hotelapp/data", fileName))
                .collect(Collectors.reducing({ e, f -> e + f })).orElseThrow()
String paths=MainSpec.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()
 //      String query= new String(Files.readAllBytes("src/test/groovy/com/hotelapp/data"))
        List<Integer> results = new ArrayList<>()
        for (String sinqleQuery : query.split(";")) {
            results.add(callQuery(sinqleQuery))
        }


    }

    int callQuery(String query) {


        return sql.call(query)
    }

    def cleanupSpec() {

        sql.close()
    }

    def "GET TestController"() {
        given:
        when:
        def ra = template.getForEntity("/tests", String)
        def response = template.exchange("/tests", HttpMethod.GET, null, String)
        //  response= template.getForEntity("/tests",String.class)

        then:
        response.statusCode.'2xxSuccessful'
        response.statusCode == HttpStatus.OK
        response.statusCode.value() == 200
    }
}