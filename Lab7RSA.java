import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

// This class implements the RSA algorithm
class RSAalgorithm {

    // prk--private key (d)
    // puk--public key (e)
    // n--modulus (n = p × q)
    BigInteger d,e, n;

    // This method generates the RSA public and private keys
    void getkeys(int bitlen) {

        // Random object used to generate prime numbers
        Random r = new Random();

        // Generate first large prime number p
        BigInteger p = BigInteger.probablePrime(bitlen, r);

        // Generate second large prime number q
        BigInteger q = BigInteger.probablePrime(bitlen, r);

        // Calculate modulus n = p × q
        n = p.multiply(q);

        // Calculate Euler’s Totient Function φ(n) = (p − 1)(q − 1)
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        // Choose public key exponent e
        e = BigInteger.probablePrime(bitlen / 2, r);

        // Ensure e is coprime with φ(n) and less than φ(n)
        while (!phi.gcd(e).equals(BigInteger.ONE) || e.compareTo(phi) >= 0) {
            e = BigInteger.probablePrime(bitlen / 2, r);
        }

        // Calculate private key exponent d 
        //d × e ≡ 1 (mod φ(n))
        d = e.modInverse(phi);
    }
    //public key (e,n)
    //private key (d,n)

    // Encryption formula: C = M^e mod n
    BigInteger encrypt(BigInteger message) {
        return message.modPow(e, n);
    }

    // Decryption formula: M = C^d mod n
    BigInteger decrypt(BigInteger cipher) {
        return cipher.modPow(d, n);
    }
}

// Main class that runs the RSA program
class Lab7RSA {

    public static void main(String[] args) {

        // Create an object of RSAalgorithm
        RSAalgorithm rsa = new RSAalgorithm();

        // Generate RSA keys of 512-bit length
        rsa.getkeys(512);

        // Scanner object to read input from user
        Scanner sc = new Scanner(System.in);

        // Prompt user to enter the message
        System.out.print("Enter the message to be encrypted: ");

        // Convert the input string into BigInteger
        BigInteger message = new BigInteger(sc.next().getBytes());

        // Encrypt the message using public key
        BigInteger encryptedMessage = rsa.encrypt(message);
        System.out.println("Encrypted message: " + encryptedMessage);

        // Decrypt the encrypted message using private key
        BigInteger decryptedMessage = rsa.decrypt(encryptedMessage);

        // Convert decrypted BigInteger back to string
        System.out.println("Decrypted message: " +
                new String(decryptedMessage.toByteArray()));

        // Close the scanner
        sc.close();
    }

}
