package Client;

import COMMON.*;

import java.io.*;
import java.io.Serial;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Set;

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

    public void AddAsset(Asset asset) {
        try {
            objectOutputStream.writeUTF("ADD_ASSET");
            objectOutputStream.flush();
            objectOutputStream.writeObject(asset);
            objectOutputStream.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void RemoveAsset(Asset asset1) {
        try {
        objectOutputStream.writeUTF("REMOVE_ASSET");
        objectOutputStream.flush();
        objectOutputStream.writeObject(asset1);
        objectOutputStream.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}