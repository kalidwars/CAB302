//Import Local APIs
import COMMON.*;
import Server.*;
import CustomExceptions.*;
import Client.*;

//Import Testing API's and Exceptions
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;


public class TestDatabase
{
    private BuyOrder test_case_1;
    private SellOrder test_case_2;
    private OrganisationUnit OU;

    @BeforeEach
    public void SetUp() throws StockExceptions
    {
        this.test_case_1 = new BuyOrder("Test",0.69,6,"Test_user");
        this.test_case_2 = new SellOrder("test2", 55,78,"test_user_2");
        OU = new OrganisationUnit("Admin",0,null);
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
