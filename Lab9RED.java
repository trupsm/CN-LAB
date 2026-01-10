//queue represents the current number of packets waiting in the router buffer.
/*
queue < min        → Accept
min ≤ queue < max  → Random drop
queue ≥ max        → Drop
queue = full       → Drop
 */
import java.util.Random;
import java.util.Scanner;
public class Lab9RED {
    static int queue = 0;
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Random rand = new Random();

        System.out.print("Enter min threshold: ");
        int min = sc.nextInt();

        System.out.print("Enter max threshold: ");
        int max = sc.nextInt();

        System.out.print("Enter max drop probability (0-1): ");
        double maxProb = sc.nextDouble();

        System.out.print("Enter queue size: ");
        int queueSize = sc.nextInt();

        System.out.print("Enter number of packets: ");
        int packets = sc.nextInt();

        for (int i = 1; i <= packets; i++) {

            System.out.print("Packet " + i + ": ");

            // Queue full
            if (queue >= queueSize) {
                System.out.println("Dropped (Queue Full)");
                continue;
            }

            // Below min threshold → accept
            if (queue < min) {
                queue++;
                System.out.println("Accepted");
            }

            // Above max threshold → drop
            else if (queue >= max) {
                System.out.println("Dropped (High Congestion)");
            }

            // Between min and max → RED logic
            else {
                double prob = maxProb * (queue - min) / (max - min);
                if (rand.nextDouble() < prob) {
                    System.out.println("Dropped (RED)");
                } else {
                    queue++;
                    System.out.println("Accepted");
                }
            }
        }

        sc.close();
    }
}
