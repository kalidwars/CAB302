package PROGRAM;

import CustomExceptions.*;

public class BuyOrder extends Asset
{

    /**
     * This class is to describe the asset and the value of the asset
     * additionally it keeps tracks of the QTY
     *
     * @param asset_name (STRING) The name/description of the asset avaliable
     *                   for sale/requests
     * @param value      (DOUBLE) The price of which people are willing to buy or sell
     * @param QTY        (INT) The number wanted/up for sale
     *
     * @author Hugh Glas
     *
     * @version 1.0
     *
     */
    public BuyOrder(String asset_name, double value, int QTY) throws StockExceptions
    {
        super(asset_name, value, QTY);
    }

}
