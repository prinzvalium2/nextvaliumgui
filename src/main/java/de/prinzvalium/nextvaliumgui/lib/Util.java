package de.prinzvalium.nextvaliumgui.lib;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.nextcolony.Shipyard;
import de.prinzvalium.nextvaliumgui.nextcolony.ShipyardShip;

public class Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);
    
    private static final String CONFIG_FILE = "nextvaliumgui.ini";
    
	private static final String NEXTCOLONY_API_URL = "https://api.nextcolony.io/";
	
    public static final String NEXTCOLONY_API_CMD_LOADPRODUCTION = "loadproduction?id=%s&user=%s";
    public static final String NEXTCOLONY_API_CMD_LOADBUILDINGS = "loadbuildings?id=%s";
    public static final String NEXTCOLONY_API_CMD_LOADRESSOURCEQUANTITIES = "loadqyt?id=%s";
    public static final String NEXTCOLONY_API_CMD_LOADPLANET = "loadplanet?id=%s";
    public static final String NEXTCOLONY_API_CMD_LOADUSERPLANETS = "loadplanets?from=0&to=9999&user=%s";
    public static final String NEXTCOLONY_API_CMD_LOADLASTPLANETS = "loadplanets?from=0&to=1&sort=date&user=%s";
    public static final String NEXTCOLONY_API_CMD_LOADALLPLANETS = "loadplanets?from=%s&to=%s";
    public static final String NEXTCOLONY_API_CMD_LOADMISSIONS = "loadfleetmission?user=%s";
    public static final String NEXTCOLONY_API_CMD_LOADRECENTTRANSACTIONS = "transactions?";
    public static final String NEXTCOLONY_API_CMD_LOADCORDDATA = "loadcorddata?x=%s&y=%s";
    public static final String NEXTCOLONY_API_CMD_STATE = "state";
    public static final String NEXTCOLONY_API_CMD_SEASONRANKING = "seasonranking";
    public static final String NEXTCOLONY_API_CMD_MISSIONOVERVIEW = "missionoverview?user=%s";
    public static final String NEXTCOLONY_API_CMD_GALAXYPLANETS = "galaxyplanets";
    public static final String NEXTCOLONY_API_CMD_MISSIONINFO = "missioninfo";
    
    
    public static SimpleDateFormat NextValiumDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static HashMap<String, ShipyardShip> mapShipyardShips = null;
	
	public static JSONObject getJSONObjectFromApiCommand(String apiCommand) throws JSONException, IOException {
        return new JSONObject(getJSONPlainTextFromApiCommandWithRetry(apiCommand));
	}
	
    public static JSONArray getJSONArrayFromApiCommand(String apiCommand) throws JSONException, IOException {
        return new JSONArray(getJSONPlainTextFromApiCommandWithRetry(apiCommand));
    }
    
    private static String getJSONPlainTextFromApiCommandWithRetry(String apiCommand) throws IOException {
        LOGGER.trace("getJSONPlainTextFromApiCommandWithRetry");
        
        int retries = 3;
        
        while (true) {
            try {
                return getJSONPlainTextFromApiCommand(apiCommand);
            }
            catch (IOException e) {
                LOGGER.error("getJSONPlainTextFromApiCommand: " + e.getMessage());
            }
            
            sleep(100);
            
            if (--retries <= 0)
                return getJSONPlainTextFromApiCommand(apiCommand);
        }
    }
    
    private static String getJSONPlainTextFromApiCommand(String apiCommand) throws IOException {
        LOGGER.trace("getJSONPlainTextFromApiCommand");
        
        URL url = new URL(NEXTCOLONY_API_URL + apiCommand);
        
        LOGGER.debug("    open: " + url.toString());
        
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        String jsonText = "";
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            jsonText += inputLine;
        in.close();
        
        //sleep(250); // do not stress nextcolony server
        
        LOGGER.debug("    close: " + url.toString());
        
        return jsonText;
    }
    
    public static ArrayList<String> getDachColonyUsers() throws IOException {
        
        URL url = new URL("https://bloks.xyz/token/DACH");
        
        
        try {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {return null;}
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType){}
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType){}
                }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        catch (Exception e) {
            
        }
        
        
        
        
        
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        LOGGER.debug("    " + url.toString());
        
        String htmlText = "";
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            htmlText += inputLine;
        in.close();
        
        ArrayList<String> list = new ArrayList<String>();
        int indexStart = 0;
        
        while (true) {
            
            indexStart = htmlText.indexOf("<td><a href=", indexStart);
            if (indexStart < 0)
                break;
            
            indexStart = htmlText.indexOf( ">", indexStart+12) + 1;       
            int indexEnd = htmlText.indexOf( "<", indexStart);
            String usr = htmlText.substring(indexStart, indexEnd);
            list.add(usr);
        }
        
        return list;
    }
    
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
    
    public static String readString(String output) {
        System.out.print(output);
        return readString();

    }
    
    public static String readString() {
        byte[] ab = new byte[100];
        try {
            System.in.read(ab);
        } catch (IOException e) {
        }
        return new String(ab).trim();
    }
    
    public static String getDateAsString(Date date) {
        return NextValiumDateFormat.format(date);
    }
    
    public static void setProxy() throws FileNotFoundException, IOException {
        
        Properties prop = new Properties();
        prop.load(new FileInputStream(CONFIG_FILE));

        String value = prop.getProperty("http.proxyHost");
        if (value != null && !value.isEmpty())
            System.setProperty("http.proxyHost", value);
        
        value = prop.getProperty("http.proxyPort");
        if (value != null && !value.isEmpty())
            System.setProperty("http.proxyPort", value);
        
        value = prop.getProperty("https.proxyHost");
        if (value != null && !value.isEmpty())
            System.setProperty("https.proxyHost", value);
        
        value = prop.getProperty("https.proxyPort");
        if (value != null && !value.isEmpty())
            System.setProperty("https.proxyPort", value);
    }
    
    public static ArrayList<String> loadProperties() throws FileNotFoundException, IOException {
        
        File f = new File(CONFIG_FILE);
        if (!f.exists())
            f.createNewFile();
        
        ArrayList<String> listUsers = new ArrayList<String>();
        
        Properties prop = new Properties();
        prop.load(new FileInputStream(CONFIG_FILE));

        for(int i = 0; i < 100; i++ ) {
            
            String user = String.format("user%02d", i);
            String value = prop.getProperty(user);
            
            if (value == null)
                break;
            
            String[] values = value.split(",");
            
            SteemUtil.addPrivatePostinKey(values[0].trim(), values[1].trim());
            listUsers.add(values[0].trim());
        }
        
        setProxy();
        
        return listUsers;
    }
    
    public static Color getUserColor(String userName) {
        
        if (userName == null)
            return Color.BLACK;
        
        int[] colorValues = new int[3];

        colorValues[0] = ThreadLocalRandom.current().nextInt(1, 13) * 20;
        colorValues[1] = ThreadLocalRandom.current().nextInt(1, 13) * 20;
        colorValues[2] = ThreadLocalRandom.current().nextInt(1, 13) * 20;
        
        String s = userName.toUpperCase();
        
        int i = 0;
        for (char c : s.toCharArray())  {
            if (c >= 0x30 && c <= 0x39)
                colorValues[i++] = (c - 0x30) * 28;
            else if (c >= 0x41 && c <= 0x5A)
                colorValues[i++] = (c - 0x41) * 10;
            if (i > 2)
                break;
        }
        int maxBrightness = 230;
        
        int max = 0;
        if (colorValues[0] >= colorValues[1] && colorValues[0] >= colorValues[2])
            max = colorValues[0];
        if (colorValues[1] >= colorValues[0] && colorValues[1] >= colorValues[2])
            max = colorValues[1];
        if (colorValues[2] >= colorValues[0] && colorValues[2] >= colorValues[1])
            max = colorValues[2];
        
        int brighter = maxBrightness - max;
        colorValues[0] += brighter;
        colorValues[1] += brighter;
        colorValues[2] += brighter;
        
        for (int j = 0; j < 3; j++) {
            if (colorValues[j] < 0)
                colorValues[j] = 0;
            if (colorValues[j] > maxBrightness)
                colorValues[j] = maxBrightness;
        }
          
        return new Color(colorValues[0], colorValues[1], colorValues[2]);
    }
    
    public static int getSpeedOfShip(String shipType) {
        
        if (mapShipyardShips == null) {
            try {
                mapShipyardShips = Shipyard.loadShipyard("nextcolony", "1", "Earth");
            } catch (JSONException | IOException e) {
                return -1;
            }
        }
        return mapShipyardShips.get(shipType).getSpeed();
    }
       
    public static int getSlowestSpeedOfShips(HashMap<String, Integer> mapShips) {
        
        if (mapShipyardShips == null) {
            try {
                mapShipyardShips = Shipyard.loadShipyard("nextcolony", "1", "Earth");
            } catch (JSONException | IOException e) {
                return -1;
            }
        }
        int lowestSpeed = 999;
        for (Entry<String, Integer> entry : mapShips.entrySet()) {
            
            ShipyardShip ship = mapShipyardShips.get(entry.getKey());
            int speed = ship.getSpeed();
            if (speed < lowestSpeed)
                lowestSpeed = speed;
        }
        
        return lowestSpeed;
    }
       
   public static void main(String[] args) throws IOException {
        getDachColonyUsers();
    }
}
