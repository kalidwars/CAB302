import java.util.ArrayList;
import java.util.Arrays;

public class trade
{
    private orgnasiton_unit seller_org;
    private asset asset_brought;

    /**
     *
     * This is a class that keeps track of the SeLLER and the asset
     * The full history will be stored in the orgnasitional unit class
     *
     * @param seller (orgnaistion_unit) The unit in which the asset was
     *               brought from
     *
     * @param fufilled (buy_order - asset) The asset in which was brought.
     *
     */
    public trade(orgnasiton_unit seller, buy_order fufilled)
    {
        this.seller_org = seller;
        this.asset_brought = fufilled;
    }
}
