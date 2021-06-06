package Client;

import COMMON.*;

import javax.swing.*;
import java.io.*;
import java.io.Serial;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * Class for connecting to the server/sending commands from the client
 *
 * @author Adam
 *
 * @version 1.2
 *
 */


public class ServerConnection implements Serializable
{
    @Serial
    private static final long serialVersionUID = -2393708718755176852L;
    private static final String HostIP = "127.0.0.1";
    private static final int PORT = 5000;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public ServerConnection()
    {
        try
        {
            this.socket = new Socket(HostIP,PORT);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    //Method for getting assets from the server
    public ArrayList<Asset> GetAssets(User user) {
        ArrayList<Asset> assets = new ArrayList<>();
        try {
            objectOutputStream.writeUTF("GET_ASSETS_USER");
            objectOutputStream.flush();
            objectOutputStream.writeObject(user);
            objectOutputStream.flush();
            int numAssets = objectInputStream.readInt();
            for(int i = 0; i < numAssets; i++) {
                Asset asset = (Asset) objectInputStream.readObject();
                assets.add(asset);
            }
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return assets;
    }
    public ArrayList<BuyOrder> GetBuyOrders() {
        ArrayList<BuyOrder> buyOrders = new ArrayList<>();
        try {
            objectOutputStream.writeUTF("GET_BUY_ORDERS");
            objectOutputStream.flush();
            int numAssets = objectInputStream.readInt();
            for(int i = 0; i < numAssets; i++) {
                BuyOrder buyOrder = (BuyOrder) objectInputStream.readObject();
                buyOrders.add(buyOrder);
            }
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return buyOrders;
    }

    //Method for sending an asset to the server for it to be stored in the database
    public boolean AddAsset(Asset asset) {
        try {
            objectOutputStream.writeUTF("ADD_ASSET");
            objectOutputStream.flush();
            objectOutputStream.writeObject(asset);
            objectOutputStream.flush();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    //Method for sending an asset to the server for it to be removed from the database
    public boolean RemoveAsset(Asset asset1) {
        try {
            objectOutputStream.writeUTF("REMOVE_ASSET");
            objectOutputStream.flush();
            objectOutputStream.writeObject(asset1);
            objectOutputStream.flush();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean AddOU(OrganisationUnit OU) {
        try {
            objectOutputStream.writeUTF("ADD_OU");
            objectOutputStream.flush();
            objectOutputStream.writeObject(OU);
            objectOutputStream.flush();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }


    public void close() {
        try {
            if(objectInputStream != null)  objectInputStream.close();
            if(objectOutputStream != null) objectOutputStream.close();
            if(socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean RemoveOU(OrganisationUnit OU) {
        try {
            objectOutputStream.writeUTF("REMOVE_OU");
            objectOutputStream.flush();
            objectOutputStream.writeObject(OU);
            objectOutputStream.flush();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    public ArrayList<OrganisationUnit> GetOUs() {
        ArrayList<OrganisationUnit> OrganisationUnit = new ArrayList<>();
        try {
            objectOutputStream.writeUTF("GET_OUS");
            objectOutputStream.flush();
            int numOUS = objectInputStream.readInt();
            for(int i = 0; i < numOUS; i++) {
                OrganisationUnit OU = (OrganisationUnit) objectInputStream.readObject();
                OrganisationUnit.add(OU);
            }
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return OrganisationUnit;
    }

    public boolean EditOU(OrganisationUnit organisationUnit, double v) {
        try {
            objectOutputStream.writeUTF("EDIT_OU");
            objectOutputStream.flush();
            objectOutputStream.writeObject(organisationUnit);
            objectOutputStream.flush();
            objectOutputStream.writeDouble(v);
            objectOutputStream.flush();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    public boolean AddUser(User test_case_1) {
        try {
            objectOutputStream.writeUTF("ADD_USER");
            objectOutputStream.flush();
            objectOutputStream.writeObject(test_case_1);
            objectOutputStream.flush();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
    public boolean AddAdminUser(AdminUser user) {
        try {
            objectOutputStream.writeUTF("ADD_ADMIN_USER");
            objectOutputStream.flush();
            objectOutputStream.writeObject(user);
            objectOutputStream.flush();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    public ArrayList<User> GetUsers() {
        ArrayList<User> users = new ArrayList<>();
        int numOUS = 0;
        try {
            objectOutputStream.writeUTF("GET_ALL_USERS");
            objectOutputStream.flush();
            numOUS = objectInputStream.readInt();
            for(int i = 0; i < numOUS; i++) {
                User user = (User) objectInputStream.readObject();
                users.add(user);
            }
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean AttemptLogin(String Username, String Password)
    {
        boolean toReturn = false;

        ArrayList<User> rawInfor = this.GetUsers();
        User placeHolder;
        for(int j = 0; j < rawInfor.size(); j++)
        {
            placeHolder = rawInfor.get(j);
            if(placeHolder.GetUserID() == Username)
            {
                toReturn = placeHolder.PassWordCorrect(Password);
            }
        }

        return toReturn;
    }

    public boolean RemoveUser(User test_case_1) {
        try {
            objectOutputStream.writeUTF("REMOVE_USER");
            objectOutputStream.flush();
            objectOutputStream.writeObject(test_case_1);
            objectOutputStream.flush();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    public boolean EditUser(User test_case_1, String newpassword) {
        try {
            objectOutputStream.writeUTF("EDIT_USER_PASSWORD");
            objectOutputStream.flush();
            objectOutputStream.writeObject(test_case_1);
            objectOutputStream.flush();
            objectOutputStream.writeUTF(newpassword);
            objectOutputStream.flush();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    public boolean AddAssetName(String getName) {
        try {
            objectOutputStream.writeUTF("ADD_ASSET_NAME");
            objectOutputStream.flush();
            objectOutputStream.writeUTF(getName);
            objectOutputStream.flush();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    public void AddOrder(BuyOrder Order) {
        try {
            objectOutputStream.writeUTF("ADD_BUY_ORDER");
            objectOutputStream.flush();
            objectOutputStream.writeObject(Order);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void AddOrder(SellOrder Order) {
        try {
            objectOutputStream.writeUTF("ADD_SELL_ORDER");
            objectOutputStream.flush();
            objectOutputStream.writeObject(Order);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetTrades(String AssetName)
    {
        try
        {
            objectOutputStream.writeUTF("GET_TRADES_NAME");
            objectOutputStream.flush();
            objectOutputStream.writeUTF(AssetName);
            objectOutputStream.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<SellOrder> GetSellOrders() {
        ArrayList<SellOrder> sellOrders = new ArrayList<>();
        try {
            objectOutputStream.writeUTF("GET_SELL_ORDERS");
            objectOutputStream.flush();
            int numAssets = objectInputStream.readInt();
            for(int i = 0; i < numAssets; i++) {
                SellOrder sellOrder = (SellOrder) objectInputStream.readObject();
                sellOrders.add(sellOrder);
            }
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return sellOrders;
    }

    public ArrayList<Asset> GetAllAssets_OU(String OU_Name)
    {
        int numAssets = 0;
        ArrayList<Asset> toReturn = new ArrayList<Asset>();
        try
        {
            objectOutputStream.writeUTF("GET_ASSETS_OU");
            objectOutputStream.flush();
            objectOutputStream.writeUTF(OU_Name);
            objectOutputStream.flush();
            numAssets = objectInputStream.readInt();
            for(int i =0; i < numAssets; i++)
            {
                Asset placeHolder = (Asset)objectInputStream.readObject();
                toReturn.add(placeHolder);
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        return toReturn;
    }

    public ArrayList<AdminUser> GetAdminUsers() {
        ArrayList<AdminUser> adminUsers = new ArrayList<>();
        int numOUS = 0;
        try {
            objectOutputStream.writeUTF("GET_ALL_ADMIN_USERS");
            objectOutputStream.flush();
            numOUS = objectInputStream.readInt();
            for(int i = 0; i < numOUS; i++) {
                AdminUser adminUser = (AdminUser) objectInputStream.readObject();
                adminUsers.add(adminUser);
            }
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return adminUsers;
    }

    public void EditAsset(Asset selectAsset, String qty) {
        try {
            objectOutputStream.writeUTF("EDIT_ASSET");
            objectOutputStream.flush();
            objectOutputStream.writeObject(selectAsset);
            objectOutputStream.flush();
            objectOutputStream.writeInt(Integer.parseInt(qty));
            objectOutputStream.flush();
        }
        catch (IOException e) {
        }
    }
}