package COMMON;

import Server.DBConnection;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminUser extends COMMON.User
{

    //SQL Variables
    private Connection connection;
    private static final String upload_statement =
            "INSERT INTO users (username, password, privilege, orgunit) VALUES (?, ?, ?, ?)";
    private PreparedStatement UPLOADING;

    public AdminUser(String id, String passWord) throws NoSuchAlgorithmException, NoSuchPaddingException
    {
        super(id, passWord, null);
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
    @Override
    public boolean Upload()
    {
        boolean toReturn = true;
        connection = DBConnection.getInstance();

        try
        {
            UPLOADING = connection.prepareStatement(upload_statement);
            UPLOADING.setString(1, this.GetUserID());
            UPLOADING.setString(2,this.RawPassword());
            UPLOADING.setString(3,String.valueOf(true));
            UPLOADING.setString(4,"ADMINS");
            UPLOADING.executeQuery();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            toReturn = false;
        }

        return toReturn;
    }



    public void promote_user()
    {

    }

    public void decrease_user()
    {

    }
}
