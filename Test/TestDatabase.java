//Import Local APIs
import COMMON.*;
import Server.*;
import CustomExceptions.*;
import Client.*;

//Import Testing API's and Exceptions
import jdk.jfr.Name;
import org.junit.jupiter.api.*;

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
    private User BuyUser;
    private User SellUser;
    private OrganisationUnit TestUnit;
    private SellOrder Test2;

    @BeforeEach
    public void SetUp() throws StockExceptions, IOException, SQLException
    {
        test = new DataSource();
        OU_test = new OrganisationUnit("Basecase",100,null);
        testStock = new StockMarket();
        TestUnit = new OrganisationUnit("Buys",0,null);
        test_Case_1 = new User("NormalUser","PW",OU_test);
        test_Case_2 = new AdminUser("root","root");
        BuyUser = new User("BuyTest","PW", TestUnit);
        SellUser = new User("SellTest","PW",TestUnit);
        test_case_1_B = new BuyOrder("Test",0.69,6,test_Case_1.GetUserID());
        test_case_2_S = new SellOrder("test2", 55,78,test_Case_2.GetUserID());
        Test2 = new SellOrder("asset name 2",550,6,SellUser.GetUserID());
    }

    @Test
    @DisplayName("Database Connection")
    public void test_1 () throws SQLException
    {
        test.UNITTESTING();
        assertDoesNotThrow(() -> new DataSource());

    }

    @Test
    @DisplayName("Uploading_DownLoading_OU")
    public void test_2() throws StockExceptions, SQLException
    {
        test.UNITTESTING();

        ArrayList<OrganisationUnit> checkOU = new ArrayList<OrganisationUnit>();

        //OU_test.Upload();

        checkOU = test.GetAllOU();

        OrganisationUnit critera = checkOU.get(0);

        //Check Values are correct
        assertEquals("Basecase",critera.orgName());
        assertEquals(100.0,critera.currentCredits());

    }

    @Test
    @DisplayName("Uploading_DownLoading_Users")
    void test_3() throws SQLException, IOException, StockExceptions
    {
        //This needs to be done to clear table and to allow for primary keys
        test.UNITTESTING();
        //OU_test.Upload();
        OrganisationUnit ADMIN = new OrganisationUnit("ADMINS", 0, null);
        //ADMIN.Upload();

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

    @Test
    @DisplayName("AddAssetToDB")
    public void test_4() throws StockExceptions {
        Asset Asset1 = new Asset("Test Asset 1",100.0,30,SellUser.GetUserID());
        ServerConnection test = new ServerConnection();
        test.AddAsset(Asset1);
    }


    @Test
    @DisplayName("RemoveAssetFromDB")
    public void test_5() throws StockExceptions {
        Asset Asset1 = new Asset("Test Asset 1",100.0,30,BuyUser.GetUserID());
        ServerConnection test = new ServerConnection();
        test.RemoveAsset(Asset1);
    }

    @Test
    @DisplayName("GetAssetFromServer")
    public void test_6() throws SQLException
    {
        ServerConnection test = new ServerConnection();
        test.AddAsset(Test2);
        ArrayList<Asset> testassets = test.GetAssets(BuyUser);
    }

    @Test
    @DisplayName("TestEncryption")
    void test_7() throws IOException, SQLException {
        test_Case_2.Upload();
    }

    @Test
    @DisplayName("Test Trade Implmentation")
    void test_8() throws SQLException
    {

    }

}
