package pl.bnb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Drink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer idDrink;
    private String name;
    private BigDecimal price;
    private Double capacity;
    private int qtyDrink;

    public Integer getIdDrink() {
        return idDrink;
    }

    public void setIdDrink(Integer idDrink) {   // setter only for test - automatically increment in base date
        this.idDrink = idDrink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public int getQtyDrink() {
        return qtyDrink;
    }

    public void setQtyDrink(int qtyDrink) {
        this.qtyDrink = qtyDrink;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Drink drink)) return false;

        return getQtyDrink() == drink.getQtyDrink() && Objects.equals(getIdDrink(), drink.getIdDrink()) && Objects.equals(getName(), drink.getName()) && Objects.equals(getPrice(), drink.getPrice()) && Objects.equals(getCapacity(), drink.getCapacity());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getIdDrink());
        result = 31 * result + Objects.hashCode(getName());
        result = 31 * result + Objects.hashCode(getPrice());
        result = 31 * result + Objects.hashCode(getCapacity());
        result = 31 * result + getQtyDrink();
        return result;
    }
}
