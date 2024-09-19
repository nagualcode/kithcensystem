package br.nagualcode.printservice.model;

public class OrderItem {

    private String plateDescription;
    private double platePrice;

    // Getters and Setters
    public String getPlateDescription() {
        return plateDescription;
    }

    public void setPlateDescription(String plateDescription) {
        this.plateDescription = plateDescription;
    }

    public double getPlatePrice() {
        return platePrice;
    }

    public void setPlatePrice(double platePrice) {
        this.platePrice = platePrice;
    }
}
