package userInterface;
import COMMON.*;
import CustomExceptions.StockExceptions;

import java.io.IOException;

public class main {
    public static void main(String[] args) throws StockExceptions, IOException {
        OrganisationUnit TestOU = new OrganisationUnit("TestOU",5000,null);
        User Testing = new User("test","test",TestOU);
        login.main(null);
    }
}
