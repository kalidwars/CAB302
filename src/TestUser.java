//Import main library (also custom libraries)
import COMMON.*;
import CustomExceptions.*;

//Import SQL API
import Server.DataSource;
import java.sql.SQLException;
import Client.ServerConnection;

//IMPORT JUIN API
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

//IMPORT java
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class TestUser
{
    private OrganisationUnit OU_test;
    private User test_Case_1;
    private AdminUser test_Case_2;
    private DataSource test;

    @BeforeEach
    void SetUp() throws NoSuchAlgorithmException, NoSuchPaddingException, StockExceptions
    {
        OU_test = new OrganisationUnit("Basecase",100,null);
        test_Case_1 = new User("NormalUser","PW",OU_test);
        test_Case_2 = new AdminUser("root","root");
        test = new DataSource();
    }

    @Test
    void TestUserID()
    {
        //Test Case 1
        assertEquals("NormalUser",test_Case_1.GetUserID());
        //Test Case 2
        assertEquals("root",test_Case_2.GetUserID());
    }
    /**
    @Test
    public void TestEncryption()
    {

    }

    @Test
    public void TestDecryption()
    {

    }
    */
    @Test
    void Uploading_DownLoading_Users() throws SQLException, NoSuchPaddingException,NoSuchAlgorithmException
    {
        //Test Case 1
        ArrayList<User> check1 = new ArrayList<User>();
        ServerConnection serverConnection = new ServerConnection();
        try
        {
            test_Case_1.Upload();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        try
        {
            check1 = test.getUser();
        }
        catch (SQLException e2)
        {
            e2.printStackTrace();
        }

        assertTrue(check1.contains(test_Case_1));

    }
}
