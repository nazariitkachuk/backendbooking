package com.hotelapp

import com.hotelapp.daos.UserDto
import groovy.json.JsonOutput
import org.springframework.http.HttpMethod

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.util.MultiValueMap

import org.springframework.http.HttpHeaders

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = HotelappApplication)
class UsersControllerTest extends MainSpec{

   def setup(){
        executeFromFile("user-controller-data")
    }

    def cleanup(){
        executeFromFile("user-controller-data-clean")
    }

    def USERS_API='/users'
    def 'GET loginStatus'(){

        given:
        def link = USERS_API+'/login'

        def map=[username: 'admin',
                           password: 'admin']

        HttpHeaders headers= new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)

      RequestEntity requestEntity =new RequestEntity(
              JsonOutput.toJson(map),
              headers,
              HttpMethod.POST,
              URI.create(link)
      )



  //  def response=   template.postForObject(link,json,String.class)
        when:

        def response=template.exchange(requestEntity,String)
        then:
        response.statusCode== HttpStatus.ACCEPTED
    }
}
