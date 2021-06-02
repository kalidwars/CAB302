package Server;

import COMMON.*;

import java.io.*;
import java.io.Serial;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * Class for running the server/Accepting commands from clients
 *
 * @author Adam
 *
 * @version 1.2
 *
 */



public class NetworkServer implements Serializable {
    @Serial
    private static final long serialVersionUID = -2393708718755176852L;
    private static final int PORT = 5000;
    private static final int SOCKET_TIMEOUT = 100;
    private AtomicBoolean running = new AtomicBoolean(true);
    private DataSource DataSource = new DataSource();
    /**
     * Handles the connection received from ServerSocket
     * @param socket The socket used to communicate with the currently connected client
     */
    private void handleConnection(Socket socket) throws Exception
    {
        try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
            for(;;) {
                oos.flush();
                String command = ois.readUTF();
                switch (command)
                {
                    case "GET_ASSETS":
                        String OUName = ois.readUTF();
                        ArrayList<Asset> temp = new ArrayList<>();
                        temp = DataSource.getAssets(OUName);
                        oos.writeInt(temp.size());
                        for(Asset asset : temp) {
                            oos.writeObject(asset);
                            oos.flush();
                        }
                    case "ADD_ASSET":
                        Asset asset = (Asset) ois.readObject();
                        DataSource.AddAsset(asset);
                    case "REMOVE_ASSET":
                        Asset assettoremove = (Asset) ois.readObject();
                        DataSource.RemoveAsset(assettoremove);
                    case "GET_ALL_USER":
                        String QStock = ois.readUTF();
                        ArrayList<User> tempUser = new ArrayList<User>();
                        //tempUser = DataSource.convertToUsers();
                        oos.writeInt(tempUser.size());
                        for(User user : tempUser)
                        {
                            oos.writeObject(user);
                            oos.flush();
                        }
                }
                ArrayList<BuyOrder> BuyOrders = DataSource.GetBuyOrders();
                ArrayList<BuyOrder> SellOrders = DataSource.GetSellOrders();
                for(int i = 0; i < BuyOrders.size(); i++) {
                    for(int j = 0; j < SellOrders.size(); j++) {
                        if(BuyOrders.get(i).GetName() == SellOrders.get(j).GetName()
                        && BuyOrders.get(i).getIndPrice() == SellOrders.get(j).getIndPrice()
                        ) {
                            //NEED TO ADD IN WHATS GONNA HAPPEN HERE
                        }
                    }
                }

            }
        }
    }

    /**
     * Returns the port the server is configured to use
     *
     * @return The port number
     */
    public static int getPort() {
        return PORT;
    }

    /**
     * Starts the server running on the default port
     */
    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            serverSocket.setSoTimeout(SOCKET_TIMEOUT);
            for (;;) {
                if (!running.get()) {
                    // The server is no longer running
                    break;
                }
                try {
                    Socket socket = serverSocket.accept();
                    handleConnection(socket);
                } catch (SocketTimeoutException ignored) {
                    // Do nothing. A timeout is normal- we just want the socket to
                    // occasionally timeout so we can check if the server is still running
                } catch (Exception e) {
                    // We will report other exceptions by printing the stack trace, but we
                    // will not shut down the server. A exception can happen due to a
                    // client malfunction (or malicious client)
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            // If we get an error starting up, show an error dialog then exit
            e.printStackTrace();
            System.exit(1);
        }

        // Close down the server
        System.exit(0);
    }

    /**
     * Requests the server to shut down
     */
    public void shutdown() {
        // Shut the server down
        running.set(false);
    }
}
