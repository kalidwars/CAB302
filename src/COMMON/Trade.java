package COMMON;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.ArrayList;

public class Trade implements Serializable
{
    @Serial
    private static final long serialVersionUID = -2393708718755176852L;
    private String seller_org;
    private Asset asset_brought;
    private String userSeller;

    /**
     *
     * This is a class that keeps track of the SELLER and the asset
     * The full history will be stored in the orgnasitional unit class
     * (THIS IS COMPLETED TRADES)
     *
     * @param seller (orgnaistion_unit) The unit in which the asset was
     *               brought from
     *
     * @param fulfilled (PROGRAM.buy_order - asset) The asset in which was brought.
     *
     * @author Hugh Glas
     *
     * @version 1.0
     *
     */
    public Trade(String seller, String sellerUser, BuyOrder fulfilled)
    {
        this.seller_org = seller;
        this.asset_brought = fulfilled;
        this.userSeller = sellerUser;
    }
}
