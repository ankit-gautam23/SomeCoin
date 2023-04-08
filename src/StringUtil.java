import java.security.*;
import java.util.Base64;

public class StringUtil {
    public static String applySha256(String input){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer(); // Hexadecimal Hash
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static String getStringFromKey(Key key){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static byte[] applyEDSASASign(PrivateKey privateKey, String input){
        Signature dsa;
        byte[] output = new byte[0];
        try{
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            byte[] realSign = dsa.sign();
            output = realSign;
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
        return output;
    }

    public static boolean verifyECDSASign(PublicKey publicKey, String data, byte[] sign){
        try{
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(sign);
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
