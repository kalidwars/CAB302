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
 * @author Adam
 *
 * @version 1.2
 *
 */
public class DataSource
{
    private static final String TESTING  = "DROP TABLE IF EXISTS users; DROP TABLE IF EXISTS organisationunits;";
    private static final String GET_ASSETS = "SELECT * FROM assets WHERE OrgUnit=?";
    private static final String ADD_ASSET = "INSERT INTO assets (name, username, amount,value) VALUES (?, ?, ?, ?);";
    private static final String REMOVE_ASSET = "DELETE FROM assets WHERE name=? AND username=? AND amount=? AND value=?";
    private static final String GET_ALL_USER = "SELECT * FROM users";
    private Connection connection;

    //Create String For Creating tables in Server
    //Each variable name is what the table is designed for
    //Designed by Hugh and Adam
    private static final String CREATE_TABLE_SELL_ASSETS =
            "CREATE TABLE IF NOT EXITS assets (" +
                    "  AssetID int(50) NOT NULL AUTO_INCREMENT" +
                    "  Name varchar(45) NOT NULL," +
                    "  OrgUnit varchar(45) NOT NULL," +
                    "  Price double (11) NOT NULL," +
                    "  Amount int(11) NOT NULL," +
                    "  UserName varchar(45) NOT NULL," +
                    "  PRIMARY KEY (AssetID)," +
                    "  KEY fk_orgunit (orgunit`," +
                    "  CONSTRAINT fk_orgunit FOREIGN KEY (orgunit) REFERENCES organisationunits (Orgunit);";
    private static final String CREATE_TABLE_BUY_ORDERS =
            "CREATE TABLE orders (" +
                    "OrderID int(50) NOT NULL AUTO_INCREMENT," +
                    "Name varchar(45) NOT NULL," +
                    "OrgUnit varchar(45) NOT NULL," +
                    "Price double(11) NOT NULL" +
                    "Amount int(11) NOT NULL," +
                    "UserName varchar(45) NOT NULL," +
                    "PRIMARY KEY (AssetId)," +
                    "  KEY fk_orgunit (orgunit)," +
                    "  CONSTRAINT fk_orgunit FOREIGN KEY (orgunit) REFERENCES organisationunits (Orgunit));";
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE IF NOT EXISTS users (username varchar(45) NOT NULL,password varchar(45) NOT NULL,privilege varchar(45) NOT NULL,orgunit varchar(45),PRIMARY KEY (username),FOREIGN KEY (orgunit) REFERENCES organisationunits(orgunit));";
    private static final String CREATE_TABLE_HISTORY =
            "CREATE TABLE ‘TRADE_HISTORY’ (" +
                    "TradeID int(11) NOT NULL AUTO_INCREMENT," +
                    "OrgUnitBuy varchar(45) NOT NULL," +
                    "OrgUnitSell varchar(45) NOT NULL," +
                    "UserSeller varchar(45) NOT NULL," +
                    "UserBuyer varchar(45) NOT NULL," +
                    "QTY int(11) NOT NULL," +
                    "PRICE double(11) NOT NULL," +
                    "PRIMARY KEY(TradeID)" +
                    "FOREIGN KEY (OrgUnitBuy) REFERENCES organisationunits (Orgunit)" +
                    "FOREIGN KEY (OrgUnitSell) REFERENCES organisationunits (Orgunit)" +
                    "FOREIGN KEY (UserSeller) REFERENCES user  (username)" +
                    "FOREIGN KEY (UserBuyer) REFERENCES ‘user’  (username));";
    private static final String CREATE_TABLE_OU =
            "CREATE TABLE IF NOT EXISTS organisationunits (Orgunit varchar(45) NOT NULL, credits double(11) NOT NULL, PRIMARY KEY (Orgunit));";
    private PreparedStatement getAssets;
    private PreparedStatement getAllUsers;
    private PreparedStatement addAsset;
    private PreparedStatement removeAsset;

    public DataSource()
    {
        connection = DBConnection.getInstance();
        try {
            //this.PrepareTesting();
            Statement st = connection.createStatement();
            //st.execute(CREATE_TABLE_OU);
            //st.execute(CREATE_TABLE_SELL_ASSETS);
            //st.execute(CREATE_TABLE_BUY_ORDERS);
            //st.execute(CREATE_TABLE_USERS);

            //st.execute(CREATE_TABLE_HISTORY);
            getAssets = connection.prepareStatement(GET_ASSETS);
            getAllUsers = connection.prepareStatement(GET_ALL_USER);
            addAsset = connection.prepareStatement(ADD_ASSET);
            removeAsset = connection.prepareStatement(REMOVE_ASSET);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void PrepareTesting()
    {
        try {
            Statement st = connection.createStatement();
            st.execute(TESTING);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
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
}