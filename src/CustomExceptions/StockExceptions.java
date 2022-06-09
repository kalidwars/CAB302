package CustomExceptions;

import java.io.Serial;
import java.io.Serializable;

public class StockExceptions extends Exception implements Serializable
{
    @Serial
    private static final long serialVersionUID = -2393708718755176852L;
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
