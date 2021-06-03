//Import main library (also custom libraries)
import COMMON.*;
import CustomExceptions.*;

//Import SQL API
import Server.DataSource;

import java.io.IOException;
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
    private StockMarket testStock;

    /**
     * SETUPS MARKET TEST CA
     *
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws StockExceptions
     * @throws SQLException
     */
    @BeforeEach
    void SetUp() throws IOException, StockExceptions, SQLException
    {
        OU_test = new OrganisationUnit("Basecase",100,null);
        test_Case_1 = new User("NormalUser","PW",OU_test);
        test_Case_2 = new AdminUser("root","root");
    }

    @Test
    void TestUserID()
    {
        //Test Case 1
        assertEquals("NormalUser",test_Case_1.GetUserID());
        //Test Case 2
        assertEquals("root",test_Case_2.GetUserID());
    }

    @Test
    void TestOUID()
    {
        //Test Case 1
        assertEquals("Basecase",test_Case_1.OUID_Owner());
        //Test Case 2
        assertEquals("ADMINS",test_Case_2.OUID_Owner());
    }

    @Test
    void TestEncryption()
    {
        assertNotEquals("root",test_Case_2.RetrivePassword());
    }

}
