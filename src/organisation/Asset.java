package organisation;

public class Asset
{
    //Variables to use throughout the class
    public String nameOfAsset;
    public float indPrice;
    public int numAvailable;

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
    public Asset(String asset_name, float value, int QTY)
    {
        //Assign values into class
        this.nameOfAsset = asset_name;
        this.indPrice = value;
        this.numAvailable = QTY;
    }

    /**
     * This method is used to adjust the sale or buy price of the asset
     *
     * @param new_value (FLOAT) Enter new value for the assset
     *
     * @return Returns True/False if the method has executed correctly
     */
    public boolean adjustValue(float new_value)
    {
        if((this.indPrice - new_value) <= 0);
        {

        }

        return true;
    }

    /**
     *This is for adjusting the number of buy or sell of the asset
     *
     * @param adjustment
     * @return
     */
    public boolean adjustQTY(int adjustment)
    {
        return true;
    }
}
