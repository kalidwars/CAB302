package Server;

import COMMON.*;
import CustomExceptions.*;
import Server.*;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 *
 * Class for retrieving data from the XML file holding the address list.
 *
 * @author Adam, Hugh
 *
 * @version 1.2
 *
 */
public class DataSource
{

    //Useful methods
    private static final String GET_ASSETS = "SELECT * FROM assets WHERE OrgUnit=?";
    private static final String ADD_ASSET = "INSERT INTO assets (name, username, amount,value) VALUES (?, ?, ?, ?);";
    private static final String REMOVE_ASSET = "DELETE FROM assets WHERE name=? AND username=? AND amount=? AND value=?";
    private static final String GET_ALL_USER = "SELECT * FROM users";
    private static final String GET_ALL_OU = "SELECT * FROM organisationunits;";
    private static final String GET_BUY_ORDER = "SELECT * FROM orders where offertype = buy AND complete = 0";
    private static final String GET_SELL_ORDER = "SELECT * FROM orders where offertype = sell AND complete = 0";

    //Testing SQL Methods
    private static final String TESTING  = "DELETE FROM users WHERE true;";
    private static final String TESTING2 = "DELETE FROM organisationunits WHERE true;";
    private static final String TESTING3 = "DELETE FROM sells WHERE true;";
    private static final String TESTING4 = "DELETE FROM buys WHERE true";
    private static final String TESTING5 = "DELETE FROM trade_history WHERE true;";

    private Connection connection;

    //Create String For Creating tables in Server
    //Each variable name is what the table is designed for
    //Designed by Hugh and Adam
    private static final String CREATE_TABLE_SELL_ASSETS =
            "CREATE TABLE IF NOT EXISTS sells (SaleID int(50) NOT NULL AUTO_INCREMENT, Name varchar(45) NOT NULL, username VARCHAR(45) NOT NULL,OrgUnit varchar(45) NOT NULL, Price double(11,2) NOT NULL, Amount int(11) NOT NULL, PRIMARY KEY (SaleId), KEY fk_orgunit (orgunit),  CONSTRAINT sale_orgunit FOREIGN KEY (orgunit) REFERENCES organisationunits (Orgunit), FOREIGN KEY (username) REFERENCES users(username));";
    private static final String CREATE_TABLE_BUY_ORDERS =
            "CREATE TABLE IF NOT EXISTS buys (AssetID int(50) NOT NULL AUTO_INCREMENT, Name varchar(45) NOT NULL, username VARCHAR(45) NOT NULL,OrgUnit varchar(45) NOT NULL, Price double(11,2) NOT NULL, Amount int(11) NOT NULL, PRIMARY KEY (AssetID), KEY fk_orgunit (orgunit),  CONSTRAINT fk_orgunit FOREIGN KEY (orgunit) REFERENCES organisationunits (Orgunit), FOREIGN KEY (username) REFERENCES users(username));";
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE IF NOT EXISTS users (username varchar(45) NOT NULL,password varchar(45) NOT NULL,privilege varchar(45) NOT NULL,orgunit varchar(45),PRIMARY KEY (username),FOREIGN KEY (orgunit) REFERENCES organisationunits(orgunit));";
    private static final String CREATE_TABLE_HISTORY =
            "CREATE TABLE IF NOT EXISTS trade_history (TradeID int(11) NOT NULL AUTO_INCREMENT, OrgUnitBuy varchar(45) NOT NULL,OrgUnitSell varchar(45) NOT NULL,UserSeller varchar(45) NOT NULL,UserBuyer varchar(45) NOT NULL,QTY int(11) NOT NULL,PRICE double(11,2) NOT NULL, PRIMARY KEY(TradeID),\n" +
                    "FOREIGN KEY (OrgUnitBuy) REFERENCES organisationunits (Orgunit)," +
                    "FOREIGN KEY (OrgUnitSell) REFERENCES organisationunits (Orgunit)," +
                    "FOREIGN KEY (UserSeller) REFERENCES users (username)," +
                    "FOREIGN KEY (UserBuyer) REFERENCES users (username));";
    private static final String CREATE_TABLE_OU =
            "CREATE TABLE IF NOT EXISTS organisationunits (Orgunit varchar(45) NOT NULL, credits double(11,2), PRIMARY KEY (Orgunit));";
    private PreparedStatement getAssets;
    private PreparedStatement getAllUsers;
    private PreparedStatement addAsset;
    private PreparedStatement removeAsset;
    private PreparedStatement getAllOU;
    private PreparedStatement getbuyorders;
    private PreparedStatement getsellorders;

