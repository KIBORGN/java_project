package Vehicle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Engine.Engine;
import InsuranceOption.InsuranceOption;
import Enums.VehicleType;

public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;
    private String vin;
    private String make;
    private String model;
    private int year;
    private double basePrice;
    private Engine engine;
    private int horsepower;
    private VehicleType type;
    private List<InsuranceOption> options = new ArrayList<>();

    public Vehicle(String vin, String make, String model, int year, double basePrice,
                   Engine engine, int horsepower, VehicleType type) {
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.year = year;
        this.basePrice = basePrice;
        this.engine = engine;
        this.horsepower = horsepower;
        this.type = type;
    }

    public void addOption(InsuranceOption option) {
        options.add(option);
    }

    public double calculateDepreciation() {
        int age = java.time.Year.now().getValue() - year;
        double depreciationRate = Math.min(0.5, age * 0.05);
        return basePrice * (1 - depreciationRate);
    }

    public double optionsCost() {
        return options.stream().mapToDouble(InsuranceOption::getCost).sum();
    }

    public Engine getEngine() {
        return engine;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public int getHorsepower() {
        return horsepower;
    }

    public VehicleType getType() {
        return type;
    }

    public double getRiskIndex() {
        double index = 1.0;
        if (horsepower > 150) {
            index += 0.3;
        } else if (horsepower > 100) {
            index += 0.1;
        }

        switch (type) {
            case MOTORCYCLE:
                index += 0.5;
                break;
            case CARRIAGE:
                index += 0.7;
                break;
            default:
                break;
        }
        return index;
    }

    public double getPollutionIndex() {
        int age = java.time.Year.now().getValue() - year;
        return engine.getPollutionIndex(age);
    }

    @Override
    public String toString() {
        return make + " " + model;
    }
}