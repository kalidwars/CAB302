package PROGRAM;

import java.util.ArrayList;

public class StockMarket
{
    private ArrayList<OrganisationUnit> StockMarketLive = new ArrayList<OrganisationUnit>();

    /**
     *
     * This is a class to act like a stock market and to keep
     * all organisation units in one place
     *
     * @version 1.0
     *
     * @author Hugh Glas
     *
     */
    public StockMarket()
    {

    }

    /**
     *
     * Simply add the unit to the stock market
     *
     * @param toBeAdded (ORGANISATIONUNIT) The unit to be added to the stock market
     *
     * @return (BOOLEAN) returns if the unit was added (T) or not (F)
     *
     * @version 1.0
     *
     * @author Hugh Glas
     *
     *
     */
    public boolean addOrgnsiationUnit(OrganisationUnit toBeAdded)
    {

        boolean canBeAdded = StockMarketLive.contains(toBeAdded);
        if(!canBeAdded)
        {
            StockMarketLive.add((toBeAdded));
        }

        return !canBeAdded;
    }
}
