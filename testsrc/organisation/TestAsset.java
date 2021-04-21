package organisation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestAsset {
    Asset asset;

    @BeforeEach
    void setup()
    {
        asset = new Asset("asset name", 300, 5);
    }

    // Test if asset name is correct

    @Test
    public void testAssetName()
    {
        assertEquals("asset name", asset.nameOfAsset);
    }

    // Test if organisation has positive asset value

    @Test
    public void testPositiveAssetValue()
    {
        assertEquals(300, asset.indPrice);
    }

    // Test if organisation has positive asset quantity

    @Test
    public void testPositiveAssetQTY()
    {
        assertEquals(5, asset.numAvailable);
    }

}
