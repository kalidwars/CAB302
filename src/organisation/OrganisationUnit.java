package organisation;

import java.util.ArrayList;
import java.util.Arrays;

public class OrganisationUnit
{
    //Variables that are to be used throughout class
    public String orgName;
    public float currentCredits;
    public ArrayList<Asset> orgAssets;

    /**
     * Constructs the orgnaisation unit to keep track on current
     * asserts on sale furthermore keeping track of credits
     *
     * @param name (STRING) the name of which the orgnaisiton unit is to be
     *              referred to
     *
     * @param credits (FLOATS) the current value of the company
     *
     * @param initialAssets (asset[]) an array is to be feed in (it is fine
     *                       if null) and these are to be added to the organisation.
     *
     */
    public OrganisationUnit(String name, float credits, Asset[] initialAssets)
    {
        this.orgName = name;
        this.currentCredits = credits;
        this.orgAssets = new ArrayList<Asset>();

        if(initialAssets != null)
        {
            this.orgAssets.addAll(Arrays.asList(initialAssets));
        }
    }
}
