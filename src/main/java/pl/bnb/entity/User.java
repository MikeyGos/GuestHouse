package pl.bnb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer idUser;
    private String name;
    private String password;
    private String lastName;
    private String phoneNumber;
    private String bookingNumber;
    private Boolean payOnline;
    private Boolean admin;


    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public Boolean getPayOnline() {
        return payOnline;
    }

    public void setPayOnline(Boolean payOnline) {
        this.payOnline = payOnline;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;

        return Objects.equals(getIdUser(), user.getIdUser()) && Objects.equals(getName(), user.getName()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getLastName(), user.getLastName()) && Objects.equals(getPhoneNumber(), user.getPhoneNumber()) && Objects.equals(getBookingNumber(), user.getBookingNumber()) && Objects.equals(getPayOnline(), user.getPayOnline()) && Objects.equals(getAdmin(), user.getAdmin());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getIdUser());
        result = 31 * result + Objects.hashCode(getName());
        result = 31 * result + Objects.hashCode(getPassword());
        result = 31 * result + Objects.hashCode(getLastName());
        result = 31 * result + Objects.hashCode(getPhoneNumber());
        result = 31 * result + Objects.hashCode(getBookingNumber());
        result = 31 * result + Objects.hashCode(getPayOnline());
        result = 31 * result + Objects.hashCode(getAdmin());
        return result;
    }
}
