import java.security.PublicKey;

public class TransactionOutput {
    public String id;
    public PublicKey reciever;
    public float value;
    public String parentTransactionId;


    public TransactionOutput(PublicKey reciever,float value, String parentTransactionId){
        this.reciever = reciever;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(reciever)+Float.toString(value)+parentTransactionId);

    }
    public boolean isMine(PublicKey publicKey){
        return (publicKey == reciever);
    }
}
