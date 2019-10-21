package de.prinzvalium.nextvaliumgui.lib;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import de.prinzvalium.nextvaliumgui.steem.SteemUtil;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;

public class Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);
    
	private static final String NEXTCOLONY_API_URL = "https://api.nextcolony.io/";
	
    public static final String NEXTCOLONY_API_CMD_LOADPRODUCTION = "loadproduction?id=%s&user=%s";
    public static final String NEXTCOLONY_API_CMD_LOADBUILDINGS = "loadbuildings?id=%s";
    public static final String NEXTCOLONY_API_CMD_LOADRESSOURCEQUANTITIES = "loadqyt?id=%s";
    public static final String NEXTCOLONY_API_CMD_LOADPLANET = "loadplanet?id=%s";
    public static final String NEXTCOLONY_API_CMD_LOADPLANETS = "loadplanets?from=0&to=9999&user=%s";
    public static final String NEXTCOLONY_API_CMD_LOADMISSIONS = "loadfleetmission?user=%s";
    public static final String NEXTCOLONY_API_CMD_LOADRECENTTRANSACTIONS = "transactions?";
    public static final String NEXTCOLONY_API_CMD_LOADCORDDATA = "loadcorddata?x=%s&y=%s";
    
    // prinzvalium
    public static final String PLANETID_06_SANDURZ = "P-ZWZVQCFOS34";
    
    public static SimpleDateFormat NextValiumDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
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
        
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        LOGGER.debug("    " + url.toString());
        
        String jsonText = "";
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            jsonText += inputLine;
        in.close();
        
        //sleep(250); // do not stress nextcolony server
        
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
    
	public static void broadcastJSONObjectToSteem(JSONObject jsonObject) throws SteemInvalidTransactionException, SteemCommunicationException, SteemResponseException {
        SteemUtil.broadcastJSONStringWithRetry(jsonObject.toString(), "nextcolony");
	}
    
    public static void broadcastJSONObjectToSteem(JsonObject jsonObject) throws SteemInvalidTransactionException, SteemCommunicationException, SteemResponseException {
        SteemUtil.broadcastJSONStringWithRetry(jsonObject.toString(), "nextcolony");
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
        prop.load(new FileInputStream("nextvalium.ini"));

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
    
    public static void loadProperties(HashMap<String, String> mapUserAndKey) throws FileNotFoundException, IOException {
        
        Properties prop = new Properties();
        prop.load(new FileInputStream("nextvalium.ini"));

        for(int i = 0; i < 100; i++ ) {
            
            String user = String.format("user%02d", i);
            String value = prop.getProperty(user);
            
            if (value == null)
                break;
            
            String[] values = value.split(",");
            
            mapUserAndKey.put(values[0].trim(), values[1].trim());
        }
        
        setProxy();
    }
       
    public static void main(String[] args) throws IOException {
        getDachColonyUsers();
    }
}
