package CustomExceptions;

public class StockExceptions extends Exception
{
    /**
     *
     * Excpetion Related to Trade and other stock things
     *
     * @param message (STRING) a message describing fault
     *
     * @author Hugh Glas
     *
     * @version 1.0
     */
    public StockExceptions(String message)
    {
        super(message);
    }
}
