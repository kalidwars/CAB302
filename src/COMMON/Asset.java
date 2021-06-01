package COMMON;

import CustomExceptions.*;

public class Asset
{
    //Variables to use throughout the class
    private String name_of_asset;
    private double ind_price;
    private int num_available;

    /**
     *
     * This class is to describe the asset and the value of the asset
     * additionally it keeps tracks of the QTY
     *
     * @param asset_name (STRING) The name/description of the asset avaliable
     *                   for sale/requests
     *
     * @param value  (DOUBLE) The price of which people are willing to buy or sell
     *
     * @param QTY (INT) The number wanted/up for sale
     *
     * @exception Exception Thrown in 2 casses:
     *                      a) When the Value is negative
     *                      b) When QTY is negative or zeros
     *
     * @author Hugh Glas
     *
     * @version 1.1
     *
     */
    public Asset(String asset_name, Double value, int QTY) throws StockExceptions
    {
        //Assign values into class
        this.name_of_asset = asset_name;

        if(value < 0)
        {
            throw new StockExceptions("Expected a Positive Value or 0 value for credits");
        }
        else {
            this.ind_price = value;
        }

        if(QTY <= 0)
        {
            throw new StockExceptions("Expected a Positive value for Quantity");
        }
        else {
            this.num_available = QTY;
        }
    }

    /**
     * This method is used to adjust the sale or buy price of the asset
     *
     * @param new_value (FLOAT) Enter new value for the assset
     *
     * @return Returns True/False if the method has executed correctly without errors
     * @author Hugh Glas
     *
     * @version 1.0
     */
    public boolean adjustValue(float new_value)
    {
        boolean did_it_work = false;

        if(!((new_value) <= 0))
        {
            this.ind_price = new_value;
            did_it_work = true;
        }

        return did_it_work;
    }

    /**
     *This is for adjusting the number of buy or sell of the asset
     *
     * @param adjustment (INT) the new number of assets avaliable
     *
     * @return boolean if the processs was excuted correctly
     *
     * @author Hugh Glas
     *
     * @version 1.0
     *
     */
    public boolean adjustQTY(int adjustment)
    {
        boolean check_if_worked = false;

        if(!(adjustment <= 0))
        {
            this.num_available = adjustment;
            check_if_worked = true;
        }

        return check_if_worked;
    }

    /**
     *
     * Simply returns the indvidual price of the asset
     *
     * @return (FLOAT) reutnrs the indvidual price of the asset
     *
     * @author Hugh Glas
     *
     * @version 1.0
     *
     */
    public double getIndPrice()
    {
        return ind_price;
    }

    /**
     *
     * Simply returns the number of asset wanted to buy or sell
     *
     * @return (INT) the number of asset required for a buy
     *      or sell
     *
     * @author Hugh Glas
     *
     * @version 1.0
     *
     */
    public int getNumAvailable()
    {
        return this.num_available;
    }

    /**
     *
     * The value of which the final sale will cost
     *
     * @param num_required (INT) Pass through the number of assets to buy
     *
     * @return (FLOAT) Returns the simple multiplication of the number required
     *              by the indvidual price
     *
     * @author Hugh Glas
     *
     * @version 1.0
     *
     * TO DO
     * - Throw exception when given a num required that is greater then current value
     *
     */
    public double GetValue(int num_required)
    {
        return (this.ind_price * num_required);
    }

    /**
     *
     * Simple method that returns the name
     *
     * @return (STRING) returns the name of the asset
     *
     * @author Hugh Glas
     *
     * @version 1.0
     *
     */
    public String GetName()
    {
        return name_of_asset;
    }
}
