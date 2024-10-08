package pl.bnb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFood;
    private String name;
    private BigDecimal price;
    private String size;
    private int qtyFood;


    public Integer getIdFood() {
        return idFood;
    }

    public void setIdFood(Integer idFood) {         //setter only for test = autoincrement in db
        this.idFood = idFood;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQtyFood() {
        return qtyFood;
    }

    public void setQtyFood(int qtyFood) {
        this.qtyFood = qtyFood;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Food food)) return false;

        return getQtyFood() == food.getQtyFood() && Objects.equals(getIdFood(), food.getIdFood()) && Objects.equals(getName(), food.getName()) && Objects.equals(getPrice(), food.getPrice()) && Objects.equals(getSize(), food.getSize());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getIdFood());
        result = 31 * result + Objects.hashCode(getName());
        result = 31 * result + Objects.hashCode(getPrice());
        result = 31 * result + Objects.hashCode(getSize());
        result = 31 * result + getQtyFood();
        return result;
    }
}
