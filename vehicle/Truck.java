package vehicle;

import toll_collection.CollectionCenter;

import java.util.Random;

public class Truck {
    private int truckNumber;

    private int numberOfAxle;
    private int totalWeight; // in tons

    public Truck(int truckNumber, int numberOfAxle, int totalWeight) {
        this.truckNumber = truckNumber;
        this.numberOfAxle = numberOfAxle;
        this.totalWeight = totalWeight;
    }

    public int selectTollBooth() {
        return new Random().nextInt(CollectionCenter.numberOfTollBooths);
    }

    public int getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(int truckNumber) {
        this.truckNumber = truckNumber;
    }

    public int getNumberOfAxle() {
        return numberOfAxle;
    }

    public void setNumberOfAxle(int numberOfAxle) {
        this.numberOfAxle = numberOfAxle;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(int totalWeight) {
        this.totalWeight = totalWeight;
    }
}
