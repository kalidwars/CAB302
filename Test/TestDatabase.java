//Import Local APIs
import COMMON.*;
import Server.*;
import CustomExceptions.*;
import Client.*;

//Import Testing API's and Exceptions
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;


public class TestDatabase
{
    private BuyOrder test_case_1_B;
    private SellOrder test_case_2_S;
    private OrganisationUnit OU_test;
    private User test_Case_1;
    private AdminUser test_Case_2;
    private DataSource test;
    private StockMarket testStock;

    @BeforeEach
    public void SetUp() throws StockExceptions, NoSuchPaddingException, NoSuchAlgorithmException, SQLException
    {
        test = new DataSource();
        OU_test = new OrganisationUnit("Basecase",100,null);
        testStock = new StockMarket();
        test_Case_1 = new User("NormalUser","PW",OU_test);
        test_Case_2 = new AdminUser("root","root");
        test_case_1_B = new BuyOrder("Test",0.69,6,test_Case_1);
        test_case_2_S = new SellOrder("test2", 55,78,test_Case_2);
    }

    @Test
    public void DatabaseConnection() throws SQLException
    {
        test.UNITTESTING();
        assertDoesNotThrow(() -> new DataSource());
    }

    /**
    @Test
    @DisplayName("Add asset to database/Server")
    public void AddAssetToDB() throws StockExceptions {
        Asset Asset1 = new Asset("Test Asset 1",100.0,30,SellUser);
        ServerConnection test = new ServerConnection();
        test.AddAsset(Asset1);
    }
    @Test
    @DisplayName("Remove asset from database/Server")
    public void RemoveAssetFromDB() throws StockExceptions {
        Asset Asset1 = new Asset("Test Asset 1",100.0,30,BuyUser);
        ServerConnection test = new ServerConnection();
        test.RemoveAsset(Asset1);
    }
    @Test
    @DisplayName("Get Asset from database/Server")
    public void GetAssetFromServer()
    {
        ServerConnection test = new ServerConnection();
        ArrayList<Asset> testassets = test.GetAssets("Buys");
    }
     */

    @Test
    public void Uploading_DownLoading_OU() throws StockExceptions, SQLException
    {
        test.UNITTESTING();

        ArrayList<OrganisationUnit> checkOU = new ArrayList<OrganisationUnit>();

        OU_test.Upload();

        checkOU = test.convertToOU();

        OrganisationUnit critera = checkOU.get(0);

        //Check Values are correct
        assertEquals("Basecase",critera.orgName());
        assertEquals(100.0,critera.currentCredits());

    }
    @Test
    void Uploading_DownLoading_Users() throws SQLException, NoSuchPaddingException,NoSuchAlgorithmException, StockExceptions
    {
        //This needs to be done to clear table and to allow for primary keys
        test.UNITTESTING();
        OU_test.Upload();
        OrganisationUnit ADMIN = new OrganisationUnit("ADMINS", 0, null);
        ADMIN.Upload();

        //Test Case 1
        ArrayList<User> check1 = new ArrayList<User>();
        try
        {
            test_Case_1.Upload();
            test_Case_2.Upload();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        try
        {
            check1 = test.convertToUsers(testStock);
        }
        catch (SQLException e2)
        {
            e2.printStackTrace();
        }

        //Check Values are correct
        assertEquals("NormalUser",check1.get(0).GetUserID());
        assertEquals("root",check1.get(1).GetUserID());

        //Check Typing is correct
        assertTrue(check1.get(0) instanceof User);
        assertTrue(check1.get(1) instanceof AdminUser);


    }

}
