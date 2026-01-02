import java.util.Scanner;

public class Lab3CRC {
//This function generates a string containing the given number of '0's.Used to append zeros to data during encoding.
    static String generateZeros(int count) {
        String zeros = "";
        for (int i = 0; i < count; i++) {
            zeros += "0";
        }
        return zeros;
    }

    /*
     * This function performs CRC division (XOR division).
     * It takes:
     *  - dividend : data bits (with appended zeros or received data)
     *  - divisor  : CRC key (generator polynomial)
     * It returns:
     *  - remainder after division
     */
    static String getRemainder(String dividend, String divisor) {

        // Convert input strings into character arrays
        char[] dataBits = dividend.toCharArray();
        char[] keyBits = divisor.toCharArray();

        int dataLen = dataBits.length;
        int keyLen = keyBits.length;

        // Perform binary division using XOR
        for (int i = 0; i <= dataLen - keyLen; i++) {

            // If the current bit is 1, perform XOR with the key
            if (dataBits[i] == '1') {

                for (int j = 0; j < keyLen; j++) {

                    // XOR operation:
                    // same bits -> 0, different bits -> 1
                    dataBits[i + j] =
                            (dataBits[i + j] == keyBits[j]) ? '0' : '1';
                }
            }
        }

        // The last (keyLen - 1) bits form the CRC remainder
        return new String(dataBits,dataLen - keyLen + 1,keyLen - 1);
    }

    /*
     * This function encodes the data by:
     * 1. Appending (key length - 1) zeros to the data
     * 2. Finding the CRC remainder
     * 3. Appending the remainder to the original data
     */
    static String encodeData(String data, String key) {

        // Append zeros to the data
        String paddedData = data + generateZeros(key.length() - 1);

        // Calculate remainder
        String remainder = getRemainder(paddedData, key);

        // Encoded data = original data + remainder
        return data + remainder;
    }

    /*
     * This function decodes the received data.
     * It checks whether an error occurred during transmission.
     * If remainder contains '1' -> error detected
     * If remainder is all '0' -> data is error free
     */
    static boolean decodeData(String receivedData, String key) {
        // Calculate remainder of received data
        String remainder = getRemainder(receivedData, key);

        // Check for presence of error
        return remainder.contains("1");
    }

    /*
     * Main function:
     * Takes input from user,
     * performs encoding and decoding,
     * and displays the result.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Input original data
        System.out.print("Enter binary data: ");
        String data = sc.next();

        // Input CRC key
        System.out.print("Enter binary key: ");
        String key = sc.next();

        // Encode the data
        String encodedData = encodeData(data, key);
        System.out.println("Encoded data: " + encodedData);

        // Input received data for decoding
        System.out.print("Enter received data: ");
        String receivedData = sc.next();

        // Decode and display result
        if (decodeData(receivedData, key))
            System.out.println("Error detected in data");
        else
            System.out.println("Data is error free");

        sc.close();
    }
}
