public class asset
{
    //Variables to use throughout the class
    private String name_of_asset;
    private float ind_price;
    private int num_avaliable;

    /**
     *
     * This class is to describe the asset and the value of the asset
     * additionally it keeps tracks of the QTY
     *
     * @param asset_name (STRING) The name/description of the asset avaliable
     *                   for sale/requests
     *
     * @param value  (FLOAT) The price of which people are willing to buy or sell
     *
     * @param QTY (INT) The number wanted/up for sale
     *
     */
    public asset(String asset_name, float value, int QTY)
    {
        //Assign values into class
        this.name_of_asset = asset_name;
        this.ind_price = value;
        this.num_avaliable = QTY;
    }

    /**
     * This method is used to adjust the sale or buy price of the asset
     *
     * @param new_value (FLOAT) Enter new value for the assset
     *
     * @return Returns True/False if the method has executed correctly without errors
     */
    public boolean adjust_value(float new_value)
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
     */
    public boolean adjust_QTY(int adjustment)
    {
        boolean check_if_worked = false;

        if(!(adjustment <= 0))
        {
            this.num_avaliable = adjustment;
            check_if_worked = true;
        }

        return check_if_worked;
    }
}
