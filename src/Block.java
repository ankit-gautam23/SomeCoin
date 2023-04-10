import java.util.ArrayList;
import java.util.Date;

public class Block {
    public String hash;
    public String prevHash;
    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    public long timeStamp;
    public int nonce;


    public Block(String prevHash){
        this.prevHash = prevHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }
    public String calculateHash(){
        String hashValue = StringUtil.applySha256(prevHash+Long.toString(timeStamp)+Integer.toString(nonce)+merkleRoot);
        return hashValue;
    }

    public void mineBlock(int difficulty){
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = StringUtil.getDifficultyString(difficulty);
        while(!hash.substring(0, difficulty).equals(target)){
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Mined Block Hash: "+hash);
    }
    public boolean addTransaction(Transaction transaction) {
        if(transaction == null) return false;
        if((!"0".equals(prevHash))) {
            if((transaction.processTransaction() != true)) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }
}
