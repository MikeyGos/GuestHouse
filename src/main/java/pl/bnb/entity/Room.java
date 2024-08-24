package pl.bnb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idParty;
    private String bookingNumber;
    private LocalDate date;
    private LocalTime time;
    private String roomName;

    public Integer getIdParty() {
        return idParty;
    }
    public void setIdParty(Integer idParty) {}

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room room)) return false;

        return Objects.equals(getIdParty(), room.getIdParty()) && Objects.equals(getBookingNumber(), room.getBookingNumber()) && Objects.equals(getDate(), room.getDate()) && Objects.equals(getTime(), room.getTime()) && Objects.equals(getRoomName(), room.getRoomName());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getIdParty());
        result = 31 * result + Objects.hashCode(getBookingNumber());
        result = 31 * result + Objects.hashCode(getDate());
        result = 31 * result + Objects.hashCode(getTime());
        result = 31 * result + Objects.hashCode(getRoomName());
        return result;
    }
}