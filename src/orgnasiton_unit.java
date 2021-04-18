import java.util.ArrayList;
import java.util.Arrays;

public class orgnasiton_unit
{
    //Variables that are to be used throughout class
    private String org_name;
    private float current_credits;
    private ArrayList<asset> org_assets;

    /**
     * Constructs the orgnaisation unit to keep track on current
     * asserts on sale furthermore keeping track of credits
     *
     * @param name (STRING) the name of which the orgnaisiton unit is to be
     *              referred to
     *
     * @param credits (FLOATS) the current value of the company
     *
     * @param initial_assets (asset[]) an array is to be feed in (it is fine
     *                       if null) and these are to be added to the organisation.
     *
     */
    public orgnasiton_unit(String name, float credits, asset[] initial_assets)
    {
        this.org_name = name;
        this.current_credits = credits;
        this.org_assets = new ArrayList<asset>();

        if(initial_assets != null)
        {
            this.org_assets.addAll(Arrays.asList(initial_assets));
        }
    }
}
