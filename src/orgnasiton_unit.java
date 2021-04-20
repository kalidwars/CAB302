import java.util.ArrayList;
import java.util.Arrays;

public class orgnasiton_unit
{
    //Variables that are to be used throughout class
    private String org_name;
    private float current_credits;
    private ArrayList<asset> org_assets;
    private ArrayList<trade> trade_buys = new ArrayList<trade>();

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


    /**
     *
     * This is to make a sale occur and add it to the orgnisation unit
     * Trade history the variable is to pass through the seller
     *
     * @param SELLER (orgnasition_unit) the unit in which the asset
     *               was sold from
     *
     * @param order_fulfilled (asset - buy_order) The order in which is being
     *                       fufilled
     *
     * @param sale_taken (asset - sell_order) The order in which is being used
     *                   to complete the trade
     *
     * @return (boolean) To return if the process was completed
     *                  fully
     */
    private boolean Complete_Sale(orgnasiton_unit SELLER, buy_order order_fulfilled, sell_order sale_taken)
    {
        boolean completed_sucessfully;

        int num_brought = order_fulfilled.get_Num_available();
        int current_avaliable = sale_taken.get_Num_available();

        completed_sucessfully = sale_taken.adjust_QTY(current_avaliable - num_brought);

        if(completed_sucessfully)
        {
            trade_buys.add((new trade(SELLER,order_fulfilled)));
        }

        return completed_sucessfully;
    }


    /**
     *
     * Checks if the assert can be brought without breaking the company
     * i.e. if the sale will cause the company to go into the negatives
     * or if it results in 0 credits
     *
     * @param order_to_be_checked (buy_order - asset) The buy request attempted
     *                            to be fuilled
     *
     * @param to_be_satisfied (sell_order - asset) The sell that is being checked
     *                        to be completed
     *
     * @return (boolean) Returns false if the sale will cause the company to bust
     *                   Returns true if the sale will not cause the company to bust
     *
     */
    public boolean Check_Balance(buy_order order_to_be_checked, sell_order to_be_satisfied)
    {
        int buy_qty = order_to_be_checked.get_Num_available();
        float sell_price = to_be_satisfied.get_Value(buy_qty);

        if((this.current_credits -  sell_price) <= 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
