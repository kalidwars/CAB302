package COMMON;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class Serial implements Serializable {
    private static final long serialVersionUID = 6412491339292614630L;


    private String data;
    private ArrayList<String> hiddenList;

    /**
     * Setups Class for Serilsation
     * This will protect critical information such as password
     * <b>NOTE: This will only work on Strings</b>
     *
     * @param HiddenData (STRING) this is the string that needs encryption
     */
    public Serial(String HiddenData) {
        //Translate into bytes to upload for later usage
        this.data = HiddenData;
    }

    public byte[] GetBinaryOutput()
    {
        return this.data.getBytes();
    }
}

