import Client.ServerConnection;
import CustomExceptions.*;
import COMMON.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class TestOrganisationUnit
{
    private OrganisationUnit TestUnit = new OrganisationUnit("Test",0,null);
    private OrganisationUnit organisationUnit;
    private OrganisationUnit organisationUnit_withAssets;
    private OrganisationUnit Uploading;
    private Asset[] AssetTest = new Asset[2];
    private User Test;
    private BuyOrder SubTest1;
    private SellOrder SubTest2;

    public TestOrganisationUnit() throws StockExceptions
    {

    }

    @BeforeEach
    void setup() throws StockExceptions, IOException
    {
            Test = new User("Test", "PW", TestUnit);
            SubTest1 =  new BuyOrder("Chip", 5, 8, Test.GetUserID());
            SubTest2 = new SellOrder("GPU", 8.8, 420, Test.GetUserID());
            organisationUnit = new OrganisationUnit("organisation name", 10000, null);
            AssetTest[0] = (SubTest1);
            AssetTest[1] = (SubTest2);
            organisationUnit_withAssets = new OrganisationUnit("org_name", 50, AssetTest);

    }

    // Test if organisations name is correct

    @Test
    public void testOrgName()
    {
        assertEquals("organisation name", organisationUnit.orgName());
    }

    // Test if organisation has positive credit value

    @Test
    public void testPositiveCreditValue()
    {
        assertEquals(10000, organisationUnit.currentCredits());
    }

    // Test if organisation has any assets

    @Test
    public void testNullAssets()
    {
        assertEquals(Collections.emptyList(), organisationUnit.getAllAssets());
    }

    // Test if all assets is present when called

    @Test
    public void testAssets()
    {
        assertEquals(Arrays.asList(AssetTest),organisationUnit_withAssets.getAllAssets());
    }

    //Test Exception Throwing
    @Test
    public void testException() throws StockExceptions
    {
        assertThrows(StockExceptions.class, () -> {
            OrganisationUnit ExceptionTest = new OrganisationUnit("Exception",-20,AssetTest);
        });
    }

    @Test
    @DisplayName("Testing Adding an OU to the database via server")
    public void AddOUToServer() {
        ServerConnection testConnection = new ServerConnection();
        assertEquals(true,testConnection.AddOU(organisationUnit));
    }
    @Test
    @DisplayName("Removing an OU from the database via server")
    public void RemoveOUFromServer() {
        ServerConnection testConnection = new ServerConnection();
        assertEquals(true,testConnection.RemoveOU(organisationUnit));
    }
    @Test
    @DisplayName("Get a list of OU's from the server")
    public void GetOUsFromServer() {
        ServerConnection testConnection = new ServerConnection();
        ArrayList<OrganisationUnit> testOUList = testConnection.GetOUs();
        assertNotNull(testOUList);
    }
    @Test
    @DisplayName("Get a list of OU's from the server")
    public void EditOUCredits() {
        ServerConnection testConnection = new ServerConnection();
        testConnection.AddOU(organisationUnit);
        assertEquals(true, testConnection.EditOU(organisationUnit, 3001.0));
    }

}