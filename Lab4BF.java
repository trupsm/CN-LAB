import java.util.Scanner;
public class Lab4BF{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of vertices: ");
        int n = sc.nextInt();
        int graph[][] = new int[n][n];

        System.out.println("Enter weight matrix (0 = no edge):");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                graph[i][j] = sc.nextInt();
            }
        }

        System.out.print("Enter source vertex (1-based): ");
        int src = sc.nextInt() - 1;

        int dist[] = new int[n];

        // Step 1: initialize distances
        for (int i = 0; i < n; i++) {
            dist[i] = 9999;   // instead of Integer.MAX_VALUE
        }
        dist[src] = 0;

        // Step 2: relax edges n-1 times
        for (int i = 0; i < n - 1; i++) {
            for (int u = 0; u < n; u++) {
                for (int v = 0; v < n; v++) {
                    if (graph[u][v] != 0 && dist[u] + graph[u][v] < dist[v]) {
                        dist[v] = dist[u] + graph[u][v];
                    }
                }
            }
        }

        // Step 3: check negative cycle
        for (int u = 0; u < n; u++) {
            for (int v = 0; v < n; v++) {
                if (graph[u][v] != 0 && dist[u] + graph[u][v] < dist[v]) {
                    System.out.println("Negative weight cycle exists");
                    return;
                }
            }
        }

        // Step 4: print result
        System.out.println("\nVertex   Distance");
        for (int i = 0; i < n; i++) {
            System.out.println((i + 1) + "        " + dist[i]);
        }

        sc.close();
    }
}
