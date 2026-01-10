/*
Bucket = Bandwidth limit
Tokens = Permission to send data
Packet = Data request
If permission exists → Accepted
If not → Dropped
*/
import java.util.Scanner;
public class TokenBucket {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); 

        System.out.print("Enter bucket capacity: ");
        int capacity = sc.nextInt(); // Maximum tokens

        System.out.print("Enter token rate: ");
        int rate = sc.nextInt(); // Tokens added per second

        System.out.print("Enter number of packets: ");
        int n = sc.nextInt();  // Total packets

        int tokens = 0;// Initial tokens
        int[] packets = new int[n]; // Store packet sizes

        System.out.println("Enter packet sizes:");
        for (int i = 0; i < n; i++) {
            packets[i] = sc.nextInt(); // Read packet sizes
        }

        for (int i = 0; i < n; i++) {
            tokens = Math.min(tokens + rate, capacity); // Generate tokens in such a way that they don't exceed the bucket capacity
            System.out.println("Tokens: " + tokens);

            if (packets[i] <= tokens) { // Check availability of tokens 
                tokens -= packets[i]; // Consume tokens to send the packets
                System.out.println("Packet " + (i + 1) + " Accepted");
            } 
            else {   //no tokens available drop the packets 
                System.out.println("Packet " + (i + 1) + " Dropped");
            }
        }
        sc.close();                            
    }
}
