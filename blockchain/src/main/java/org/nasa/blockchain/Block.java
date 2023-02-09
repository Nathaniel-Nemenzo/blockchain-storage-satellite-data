package org.nasa.blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Block {
    public String data;
    public byte[] hash;

    // Forms block chain
    public byte[] previousHash;

    // Time of block generation
    public long timestamp;

    // Needed in Proof-of-work consensus
    public int nonce;

    /**
     * Calculates the hash of a block using the previous hash, block data, timestamp, and nonce with SHA256.
     * @return Byte array of hash
     * @throws NoSuchAlgorithmException
     */
    public byte[] calculateHash() throws NoSuchAlgorithmException {
        StringBuilder hash = new StringBuilder();

        // Hash the contents of the block
        hash.append(previousHash);
        hash.append(data);
        hash.append(timestamp);
        hash.append(nonce);

        // Hash to SHA-256
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(hash.toString().getBytes(StandardCharsets.UTF_8));
        return hashBytes;
    }

    /**
     * Mine a new block. Involves incrementing the nonce value of the current block
     * until getting a block hash with some number of trailing zeros.
     * @return
     */
    public void mine(int difficulty) {
        try {
            while (countLeadingZeros(this.calculateHash()) < difficulty) {
                this.nonce += 1;
                this.hash = this.calculateHash();

            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Count the number of leading zeros in a byte array, using in proof-of-work.
     * @param bytes
     * @return int
     */
    private int countLeadingZeros(byte[] bytes) {
        int count = 0;
        for (byte b : bytes) {
            int i = b;
            if (i == 0) {
                count += 8;
            } else {
                count += Integer.numberOfLeadingZeros(i);
                break;
            }
        }
        return count;
    }
}
