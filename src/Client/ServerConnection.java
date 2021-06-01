package Client;

import COMMON.*;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Set;

public class ServerConnection {
    private static final long serialVersionUID = -7092701502990374424L;
    private static final String HostIP = "127.0.0.1";
    private static final int PORT = 10000;
    private Socket socket = null;
    private ObjectOutputStream objectOutputStream = null;
    private ObjectInputStream objectInputStream = null;

    public ServerConnection() {
        try {
            this.socket = new Socket(HostIP,PORT);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
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
     */
    public ArrayList<User> getUser()
    {
        ArrayList<User> toReturn = new ArrayList<User>();
        try
        {
            objectOutputStream.writeUTF("GET_ALL_USER");
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