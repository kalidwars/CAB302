import COMMON.*;
import Client.ServerConnection;
import CustomExceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class TestAsset {
    BuyOrder Test1;
    SellOrder Test2;
    User BuyUser;
    User SellUser;
    OrganisationUnit TestUnit;
    Asset asset;

    @BeforeEach
    void setup() throws StockExceptions, IOException
    {
        TestUnit = new OrganisationUnit("Buys",0,null);
        BuyUser = new User("BuyTest","PW", TestUnit);
        SellUser = new User("SellTest","PW",TestUnit);
        Test1 = new BuyOrder("asset name", 300, 5,BuyUser);
        Test2 = new SellOrder("asset name 2",550,6,SellUser);
        asset = new Asset("Test Asset 1",100.0,10,BuyUser);
    }

    // Test if asset name is correct

    @Test
    public void testUserName()
    {
        assertEquals("BuyTest",Test1.GetUser());
        assertEquals("SellTest",Test2.GetUser());
    }

    @Test
    public void testAssetName()
    {
        assertEquals("asset name", Test1.GetName());
        assertEquals("asset name 2",Test2.GetName());
    }

    // Test if organisation has positive asset value

    @Test
    public void testPositiveAssetValue()
    {
        assertEquals(300, Test1.getIndPrice());
        assertEquals(550, Test2.getIndPrice());
    }

    // Test if organisation has positive asset quantity

    @Test
    public void testPositiveAssetQTY()
    {
        assertEquals(5, Test1.getNumAvailable());
        assertEquals(6,Test2.getNumAvailable());
    }

    //Test that both a false value is returned
    // when an incorrect adjustment to number or value is made
    // Test that the value hasn't been overwritten
    @Test
    public void testNegativeZeroValues()
    {
        //QTY TEST
        assertEquals(false,Test1.adjustQTY(0));
        assertEquals(false,Test1.adjustQTY(-2));
        assertEquals(5, Test1.getNumAvailable());

        assertEquals(false, Test2.adjustQTY(0));
        assertEquals(false, Test2.adjustQTY(-4));
        assertEquals(6, Test2.getNumAvailable());

        //Value
        assertEquals(false,Test1.adjustValue(0));
        assertEquals(false,Test1.adjustValue(-60));
        assertEquals(300,Test1.getIndPrice());

        assertEquals(false,Test2.adjustValue(0));
        assertEquals(false,Test2.adjustValue(-666));
        assertEquals(550,Test2.getIndPrice());
    }

    //A test designed to check if the sale price is passing correctly
    @Test
    public void testSellPrice()
    {
        //Variables to use
        int testCase1 = 2;
        int testCase2 = 7;

        assertEquals(testCase1*300,Test1.GetValue(testCase1));
        assertEquals(testCase2*550,Test2.GetValue(testCase2));
    }

    @Test
    public void testOUID()
    {
        assertEquals("Buys",Test1.GetOUID());
        assertEquals("Buys",Test2.GetOUID());
    }

    @Test
    public void ExceptionThrowing() throws IOException
    {
        User UserExcep = new User("Exception","asdf",TestUnit);
        assertThrows(StockExceptions.class, () -> {
            BuyOrder Test1 = new BuyOrder("Test1", -2,50,UserExcep);
        });
        assertThrows(StockExceptions.class, () -> {
            SellOrder Test2 = new SellOrder("Test2",0,-10,UserExcep);
        });
        assertThrows(StockExceptions.class, () -> {
            BuyOrder Test3 = new BuyOrder("Test3",6,0,UserExcep);
        });
    }
    @Test
    @DisplayName("Testing adding AssetName to the asset register in db")
    public void AddAssetNametoDB() {
        ServerConnection testConnection = new ServerConnection();
        assertEquals(true,testConnection.AddAssetName(asset.GetName()));
    }
    @Test
    @DisplayName("Testing adding assets to the database")
    public void AddAssetToDB() throws StockExceptions {
        ServerConnection testConnection = new ServerConnection();
        testConnection.AddOU(TestUnit);
        testConnection.AddUser(BuyUser);
        assertEquals(true,testConnection.AddAsset(asset));
    }

    @Test
    @DisplayName("Testing removing assets from the database")
    public void RemoveAssetFromDB() throws StockExceptions {
        ServerConnection testConnection = new ServerConnection();
        assertEquals(true,testConnection.RemoveAsset(new Asset("Test Asset 1",100.0,10,BuyUser)));
    }

    @Test
    @DisplayName("Testing retrieving assets from the database")
    public void RetrieveAssetsFromDB() throws StockExceptions {
        ServerConnection testConnection = new ServerConnection();
        Asset asset = new Asset("Test Asset 1",100.0,10,BuyUser);
        testConnection.AddAsset(asset);
        ArrayList<Asset> testArrayList = new ArrayList<>();
        testArrayList = testConnection.GetAssets(BuyUser);
        assertNotNull(testArrayList);
    }


}