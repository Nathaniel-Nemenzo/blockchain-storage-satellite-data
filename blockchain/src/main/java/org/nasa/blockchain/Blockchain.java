package org.nasa.blockchain;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Blockchain {
    // First block in the blockchain, this has no transactions
    public Block genesisBlock;

    // Blockchain keeps tracks of the chain
    public List<Block> blocks;

    // Minimum amount of difficulty required for a miner to mine a block
    public int difficulty;

    /**
     * Creates the blockchain, initializes the genesis block.
     * @param difficulty
     */
    public Blockchain() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.genesisBlock = new Block();
        this.blocks = new ArrayList<>();
        this.blocks.add(this.genesisBlock);

        // Initalize the block with a '0' hash.
        this.genesisBlock.timestamp = timestamp.getTime();
        this.genesisBlock.hash = new byte[1];

        // Set the initial difficulty of the blockchain to 1 (miner needs to find hash with one zero in front first)
        this.difficulty = 3;
    }

    /**
     * Add a block into the blockchain, encoding the transaction into the data of the blockchain.
     * @param from
     * @param to
     * @param amount
     */
    public void addBlock(String from, String to, double amount) {
        // Create the new block
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Block newBlock = new Block();
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("from: " + from);
        sb.append(", to: " + to);
        sb.append(", amount: " + amount);
        sb.append("}");
        newBlock.data = sb.toString();

        // Set new block attributes
        newBlock.data = sb.toString();
        newBlock.timestamp = timestamp.getTime();
        newBlock.previousHash = this.blocks.get(this.blocks.size() - 1).hash;
        newBlock.nonce = this.blocks.get(this.blocks.size() - 1).nonce;

        // Mine the new block with the difficulty of the last block in the chain +1 (simulate proof of work)
        newBlock.mine(this.difficulty);

        // Calculate the hash one last time before adding it to the chain
        try {
            newBlock.hash = newBlock.calculateHash();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        // Add the new block to the blockchain
        this.blocks.add(newBlock);

        // Increase the difficulty of the blockchain
        this.difficulty += 1;
    }

    /**
     * Verifies the validity of the blockchain by checking hashes
     * @return
     */
    public boolean valid() {
        for (int i = 1; i < this.blocks.size(); i++) {
            Block previous = this.blocks.get(i - 1);
            Block current = this.blocks.get(i);

            // Make sure that the current hashes match
            try {
                byte[] calculatedHash = current.calculateHash();
                byte[] storedHash = current.hash;
                if (!Arrays.equals(calculatedHash, storedHash) || !current.previousHash.equals(previous.hash)) {
                    return false;
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void printBlockchain() throws NoSuchAlgorithmException {
        for (Block b : this.blocks) {
            System.out.println("-----");
            System.out.println("data: " + b.data);
            System.out.println("hash" + b.hash);
            System.out.println("calculated hash: " + b.calculateHash());
            System.out.println("previous hash: " + b.previousHash);
            System.out.println("timestamp: " + b.timestamp);
            System.out.println("nonce: " + b.nonce);
            System.out.println("-----");
        }
    }

    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();

        // Transactions
        blockchain.addBlock("Andrew", "Jaden", 100.04);

        blockchain.addBlock("Nathaniel", "Jaden", 50);

        // Make sure that the blockchain is valid
        boolean valid = blockchain.valid();
        if (valid) {
            System.out.println("The blockchain is valid!");
        } else {
            System.out.println("The blockchain is not valid!");
        }

        // try {
        //     blockchain.printBlockchain();
        // } catch (NoSuchAlgorithmException e) {
        //     e.printStackTrace();
        // }
    }
}
