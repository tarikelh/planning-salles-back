package fr.dawan.calendarproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.dawan.calendarproject.dto.BookingDto;
import fr.dawan.calendarproject.entities.Booking;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.RoomRepository;

@Mapper(componentModel = "spring", uses = { InterventionRepository.class})
public interface BookingMapper {

	@Mapping(target="id", source="id")
	@Mapping(target="dateStart", source="beginDate")
	@Mapping(target="dateEnd", source="endingDate")
//	@Mapping(target="roomId", source="room.id")
	@Mapping(target="interventionId", source="intervention.id")
	@Mapping(target="version", ignore=true)
	BookingDto bookingToBookingDto(Booking booking);
	
	
	@Mapping(target="id", source="id")
	@Mapping(target="beginDate", source="dateStart")
	@Mapping(target="endingDate", source="dateEnd")
	@Mapping(target="rooms", ignore=true)
	@Mapping(target="intervention", source="interventionId")
	@Mapping(target="version", ignore=true)
	Booking bookingDtoTobooking(BookingDto bookingDto);
	
	// ?? TO-DO Booking to Dg2 ??
	
}
