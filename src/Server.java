import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.util.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Server {
    private static final Set<String> votedUsers = new HashSet<>();
    private static final Map<String, PublicKey> registeredVoters = new HashMap<>();

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(4000);
        KeyPair serverKeyPair = CryptoUtils.generateRSAKeyPair();

        System.out.println("Server listening...");

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(() -> handleVote(socket, serverKeyPair)).start();
        }
    }

    private static void handleVote(Socket socket, KeyPair serverKeyPair) {
        try (
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
        ) {
            // Send server's public key
            out.writeObject(serverKeyPair.getPublic());

            // Read data from voter
            String voterID = (String) in.readObject();
            PublicKey voterPubKey = (PublicKey) in.readObject();
            byte[] encryptedVote = (byte[]) in.readObject();
            byte[] encryptedAESKey = (byte[]) in.readObject();
            byte[] signature = (byte[]) in.readObject();

            if (votedUsers.contains(voterID)) {
                System.out.println("Rejected: Duplicate vote from " + voterID);
                return;
            }

            if (!CryptoUtils.verifySignature(encryptedVote, signature, voterPubKey)) {
                System.out.println("Invalid signature from " + voterID);
                return;
            }

            // Decrypt AES key
            byte[] aesKeyBytes = CryptoUtils.decryptRSA(encryptedAESKey, serverKeyPair.getPrivate());
            SecretKey aesKey = new SecretKeySpec(aesKeyBytes, "AES");

            // Decrypt vote
            byte[] voteBytes = CryptoUtils.decryptAES(encryptedVote, aesKey);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(voteBytes));
            Vote vote = (Vote) ois.readObject();

            System.out.println("Vote received from " + voterID + ": " + vote.getCandidate());

            // Store vote anonymously (e.g., in file or memory)
            votedUsers.add(voterID);
            // Write to file or count the vote here

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
