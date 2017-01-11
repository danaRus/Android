package com.example.dana.carmanagement.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Car {
    private Long id;
    private String uuid;
    private String make;
    private String model;
    private Integer year;
    private Integer price;

    public Car(){

    }

    public Car(String make, String model, Integer year, Integer price) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
        return uuid;
    }

    public void setUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Map<String, Object> carToMap() {
        Map<String,Object> carMap = new HashMap<>();
        carMap.put("uuid", uuid);
        carMap.put("make", make);
        carMap.put("model", model);
        carMap.put("year", year);
        carMap.put("price", price);
        return carMap;
    }

    @Override
    public String toString() {
        return "Car{" +
                "make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", price=" + price +
                '}';
    }
}
