package br.nagualcode.orderservice.model;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderMessage {

    private Long orderId;
    private String customer;
    private String email;
    private String status;
    private BigDecimal total;

 
    public OrderMessage() {
    }

  
    public OrderMessage(Long orderId, String customer, String email, String status, BigDecimal total) {
        this.orderId = orderId;
        this.customer = customer;
        this.email = email;
        this.status = status;
        this.total = total;
    }

    // Getters and setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    // equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderMessage that = (OrderMessage) o;
        return Objects.equals(orderId, that.orderId) &&
                Objects.equals(customer, that.customer) &&
                Objects.equals(email, that.email) &&
                Objects.equals(status, that.status) &&
                Objects.equals(total, that.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, customer, email, status, total);
    }


    @Override
    public String toString() {
        return "OrderMessage{" +
                "orderId=" + orderId +
                ", customer='" + customer + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                ", total=" + total +
                '}';
    }
}
