package COMMON;
import Server.*;

//Needed Library
import java.sql.*;
import java.io.*;

//May be Deleted later
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
    //User Variables
    private String ID;
    private String PassWord;
    private Serial privatePassWord;
    private OrganisationUnit OU_OWNER;

    //Private Variables used for encryption and decryption
    //private Cipher internallCipher;
    //Setup Key
    private byte[] keyBytes = new byte[]{11,0,5,50,100,60,78,55};
    private String SecretAlgorithm = "EnCrYpTeD";
    private SecretKeySpec GeneratedKey = new SecretKeySpec(keyBytes, SecretAlgorithm);

    //SQL Variables
    private Connection connection;
    private static final String upload_statement =
            "INSERT INTO users (username, password, privilege, orgunit) VALUES (?,?,?,?);";
    private PreparedStatement UPLOADING;



    /**
     *
     * User class for log in and
     *
     * @param id - Raw String id (Initial.Lastname (i.e. Hugh Glas => H.Glas)
     * @param passWord - Encrypted Password String
     * @param ParentUnit - The Unit the user can be selling stuff in
     *
     * @author Hugh Glas
     *
     * @version 1.1
     *
     */
    public User(String id, String passWord, OrganisationUnit ParentUnit) throws IOException {
        this.ID = id;
        this.OU_OWNER = ParentUnit;
        this.privatePassWord = new Serial(passWord);
        privatePassWord.EncryptPassword();
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
    //public byte[] PassWordEncryption(String RawPassword) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    //{
        //Initialise Encryption Mode of cipher
        //internallCipher.init(Cipher.ENCRYPT_MODE,GeneratedKey);
        //Get into acceptable passable variables
        //byte[] plainText = RawPassword.getBytes(StandardCharsets.UTF_8);
        //byte[] CipherText = internallCipher.doFinal(plainText);
        //return CipherText;
    //}

    /**
     *
     * Simple Method to return the ID string
     *
     * @return (STRING) Raw string of the ID
     *
     * @version 1.0
     *
     * @Author Hugh
     */
    public String GetUserID()
    {
        return this.ID;
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

    public String RetrivePassword()
    {
        return this.privatePassWord.getHiddenData();
    }

    public String OUID_Owner()
    {
        if(this.OU_OWNER == null)
            return "ADMINS";
        else
            return this.OU_OWNER.orgName();
    }

    /**
     *
     * Uploading method for User (this will be overwritten in Admin User
     *
     * @return (BOOLEAN)    True - if upload occurred correctly
     *                      False - if upload occurred incorrectly
     *
     * @version 1.0
     *
     * @author Hugh
     *
     */
    public boolean Upload() throws SQLException
    {
        //Create Pass Fail variable boolean
        boolean toReturn = true;

        //Set UP Connection Information to server
        connection = DBConnection.getInstance();

        try
        {
            UPLOADING = connection.prepareStatement(upload_statement);
            UPLOADING.setString(1,this.ID);
            UPLOADING.setString(2,this.PassWord);
            UPLOADING.setString(3,String.valueOf(false));
            UPLOADING.setString(4,this.OU_OWNER.orgName());
            UPLOADING.executeQuery();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            toReturn = false;
        }

        return toReturn;
    }
}
