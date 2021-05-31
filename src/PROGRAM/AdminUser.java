package PROGRAM;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

public class AdminUser extends User
{
    public AdminUser(String id, String passWord) throws NoSuchAlgorithmException, NoSuchPaddingException
    {
        super(id, passWord, null);
    }

    private void promote_user()
    {

    }

    private void decrease_user()
    {

    }
}
