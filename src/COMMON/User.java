package COMMON;
import Server.*;

//Needed Library
import java.sql.*;
import java.io.*;
import java.io.Serial;
import java.io.Serializable;



public class User implements Serializable
{
    @Serial
    private static final long serialVersionUID = -2393708718755176852L;
    //User Variables
    private String ID;
    //private String PassWord;
    private SerialData privatePassWord;
    private OrganisationUnit OU_OWNER;

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
        this.privatePassWord = new SerialData(passWord);
    }

    public SerialData GetPassword() {
        return privatePassWord;
    }


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
        //return this.privatePassWord.getHiddenData();
        return "test";
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
            //UPLOADING.setString(2,this.PassWord);
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

    public void SetPassword(String newPass) {
        this.privatePassWord = new SerialData(newPass);
    }
}
