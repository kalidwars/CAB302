package PROGRAM;

public class Trade
{
    private OrganisationUnit seller_org;
    private Asset asset_brought;

    /**
     *
     * This is a class that keeps track of the SeLLER and the asset
     * The full history will be stored in the orgnasitional unit class
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
    public Trade(OrganisationUnit seller, BuyOrder fulfilled)
    {
        this.seller_org = seller;
        this.asset_brought = fulfilled;
    }
}
