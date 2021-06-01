package Client;

import COMMON.*;

import java.io.*;
import java.io.Serial;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Set;

public class ServerConnection
{
    @Serial
    private static final long serialVersionUID = -2393708718755176852L;
    private static final String HostIP = "127.0.0.1";
    private static final int PORT = 3306;
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
            //objectInputStream = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     *
     * Function to Retrieve Users
     *
     * @return List of Users in db
     *
     * @author Hugh (based on Adam's)
     *
     * @version 1.0
     *
     * This may need to be deleted later but keep just in case
     *
     */
    /**
    public ArrayList<User> getUser()
    {
        ArrayList<User> toReturn = new ArrayList<User>();
        try
        {
            objectOutputStream.writeUTF("GET_ALL_USER");
            objectOutputStream.flush();
            objectOutputStream.writeUTF("");
            objectOutputStream.flush();
            int numUsers = objectInputStream.readInt();
            for (int i = 0; i < numUsers; i++)
            {
                User user = (User) objectInputStream.readObject();
                toReturn.add(user);
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        return toReturn;

    }
     */

    public ArrayList<Asset> GetAssets(String OUName) {
        ArrayList<Asset> assets = new ArrayList<>();
        try {
            objectOutputStream.writeUTF("GET_ASSETS");
            objectOutputStream.flush();
            objectOutputStream.writeUTF(OUName);
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

    public void close() {
        try {
            if(objectInputStream != null)  objectInputStream.close();
            if(objectOutputStream != null) objectOutputStream.close();
            if(socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}