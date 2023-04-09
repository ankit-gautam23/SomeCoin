import java.security.*;
import java.util.ArrayList;


public class Transaction {
    public String transactionId;
    public PublicKey sender;
    public PublicKey reciever;
    public float value;
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs){
        this.sender = from;
        this.reciever = to;
        this.value = value;
        this.inputs = inputs;
    }

    public boolean processTransaction(){
        if(verifySignature() == false){
            System.out.println("Signature verification Failed");
            return false;
        }
        for(TransactionInput i: inputs){
            i.UTXO = SomeCoin.UTXOs.get(i.transactionOutputId);
        }
        if(getInputValue() < SomeCoin.minimumTransaction){
            System.out.println("Transction input to small: "+ getInputValue());
            return false;
        }

        float leftOver = getInputValue() - value;
        transactionId = calculateHash();
        outputs.add(new TransactionOutput(this.reciever, value, transactionId));
        outputs.add(new TransactionOutput(this.sender, value, transactionId));

        for(TransactionOutput o : outputs){
            SomeCoin.UTXOs.put(o.id, o);
        }

        for(TransactionInput i: inputs){
            if(i.UTXO == null) continue;
            SomeCoin.UTXOs.remove(i.UTXO.id);
        }
        return true;
    }

    public float getInputValue(){
        float total = 0;
        for(TransactionInput i: inputs){
            if(i.UTXO == null) continue;
            total += i.UTXO.value;
        }
        return total;
    }

    public float getOutputValue(){
        float total = 0;
        for(TransactionOutput o: outputs){
            total += o.value;
        }
        return value;
    }

    private String calculateHash(){
        sequence++;
        return StringUtil.applySha256(StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciever) + Float.toString(value) + sequence);
    }
    public void generateSignature(PrivateKey privateKey){
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciever) + Float.toString(value);
        signature = StringUtil.applyEDSASASign(privateKey, data);
    }

    public boolean verifySignature(){
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciever) + Float.toString(value);
        return StringUtil.verifyECDSASign(sender, data, signature);
    }
}
