package organisation;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestOrganisationUnit {
    OrganisationUnit organisationUnit;


    @BeforeEach
    void setup()
    {
        organisationUnit = new OrganisationUnit("organisation name", 10000, null);
    }

    // Test if organisations name is correct

    @Test
    public void testOrgName()
    {
        assertEquals("organisation name", organisationUnit.orgName);
    }

    // Test if organisation has positive credit value

    @Test
    public void testPositiveCreditValue()
    {
        assertEquals(10000, organisationUnit.currentCredits);
    }

    // Test if organisation has any assets

    @Test
    public void testNullAssets()
    {
        // assertEquals(null, organisationUnit.orgAssets);
    }
}
