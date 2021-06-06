package Client;

import COMMON.*;

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
            objectOutputStream.writeUTF("GET_ASSETS");
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
        try {
            objectOutputStream.writeUTF("GET_USERS");
            objectOutputStream.flush();
            int numOUS = objectInputStream.readInt();
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
}