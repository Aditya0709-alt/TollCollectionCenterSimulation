import toll_collection.CollectionCenter;
import toll_collection.TollBooth;
import vehicle.Truck;
import java.util.*;

public class Main {

    private static volatile boolean TRUCK_GENERATOR = true;

    private static final int ONE_DAY_TIME = 50000;

    private static ArrayList<TollBooth> tollBooths = new ArrayList<>(4);

    private static int truckNumber = 0;

    public static void main(String[] args) {

        CollectionCenter collectionCenter = new CollectionCenter();

        initializeTollBooths();
        startTruckGenerator();

        Thread collectionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                collectionCenter.startCollection(tollBooths);

                try {
                    Thread.sleep(ONE_DAY_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                stopTruckGenerator();
                collectionCenter.stopCollection(tollBooths);
            }
        });
        collectionThread.start();

    }

    private static void initializeTollBooths() {

        System.out.println("Tollbooths started for collection");

        for (int i = 1; i <= CollectionCenter.numberOfTollBooths; i++) {
            TollBooth tollBooth = new TollBooth(i);
            tollBooths.add(tollBooth);
        }

        tollBooths.forEach(TollBooth::openForCollection);
    }

    private static void startTruckGenerator() {
        Random random = new Random();

        Thread truckGeneratorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (TRUCK_GENERATOR) {
                    truckNumber++;
                    System.out.println("Truck " + truckNumber + " created");
                    Truck truck = new Truck(truckNumber, 2 + random.nextInt(10), 20 + random.nextInt(1000));

                    int tollBoothNumber = truck.selectTollBooth();
                    tollBooths.get(tollBoothNumber).addToQueue(truck);

                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        truckGeneratorThread.start();
    }

    private static void stopTruckGenerator() {
        TRUCK_GENERATOR = false;
    }
}
