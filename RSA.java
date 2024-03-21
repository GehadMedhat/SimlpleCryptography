package rsa;

import java.math.BigInteger;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;

public class RSA {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Enter P :");
        int p = Prime();

        System.out.println("Enter Q :");
        int q = Prime();

        int e;
        int n = p * q;
        System.out.println("n = " + n);

        int m = (p - 1) * (q - 1);
        System.out.println("m = " + m);

        for (e = 2; e < m; e++) {
            if (gcd(e, m) == 1) {
                break;
            }
        }
        System.out.println("e = " + e);

        int[] rsa = rsaGen(e, m, n);
        BigInteger N = BigInteger.valueOf(rsa[0]);
        BigInteger E = BigInteger.valueOf(rsa[1]);
        BigInteger D = BigInteger.valueOf(rsa[2]);

        System.out.print("Enter the message to be encrypted: ");
Scanner messageScanner = new Scanner(System.in);
String message = messageScanner.nextLine();

        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        BigInteger encryptedMessage = encrypt(messageBytes, E, N);
        byte[] decryptedMessage = decrypt(encryptedMessage, D, N);
        System.out.println("Original message: " + message);
        System.out.println("Encrypted message: " + encryptedMessage.toString());
        System.out.println("Decrypted message: " + decryptedMessage);

    }

    public static int Prime() {
        int counter = 0;
        boolean flag = true;
        int p = 0;
        while (true) {
            counter = 0;
            p = sc.nextInt();
            for (int i = 1; i <= p; i++) {
                if (p % i == 0) {
                    counter++;
                }
            }
            if (counter == 2) {
                System.out.println("Number is prime");
                flag = false;
                break;
            } else {
                System.out.println("Number is not Prime \nPlease try again");

            }
        }
        return p;
    }

    public static int gcd(int e, int m) {
        if (e == 0) {
            return m;
        } else {
            return gcd(m % e, e);
        }
    }

    public static int[] rsaGen(int e, int m, int n) {
        int d = modInverse(e, m);
        return new int[]{n, e, d};
    }

    public static int modInverse(int a, int m) {
        int m0 = m, t, q;
        int x0 = 0, x1 = 1;
        if (m == 1) {
            return 0;
        }
        while (a > 1) {
            q = a / m;
            t = m;
            m = a % m;
            a = t;
            t = x0;
            x0 = x1 - q * x0;
            x1 = t;
        }
        if (x1 < 0) {
            x1 += m0;
        }
        return x1;
    }

    public static BigInteger encrypt(byte[] messageBytes, BigInteger e, BigInteger n) {
        BigInteger m = new BigInteger(1, messageBytes);
        return m.modPow(e, n);
    }

    public static byte[] decrypt(BigInteger encryptedMessage, BigInteger d, BigInteger n) {
    BigInteger m = encryptedMessage.modPow(d, n);
    byte[] messageBytes = m.toByteArray();
    if (messageBytes[0] == 0) {
        // the decrypted message has leading zeros
        byte[] trimmedMessageBytes = new byte[messageBytes.length - 1];
        System.arraycopy(messageBytes, 1, trimmedMessageBytes, 0, trimmedMessageBytes.length);
        return trimmedMessageBytes;
    } else {
        // the decrypted message has no leading zeros
        return messageBytes;
    }
}

}
