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
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

//Alot of these exceptions are listed in the TestAsset class

public class TestOrders {
    private OrganisationUnit SellingOrg;
    private OrganisationUnit BuyingOrg;
    private BuyOrder buyOrder;
    private SellOrder sellOrder;
    private User SellingUser;
    private User BuyingUser;
    private ServerConnection testconneciton;
    @BeforeEach
    void SetUp() throws IOException, StockExceptions, SQLException, InterruptedException {
        SellingOrg = new OrganisationUnit("SellingOrg",1000,null);
        BuyingOrg = new OrganisationUnit("BuyingOrg",1000,null);
        BuyingUser = new User("Buying User","BuyPass",BuyingOrg);
        SellingUser = new User("Selling User", "SellPass", SellingOrg);
        testconneciton = new ServerConnection();
        testconneciton.AddAssetName("CPU HOUR");
        TimeUnit.SECONDS.sleep(1);
        testconneciton = new ServerConnection();
        testconneciton.AddOU(SellingOrg);
        TimeUnit.SECONDS.sleep(1);
        testconneciton = new ServerConnection();
        testconneciton.AddOU(BuyingOrg);
        TimeUnit.SECONDS.sleep(1);
        testconneciton = new ServerConnection();
        testconneciton.AddUser(BuyingUser);
        TimeUnit.SECONDS.sleep(1);
        testconneciton = new ServerConnection();
        testconneciton.AddUser(SellingUser);
        TimeUnit.SECONDS.sleep(1);
    }
    @Test
    @DisplayName("Creating Buy order and storing in DB")
    public void SendBuyOrder() throws StockExceptions, InterruptedException {
        testconneciton = new ServerConnection();
        buyOrder = new BuyOrder("CPU HOUR",10,20,BuyingUser.GetUserID());
        testconneciton.AddOrder(buyOrder);
        //TimeUnit.SECONDS.sleep(1);
    }
    @Test
    @DisplayName("Creating Buy order and storing in DB")
    public void SendSellOrder() throws StockExceptions {
        testconneciton = new ServerConnection();
        sellOrder = new SellOrder("CPU HOUR",10,20,SellingUser.GetUserID());
        testconneciton.AddOrder(sellOrder);
    }
}
