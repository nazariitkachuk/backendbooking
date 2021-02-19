package com.hotelapp.services;

import com.hotelapp.daos.*;
import com.hotelapp.entities.*;
import com.hotelapp.exceptions.DataNotFoundException;
import com.hotelapp.repository.*;
import com.hotelapp.utils.CustomBeanUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hotelapp.configuration.security.constants.RoleNames.ROLE_USER;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class ReservationService {

    @NonNull
    private ReservationRepository reservationRepository;

    @NonNull
    private UserRepository userRepository;

    @NonNull
    private RoleRepository roleRepository;

    @NonNull
    private RoomsRepository roomsRepository;

    @NonNull
    private RoomTypeRepository typeRepository;

    @NonNull
    private MailService mailService;

    private void sendMail(ReservationDto dto) {
        String from = dto.getValidFrom()!=null?dto.getValidFrom().format(DateTimeFormatter.ISO_DATE):null;
        String to = dto.getValidTo()!=null?dto.getValidTo().format(DateTimeFormatter.ISO_DATE):null;
        String bookingTime = dto.getBookingTime()!=null?dto.getBookingTime().format(DateTimeFormatter.ISO_DATE):null;
        mailService.sendMail(dto.getEmail()
                , "Reservation in days from :"
                        + from
                        + " to: " + to
                        + " created at :" + bookingTime
                , "HOTEL@gmail.com"
                , "Reservation nr " + dto.getId());
    }

    public ReservationDto createSimpleReservation(SimpleReservationDto reservationDto, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new DataNotFoundException("User with username " + userDetails.getUsername() + " does not exist"));//TODO code duplication
        ReservationDto fullReservationDto = new ReservationDto();
        BeanUtils.copyProperties(user,fullReservationDto);
        BeanUtils.copyProperties(reservationDto, fullReservationDto);
        sendMail(fullReservationDto);
        ReservationDto reservationSaved = this.createReservationTransaction(fullReservationDto, true, userDetails);
        return reservationSaved;
    }

    public ReservationDto createReservation(ReservationDto reservationDto) {
        ReservationDto reservationSaved = this.createReservationTransaction(reservationDto, false, null);
        sendMail(reservationSaved);
        return reservationSaved;
    }

    @Transactional
    private ReservationDto createReservationTransaction(ReservationDto reservationDto, Boolean isRegisteredUser, UserDetails userDetails) {
        if (isRegisteredUser) {
            return createReservationForLoggedInUserTransaction(reservationDto, userDetails);
        }
        Optional<User> optionalUser = userRepository.findByEmail(reservationDto.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return createReservationForExistingUserTransaction(reservationDto, user);
        }
        return createReservationForNonExistingUser(reservationDto);
    }

    @Transactional
    private ReservationDto createReservationForNonExistingUser(ReservationDto reservationDto) {
        User user = new User();
        BeanUtils.copyProperties(reservationDto, user);
        user.setIsActive(Boolean.FALSE);
        user.setId(null);
        Role role = roleRepository.findByRoleName(ROLE_USER).orElseThrow(() -> new DataNotFoundException("Role " + ROLE_USER + "does not exist in db"));
        user.setRoles(Arrays.asList(role));
        user = userRepository.save(user);
        return createReservationForExistingUserTransaction(reservationDto, user);
    }

    @Transactional
    private ReservationDto createReservationForLoggedInUserTransaction(ReservationDto reservationDto,
                                                                       UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new DataNotFoundException("User with username " + userDetails.getUsername() + " does not exist"));
        return createReservationForExistingUserTransaction(reservationDto, user);
    }

    @Transactional
    private ReservationDto createReservationForExistingUserTransaction(ReservationDto reservationDto, User user) {
        Room room = getAvailableRoom(reservationDto);
        Reservation reservation = new Reservation();
        BeanUtils.copyProperties(reservationDto, reservation);
        reservation.setUser(user);
        reservation.setRoom(room);
        Reservation reservationReturn=reservationRepository.save(reservation);
        ReservationDto dtoReturn = new ReservationDto();
        BeanUtils.copyProperties(user, dtoReturn);
        BeanUtils.copyProperties(reservationReturn, dtoReturn);
        dtoReturn.setRoomType(reservationReturn.getRoom().getRoomType().getName());
        return dtoReturn;
    }

    @Transactional
    public ReservationResponseDto confirmReservationAndGetList(Long reservationId) {
        confirmReservation(reservationId);
        return getReservationForCurrentHotel();
    }

    @Transactional
    public ReservationDto confirmReservation(Long reservationId) throws DataNotFoundException {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new DataNotFoundException("Reservation with id : " + reservationId + " does not exist"));
        ReservationDto reservationDto = new ReservationDto();
        reservation.setActive(true);
        BeanUtils.copyProperties(reservation, reservationDto);
        reservationRepository.save(reservation);
        return reservationDto;
    }

    @Transactional
    public ReservationResponseDto deleteReservationAndGetList(Long reservationId) {
        deleteReservation(reservationId);
        return getReservationForCurrentHotel();
    }

    @Transactional
    public DeleteDto deleteReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new DataNotFoundException("Reservation with id :" + reservationId + " does not exist"));
        reservationRepository.delete(reservation);
        return DeleteDto.of(1);
    }

    @Transactional
    public ReservationDto getReservationInfoByIdForUser(Long reservationId, String userName) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new DataNotFoundException("Reservation with id: " + reservationId + " does not exist"));
        if (!userName.equals(reservation.getUser().getUsername())) {
            throw new DataNotFoundException("Reservation no: " + reservationId + "is not assigned to user with username: " + userName);
        }
        ReservationDto dto = new ReservationDto();
        BeanUtils.copyProperties(reservation, dto);
        return dto;
    }

    @Transactional
    public ReservationDto getReservationInfoById(String email, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new DataNotFoundException("Reservation with id: " + reservationId + " does not exist"));
        if (!email.equals(reservation.getUser().getEmail()))
            throw new DataNotFoundException("Email " + email + " not assigned to reservation no: " + reservationId);
        ReservationDto dto = new ReservationDto();
        BeanUtils.copyProperties(reservation, dto);
        dto.setEmail(email);
        return dto;
    }

    @Transactional
    public ReservationDto getReservationInfoById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new DataNotFoundException("Reservation with id :" + reservationId + " does not exist"));
        ReservationDto dto = new ReservationDto();
        BeanUtils.copyProperties(reservation, dto);
        return dto;
    }

    @Transactional
    public ReservationsFuturePastResponseDto getReservationsListForCurrentUser(UserDetails userP) {
        User user = userRepository.findByUsername(userP.getUsername())
                .orElseThrow(() -> new DataNotFoundException("User with username: " + userP.getUsername() + " does not exist"));
        return getFutureAndPastReservationsForUser(user);
    }

    @Transactional
    public ReservationResponseDto getReservationForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with id :" + userId + " does not exist"));
        return getReservationsForUser(user);
    }

    private ReservationsFuturePastResponseDto getFutureAndPastReservationsForUser(User user){
        List<ReservationDto> reservationDtos =
                reservationRepository.findByUser(user)
                        .stream()
                        .map(e -> {
                            ReservationDto dto = new ReservationDto();
                            BeanUtils.copyProperties(e, dto);
                            return dto;
                        }).collect(Collectors.toList());
       return splitReservationsOnFutureAndPast(reservationDtos);
    }
    @Transactional
    private ReservationResponseDto getReservationsForUser(User user) {
        List<ReservationDto> reservationDtos =
                reservationRepository.findByUser(user)
                        .stream()
                        .map(e -> {
                            ReservationDto dto = new ReservationDto();
                            BeanUtils.copyProperties(e, dto);
                            return dto;
                        }).collect(Collectors.toList());
        return sortListOfReservations(reservationDtos);
    }

    public ReservationResponseDto getReservationForHotel(Long hotelId) {
//TODO
        return null;
    }

    @Transactional
    public ReservationResponseDto getReservationForCurrentHotel() {

        List<ReservationDto> reservationDtos = reservationRepository
                .findAll().stream()
                .map(e -> {
                    ReservationDto dto = new ReservationDto();
                    CustomBeanUtils.copyNotNullProperties(e, dto);
                    CustomBeanUtils.copyNotNullProperties(e.getUser(), dto, "id");
                    return dto;
                }).collect(Collectors.toList());
        return sortListOfReservations(reservationDtos);
    }

    private Room getAvailableRoom(ReservationDto reservationDto) {
        RoomType type = typeRepository.findByName(reservationDto.getRoomType())
                .orElseThrow(() -> new DataNotFoundException("Not found room of type :" + reservationDto.getRoomType()));
        long places = Long.sum(reservationDto.getAdultsNumber(), reservationDto.getChildrenNumber());
        List<Room> roomList = roomsRepository.
                findAvailableRooms(reservationDto.getValidFrom(),
                        reservationDto.getValidTo(), type.getTypeId(), places);
        return roomList.stream().findFirst()
                .orElseThrow(() -> new DataNotFoundException("Not found places available in desired time, Please choose another date"));
    }

    private ReservationResponseDto sortListOfReservations(List<ReservationDto> reservationDtos) {
        List<ReservationDto> active = reservationDtos.stream().filter(e -> nonNull(e.getActive()) && e.getActive()).collect(Collectors.toList());
        List<ReservationDto> inactive = reservationDtos.stream().filter(e -> isNull(e.getActive()) || !e.getActive()).collect(Collectors.toList());
        return ReservationResponseDto.of(active, inactive);
    }

    private ReservationsFuturePastResponseDto splitReservationsOnFutureAndPast(List<ReservationDto> reservationDtos){
        LocalDate today = LocalDate.now();
        List<ReservationDto> past = reservationDtos.stream().filter(e -> nonNull(e.getValidTo()) && e.getValidTo().isBefore(today)).collect(Collectors.toList());
        List<ReservationDto> future = reservationDtos.stream().filter(e -> !(nonNull(e.getValidTo()) && e.getValidTo().isBefore(today))).collect(Collectors.toList());
        return ReservationsFuturePastResponseDto.of(future,past);
    }
}
