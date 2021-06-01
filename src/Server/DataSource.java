package Server;

import COMMON.Asset;
import COMMON.SellOrder;
import CustomExceptions.*;

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
 *
 */
public class DataSource
{

    private static final String GET_ASSETS = "SELECT * FROM assets WHERE OrgUnit=?";
    private Connection connection;

    //Create String For Creating tables in Server
    //Each variable name is what the table is designed for
    //Designed by Hugh and Adam
    private static final String CREATE_TABLE_SELL_ASSETS =
            "CREATE TABLE IF NOT EXITS `assets` (" +
                    "  `AssetID int(50) NOT NULL AUTO_INCREMENT`" +
                    "  `Name` varchar(45) NOT NULL," +
                    "  `OrgUnit` varchar(45) NOT NULL," +
                    "  `Price` double (11) NOT NULL," +
                    "  `Amount` int(11) NOT NULL," +
                    "  `UserName` varchar(45) NOT NULL," +
                    "  PRIMARY KEY (`AssetID`)," +
                    "  KEY `fk_orgunit` (`orgunit`)," +
                    "  CONSTRAINT `fk_orgunit` FOREIGN KEY (`orgunit`) REFERENCES `organisationunits` (`Orgunit`);";
    private static final String CREATE_TABLE_BUY_ORDERS =
            "CREATE TABLE `orders` (" +
                    "`OrderID` int(50) NOT NULL AUTO_INCREMENT," +
                    "‘Name’ varchar(45) NOT NULL," +
                    "‘OrgUnit” varchar(45) NOT NULL," +
                    "‘Price’ double(11) NOT NULL" +
                    "‘Amount’ int(11) NOT NULL," +
                    " `UserName` varchar(45) NOT NULL," +
                    " PRIMARY KEY (`AssetId`)," +
                    "  KEY `fk_orgunit` (`orgunit`)," +
                    "  CONSTRAINT `fk_orgunit` FOREIGN KEY (`orgunit`) REFERENCES `organisationunits` (`Orgunit`));";
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE IF NOT EXITS `users` (" +
                    "  `username` varchar(45) NOT NULL," +
                    "  `password` varchar(45) NOT NULL," +
                    "  `privilege` varchar(45) NOT NULL," +
                    "  `orgunit` varchar(45) NOT NULL," +
                    "  PRIMARY KEY (`username`)," +
                    "  KEY `orgunit` (`orgunit`)," +
                    "  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`orgunit`) REFERENCES `organisationunits` (`Orgunit`);";
    private static final String CREATE_TABLE_HISTORY =
            "CREATE TABLE ‘TRADE_HISTORY’ (" +
                    "`TradeID` int(11) NOT NULL AUTO_INCREMENT," +
                    "`OrgUnitBuy’ varchar(45) NOT NULL," +
                    "`OrgUnitSell` varchar(45) NOT NULL," +
                    "‘UserSeller` varchar(45) NOT NULL," +
                    "`UserBuyer` varchar(45) NOT NULL," +
                    "`QTY` int(11) NOT NULL," +
                    "`PRICE` double(11) NOT NULL," +
                    "PRIMARY KEY(`TradeID`)" +
                    "FOREIGN KEY (`OrgUnitBuy’`) REFERENCES `organisationunits` (`Orgunit`)" +
                    "FOREIGN KEY (`OrgUnitSell`) REFERENCES `organisationunits` (`Orgunit`)" +
                    "FOREIGN KEY (`‘UserSeller`) REFERENCES `‘user’ ` (`‘username’`)" +
                    "FOREIGN KEY (`UserBuyer`) REFERENCES `‘user’ ` (`‘username’`));";
    private static final String CREATE_TABLE_OU =
            "CREATE TABLE IF NOT EXITS `organisationunits` (" +
                    "  `Orgunit` varchar(45) NOT NULL," +
                    "  `credits` int(11) NOT NULL," +
                    "  PRIMARY KEY (`Orgunit`);";
    private PreparedStatement getAssets;


    public DataSource() {
        connection = DBConnection.getInstance();
        try {
            Statement st = connection.createStatement();
            st.execute(CREATE_TABLE_SELL_ASSETS);
            st.execute(CREATE_TABLE_BUY_ORDERS);
            st.execute(CREATE_TABLE_USERS);
            st.execute(CREATE_TABLE_OU);
            st.execute(CREATE_TABLE_HISTORY);
            getAssets = connection.prepareStatement(GET_ASSETS);
        } catch (SQLException ex) {
            ex.printStackTrace();
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
     */
    public ArrayList<Asset> getAssets(String OUName) throws SQLException, StockExceptions
    {
        ArrayList<Asset> temp = new ArrayList<>();
        ResultSet rs = null;
        try {
            getAssets.setString(1,OUName);
            rs = getAssets.executeQuery();
            while(rs.next())
            {
                temp.add(new SellOrder(rs.getString("Name"),rs.getDouble("Amount"),rs.getInt("Price"),rs.getString("UserName")));
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

}