    public DataSource()
    {
        connection = DBConnection.getInstance();
        try {
            Statement st = connection.createStatement();
            //Create Tables from above statementes
            st.execute(CREATE_TABLE_OU);
            st.execute(CREATE_TABLE_USERS);
            st.execute(CREATE_TABLE_BUY_ORDERS);
            st.execute(CREATE_TABLE_SELL_ASSETS);
            st.execute(CREATE_TABLE_HISTORY);

            //Prepare 'concrete' statements
            getAssets = connection.prepareStatement(GET_ASSETS);
            getAllUsers = connection.prepareStatement(GET_ALL_USER);
            getAllOU = connection.prepareStatement(GET_ALL_OU);
            addAsset = connection.prepareStatement(ADD_ASSET);
            removeAsset = connection.prepareStatement(REMOVE_ASSET);
            getbuyorders = connection.prepareStatement(GET_BUY_ORDER);
            getsellorders = connection.prepareStatement(GET_SELL_ORDER);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * <b>DO NOT USE FOR FINAL PRODUCT ONLY FOR TESTING</b>
     *
     * This is for clearing all the tables of the data
     *
     * @throws SQLException When an error occurs while executing
     */
    public void UNITTESTING() throws SQLException
    {
        Statement st = connection.createStatement();
        st.execute(TESTING);
        st.execute(TESTING2);
        st.execute(TESTING3);
        st.execute(TESTING4);
        st.execute(TESTING5);
    }

    /**
     *
     * Collects Array list of Assets of the Query OU
     *
     * @param OUName (STRING) - Raw String of the OU name to retrieve data
     *
     * @return (ARRAYLIST<Asset>) Returns the list of assets
     *
     * @throws SQLException OCCURS when an Sql error occurs
     *
     * @throws StockExceptions This should not be thrown here
     *
     * @author Adam
     */
    public ArrayList<Asset> getAssets(String OUName) throws SQLException, StockExceptions
    {
        ArrayList<Asset> temp = new ArrayList<>();
        User TOPass;
        ResultSet rs = null;
        try {
            getAssets.setString(1,OUName);
            rs = getAssets.executeQuery();
            while(rs.next())
            {
                TOPass = StartServer.CurrentStockMarket.getUserFromID(rs.getString("UserName"));
                temp.add(new SellOrder(rs.getString("Name"),rs.getDouble("Amount"),rs.getInt("Price"),TOPass));
            }
        }
        catch (SQLException throwable)
        {
            throwable.printStackTrace();
        }
        catch (StockExceptions e)
        {
            //Do Nothing as this error doesn't occur here
        }
        return temp;
    }
    public void AddAsset(Asset asset) throws SQLException {
        addAsset.setString(1, asset.GetName());
        addAsset.setString(2, asset.GetOUID());
        addAsset.setInt(3, asset.getNumAvailable());
        addAsset.setDouble(4,asset.getIndPrice());
        addAsset.execute();
    }
    public void RemoveAsset(Asset asset) throws SQLException {
        removeAsset.setString(1, asset.GetName());
        removeAsset.setString(2, asset.GetUser());
        removeAsset.setInt(3, asset.getNumAvailable());
        removeAsset.setDouble(4,asset.getIndPrice());
        removeAsset.execute();
    }

    /**
     *
     * This function is to return ALL users in the DB and convert them
     * to the user class
     *
     * @return (ARRAYLIST<User>) Array list of all users currently in DB
     *                          (NOTE RETURNS NULL IF THERE ARE NO USERS)
     *
     * @throws SQLException - is presented if the SQL commmand causes error
     *
     * @throws NoSuchPaddingException - Throws if there is a memory overflow
     * @throws NoSuchAlgorithmException - Throws if algoritihm is incorrect
     *
     * @version 1.0
     *
     * @author Hugh
     */
    public ArrayList<User> convertToUsers(StockMarket QuestionStockMarket) throws SQLException, NoSuchPaddingException, NoSuchAlgorithmException
    {
        //Setup variables for setting up users
        String USER;
        String PW;
        boolean PRI;
        String OU_name;
        OrganisationUnit OU_Info;

        //Setup variables to be used in connection
        ArrayList<User> toReturn = new ArrayList<User>();
        ResultSet raw = null;

        //Attempt grab of information
        try
        {
            //Query DB with
            raw = getAllUsers.executeQuery();
        }
        catch (SQLException error)
        {
            error.printStackTrace();
        }

        //If the query returns something set up and return user
        if(raw != null)
        {
            //Iterate through list of results
            while(raw.next())
            {
                //Store Variables
                USER = raw.getString("username");
                PW = raw.getString("password");
                PRI = raw.getBoolean("privilege");
                OU_name = raw.getString("orgunit");

                //If the user is Admin user (i.e. privilege = true)
                if(PRI)
                {
                    toReturn.add(new AdminUser(USER,PW));
                }
                else
                {
                    //Grab OU information
                    OU_Info = QuestionStockMarket.DBfindOU(OU_name);
                    toReturn.add(new User(USER,PW,OU_Info));
                }

            }
        }
        else
        {
            //Return empty variable
            toReturn = null;
        }

        return toReturn;
    }

    public ArrayList<BuyOrder> GetBuyOrders() {
        ArrayList<BuyOrder> buyOrders = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = getbuyorders.executeQuery();
            while(rs.next())
            {
                /// Just need to add what columns we are going to use buyOrders.add(BuyOrder();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return buyOrders;
    }

    public ArrayList<BuyOrder> GetSellOrders() {
        ArrayList<BuyOrder> sellOrders = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = getsellorders.executeQuery();
            while(rs.next())
            {
                /// Just need to add what columns we are going to use buyOrders.add(BuyOrder();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sellOrders;
    }

    public ArrayList<OrganisationUnit> convertToOU() throws StockExceptions
    {
        // Variable to return to the OU
        ArrayList<OrganisationUnit> toReturn = new ArrayList<OrganisationUnit>();
        Double retrievedCredits;
        String nameOU;

        //SQL Variable
        ResultSet raw = null;

        try
        {
            raw = getAllOU.executeQuery();

            //If the query returns something run through results
            if(raw != null)
            {
                while(raw.next())
                {
                    //Store Variables
                    retrievedCredits = raw.getDouble("credits");
                    nameOU = raw.getString("Orgunit");

                    toReturn.add(new OrganisationUnit(nameOU,retrievedCredits,null));
                }
            }
            else
            {
                toReturn = null;
            }
        }
        catch (SQLException SQL)
        {
            SQL.printStackTrace();
        }


        return toReturn;
    }
}