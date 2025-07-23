import java.io.*;
import java.net.Socket;
import java.security.*;
import javax.crypto.SecretKey;
import java.util.Scanner;

public class Voter {
    public static void main(String[] args) throws Exception {
        // Initialize scanner to take user input
        Scanner scanner = new Scanner(System.in);

        // Prompt user to enter Voter ID and Candidate Name
        System.out.print("Enter your Voter ID: ");
        String voterID = scanner.nextLine().trim();

        System.out.print("Enter the Candidate you want to vote for: ");
        String candidateName = scanner.nextLine().trim();

        // Connect to the server
        Socket socket = new Socket("localhost", 4000);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        // Generate RSA Key Pair for the voter
        KeyPair voterKeyPair = CryptoUtils.generateRSAKeyPair();

        // Step 1: Create Vote object with candidate name from input
        Vote vote = new Vote(candidateName);

        // Serialize vote object to byte[]
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(vote);
        byte[] voteBytes = baos.toByteArray();

        // Step 2: Generate AES key and encrypt vote
        SecretKey aesKey = CryptoUtils.generateAESKey();
        byte[] encryptedVote = CryptoUtils.encryptAES(voteBytes, aesKey);

        // Step 3: Receive server's public key
        PublicKey serverPublicKey = (PublicKey) in.readObject();

        // Step 4: Encrypt AES key using server's public key (RSA)
        byte[] encryptedAESKey = CryptoUtils.encryptRSA(aesKey.getEncoded(), serverPublicKey);

        // Step 5: Sign the encrypted vote with voter's private key
        byte[] signature = CryptoUtils.signData(encryptedVote, voterKeyPair.getPrivate());

        // Step 6: Send data to server
        out.writeObject(voterID);
        out.writeObject(voterKeyPair.getPublic()); // For verification
        out.writeObject(encryptedVote);
        out.writeObject(encryptedAESKey);
        out.writeObject(signature);

        System.out.println("Vote sent successfully!");

        // Close everything
        socket.close();
        scanner.close();
    }
}
