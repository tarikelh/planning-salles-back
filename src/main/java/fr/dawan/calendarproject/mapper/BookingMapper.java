package fr.dawan.calendarproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.dawan.calendarproject.dto.BookingDto;
import fr.dawan.calendarproject.entities.Booking;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.RoomRepository;

@Mapper(componentModel = "spring", uses = { InterventionRepository.class, RoomRepository.class })
public interface BookingMapper {
	
	
	@Mapping(target="dateStart", source="beginDate")
	@Mapping(target="dateEnd", source="endingDate")
	@Mapping(target="roomId", source="room.id")
	@Mapping(target="interventionId", source="intervention.id")
	BookingDto bookingToBookingDto(Booking booking);
	
	
	@Mapping(target="beginDate", source="dateStart")
	@Mapping(target="endingDate", source="dateEnd")
	@Mapping(target="room", ignore=true)
	@Mapping(target="intervention", ignore=true)
	Booking bookingDtoTobooking(BookingDto bookingDto);
	
	
	// ?? TODO Booking to Dg2 ??
	
}
