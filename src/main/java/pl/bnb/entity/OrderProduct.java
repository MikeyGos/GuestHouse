package pl.bnb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
    public class OrderProduct {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer idOrder;
    private String bookingNumber;
    private Integer idDrink;
    private Integer idFood;
    private String nameProduct;
    private Integer qtyProduct;
    private BigDecimal totalPrice;

    public Integer getIdOrder() {
        return idOrder;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }
    public void setBookingNumber(String bookingNumberOrder) {
        this.bookingNumber = bookingNumberOrder;
    }

    public Integer getIdDrink() {
        return idDrink;
    }

    public void setIdDrink(Integer idDrink) {
        this.idDrink = idDrink;
    }

    public Integer getIdFood() {
        return idFood;
    }

    public void setIdFood(Integer idFood) {
        this.idFood = idFood;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public Integer getQtyProduct() {
        return qtyProduct;
    }

    public void setQtyProduct(Integer qtyProduct) {
        this.qtyProduct = qtyProduct;
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "idOrder=" + idOrder +
                ", bookingNumber='" + bookingNumber + '\'' +
                ", idDrink=" + idDrink +
                ", idFood=" + idFood +
                ", nameProduct='" + nameProduct + '\'' +
                ", qtyProduct=" + qtyProduct +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
