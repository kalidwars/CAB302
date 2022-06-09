package COMMON;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Random;

public class SerialData implements Externalizable {
    private static final long serialVersionUID = 6412491339292614630L;


    private String data;
    private ArrayList<String> hiddenList;


    public SerialData() {
        data = "";
    }
    /**
     * Setups Class for Serilsation
     * This will protect critical information such as password
     * <b>NOTE: This will only work on Strings</b>
     *
     * @param HiddenData (STRING) this is the string that needs encryption
     */
    public SerialData(String HiddenData) {
        //Translate into bytes to upload for later usage
        this.data = HiddenData;
    }
    public String getHiddenValue() {
        return data;
    }

    public byte[] GetBinaryOutput()
    {
        return this.data.getBytes();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        String Ciphered = new String();
        for(char c : data.toCharArray()) {
            Ciphered = Ciphered + (char) (c + 'a');
        }
        out.writeObject(Ciphered);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        String Ciphered = new String();
        Ciphered = (String) in.readObject();
        for(char c : Ciphered.toCharArray()) {
            data = data + (char) (c - 'a');
        }
    }
}

