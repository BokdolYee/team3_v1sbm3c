package dev.mvc.tool;
 
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
 
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
 
import org.apache.commons.codec.binary.Base64;
 
public class Security {
  private String iv;
  private Key keySpec;
 
  // 키 생성
  public Security() {
    try {
      // String key = "soldeskAES256KEY"; // key는 16자, 123 -> spyryuhFvhnnv6cD2ROB8g==
      String key = "soldeskAES256235";    // key는 16자, 123 -> KUxUbf+4VrlpsFZ3tSAKdw==
      this.iv = key.substring(0, 16);
   
      byte[] keyBytes = new byte[16];
      byte[] b = key.getBytes("UTF-8");
      int len = b.length;
      if (len > keyBytes.length)
        len = keyBytes.length;
      System.arraycopy(b, 0, keyBytes, 0, len);
      SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
   
      this.keySpec = keySpec;
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }
 
  // 암호화
  public String aesEncode(String str) {    
    String enStr = "";
    
    try {
      Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
      c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
   
      byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
      enStr = new String(Base64.encodeBase64(encrypted));
    } catch(UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch(NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch(NoSuchPaddingException e) {
      e.printStackTrace();
    } catch(InvalidKeyException e) {
      e.printStackTrace();
    } catch(InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    } catch(IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch(BadPaddingException e) {
      e.printStackTrace();
    }
 
    return enStr;
  }
  
  // 복호화
  public String aesDecode(String str)
      throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException,
      InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
    c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes("UTF-8")));
  
    byte[] byteStr = Base64.decodeBase64(str.getBytes());
  
    return new String(c.doFinal(byteStr), "UTF-8");
  }
  
 
//  // 복호화
//  public String aesDecode(String str)
//      throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException,
//      InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
//    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
//    c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes("UTF-8")));
// 
//    byte[] byteStr = Base64.decodeBase64(str.getBytes());
// 
//    return new String(c.doFinal(byteStr), "UTF-8");
//  }
 
//  public static void main(String[] args) throws Exception {
//    AES256Util aes256 = new AES256Util();
// 
//    String text = "가나다 123 ABC !@#";
//    // String text = "123"; // spyryuhFvhnnv6cD2ROB8g==
//    System.out.println("암호화할 문자 : " + text);
//    
//    String encText = aes256.aesEncode(text);      // 암호화
//    System.out.println("암호화된 문자(DBMS 저장) : " + encText);
//    
//    String decText = aes256.aesDecode(encText); // 복호화
//    System.out.println("복호화된 문자 : " + decText);
//    
//    System.out.println("새로운 문자열 다시 암호화");
//    String text2 = "가나다 123 ABC !@#";
//    String encText2 = aes256.aesEncode(text);      // 암호화
//    System.out.println("암호화된 문자(DBMS 저장) : " + encText2);
//    
//    // 비교시 복호화가 아니라 암호화를하여 비교
//    if (encText.equals(encText2)) {
//      System.out.println("암호화를하여 비교했으며 동일한 패스워드임.");
//    }
// 
//  }
 
}

