package Vehicle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Engine.Engine;
import InsuranceOption.InsuranceOption;

public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;
    private String vin;
    private String make;
    private String model;
    private int year;
    private double basePrice;
    private Engine engine;
    private List<InsuranceOption> options = new ArrayList<>();

    public Vehicle(String vin, String make, String model, int year, double basePrice, Engine engine) {
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.year = year;
        this.basePrice = basePrice;
        this.engine = engine;
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
}
