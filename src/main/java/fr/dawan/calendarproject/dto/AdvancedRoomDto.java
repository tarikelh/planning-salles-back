package fr.dawan.calendarproject.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdvancedRoomDto extends RoomDto {

    private List<Long> roomIds;

    public AdvancedRoomDto(long id, String name, long fullCapacity, long partialCapacity, boolean isAvailable, long locationId, int version, List<Long> roomIds) {
        super(id, name, fullCapacity, partialCapacity, isAvailable, locationId, version);
        this.roomIds = roomIds;
    }

    public AdvancedRoomDto() {
        this.roomIds = new ArrayList<>();
    }

    public List<Long> getRoomsId(){
        return roomIds;
    }

    public void setRoomId(List<Long> roomsId){
        this.roomIds = roomsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdvancedRoomDto that = (AdvancedRoomDto) o;
        return Objects.equals(roomIds, that.roomIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomIds);
    }
}
