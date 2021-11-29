package toll_collection;

import vehicle.Truck;

import java.util.*;

public class TollBooth {

    private int totalCollection;

    private int tollBoothNumber;

    private int cashDrawer;
    private int numOfTrucksServed;

    private Queue<Truck> truckQueue;

    private Thread collectionThread;
    private Thread startCollection;

    private volatile boolean OPEN_FOR_COLLECTION = true;

    public TollBooth(int tollBoothNumber) {
        this.tollBoothNumber = tollBoothNumber;

        totalCollection = 0;
        cashDrawer = 0;
        numOfTrucksServed = 0;

        truckQueue = new LinkedList<>();
    }

    public void openForCollection() {
        startCollection = new Thread(new Runnable() {
            @Override
            public void run() {
                while (OPEN_FOR_COLLECTION) {
                    if (truckQueue.size() > 0) {
                        System.out.println("Tollbooth: " + tollBoothNumber + ", queue size: " + truckQueue.size());
                        Truck truck = truckQueue.remove();
                        collectTollFromTruck(truck);

                        try {
                            collectionThread.join();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        startCollection.start();
    }

    public void addToQueue(Truck truck) {
        truckQueue.add(truck);
    }

    public void collectTollFromTruck(Truck truck) {
        collectionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int moneyDue;
                moneyDue = (5 * truck.getNumberOfAxle()) + (20 * truck.getTotalWeight());

                setCashDrawer(getCashDrawer() + moneyDue);
                totalCollection = totalCollection + moneyDue;

                numOfTrucksServed++;

                printStatus(truck, moneyDue);
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
            }
        });
        collectionThread.start();
    }

    private void printStatus(Truck truck, int moneyDue) {
        System.out.println("Toll Number:" + tollBoothNumber + " -> Truck arrival - " +
                "Truck No: " + truck.getTruckNumber() +
                ", Axles: " + truck.getNumberOfAxle() +
                ", Total weight(in tons): " + truck.getTotalWeight() +
                ", Toll due: Rs." + moneyDue);
    }

    public int getTollBoothNumber() {
        return tollBoothNumber;
    }

    public void setTollBoothNumber(int tollBoothNumber) {
        this.tollBoothNumber = tollBoothNumber;
    }

    public void stopTollCollection() {
        OPEN_FOR_COLLECTION = false;
        while (startCollection.isAlive()) {
        }
    }

    public void clearQueueAndStopCollection() {

        Thread clearQueue = new Thread(new Runnable() {
            @Override
            public void run() {
                while (truckQueue.size() != 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        clearQueue.start();
        try {
            clearQueue.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        OPEN_FOR_COLLECTION = false;
        collectionThread.interrupt();
    }

    public void resumeTollCollection() {
        OPEN_FOR_COLLECTION = true;
        openForCollection();
    }

    public synchronized int getCashDrawer() {
        return cashDrawer;
    }

    public synchronized void setCashDrawer(int cashDrawer) {
        this.cashDrawer = cashDrawer;
    }

    public int getNumOfTrucksServed() {
        return numOfTrucksServed;
    }

    public void setNumOfTrucksServed(int numOfTrucksServed) {
        this.numOfTrucksServed = numOfTrucksServed;
    }

    public int getTotalCollection() {
        return totalCollection;
    }

    public void setTotalCollection(int totalCollection) {
        this.totalCollection = totalCollection;
    }
}
