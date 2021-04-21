import PROGRAM.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestAsset {
    BuyOrder Test1;
    SellOrder Test2;

    @BeforeEach
    void setup()
    {
        Test1 = new BuyOrder("asset name", 300, 5);
        Test2 = new SellOrder("asset name 2",550,6);
    }

    // Test if asset name is correct

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
        assertEquals(300, Test1.getInd_price());
        assertEquals(550, Test2.getInd_price());
    }

    // Test if organisation has positive asset quantity

    @Test
    public void testPositiveAssetQTY()
    {
        assertEquals(5, Test1.get_Num_available());
        assertEquals(6,Test2.get_Num_available());
    }

}