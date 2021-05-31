package PROGRAM;

import PROGRAM.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Security;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

public class User
{
    private String ID;
    //Private Variables used for encryption and decryption
    private Cipher internallCipher;
    //Setup Key
    private byte[] keyBytes = new byte[]{11,0,5,50,100,60,78,55};
    private String SecretAlgorithm = "EnCrYpTeD";
    private SecretKeySpec GeneratedKey = new SecretKeySpec(keyBytes, SecretAlgorithm);
    /**
     *
     * User class for log in and
     *
     * @param id - Raw String id (Initial.Lastname (i.e. Hugh Glas => H.Glas)
     * @param passWord - Encrypted Password String
     * @param ParentUnit - The Unit the user can be
     *
     * @author Hugh Glas
     *
     * @version 1.1
     *
     */
    public User(String id, String passWord, OrganisationUnit ParentUnit) throws NoSuchPaddingException, NoSuchAlgorithmException
    {
        internallCipher = Cipher.getInstance("DES/CBC/NoPadding");
    }

    /**
     *
     * Method to Complete PassWord Encryption
     *
     * @param RawPassword - String PassWord Pass through
     * @return (BYTE) Encrypted Password array
     *
     * @exception InvalidKeyException - is thrown when a key isn't generated correctly
     *            BadPaddingException - is thrown when a variable size is passed
     *            IllegalBlockSizeException - is thrown when a vairable size is incorrect
     *
     * @version 1.0
     *
     * @author Hugh Glas
     */
    public byte[] PassWordEncryption(String RawPassword) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        //Initialise Encryption Mode of cipher
        internallCipher.init(Cipher.ENCRYPT_MODE,GeneratedKey);
        //Get into acceptable passable variables
        byte[] plainText = RawPassword.getBytes(StandardCharsets.UTF_8);
        byte[] CipherText = internallCipher.doFinal(plainText);
        return CipherText;
    }

    /**
     *
     * Method In Which Proves the password is correct
     *
     * @param PasswordAttempt - RawString Password to check against the actual password
     * @return (BOOLEAN) - True if Password is correct
     *                  - False if password is incorrect
     */
    public Boolean PassWordCorrect(String PasswordAttempt)
    {
        return null;
    }
}
