//Import main library (also custom libraries)
import COMMON.*;
import Client.ServerConnection;
import CustomExceptions.*;

//Import SQL API

import java.io.IOException;
import java.sql.SQLException;

//IMPORT JUIN API
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
        test_Case_1 = new User("NormalUser","ThisIsMyPassword",OU_test);
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
    @DisplayName("Add user to database")
    public void AddUsertoDB() {
        ServerConnection testConnection = new ServerConnection();
        testConnection.AddOU(OU_test);
        assertEquals(true,testConnection.AddUser(test_Case_1));
    }
    @Test
    @DisplayName("Add Admin user to database")
    public void AddAdminUsertoDB() {
        ServerConnection testConnection = new ServerConnection();
        testConnection.AddOU(OU_test);
        assertEquals(true,testConnection.AddAdminUser(test_Case_2));
    }
    @Test
    @DisplayName("Get Users from database")
    public void GetUser() {
        ServerConnection testConnection = new ServerConnection();
        ArrayList<User> users = testConnection.GetUsers();
        assertNotNull(users);
    }
    @Test
    @DisplayName("Remove user from database")
    public void RemoveUserFromDB() {
        ServerConnection testConnection = new ServerConnection();
        assertEquals(true,testConnection.RemoveUser(test_Case_1));
    }
    @Test
    @DisplayName("Edit user in database")
    public void EditUser() {
        ServerConnection testConnection = new ServerConnection();
        assertEquals(true,testConnection.EditUser(test_Case_1, "NewPasswordIsThis"));
    }


}
