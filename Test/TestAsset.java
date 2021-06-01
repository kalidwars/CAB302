import COMMON.*;
import CustomExceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;


public class TestAsset {
    BuyOrder Test1;
    SellOrder Test2;
    User BuyUser;
    User SellUser;
    OrganisationUnit TestUnit;

    @BeforeEach
    void setup() throws StockExceptions, NoSuchAlgorithmException, NoSuchPaddingException
    {
        TestUnit = new OrganisationUnit("Buys",0,null);
        BuyUser = new User("BuyTest","PW", TestUnit);
        SellUser = new User("SellTest","PW",TestUnit);
        Test1 = new BuyOrder("asset name", 300, 5,BuyUser);
        Test2 = new SellOrder("asset name 2",550,6,SellUser);
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
    public void ExceptionThrowing() throws StockExceptions, NoSuchAlgorithmException, NoSuchPaddingException
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

}