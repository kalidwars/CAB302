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



public class TestOrders {
    private OrganisationUnit SellingOrg;
    private OrganisationUnit BuyingOrg;
    private BuyOrder buyOrder;
    private SellOrder sellOrder;
    private User SellingUser;
    private User BuyingUser;
    ServerConnection testconneciton = new ServerConnection();
    @BeforeEach
    void SetUp() throws IOException, StockExceptions, SQLException, InterruptedException {
        SellingOrg = new OrganisationUnit("SellingOrg",1000,null);
        BuyingOrg = new OrganisationUnit("BuyingOrg",1000,null);
        BuyingUser = new User("Buying User","BuyPass",BuyingOrg);
        SellingUser = new User("Selling User", "SellPass", SellingOrg);
        testconneciton.AddAssetName("CPU HOUR");
        testconneciton = new ServerConnection();
        testconneciton.AddOU(SellingOrg);
        testconneciton = new ServerConnection();
        testconneciton.AddOU(BuyingOrg);
        testconneciton = new ServerConnection();
        testconneciton.AddUser(BuyingUser);
        testconneciton = new ServerConnection();
        testconneciton.AddUser(SellingUser);
    }
    @Test
    @DisplayName("Creating Buy order and storing in DB")
    public void SendBuyOrder() throws StockExceptions {
        buyOrder = new BuyOrder("CPU HOUR",10,20,BuyingUser);
        testconneciton.AddOrder(buyOrder);
    }
    @Test
    @DisplayName("Creating Buy order and storing in DB")
    public void SendSellOrder() throws StockExceptions {
        sellOrder = new SellOrder("CPU HOUR",10,20,SellingUser);
        testconneciton.AddOrder(sellOrder);
    }
}
