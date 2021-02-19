package com.hotelapp.controllers;

import com.hotelapp.daos.*;
import com.hotelapp.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import static com.hotelapp.configuration.security.constants.RoleNames.ROLE_ADMIN;
import static com.hotelapp.configuration.security.constants.RoleNames.ROLE_HOTEL;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

@RestController
@RequestMapping("reservations")
@CrossOrigin(allowCredentials="true",allowedHeaders = "*",methods ={GET,POST,PUT,DELETE} ,origins = "*")
public class ReservationController {

    @Autowired
    ReservationService reservationService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationDto createReservation(@Valid @RequestBody ReservationDto reservationDto) {
        return reservationService.createReservation(reservationDto);

    }


    @PostMapping("/currentuser")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationDto createReservationForUser(@Valid @RequestBody SimpleReservationDto reservationDto, @AuthenticationPrincipal UserDetails userDetails) {
        return reservationService.createSimpleReservation(reservationDto, userDetails);
    }

    @RolesAllowed({ROLE_HOTEL, ROLE_ADMIN})
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponseDto deleteReservation(@RequestParam Long reservationId) {
        return reservationService.deleteReservationAndGetList(reservationId);
    }


    @RolesAllowed({ROLE_HOTEL, ROLE_ADMIN})
    @PutMapping("/confirm")
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponseDto confirmReservation(@RequestParam Long reservationId) {
        return reservationService.confirmReservationAndGetList(reservationId);
    }
    //TODO - not working YET
    @RolesAllowed(ROLE_ADMIN)
    @GetMapping("/hotel")
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponseDto getReservationForHotel(@RequestParam Long hotelId) {

        return reservationService.getReservationForHotel(hotelId);
    }


    @RolesAllowed({ROLE_HOTEL, ROLE_ADMIN})
    @GetMapping("/hotel/current")
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponseDto getReservationForCurrentHotel() {
        return reservationService.getReservationForCurrentHotel();
    }


    @GetMapping("/list/currentuser")
    @ResponseStatus(HttpStatus.OK)
    public ReservationsFuturePastResponseDto getReservationsListForCurrentUser(@AuthenticationPrincipal UserDetails user) {
        return reservationService.getReservationsListForCurrentUser(user);

    }

    @RolesAllowed({ROLE_ADMIN, ROLE_HOTEL})
    @GetMapping("/details/byuser")
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponseDto getReservationForUser(@RequestParam Long userId) {
        return reservationService.getReservationForUser(userId);
    }

    @GetMapping("/details/byemail")
    @ResponseStatus(HttpStatus.OK)
    public ReservationDto getReservationDetailsForEmail(@RequestParam String email, @RequestParam Long reservationId) {


        return reservationService.getReservationInfoById(email, reservationId);
    }

    @GetMapping("/details/currentuser/{reservationId}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationDto getReservationDetailsForCurrentUser(@PathVariable Long reservationId, @AuthenticationPrincipal UserDetails userDetails) {

        return reservationService.getReservationInfoByIdForUser(reservationId, userDetails.getUsername());
    }

    @RolesAllowed({ROLE_ADMIN, ROLE_HOTEL})
    @GetMapping("/details/hoteladmin/{reservationId}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationDto getReservationDetails(@PathVariable Long reservationId) {


        return reservationService.getReservationInfoById(reservationId);
    }
}
