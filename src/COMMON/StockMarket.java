package COMMON;

import CustomExceptions.StockExceptions;

import java.io.Serial;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

public class StockMarket implements Serializable {
    @Serial
    private static final long serialVersionUID = -2393708718755176852L;
    public ArrayList<OrganisationUnit> StockMarketLive;

    public ArrayList<Asset> activeTrades;

    public ArrayList<Trade> pastTrades;

    public ArrayList<User> ListOfUsers;

    /**
     * This is a class to act like a stock market and to keep
     * all organisation units in one place
     *
     * @version 1.0
     * @author Hugh Glas
     */
    public StockMarket() throws StockExceptions, SQLException
    {
        StockMarketLive = new ArrayList<OrganisationUnit>();
        activeTrades = new ArrayList<Asset>();
        ListOfUsers = new ArrayList<User>();
        pastTrades = new ArrayList<Trade>();

        //This Is to prevent any SQL Error throws
        OrganisationUnit ADMIN = new OrganisationUnit("ADMINS", 0, null);
        //ADMIN.Upload();
        StockMarketLive.add(ADMIN);
    }

    /**
     * Simply add the unit to the stock market
     *
     * @param toBeAdded (ORGANISATIONUNIT) The unit to be added to the stock market
     * @return (BOOLEAN) returns if the unit was added (T) or not (F)
     * @version 1.0
     * @author Hugh Glas
     */
    public boolean addOrgnsiationUnit(OrganisationUnit toBeAdded) {

        boolean canBeAdded = StockMarketLive.contains(toBeAdded);
        if (!canBeAdded) {
            StockMarketLive.add((toBeAdded));
        }

        return !canBeAdded;
    }

    public User getUserFromID(String ID)
    {
        int iterator = 0;
        User PlaceHolder;
        User toReturn = null;
        while(iterator < ListOfUsers.size())
        {
            PlaceHolder = ListOfUsers.get(iterator);

            if(PlaceHolder.GetUserID() == ID)
            {
                toReturn = PlaceHolder;
                iterator = ListOfUsers.size();
            }

            iterator++;
        }

        return toReturn;
    }

    public ArrayList<Trade> DBfindTrade(String AssetName)
    {
        ArrayList<Trade> toReturn = new ArrayList<Trade>();


        return toReturn;
    }

    /**
     * Database information
     * Method to return Orgnisation Unit from OU_name
     *
     * @param OU_Name (STRING) Ou name within database
     * @return (OrganisationUnit) Return the unit in its entirety
     */
    public OrganisationUnit DBfindOU(String OU_Name) {
        //Variables to use to assit in finding
        OrganisationUnit toReturn = null;
        String nameOU;

        //Set up variables for 'for' loop
        int length = StockMarketLive.size();

        //Iterate through Stockmarket
        for (int i = 0; i < length; i++) {
            toReturn = StockMarketLive.get(i);
            nameOU = toReturn.orgName();
            if (nameOU == OU_Name) {
                i = length;
            }
        }

        return toReturn;
    }

    /**
     * Updates the currently working users
     *
     * @param toBeAdded - User to be added to the StockMarket
     * @version 1.0
     * @author Hugh
     */
    public void UpdateUsers(User toBeAdded) {
        this.ListOfUsers.add(toBeAdded);
    }

    /**
     *
     * Returns the list of all orders in which match the critera
     *
     * @param BUY_OR_SELL (BOOLEAN/NULL) True - looking for Buy Orders
     *                                   False - looking for sell orders
     *
     * @param OUSearch (OrganisationUnit/NULL) THe orgnaisation in which the filter is being associated with
     *
     * @param UserSearch (USER/NULL) The User in which the filter is being associated with
     *
     * @return (ARRAYLIST<STRING>) Returns an arrray of the Asset names (strings)
     *
     * @author Hugh
     *
     * @Version 1.0
     *
     */
    public ArrayList<String> returnOrders(User UserSearch, OrganisationUnit OUSearch, Boolean BUY_OR_SELL) {
        int Length = this.activeTrades.size();
        ArrayList<String> toReturn = new ArrayList<String>(this.activeTrades.size());
        Asset placeHolder;
        Boolean Test1 = UserSearch == null;
        Boolean Test2 = OUSearch == null;
        Boolean Test3 = BUY_OR_SELL == null;

        if (Test1 || Test2 || Test3) //Occurs at Null Null Null
        {
            // This accounts for no imputs
            for (int i = 0; i < Length; i++) {
                toReturn.add(activeTrades.get(i).GetName());
            }
        } else {
            for (int i = 0; i < Length; i++) {
                placeHolder = activeTrades.get(i);
                if (Test1) {
                    if (Test2) {
                        if (!Test3)  //Occurs at Null Null Something
                        {
                            if (BUY_OR_SELL) {
                                if (placeHolder instanceof BuyOrder) {
                                    toReturn.add(placeHolder.GetName());
                                }
                            } else {
                                if (placeHolder instanceof SellOrder) {
                                    toReturn.add(placeHolder.GetName());
                                }
                            }
                        } else {
                            if (!Test3) //Occurs at Null Something Something
                            {
                                if (BUY_OR_SELL) {
                                    if (placeHolder.GetOUID() == OUSearch.orgName()) ;
                                    {
                                        if (placeHolder instanceof BuyOrder) {
                                            toReturn.add(placeHolder.GetName());
                                        }
                                    }
                                } else {
                                    if (placeHolder.GetOUID() == OUSearch.orgName()) ;
                                    {
                                        if (placeHolder instanceof SellOrder) {
                                            toReturn.add(placeHolder.GetName());
                                        }
                                    }
                                }
                            } else // Occurs at Null Something Null
                            {
                                if (placeHolder.GetOUID() == OUSearch.orgName()) ;
                                {
                                    toReturn.add(placeHolder.GetName());
                                }
                            }
                        }
                    } else {
                        if (Test2) {
                            if (!Test3) // Occurs at something null Something
                            {
                                if (placeHolder.GetUser() == UserSearch.GetUserID()) {
                                    if (BUY_OR_SELL) {
                                        if (placeHolder instanceof BuyOrder) {
                                            toReturn.add(placeHolder.GetName());
                                        }
                                    } else {
                                        if (placeHolder instanceof SellOrder) {
                                            toReturn.add(placeHolder.GetName());
                                        }
                                    }
                                }
                            } else // Occurs at Something null null
                            {
                                if (placeHolder.GetUser() == UserSearch.GetUserID()) {
                                    toReturn.add(placeHolder.GetName());
                                }
                            }
                        } else {
                            if (!Test3) //Occurs at Something Something Something
                            {
                                if (placeHolder.GetUser() == UserSearch.GetUserID()) {
                                    if (placeHolder.GetOUID() == OUSearch.orgName()) {
                                        if (BUY_OR_SELL) {
                                            if (placeHolder instanceof BuyOrder) {
                                                toReturn.add(placeHolder.GetName());
                                            }
                                        } else {
                                            if (placeHolder instanceof SellOrder) {
                                                toReturn.add(placeHolder.GetName());
                                            }
                                        }
                                    }
                                }
                            } else //Occurs at Something Something Null
                            {
                                if (placeHolder.GetUser() == UserSearch.GetUserID()) {
                                    if (placeHolder.GetOUID() == OUSearch.orgName()) {
                                        toReturn.add(placeHolder.GetName());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return toReturn;
    }
}
