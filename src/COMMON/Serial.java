package COMMON;

import java.io.*;
import java.util.ArrayList;

public class Serial implements Serializable
{

    private static final long serialVersionUID = 6412491339292614630L;

    private String data;
    private ArrayList<String> hiddenlist;

    /**
     *
     * Setups Class for Serilsation
     * This will protect critical information
     *
     * @param HiddenData (STRING) this is the string that needs encryption
     *
     *
     */
    public Serial(String HiddenData)
    {
        data =  HiddenData;
    }

    public String getHiddenData()
    {
        return this.data;
    }

    public void EncryptPassword() throws IOException
    {
        FileOutputStream tempPlaceHolder = new FileOutputStream("tmp/information.txt");

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(tempPlaceHolder);
        objectOutputStream.writeObject(data);
        objectOutputStream.close();
        tempPlaceHolder.close();
    }

    public void UpLoadPassword()
    {}


}