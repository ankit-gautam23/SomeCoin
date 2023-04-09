import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

public class SomeCoin {
    public static ArrayList<Block> blockChain = new ArrayList<Block>();
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();
    public static int difficulty = 5;
    public static Wallet walletA;
    public static Wallet walletB;
    public static float minimumTransaction = 0.1f;
    public static Transaction genesisTransaction;

    public static void main(String[] args) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Wallet walletA = new Wallet();
        Wallet walletB = new Wallet();

        System.out.println("Private and Public Keys : ");
        System.out.println(StringUtil.getStringFromKey(walletA.privateKey));
        System.out.println(StringUtil.getStringFromKey(walletA.publicKey));

        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
        transaction.generateSignature(walletA.privateKey);

        System.out.println("Is Signature Verified");
        System.out.println(transaction.verifySignature());
    }
}