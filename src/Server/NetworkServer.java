package Server;

import COMMON.*;
import CustomExceptions.StockExceptions;

import javax.xml.crypto.Data;
import java.io.*;
import java.io.Serial;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
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
    private DataSource DataSource ;
    private Reconcliation Reconcliation;

    /**
     * Handles the connection received from ServerSocket as well as reconcilation of trades
     * @param socket The socket used to communicate with the currently connected client
     *
     *
     */
    private void handleConnection(Socket socket) {

        try {
            /**
             * We create the streams once at connection time, and re-use them until the client disconnects.
             * This **must** be in the opposite order to the client, because creating an ObjectInputStream
             * reads data, and an ObjectOutputStream writes data.
             */
            final ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            final ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

            /**
             * while(true) here might seem a bit confusing - why do we have an infinite loop?
             * That's because we don't want to exit until the client disconnects, and when they do, readObject()
             * will throw an IOException, which will cause this method to exit. Another option could be to have
             * a "close" message/command sent by the client, but if the client closes improperly, or they lose
             * their network connection, it's not going to get sent anyway.
             */
            while (true) {
                try {
                    /**
                     * Read the command, this tells us what to send the client back.
                     * If the client has disconnected, this will throw an exception.
                     */
                    final String command = inputStream.readUTF();
                    handleClientConnection(socket, inputStream, outputStream, command);
                } catch (SocketTimeoutException e) {
                    /**
                     * We catch SocketTimeoutExceptions, because that just means the client hasn't sent
                     * any new requests. We don't want to disconnect them otherwise. Another way to
                     * check if they're "still there would be with ping/pong commands.
                     */
                    continue;
                }
            }
        } catch (IOException | ClassCastException | ClassNotFoundException | StockExceptions | SQLException e) {
            System.out.println(String.format("Connection %s closed", socket.toString()));
        }
    }

    private void handleClientConnection(Socket socket,ObjectInputStream ois, ObjectOutputStream oos, String command) throws IOException, ClassNotFoundException, StockExceptions, SQLException {
        switch (command)
                {
                    case "GET_ASSETS_USER":
                        User user = (User) ois.readObject();
                        ArrayList<Asset> temp = new ArrayList<>();
                        synchronized (DataSource) {
                            temp = DataSource.getAssets(user);
                        }
                        oos.writeInt(temp.size());
                        for(Asset asset : temp) {
                            oos.writeObject(asset);
                            oos.flush();
                        }
                        break;
                    case "ADD_ASSET":
                        Asset asset = (Asset) ois.readObject();
                        DataSource.AddAsset(asset);
                        break;
                    case "ADD_ASSET_NAME":
                        String AssetName = ois.readUTF();
                        synchronized (DataSource) {
                            DataSource.AddAssetName(AssetName);
                        }
                        break;
                    case "REMOVE_ASSET":
                        Asset assettoremove = (Asset) ois.readObject();
                        synchronized (DataSource) {
                            DataSource.RemoveAsset(assettoremove);
                        }
                        break;
                    case "GET_ALL_USERS":
                        ArrayList<User> tempUser = new ArrayList<User>();
                        int size = 0;
                        synchronized (DataSource) {
                            tempUser = DataSource.getUsers();
                            size = tempUser.size();
                        }
                        oos.writeInt(size);
                        oos.flush();
                        for(User user1 : tempUser)
                        {
                            oos.writeObject(user1);
                            oos.flush();
                        }
                        break;
                    case "ADD_OU":
                        OrganisationUnit OU = (OrganisationUnit) ois.readObject();
                        synchronized (DataSource) {
                            DataSource.AddOU(OU);
                        }
                        break;
                    case "REMOVE_OU":
                        OU = (OrganisationUnit) ois.readObject();
                        synchronized (DataSource) {
                            DataSource.RemoveOU(OU);
                        }
                        break;
                    case "GET_OUS":
                        ArrayList<OrganisationUnit> tempOUs = new ArrayList<>();
                        synchronized (DataSource) {
                            tempOUs = DataSource.getOUs();
                        }
                        oos.writeInt(tempOUs.size());
                        for(OrganisationUnit OrgUnit : tempOUs) {
                            oos.writeObject(OrgUnit);
                            oos.flush();
                        }
                        break;
                    case "EDIT_OU":
                        OU = (OrganisationUnit) ois.readObject();
                        double credits = ois.readDouble();
                        synchronized (DataSource) {
                            DataSource.EditOU(OU, credits);
                        }
                        break;
                    case "ADD_USER":
                        User User1 = (User) ois.readObject();
                        synchronized (DataSource) {
                            DataSource.AddUser(User1);
                        }
                        break;
                    case "GET_USERS":
                        ArrayList<User> tempUsers = new ArrayList<>();
                        synchronized (DataSource) {
                            tempUsers = DataSource.getUsers();
                        }
                        oos.writeInt(tempUsers.size());
                        for(User user1 : tempUsers) {
                            oos.writeObject(user1);
                            oos.flush();
                        }
                        break;
                    case "GET_ADMIN_USERS":
                        ArrayList<AdminUser> tempAdminUsers = new ArrayList<>();
                        synchronized (DataSource) {
                            tempAdminUsers = DataSource.getAdminUsers();
                        }
                        oos.writeInt(tempAdminUsers.size());
                        for(AdminUser adminUser : tempAdminUsers) {
                            oos.writeObject(adminUser);
                            oos.flush();
                        }
                        break;
                    case "REMOVE_USER":
                        User User2 = (User) ois.readObject();
                        synchronized (DataSource) {
                            DataSource.RemoveUser(User2);
                        }
                        break;
                    case "REMOVE_ADMIN_USER":
                        AdminUser adminUser = (AdminUser) ois.readObject();
                        synchronized (DataSource) {
                            DataSource.RemoveUser(adminUser);
                        }
                        break;
                    case "EDIT_USER_PASSWORD":
                        User usertoedit = (User) ois.readObject();
                        String NewPass = ois.readUTF();
                        synchronized (DataSource) {
                            DataSource.EditUser(usertoedit,NewPass);
                        }
                        break;
                    case "ADD_BUY_ORDER":
                        BuyOrder buyorder = (BuyOrder) ois.readObject();
                        synchronized (DataSource) {
                            DataSource.AddOrder(buyorder);
                        }
                        synchronized (Reconcliation) {
                            Reconcliation.Reconcile();
                        }
                        break;
                    case "ADD_SELL_ORDER":
                        SellOrder sellOrder = (SellOrder) ois.readObject();
                        synchronized (DataSource) {
                            DataSource.AddOrder(sellOrder);
                        }
                        synchronized (Reconcliation) {
                            Reconcliation.Reconcile();
                        }
                        break;
                    case "ADD_ADMIN_USER":
                        AdminUser AdminUser = (AdminUser) ois.readObject();
                        synchronized (DataSource) {
                            DataSource.AddAdminUser(AdminUser);
                        }
                        break;
                    case "GET_TRADES_NAME":
                        String Critera = (String) ois.readObject();
                        DataSource.GetTrades(Critera);
                        break;
                    case "GET_BUY_ORDERS":
                        ArrayList<BuyOrder> tempBuyOrders = new ArrayList<>();
                        synchronized (DataSource) {
                            tempBuyOrders  = DataSource.GetBuyOrders();
                        }
                        oos.writeInt(tempBuyOrders.size());
                        oos.flush();
                        for(BuyOrder buyOrder : tempBuyOrders) {
                            oos.writeObject(buyOrder);
                            oos.flush();
                        }
                        break;
                    case "GET_SELL_ORDERS":
                        ArrayList<SellOrder> tempSellOrders = new ArrayList<>();
                        synchronized (DataSource) {
                            tempSellOrders  = DataSource.GetSellOrders();
                        }
                        oos.writeInt(tempSellOrders.size());
                        oos.flush();
                        for(SellOrder sellOrder1 : tempSellOrders) {
                            oos.writeObject(sellOrder1);
                            oos.flush();
                        }
                        break;
                    case "GET_ASSETS_OU":
                        ArrayList<Asset> tmp = new ArrayList<>();
                        String search = ois.readUTF();
                        int sizeOF = 0;
                        synchronized (DataSource)
                        {
                            tmp = DataSource.GetAssetFromOU(search);
                            sizeOF = tmp.size();
                        }
                        oos.writeInt(sizeOF);
                        oos.flush();
                        for(Asset asset1: tmp)
                        {
                            oos.writeObject(asset1);
                            oos.flush();
                        }
                        break;
                    case "GET_ALL_ADMIN_USERS":
                        ArrayList<AdminUser> tmpAdminUsers = new ArrayList<>();
                        sizeOF = 0;
                        synchronized (DataSource)
                        {
                            tmpAdminUsers= DataSource.getAdminUsers();
                            sizeOF = tmpAdminUsers.size();
                        }
                        oos.writeInt(sizeOF);
                        oos.flush();
                        for(AdminUser adminUser1: tmpAdminUsers)
                        {
                            oos.writeObject(adminUser1);
                            oos.flush();
                        }
                        break;
                    case "EDIT_ASSET":
                        Asset asset1 = (Asset) ois.readObject();
                        Integer NewQTY = ois.readInt();
                        DataSource.EditAsset(asset1,NewQTY);
                        break;
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
    public void start() {
        DataSource = new DataSource();
        Reconcliation = new Reconcliation();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            serverSocket.setSoTimeout(SOCKET_TIMEOUT);
            for (; ; ) {
                if (!running.get()) {
                    // The server is no longer running
                    break;
                }
                try {
                    final Socket socket = serverSocket.accept();
                    socket.setSoTimeout(SOCKET_TIMEOUT);
                    final Thread clientThread = new Thread(() -> handleConnection(socket));
                    clientThread.start();
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

    private void ReconcileOrders() {
        ArrayList<BuyOrder> buyOrders = DataSource.GetBuyOrders();
        ArrayList<SellOrder> sellOrders = DataSource.GetSellOrders();
        for(BuyOrder border : buyOrders) {
            for(SellOrder sorder : sellOrders) {
                if(border.GetName().equals(sorder.GetName()) && border.getIndPrice() == sorder.getIndPrice()) {
                    //Store Infomration for later use
                    String AssetName = border.GetName();
                    String SellUN = sorder.GetUser();
                    String BuyUN = border.GetUser();
                    String SellOU = sorder.GetOUID();
                    String BuyOU = border.GetOUID();
                    int QTY = border.getNumAvailable();
                    double Price = border.getNumAvailable() * sorder.getIndPrice();
                    DataSource.AddTrade(AssetName,SellUN,BuyUN,SellOU,BuyOU,QTY,Price,border.getNumAvailable(),sorder.getNumAvailable());
                }
            }
        }
    }

}
