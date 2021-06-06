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
    private static final String GET_BUY_ORDER = "SELECT * FROM assets where AssetType = false";
    private static final String GET_SELL_ORDER = "SELECT * FROM assets where AssetType = true";
    private static final String ADD_OU = "INSERT INTO organisationunits (Orgunit, credits) VALUES (?, ?);";
    private static final String REMOVE_OU = "DELETE FROM organisationunits WHERE Orgunit=? AND credits=?";
    private static final String GET_OUS = "SELECT * from organisationunits";
    private static final String GET_SPECIFIC_OU = "SELECT * from organisationunits where Orgunit = ?";
    private static final String EDIT_OU = "UPDATE organisationunits set credits=? where Orgunit = ?";
    private static final String ADD_USER = "INSERT INTO Users (username, password, privilege, orgunit) VALUES (?, ?, ?, ?);";
    private static final String ADD_ADMIN_USER = "INSERT INTO Users (username, password, privilege, orgunit) VALUES (?, ?, ?, ?);";
    private static final String GET_ALL_USERS = "SELECT * from users;";
    private static final String GET_USERS = "select * from users where privilege = false";
    private static final String GET_ADMIN_USERS = "select * from users where privilege = true";
    private static final String REMOVE_USER = "DELETE FROM users WHERE username=?";
    private static final String EDIT_USER = "UPDATE users set password=? where username = ?";
    private static final String ADD_ORDER = "Insert INTO Assets (AssetName, username, OrgUnit,Price,Amount,AssetType) VALUES (?, ?, ?, ?,?,?);";
    private static final String GET_SPECFIC_USER = "Select * from users where username = ?";
    private static final String ADD_TRADE = "INSERT INTO trade_history (Name,OrgUnitBuy,OrgUnitSell,UserSeller,UserBuyer,QTY,PRICE) VALUES (?,?,?,?,?,?,?);";
    private static final String ADJUST_BUY = "UPDATE Assets SET QTY=(QTY - ?) WHERE (OrgUnit=? AND username=? AND AssetType = false);";
    private static final String ADJUST_SELL = "UPDATE Assets SET QTY=(QTY - ?) WHERE (OrgUnit=? AND username=? AND AssetType = true);";
    private static final String ADJUST_OU_BUY = "UPDATE organisationunits SET creadits = creadits - ( ? * ?) WHERE (Orgunit = ?);";
    private static final String ADJUST_OU_SELL = "UPDATE organisationunits SET creadits = creadits + ( ? * ?) WHERE (Orgunit = ?);";
    private static final String CLEAN_UP_ASSETS = "DELETE FROM Assets WHERE (QTY = 0 AND AssetType <> NULL);";
    private static final String GET_TRADES = "SELECT * FROM trade_history;";
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
            "CREATE TABLE IF NOT EXISTS trade_history (TradeID int(11) NOT NULL AUTO_INCREMENT, Name varchar(45), OrgUnitBuy varchar(45) NOT NULL,OrgUnitSell varchar(45) NOT NULL,UserSeller varchar(45) NOT NULL,UserBuyer varchar(45) NOT NULL,QTY int(11) NOT NULL,PRICE double(11,2) NOT NULL, PRIMARY KEY(TradeID)," +
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
    private PreparedStatement AddAdminUser;
    private PreparedStatement GETUSERS;
    private PreparedStatement GETALLUSERS;
    private PreparedStatement GetAdminUsers;
    private PreparedStatement GETSpecficOU;
    private PreparedStatement REMOVEUSER;
    private PreparedStatement EDITUSER;
    private PreparedStatement ADDASSETNAME;
    private PreparedStatement ADDORDER;
    private PreparedStatement GETSPECFICUSER;
    private PreparedStatement ADDTRADE;
    private PreparedStatement ADJUSTBUY;
    private PreparedStatement ADJUSTSELL;
    private PreparedStatement ADJUSTASSETBUY;
    private PreparedStatement ADJUSTASSETSELL;
    private PreparedStatement CLEANUPASSETS;
    private PreparedStatement GETTRADE;
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
            ADDTRADE = connection.prepareStatement(ADD_TRADE);
            ADJUSTBUY = connection.prepareStatement(ADJUST_BUY);
            ADJUSTSELL = connection.prepareStatement(ADJUST_SELL);
            ADJUSTASSETBUY = connection.prepareStatement(ADJUST_OU_BUY);
            ADJUSTASSETSELL = connection.prepareStatement(ADJUST_OU_SELL);
            CLEANUPASSETS = connection.prepareStatement(CLEAN_UP_ASSETS);
            AddAdminUser = connection.prepareStatement(ADD_ADMIN_USER);
            GetAdminUsers = connection.prepareStatement(GET_ADMIN_USERS);
            GETTRADE = connection.prepareStatement(GET_TRADES);
            GETALLUSERS = connection.prepareStatement(GET_ALL_USERS);
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
                BuyOrder border = new BuyOrder(rs.getString("AssetName"),rs.getDouble("price"),rs.getInt("amount"),rs.getString("username"));
                buyOrders.add(border);
            }
        } catch (SQLException | StockExceptions throwables) {
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
               sellOrders.add(new SellOrder(rs.getString("AssetName"),rs.getDouble("price"),rs.getInt("amount"),rs.getString("username")));
            }
        } catch (SQLException | StockExceptions throwables) {
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

    public ArrayList<User> GETALLUSERS()
    {
        ArrayList<User> toReturn = new ArrayList<>();
        ResultSet rs = null;
        ResultSet orgUnit = null;
        OrganisationUnit ORGunit = null;

        try
        {
            rs = GETALLUSERS.executeQuery();
            while(rs.next())
            {
                Blob passBlob = rs.getBlob("password");
                byte[] password = passBlob.getBytes(1,(int)passBlob.length());
                ByteArrayInputStream bStream = new ByteArrayInputStream(password);
                ObjectInputStream oStream = new ObjectInputStream(bStream);
                SerialData serlised = (SerialData) oStream.readObject();
                String PlainPass = serlised.getHiddenValue();
                GETSpecficOU.setString(1,rs.getString("orgunit"));
                orgUnit = GETSpecficOU.executeQuery();
                if(orgUnit.next())
                {
                    ORGunit = new OrganisationUnit(orgUnit.getString("Orgunit"),orgUnit.getDouble("credits"),null);
                }
                User user = new User(rs.getString("username"),PlainPass, ORGunit);
                toReturn.add(user);
            }
        }
        catch(SQLException | IOException | ClassNotFoundException | StockExceptions e)
        {
            e.printStackTrace();
        }
        return toReturn;
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
        OrganisationUnit temporg = null;
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
                if(orgunit.next()) {
                    temporg = new OrganisationUnit(orgunit.getString("Orgunit"),orgunit.getDouble("credits"),null);
                }
                User user = new User(rs.getString("username"),PlainPass, temporg);
                temp.add(user);
            }
        }
        catch (SQLException | StockExceptions | IOException |ClassNotFoundException throwable)
        {
            throwable.printStackTrace();
        }
        return temp;
    }
    public ArrayList<AdminUser> getAdminUsers() {
        ArrayList<AdminUser> temp = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = GetAdminUsers.executeQuery();
            while(rs.next())
            {
                Blob passblob = rs.getBlob("password");
                byte[] password = passblob.getBytes(1,(int) passblob.length());
                ByteArrayInputStream byteStream = new ByteArrayInputStream(password);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteStream);
                SerialData serialised = (SerialData) objectInputStream.readObject();
                String PlainPass = serialised.getHiddenValue();
                AdminUser Adminuser = new AdminUser(rs.getString("username"),PlainPass);
                temp.add(Adminuser);
            }
        }
        catch (SQLException | IOException |ClassNotFoundException throwable)
        {
            throwable.printStackTrace();
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

    public void RemoveUser(AdminUser user2) {
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

    public void AddAdminUser(AdminUser adminUser) {
        try {
            OrganisationUnit ADMIN = new OrganisationUnit("ADMIN",0,null);
            AddOU(ADMIN);
            ByteArrayOutputStream test = new ByteArrayOutputStream();
            ObjectOutputStream parse = new ObjectOutputStream(test);
            parse.writeObject(adminUser.GetPassword());
            byte[] ByteArray = test.toByteArray();
            AddAdminUser.setString(1,adminUser.GetUserID());
            AddAdminUser.setBlob(2, new ByteArrayInputStream(ByteArray), ByteArray.length);
            AddAdminUser.setBoolean(3, true);
            AddAdminUser.setString(4,ADMIN.orgName());
            AddAdminUser.execute();
        }
        catch (SQLException | IOException | StockExceptions throwable) {
            throwable.printStackTrace();
        }
    }


    /**
     *
     * PARAMATERS what they are obvious to mean
     *
     * @param asset
     * @param sellUn
     * @param buyUn
     * @param sellOU
     * @param buyOU
     * @param brought
     * @param selling
     */
    public void AddTrade(String asset, String sellUn, String buyUn, String sellOU, String buyOU, int brought, double selling, int previousBuyQuant, int previousSellQuant)
    {
        //ADD TRADE TO THE DATABASE
        try
        {
            //(Name, OrgUnitBuy,OrgUnitSell,UserSeller,UserBuyer,QTY,PRICE)
            ADDTRADE.setString(1,asset);
            ADDTRADE.setString(2,buyOU);
            ADDTRADE.setString(3,sellOU);
            ADDTRADE.setString(4,sellUn);
            ADDTRADE.setString(5,buyUn);
            ADDTRADE.setInt(6,brought);
            ADDTRADE.setDouble(7,selling);
            ADDTRADE.execute();
        }
        catch (SQLException throwing)
        {
            throwing.printStackTrace();
        }

        // ADJUST ASSETS/OU
        try
        {
            //UPDATE Assets SET amount=(amount - ?) WHERE (OrgUnit=? AND username=? AND AssetType = false);
            ADJUSTBUY.setInt(1,brought);
            ADJUSTBUY.setString(2,buyOU);
            ADJUSTBUY.setString(3,buyUn);
            ADJUSTBUY.executeUpdate();
            //"UPDATE Assets SET amount=(amount - ?) WHERE (OrgUnit=? AND username=? AND AssetType = true);"
            ADJUSTSELL.setInt(1,brought);
            ADJUSTSELL.setString(2,sellOU);
            ADJUSTSELL.setString(3,sellUn);
            ADJUSTSELL.executeUpdate();
            //UPDATE organisationunits SET creadits = creadits - ? WHERE (Orgunit = ?)
            ADJUSTASSETBUY.setDouble(1,selling);
            ADJUSTASSETBUY.setString(2,buyOU);
            ADJUSTASSETBUY.executeUpdate();
            //SELL UPDATE organisationunits SET creadits = creadits + ? WHERE (Orgunit = ?);
            ADJUSTASSETSELL.setDouble(1,selling);
            ADJUSTASSETSELL.setString(2,sellOU);
            ADJUSTASSETSELL.executeUpdate();


        }
        catch (SQLException adjusting)
        {
            adjusting.printStackTrace();
        }


    }
    //CLEAN UP QTY 0
    public void CleanUpAssets() {
        try
        {
            CLEANUPASSETS.executeQuery();
        }
        catch (SQLException cleaning)
        {
            cleaning.printStackTrace();
        }
    }


    /**
     *
     * A function to return all trades based on the Asset Name
     *
     * @param searching (STRING) Asset name to pass through
     * @return Returns null array if no assets matched, Returns an array of trades with matching asset name
     * @throws StockExceptions - This should not through
     *
     * @version 1.0
     *
     * @author Hugh
     */
    public ArrayList<Trade> GetTrades(String searching) throws StockExceptions
    {
        ArrayList<Trade> toReturn = new ArrayList<Trade>();

        //"SELECT * FROM trade_history WHERE Name = ?;";
        ResultSet rawInfo = null;

        // Place Holder to make trade history
        String AName;
        Double Pricing;
        int QTY;
        String buyUN;
        BuyOrder placeHolder;
        String sellUN;
        String sellOu;

        try
        {
            rawInfo = GETTRADE.executeQuery();
            while(rawInfo.next())
            {
                if(rawInfo.getString("AssetName") == searching)
                {
                    //(TradeID int(11) NOT NULL AUTO_INCREMENT, Name varchar(45), OrgUnitBuy varchar(45) NOT NULL,OrgUnitSell varchar(45) NOT NULL,
                    // UserSeller varchar(45) NOT NULL,UserBuyer varchar(45) NOT NULL,QTY int(11) NOT NULL,PRICE double(11,2) NOT NULL
                    AName = rawInfo.getString("AssetName");
                    Pricing = rawInfo.getDouble("PRICE");
                    QTY = rawInfo.getInt("QTY");
                    buyUN = rawInfo.getString("UserBuyer");
                    placeHolder = new BuyOrder(AName,Pricing,QTY,buyUN);
                    sellUN = rawInfo.getString("UserSeller");
                    sellOu = rawInfo.getString("OrgUnitSell");
                    toReturn.add(new Trade(sellOu,sellUN,placeHolder));
                }
            }
        }
        catch (SQLException exp)
        {
            exp.printStackTrace();
        }

        return toReturn;
    }

}