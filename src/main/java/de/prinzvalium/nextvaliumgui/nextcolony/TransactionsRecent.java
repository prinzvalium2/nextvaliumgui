package de.prinzvalium.nextvaliumgui.nextcolony;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.lib.Util;

public class TransactionsRecent {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionsRecent.class);
    
    public static Date getLastTransactionOfUser(String userName) throws JSONException, IOException {
        
        ArrayList<Transaction> recentTransactions = loadRecentTransactionsWithFilter("limit=1&user=" + userName);
        
        if (recentTransactions == null || recentTransactions.size() == 0)
            return null;
        
        return recentTransactions.get(0).getDate();
    }
    
    public static ArrayList<Transaction> loadRecentTransactionsWithFilter(String filter) throws JSONException, IOException {
        LOGGER.trace("loadRecentTransactionsWithFilter()");
        LOGGER.debug("    LOADING recent transactions (" + filter + "). Please wait...");
        
        String apiCmd = String.format(Util.NEXTCOLONY_API_CMD_LOADRECENTTRANSACTIONS + filter);
        JSONArray jsonRecentTransactions = Util.getJSONArrayFromApiCommand(apiCmd);
        
        ArrayList<Transaction> recentTransactions = new ArrayList<Transaction>();
        
        for (int i = 0; i < jsonRecentTransactions.length(); i++) {
            recentTransactions.add(new Transaction(jsonRecentTransactions.getJSONObject(i)));
        }
        
        return recentTransactions;
    }
    
    public static HashMap<String, HashSet<String>> loadUsersGiftedPlanets() throws JSONException, IOException {
        
        HashMap<String, String> giftedPlanetsFirstUser = loadGiftedPlanetsFirstUser();
        HashMap<String, HashSet<String>> mapUsersGiftedPlanets = new HashMap<String, HashSet<String>>();
        ArrayList<Transaction> transactions = loadRecentTransactionsWithFilter("type=giftplanet&limit=9999");
        
        for (Transaction trans : transactions) {
            
            String planetId = trans.tr_var[0];
            String user = trans.user;
            
            String firstUser = giftedPlanetsFirstUser.get(planetId);
            if (firstUser != null && !firstUser.equalsIgnoreCase(user))
                continue;
            
            HashSet<String> planets = mapUsersGiftedPlanets.get(user);
            if (planets == null) {
                planets = new HashSet<String>();
                mapUsersGiftedPlanets.put(user, planets);
            }
            
            planets.add(planetId);
        }
        
        return mapUsersGiftedPlanets;
    }
    
    public static HashMap<String, String> loadGiftedPlanetsFirstUser() throws JSONException, IOException {
        
        HashMap<String, String> mapPlanetUser = new HashMap<String, String>();
        ArrayList<Transaction> transactions = loadRecentTransactionsWithFilter("type=giftplanet&limit=99999");
        
        for (Transaction trans : transactions) {
            mapPlanetUser.put(trans.tr_var[0], trans.user);
        }
        
        return mapPlanetUser;
    }
    
    public static void main(String[] args) throws JSONException, IOException {
        
        loadGiftedPlanetsFirstUser().get("P-ZE9N1PRA43K");

        loadUsersGiftedPlanets().get("captain.kirk");
    }

}
