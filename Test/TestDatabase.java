//Import Local APIs
import COMMON.*;
import Server.*;
import CustomExceptions.*;
import Client.*;

//Import Testing API's and Exceptions
import org.junit.jupiter.api.*;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;


public class TestDatabase
{
    private BuyOrder test_case_1;
    private SellOrder test_case_2;
    private OrganisationUnit OU;
    private User User_1;
    private User User_2;

    @BeforeEach
    public void SetUp() throws StockExceptions, NoSuchPaddingException, NoSuchAlgorithmException
    {
        OU = new OrganisationUnit("Admin",0,null);
        User_1 = new User("Test_user","PW",OU);
        User_2 = new User("test_user_2","PW",OU);
        this.test_case_1 = new BuyOrder("Test",0.69,6,User_1);
        this.test_case_2 = new SellOrder("test2", 55,78,User_2);
    }

    @Test
    public void DatabaseConnection()
    {
        DataSource dataSource = new DataSource();
    }

    @Test
    public void SendingAssets()
    {
        ServerConnection serverConnection = new ServerConnection();
        ArrayList<Asset> test = serverConnection.GetAssets(OU.orgName());
        for (Asset asset : test) {
            System.out.println(asset.GetName());
        }
    }

}
