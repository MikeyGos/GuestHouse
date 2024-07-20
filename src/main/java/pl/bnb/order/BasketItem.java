package pl.bnb.order;

import java.math.BigDecimal;

public class BasketItem {
    private Integer idFood;
    private Integer idDrink;
    private String nameBasketProduct;
    private Integer qtyBasketProduct;
    private BigDecimal totalPrice;


    public Integer getIdFood() {
        return idFood;
    }

    public Integer getIdDrink() {
        return idDrink;
    }

    public String getNameBasketProduct() {
        return nameBasketProduct;
    }

    public void setNameBasketProduct(String nameBasketProduct) {
        this.nameBasketProduct = nameBasketProduct;
    }

    public Integer getQtyBasketProduct() {
        return qtyBasketProduct;
    }

    public void setQtyBasketProduct(Integer qtyBasketProduct) {
        this.qtyBasketProduct = qtyBasketProduct;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}