import java.util.Scanner; // Used to take input from user
public class LeakyBucket {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); 

        System.out.print("Enter bucket capacity: ");
        int capacity = sc.nextInt(); // Maximum bucket size

        System.out.print("Enter output rate: ");
        int oprate = sc.nextInt(); // Output rate (packets per second)

        System.out.print("Enter number of packets: ");
        int n = sc.nextInt(); // Total number of packets

        int[] packets = new int[n]; // Store packet sizes

        System.out.println("Enter packet sizes:");
        for (int i = 0; i < n; i++) {
            packets[i] = sc.nextInt(); // Read packet size
        }

        int cur_bs = 0; // Current bucket size

        System.out.println("\nPacket\tCur_BS\tSent\tLeft\tStatus");

        for (int p : packets) { // Process packets one by one

            if (cur_bs + p <= capacity) {  // Check if packet fits
                cur_bs = cur_bs + p; // Add packet to bucket
                int sent = Math.min(oprate, cur_bs); // Packets sent
                int left = cur_bs - sent; // Packets remaining
                System.out.println(p + "\t" + cur_bs + "\t" + sent + "\t" + left + "\tAccepted");
            } 
            else { // Packet overflow condition
                int sent = Math.min(oprate, cur_bs); // Packets sent
                int left = cur_bs - sent; // Packets remaining
                System.out.println(p + "\t" + cur_bs + "\t" + sent + "\t" + left + "\tDropped");
            }

            cur_bs = Math.max(0, cur_bs - oprate); // Leak packets from bucket
        }

        sc.close(); // Close scanner
    }
}
