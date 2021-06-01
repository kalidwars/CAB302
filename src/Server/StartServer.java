package Server;

import javax.print.DocFlavor;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class StartServer {

    //Create SQL connectivity stuff
    private static Connection connection;
    public static DataSource SERVER;

    /**
     *
     * Entry Point of the program this will set up GUI and also server connection
     *
     * @param  args left NULL
     *
     * @version 1.1
     *
     * @author Adam, Hugh
     *
     */
    public static void main(String[] args)
    {
        //Initialise Server
        NetworkServer test = new NetworkServer();
        try {
            test.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SERVER = new DataSource();

    }
}
