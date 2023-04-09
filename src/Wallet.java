import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {
    public PrivateKey privateKey;
    public PublicKey publicKey;
    public HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();
    public Wallet(){
        generateKeyPair();
    }

    public void generateKeyPair(){
        try{
            KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

            keyGenerator.initialize(ecSpec, random);
            KeyPair keyPair = keyGenerator.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public float getBalance(){
        float total = 0;

        for(Map.Entry<String, TransactionOutput> item: SomeCoin.UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(publicKey)){
                UTXOs.put(UTXO.id, UTXO);
                total += UTXO.value;
            }
        }

        return total;
    }

    public Transaction sendFunds(PublicKey _reciever, float value){
        if(getBalance() < value){
            System.out.println("Insufficient Funds :(");
            return null;
        }
        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
        float total = 0;
        for(Map.Entry<String, TransactionOutput> item : UTXOs.entrySet() ){
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if(total > value) break;
        }
        Transaction newTransaction = new Transaction(publicKey, _reciever, value, inputs);
        newTransaction.generateSignature(privateKey);

        for(TransactionInput i : inputs){
            UTXOs.remove(i.transactionOutputId);
        }

        return newTransaction;
    }
}
