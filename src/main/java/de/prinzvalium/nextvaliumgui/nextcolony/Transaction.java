package de.prinzvalium.nextvaliumgui.nextcolony;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Transaction {
    
    public Date date;
    public String user;
    public String[] tr_var = new String[8];

    Transaction(JSONObject jsonTransaction) {
        date = new Date(jsonTransaction.getLong("date") * 1000);
        user = jsonTransaction.getString("user");
        
        for (int i = 0; i < tr_var.length; i++) {
            try {
                tr_var[i] = jsonTransaction.getString("tr_var" + (i+1));
            }
            catch (JSONException e) {
                tr_var[i] = null;
            }
        }
    }

    public Date getDate() {
        return date;
    }
}
