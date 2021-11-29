package toll_collection;

import java.util.ArrayList;

public class CollectionCenter {

    private static volatile boolean START_COLLECTION = true;

    public static int numberOfTollBooths = 2;

    private int totalTollCollected;

    Thread collectionThread;

    public CollectionCenter() {
        totalTollCollected = 0;
    }

    public void startCollection(ArrayList<TollBooth> tollBooths) {
        collectionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (START_COLLECTION) {
                    try {
                        Thread.sleep(20000);
                    } catch (Exception e) {
                        System.out.println();
                        System.out.println("Collection center closed");
                        System.out.println();
                    }
                    collectTollFromTollBooths(tollBooths);
                }
            }
        });

        collectionThread.start();
    }

    public void collectTollFromTollBooths(ArrayList<TollBooth> tollBooths) {

        for (TollBooth tollBooth : tollBooths) {
            tollBooth.stopTollCollection();
        }
        System.out.println("*** emptying cash drawer ***");

        for (TollBooth tollBooth : tollBooths) {
            int tollBoothNumber = tollBooth.getTollBoothNumber();
            int moneyInTollBooth = tollBooth.getCashDrawer();
            int numOfTrucksServed = tollBooth.getNumOfTrucksServed();

            printTransaction(tollBoothNumber, moneyInTollBooth, numOfTrucksServed);

            totalTollCollected = totalTollCollected + moneyInTollBooth;

            tollBooth.setCashDrawer(0);
            tollBooth.setNumOfTrucksServed(0);
        }

        tollBooths.forEach(TollBooth::resumeTollCollection);
    }

    public void printTransaction(int tollBoothNumber, int moneyInTollBooth, int numOfTrucksServed) {
        System.out.println("Toll number:" + tollBoothNumber +
                " -> Total since last collection - Amount: Rs." + moneyInTollBooth +
                ", Trucks: " + numOfTrucksServed);
    }

    public void stopCollection(ArrayList<TollBooth> tollBooths) {

        for (TollBooth tollBooth : tollBooths) {
            tollBooth.clearQueueAndStopCollection();
        }
        START_COLLECTION = false;

        try {
            collectionThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        printFinalStatus(tollBooths);
    }

    public void printFinalStatus(ArrayList<TollBooth> tollBooths) {

        System.out.println();
        System.out.println("-------End of day status-------");

        for (TollBooth tollBooth : tollBooths) {
            System.out.println("Toll number:" + tollBooth.getTollBoothNumber() +
                    " -> Amount collected: " + tollBooth.getTotalCollection());
        }

        System.out.println("Total amount collected by the Collection Center: " + totalTollCollected);
        System.exit(1);
    }

}
