package COMMON;

import CustomExceptions.StockExceptions;

import java.util.ArrayList;

public class StockMarket
{
    public ArrayList<OrganisationUnit> StockMarketLive;

    public ArrayList<Asset> activeTrades;

    public ArrayList<Trade> pastTrades;

    public ArrayList<User> ListOfUsers;
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
    public StockMarket() throws StockExceptions
    {
        StockMarketLive = new ArrayList<OrganisationUnit>();
        activeTrades = new ArrayList<Asset>();
        ListOfUsers = new ArrayList<User>();
        pastTrades = new ArrayList<Trade>();
        StockMarketLive.add(new OrganisationUnit("ADMINS",0,null));
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

    /**
     * Database information
     * Method to return Orgnisation Unit from OU_name
     *
     * @param OU_Name (STRING) Ou name within database
     *
     * @return (OrganisationUnit) Return the unit in its entirety
     *
     */
    public OrganisationUnit DBfindOU(String OU_Name)
    {
        //Variables to use to assit in finding
        OrganisationUnit toReturn = null;
        String nameOU;

        //Set up variables for 'for' loop
        int length = StockMarketLive.size();

        //Iterate through Stockmarket
        for(int i = 0; i < length; i++)
        {
            toReturn = StockMarketLive.get(i);
            nameOU = toReturn.orgName();
            if(nameOU == OU_Name)
            {
                i = length;
            }
        }

        return toReturn;
    }

    /**
     *
     * Updates the currently working users
     *
     * @param toBeAdded - User to be added to the StockMarket
     *
     * @version 1.0
     *
     * @author Hugh
     *
     */
    public void UpdateUsers(User toBeAdded)
    {
        this.ListOfUsers.add(toBeAdded);
    }
}
