//Write a program to sort the frames using appropriate sorting techniques 

import java.util.*;
class Lab2{
    // Frame class
    static class Frame {
        int number;
        String data;

        Frame(int number, String data) {
            this.number = number;
            this.data = data;
        }
    }
    // Bubble sort based on frame number
    static void sortFrames(Frame[] frames) {
        int n = frames.length;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (frames[j].number > frames[j + 1].number) {
                    Frame temp = frames[j];
                    frames[j] = frames[j + 1];
                    frames[j + 1] = temp;
                }
            }
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of frames: ");
        int n = sc.nextInt();
        //create an array of frame datatype of size n  
        Frame[] frames = new Frame[n];

        // Read frame details
        for (int i = 0; i < n; i++) {
            System.out.print("\nFrame number: ");
            int num = sc.nextInt();

        //give any input to the frame  
            System.out.print("Frame content: ");
            String content = sc.next();
            frames[i] = new Frame(num, content);
        }

        // Shuffle frames
        List<Frame> list = Arrays.asList(frames);
        Collections.shuffle(list);

        //contents of the frame before sorting 
        System.out.println("\nBefore Sorting:");
        for (Frame f : frames) {
            System.out.print(f.data + " ");
        }

        // Sort frames
        sortFrames(frames);

        //contents of the frame after sorting 
        System.out.println("\n\nAfter Sorting:");
        for (Frame f : frames) {
            System.out.print(f.data + " ");
        }
        sc.close();
    }
}
