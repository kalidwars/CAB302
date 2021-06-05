package Server;

import COMMON.*;
import CustomExceptions.*;
import Server.*;

import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
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
    private static final String GET_ASSETS = "SELECT * FROM assets WHERE username=?";
    private static final String ADD_ASSET = "INSERT INTO assets (AssetName, username, OrgUnit, Amount) VALUES (?, ?, ?, ?);";
    private static final String ADD_ASSET_NAME = "INSERT INTO AssetNames VALUES (?);";
    private static final String REMOVE_ASSET = "DELETE FROM assets WHERE AssetName=? AND username=? AND OrgUnit=?";
    private static final String GET_ALL_USER = "SELECT * FROM users";
    private static final String GET_ALL_OU = "SELECT * FROM organisationunits;";
    private static final String GET_BUY_ORDER = "SELECT * FROM assets where AssetType = true";
    private static final String GET_SELL_ORDER = "SELECT * FROM assets where AssetType = false";
    private static final String ADD_OU = "INSERT INTO organisationunits (Orgunit, credits) VALUES (?, ?);";
    private static final String REMOVE_OU = "DELETE FROM organisationunits WHERE Orgunit=? AND credits=?";
    private static final String GET_OUS = "SELECT * from organisationunits";
    private static final String GET_SPECIFIC_OU = "SELECT * from organisationunits where Orgunit = ?";
    private static final String EDIT_OU = "UPDATE organisationunits set credits=? where Orgunit = ?";
    private static final String ADD_USER = "INSERT INTO Users (username, password, privilege, orgunit) VALUES (?, ?, ?, ?);";
    private static final String GET_USERS = "select * from users";
    private static final String REMOVE_USER = "DELETE FROM users WHERE username=?";
    private static final String EDIT_USER = "UPDATE users set password=? where username = ?";
    private static final String ADD_ORDER = "Insert INTO Assets (AssetName, username, OrgUnit,Price,Amount,AssetType) VALUES (?, ?, ?, ?,?,?);";
    private static final String GET_SPECFIC_USER = "Select * from users where username = ?";
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
    private static final String CREATE_TABLE_ASSETS =
            "CREATE TABLE IF NOT EXISTS Assets (AssetID int(50) NOT NULL AUTO_INCREMENT, AssetName varchar(45) NOT NULL, username VARCHAR(45) NOT NULL, OrgUnit varchar(45) NOT NULL, Price double(11,2), Amount int(11) NOT NULL, AssetType Boolean, PRIMARY KEY (AssetID),KEY fk_orgunit (orgunit),KEY fk_username (username),KEY fk_NameTest (AssetName),FOREIGN KEY (orgunit) REFERENCES organisationunits (Orgunit), FOREIGN KEY (username) REFERENCES users(username), FOREIGN KEY (AssetName) REFERENCES assetnames(name));";
    private static final String CREATE_TABLE_ASSETNAMES =
            "CREATE TABLE IF NOT EXISTS AssetNames (Name varchar(45) NOT NULL, PRIMARY KEY(Name));";
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE IF NOT EXISTS users (username varchar(45) NOT NULL,password blob NOT NULL,privilege boolean NOT NULL,orgunit varchar(45),PRIMARY KEY (username),FOREIGN KEY (orgunit) REFERENCES organisationunits(orgunit));";
    private static final String CREATE_TABLE_HISTORY =
            "CREATE TABLE IF NOT EXISTS trade_history (TradeID int(11) NOT NULL AUTO_INCREMENT, OrgUnitBuy varchar(45) NOT NULL,OrgUnitSell varchar(45) NOT NULL,UserSeller varchar(45) NOT NULL,UserBuyer varchar(45) NOT NULL,QTY int(11) NOT NULL,PRICE double(11,2) NOT NULL, PRIMARY KEY(TradeID)," +
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
    private PreparedStatement ADDOU;
    private PreparedStatement RemoveOU;
    private PreparedStatement GETOUS;
    private PreparedStatement EDITOU;
    private PreparedStatement ADDUSER;
    private PreparedStatement GETUSERS;
    private PreparedStatement GETSpecficOU;
    private PreparedStatement REMOVEUSER;
    private PreparedStatement EDITUSER;
    private PreparedStatement ADDASSETNAME;
    private PreparedStatement ADDORDER;
    private PreparedStatement GETSPECFICUSER;
    public DataSource()
    {
        connection = DBConnection.getInstance();
        try {
            Statement st = connection.createStatement();
            //Create Tables from above statementes
            st.execute(CREATE_TABLE_OU);
            st.execute(CREATE_TABLE_USERS);
            st.execute(CREATE_TABLE_ASSETNAMES);
            st.execute(CREATE_TABLE_ASSETS);
            st.execute(CREATE_TABLE_HISTORY);

            //Prepare 'concrete' statements
            getAssets = connection.prepareStatement(GET_ASSETS);
            getAllUsers = connection.prepareStatement(GET_ALL_USER);
            getAllOU = connection.prepareStatement(GET_ALL_OU);
            addAsset = connection.prepareStatement(ADD_ASSET);
            removeAsset = connection.prepareStatement(REMOVE_ASSET);
            getbuyorders = connection.prepareStatement(GET_BUY_ORDER);
            getsellorders = connection.prepareStatement(GET_SELL_ORDER);
            ADDOU = connection.prepareStatement(ADD_OU);
            RemoveOU = connection.prepareStatement(REMOVE_OU);
            GETOUS = connection.prepareStatement(GET_OUS);
            EDITOU = connection.prepareStatement(EDIT_OU);
            ADDUSER = connection.prepareStatement(ADD_USER);
            GETUSERS = connection.prepareStatement(GET_USERS);
            GETSpecficOU = connection.prepareStatement(GET_SPECIFIC_OU);
            REMOVEUSER = connection.prepareStatement(REMOVE_USER);
            EDITUSER = connection.prepareStatement(EDIT_USER);
            ADDASSETNAME = connection.prepareStatement(ADD_ASSET_NAME);
            ADDORDER = connection.prepareStatement(ADD_ORDER);
            GETSPECFICUSER = connection.prepareStatement(GET_SPECFIC_USER);
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
     * @param user (User) - Raw String of the OU name to retrieve data
     *
     * @return (ARRAYLIST<Asset>) Returns the list of assets
     *
     * @throws SQLException OCCURS when an Sql error occurs
     *
     * @throws StockExceptions This should not be thrown here
     *
     * @author Adam
     */
    public ArrayList<Asset> getAssets(User user) throws SQLException, StockExceptions
    {
        ArrayList<Asset> temp = new ArrayList<>();
        //User TOPass;
        ResultSet rs = null;
        try {
            getAssets.setString(1,user.GetUserID());
            rs = getAssets.executeQuery();
            while(rs.next())
            {
                Asset asset = new Asset(rs.getString("AssetName"),rs.getDouble("Price"),rs.getInt("amount"),user.GetUserID());
                temp.add(asset);
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
        addAsset.setString(2, asset.GetUser());
        addAsset.setString(3, asset.GetOUID());
        addAsset.setInt(4,asset.getNumAvailable());
        addAsset.execute();
    }
    public void AddAssetName(String Name) throws SQLException {
        ADDASSETNAME.setString(1, Name);
        ADDASSETNAME.execute();
    }
    public void RemoveAsset(Asset asset) throws SQLException {
        removeAsset.setString(1, asset.GetName());
        removeAsset.setString(2, asset.GetUser());
        removeAsset.setString(3, asset.GetOUID());
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
    public ArrayList<User> convertToUsers(StockMarket QuestionStockMarket) throws SQLException, IOException
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

    public ArrayList<SellOrder> GetSellOrders() {
        ArrayList<SellOrder> sellOrders = new ArrayList<>();
        ResultSet rs = null;
        ResultSet UserDetails = null;
        try {
            rs = getsellorders.executeQuery();
            while(rs.next())
            {
                GETSPECFICUSER.setString(1,rs.getString("username"));
                UserDetails = GETSPECFICUSER.executeQuery();
                UserDetails.next();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sellOrders;
    }

    public ArrayList<OrganisationUnit> GetAllOU() throws StockExceptions
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
    //Adds an organisation unit to the DB
    public void AddOU(OrganisationUnit OU) {
        try {
            ADDOU.setString(1,OU.orgName());
            ADDOU.setDouble(2,OU.currentCredits());
            ADDOU.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void RemoveOU(OrganisationUnit OU) {
        try {
            RemoveOU.setString(1,OU.orgName());
            RemoveOU.setDouble(2,OU.currentCredits());
            RemoveOU.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ArrayList<OrganisationUnit> getOUs() {
        ArrayList<OrganisationUnit> temp = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = GETOUS.executeQuery();
            while(rs.next())
            {
                OrganisationUnit OU = new OrganisationUnit(rs.getString("OrgUnit"),rs.getDouble("credits"),null);
                temp.add(OU);
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

    public void EditOU(OrganisationUnit ou, double credits) {
        try {
            EDITOU.setDouble(1,credits);
            EDITOU.setString(2,ou.orgName());
            EDITOU.executeUpdate();
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void AddUser(User user1) {
        try {
            ByteArrayOutputStream test = new ByteArrayOutputStream();
            ObjectOutputStream parse = new ObjectOutputStream(test);
            parse.writeObject(user1.GetPassword());
            byte[] ByteArray = test.toByteArray();
            ADDUSER.setString(1,user1.GetUserID());
            ADDUSER.setBlob(2, new ByteArrayInputStream(ByteArray), ByteArray.length);
            ADDUSER.setBoolean(3, false);
            ADDUSER.setString(4,user1.OUID_Owner());
            ADDUSER.execute();
        }
        catch (SQLException | IOException throwable) {
            throwable.printStackTrace();
        }
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> temp = new ArrayList<>();
        ResultSet rs = null;
        ResultSet orgunit = null;
        try {
            rs = GETUSERS.executeQuery();
            while(rs.next())
            {
                Blob passblob = rs.getBlob("password");
                byte[] password = passblob.getBytes(1,(int) passblob.length());
                ByteArrayInputStream byteStream = new ByteArrayInputStream(password);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteStream);
                SerialData serialised = (SerialData) objectInputStream.readObject();
                String PlainPass = serialised.getHiddenValue();
                GETSpecficOU.setString(1,rs.getString("orgunit"));
                orgunit = GETSpecficOU.executeQuery();
                orgunit.next();
                OrganisationUnit temporg = new OrganisationUnit(orgunit.getString("Orgunit"),orgunit.getDouble("credits"),null);
                User user = new User(rs.getString("username"),PlainPass, temporg);
                temp.add(user);
            }
        }
        catch (SQLException throwable)
        {
            throwable.printStackTrace();
        }
        catch (StockExceptions e)
        {
            //Do Nothing as this error doesn't occur here
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public void RemoveUser(User user2) {
        try {
            REMOVEUSER.setString(1,user2.GetUserID());
            REMOVEUSER.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void EditUser(User usertoedit, String newPass) {
        try {
            usertoedit.SetPassword(newPass);
            ByteArrayOutputStream test = new ByteArrayOutputStream();
            ObjectOutputStream parse = new ObjectOutputStream(test);
            parse.writeObject(usertoedit.GetPassword());
            byte[] ByteArray = test.toByteArray();
            EDITUSER.setString(2, usertoedit.GetUserID());
            EDITUSER.setBlob(1, new ByteArrayInputStream(ByteArray), ByteArray.length);
            EDITUSER.executeUpdate();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void AddOrder(BuyOrder order) {
        try {
            ADDORDER.setString(1, order.GetName());
            ADDORDER.setString(2, order.GetUser());
            ADDORDER.setString(3, order.GetOUID());
            ADDORDER.setDouble(4, order.getIndPrice());
            ADDORDER.setInt(5, order.getNumAvailable());
            ADDORDER.setBoolean(6, false);
            ADDORDER.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void AddOrder(SellOrder order) {
        try {
            ADDORDER.setString(1, order.GetName());
            ADDORDER.setString(2, order.GetUser());
            ADDORDER.setString(3, order.GetOUID());
            ADDORDER.setDouble(4, order.getIndPrice());
            ADDORDER.setInt(5, order.getNumAvailable());
            ADDORDER.setBoolean(6, true);
            ADDORDER.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}