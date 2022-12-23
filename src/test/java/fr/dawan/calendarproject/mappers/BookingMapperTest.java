package fr.dawan.calendarproject.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.dawan.calendarproject.dto.BookingDto;
import fr.dawan.calendarproject.entities.Booking;
import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.Room;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.InterventionStatus;
import fr.dawan.calendarproject.enums.UserCompany;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.mapper.BookingMapper;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class BookingMapperTest {

    @Autowired
    BookingMapper bookingMapper;

    Room room = new Room();
    Intervention intervention = new Intervention();
    Booking booking = new Booking();
    BookingDto bookingDto = new BookingDto();

    @BeforeEach
    void before() {
        Location mockedLoc = new Location(1 ,"Paris", "red", "FR", false, 0);
        Course mockedCourse = new Course(1, 1, "Java course for beginners", 2.5, "slug", 0);
        User mockedUser = new User(1L, 1L, 1L, "Daniel", "Balavoine", mockedLoc, "dbalavoine@dawan.fr", null,
                UserType.ADMINISTRATIF, UserCompany.DAWAN, "", LocalDate.now(), null, 0);
        
        room = new Room(1,1,"Room 1",25,true,0, mockedLoc);
        intervention = new Intervention(1, 1, "lambdaSlug", "I am lambda Intervention", "admin@dawan.fr", mockedLoc, mockedCourse,
                mockedUser, 1, InterventionStatus.INTRA, true, LocalDate.now(), LocalDate.now().plusDays(5),
                LocalTime.of(9, 0), LocalTime.of(17, 0), null, false, 0, "", null, 0);
        
        bookingDto = new BookingDto(1, LocalDate.now(), LocalDate.now().plusDays(5), room.getId(), intervention.getId(), 0);
        booking = new Booking(1, room, intervention, LocalDate.now(), LocalDate.now().plusDays(5), 0);
    }
    
    @Test
    void should_map_bookingToBookingDto() {
        // mapping
        BookingDto mappedBookingDto = bookingMapper.bookingToBookingDto(booking);

        // assert
        assertEquals(mappedBookingDto.getId(), booking.getId());
        assertEquals(mappedBookingDto.getRoomId(), booking.getRoom().getId());
        assertEquals(mappedBookingDto.getInterventionId(), booking.getIntervention().getId());
        assertEquals(mappedBookingDto.getDateStart(), booking.getBeginDate());
        assertEquals(mappedBookingDto.getDateEnd(), booking.getEndingDate());
        assertEquals(mappedBookingDto.getVersion(), booking.getVersion());
    }

    @Test
    void should_map_bookingDtoToBooking() {
        // mapping
        Booking mappedBooking = bookingMapper.bookingDtoTobooking(bookingDto);

        // assert
        assertEquals(mappedBooking.getId(), bookingDto.getId());
        assertEquals(mappedBooking.getBeginDate(), bookingDto.getDateStart());
        assertEquals(mappedBooking.getEndingDate(), bookingDto.getDateEnd());
        assertEquals(mappedBooking.getVersion(), bookingDto.getVersion());
        assertEquals(mappedBooking.getIntervention().getId(), bookingDto.getInterventionId());
        assertEquals(mappedBooking.getRoom().getId(), bookingDto.getRoomId());

        
    }
}
