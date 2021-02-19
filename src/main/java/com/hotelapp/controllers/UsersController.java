package com.hotelapp.controllers;

import com.hotelapp.configuration.security.JwtTokenProvider;
import com.hotelapp.configuration.security.utils.UserUtils;
import com.hotelapp.daos.UserDto;
import com.hotelapp.daos.UserLoginDto;
import com.hotelapp.services.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.hotelapp.configuration.security.constants.RoleNames.ROLE_ADMIN;
import static com.hotelapp.configuration.security.constants.RoleNames.ROLE_HOTEL;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {GET, POST, PUT, DELETE}, origins = "*")
public class UsersController {

    @NonNull
    private UserService userService;

    @NonNull
    private AuthenticationManager authenticationManager;

    @NonNull
    private JwtTokenProvider tokenPovider;

    @NonNull
    private UserUtils userUtils;


    @GetMapping(value = "/userinfo")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getCurrentUserInfo(@AuthenticationPrincipal UserDetails userPrincipal) {

        return userService.getCurrentUserInfo(userPrincipal);
    }


    @RolesAllowed({ROLE_HOTEL, ROLE_ADMIN})
    @GetMapping(value = "/userinfo", params = "userId")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserInfoById(@RequestParam Long userId) {
        return userService.getUserInfoById(userId);
    }


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> loginStatus(
            @RequestBody @Valid UserLoginDto userLoginDto) {
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        userLoginDto.getUsername(),
                        userLoginDto.getPassword()));

        List<String> authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = tokenPovider.generateToken(auth);
        return userUtils.login(auth.getName(), jwt, authorities);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> logout() {

        return userUtils.logout();
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto registerUser(@RequestBody @Valid UserDto registerUser) {
        return userService.createUser(registerUser);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateCurrentUser(@RequestBody @Valid UserDto updateUsr,
                                     @AuthenticationPrincipal UserDetails userPrincipal) {
        return userService.updateCurrentUser(updateUsr, userPrincipal.getUsername());
    }

    @RolesAllowed(ROLE_ADMIN)
    @PutMapping("/update/other")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateOtherUser(@RequestBody @Valid UserDto updateUsr,
                                   @RequestParam Long userId,
                                   @AuthenticationPrincipal UserDetails userPrincipal) {
        return userService.updateOtherUser(updateUsr, userId, userPrincipal.getUsername());
    }


 /*   @RolesAllowed(ROLE_ADMIN)
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUserRoles(@RequestBody RolesDto rolesDto,
                                   @RequestParam Long userId){

TODO service to be implemented
        return null;
    }

  */
}
