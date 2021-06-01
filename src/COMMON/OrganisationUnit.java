package COMMON;
import COMMON.*;

import CustomExceptions.*;

import java.util.ArrayList;
import java.util.Arrays;

public class OrganisationUnit
{
    //Variables that are to be used throughout class
    private String org_name;
    private double current_credits;
    private ArrayList<Asset> org_assets;
    private ArrayList<Trade> trade_history = new ArrayList<Trade>();

    /**
     * Constructs the orgnaisation unit to keep track on current
     * asserts on sale furthermore keeping track of credits
     *
     * @param name (STRING) the name of which the orgnaisiton unit is to be
     *              referred to
     *
     * @param credits (DOUBLE) the current value of the company
     *
     * @exception Exception an exception will be thrown when a negative value
     *                  is entered for initial credits
     *
     * @param initial_assets (asset[]) an array is to be feed in (it is fine
     *                       if null) and these are to be added to the organisation.
     *
     * @author Hugh Glas
     *
     * @version 1.1
     *
     * To Do
     *
     * - Throw an exception when a not list is passed through
     *
     */
    public OrganisationUnit(String name, double credits, Asset[] initial_assets) throws StockExceptions
    {
        this.org_name = name;

        if(credits < 0)
        {
            throw new StockExceptions("Expected a Positive Value or 0 value for credits");
        }
        else {
            this.current_credits = credits;
        }

        this.org_assets = new ArrayList<Asset>();

        if(initial_assets != null)
        {
            this.org_assets.addAll(Arrays.asList(initial_assets));
        }

    }

    /**
     *
     * Simply Returns the amount of credits within the organisation as a float
     *
     * @return (FLOAT) The number of credits said organisation has
     *
     * @author Hugh Glas
     *
     * @version 1.0
     *
     */
    public double currentCredits()
    {
        return this.current_credits;
    }

    /**
     *
     * Returns the name of the organisation unit
     *
     * @return (STRING) returns the name of the organisation
     *
     * @author Hugh Glas
     *
     * @version 1.0
     *
     */
    public String orgName()
    {
        return this.org_name;
    }

    /**
     *
     * This is to make a sale occur and add it to the orgnisation unit
     * Trade history the variable is to pass through the seller
     *
     * @param SELLER (orgnasition_unit) the unit in which the asset
     *               was sold from
     *
     * @param order_fulfilled (asset - PROGRAM.buy_order) The order in which is being
     *                       fufilled
     *
     * @param sale_taken (asset - PROGRAM.sell_order) The order in which is being used
     *                   to complete the PROGRAM.trade
     *
     * @return (boolean) To return if the process was completed
     *                  fully
     *
     *  @author Hugh Glas
     *
     *  @version 1.0
     *
     */
    
    private boolean Complete_Sale(OrganisationUnit SELLER, BuyOrder order_fulfilled, SellOrder sale_taken)
    {
        boolean completed_sucessfully;
        Trade to_be_added = new Trade(SELLER,sale_taken.GetUser(),order_fulfilled);

        int num_brought = order_fulfilled.getNumAvailable();
        int current_available = sale_taken.getNumAvailable();

        completed_sucessfully = sale_taken.adjustQTY(current_available - num_brought);

        if(completed_sucessfully)
        {
            this.trade_history.add(to_be_added);
        }

        return completed_sucessfully;
    }


    /**
     *
     * Checks if the assert can be brought without breaking the company
     * i.e. if the sale will cause the company to go into the negatives
     * or if it results in 0 credits
     *
     *
     * @param order_to_be_checked (PROGRAM.buy_order - asset) The buy request attempted
     *                            to be fuilled
     *
     * @param to_be_satisfied (PROGRAM.sell_order - asset) The sell that is being checked
     *                        to be completed
     *
     * @return (boolean) Returns false if the sale will cause the company to bust
     *                   Returns true if the sale will not cause the company to bust
     *
     * @author Hugh Glas
     *
     * @version 1.0
     *
     */
    public boolean Check_Sale_Process(BuyOrder order_to_be_checked, SellOrder to_be_satisfied)
    {
        int buy_qty = order_to_be_checked.getNumAvailable();
        double sell_price = to_be_satisfied.GetValue(buy_qty);

        return !((this.current_credits - sell_price) <= 0);
    }

    /**
     *
     * Returns the current list of assets attached to the business
     *
     * @return (ArrayList<Assets>) Returns all of the attached assets
     *
     * @version 1.0
     *
     * @author Hugh Glas
     *
     */
    public ArrayList<Asset> getAllAssets()
    {
        return this.org_assets;
    }
}
