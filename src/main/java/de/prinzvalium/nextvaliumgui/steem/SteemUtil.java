package de.prinzvalium.nextvaliumgui.steem;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.lib.Util;
import eu.bittrade.libs.steemj.SteemJ;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.SignedTransaction;
import eu.bittrade.libs.steemj.base.models.operations.CustomJsonOperation;
import eu.bittrade.libs.steemj.base.models.operations.Operation;
import eu.bittrade.libs.steemj.configuration.SteemJConfig;
import eu.bittrade.libs.steemj.enums.PrivateKeyType;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;

public class SteemUtil {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SteemUtil.class);
    
    public static void overwriteDefaultNode() {
        LOGGER.trace("overwriteDefaultNode()");
        
        SteemJConfig myConfig = SteemJConfig.getInstance();
        
        // By default, SteemJ version 0.4.2 and greater is configured to use the official HTTP endpoint provided by Steem
        // ('https://api.steemit.com'). This configuration is adequate for most usecases as the most common plugins are
        // enabled. However, if you want to add additional nodes or connect to a WebSocket endpoint as older versions of
        // SteemJ have done it, the following example shows how to add those endpoints to the connection pool.
        try {
            ArrayList<Pair<URI, Boolean>> endpoints = new ArrayList<>();

            ImmutablePair<URI, Boolean> webSocketEndpointOne = new ImmutablePair<>(new URI("https://steemd.minnowsupportproject.org"), true);
            ImmutablePair<URI, Boolean> webSocketEndpointTwo = new ImmutablePair<>(new URI("https://anyx.io"), true);
            //ImmutablePair<URI, Boolean> webSocketEndpointThree = new ImmutablePair<>(new URI("https://steemd.steemit.com"), true);

            endpoints.add(webSocketEndpointOne );
            endpoints.add(webSocketEndpointTwo );
            //endpoints.add(webSocketEndpointThree );

            myConfig.setEndpointURIs(endpoints);
            
        } catch (URISyntaxException e) {
                throw new RuntimeException("The given URI is not valid.", e);
        }
        
        // If you want to override the whole pool you can do it like this:
//      try {
//          // Reset the currently configured endpoints.
//          myConfig.setEndpointURIs(new ArrayList<Pair<URI, Boolean>>());
//          // Change the default settings if needed.
//          myConfig.addEndpointURI(new URI("https://steemd.minnowsupportproject.org"), true);
//      } catch (URISyntaxException e) {
//              throw new RuntimeException("The given URI is not valid.", e);
//      }        
    }
    
    public static void addPrivatePostinKey(String userName, String privatePostingKey) {
        LOGGER.trace("addPrivatePostinKey()");
        
        SteemJConfig myConfig = SteemJConfig.getInstance();
        List<ImmutablePair<PrivateKeyType, String>> privateKeys = new ArrayList<>();
        privateKeys.add(new ImmutablePair<>(PrivateKeyType.POSTING, privatePostingKey));
        myConfig.getPrivateKeyStorage().addAccount(new AccountName(userName), privateKeys);
   }
    
    public static void setDefaultAccount(String userName, String privatePostingKey) {
        LOGGER.trace("setDefaultAccount()");
        
        SteemJConfig myConfig = SteemJConfig.getInstance();
        //myConfig.setResponseTimeout(100000);
        myConfig.setDefaultAccount(new AccountName(userName));
    }
    
    public static boolean isAccountRegistered(String userName) {
        LOGGER.trace("isAccountRegistered()");
        
        for (AccountName an : SteemJConfig.getInstance().getPrivateKeyStorage().getAccounts()) {
            if (an.getName().equalsIgnoreCase(userName))
                return true;
        }
        return false;
    }
    
    public static void broadcastJSONStringWithRetry(String json, String id) throws SteemInvalidTransactionException, SteemCommunicationException, SteemResponseException {
        LOGGER.trace("broadcastJSONStringWithRetry()");
        
        int retries = 3;
        
        while (true) {
            try {
                broadcastJSONString(json, id);
                return;
            }
            catch(Exception e) {
                LOGGER.error("broadcastJSONStringWithRetry: " + e.getMessage());
            }
            
            Util.sleep(100);
            
            if (--retries <= 0) {
                broadcastJSONString(json, id);
                return;
            }
        }
    }
    
    public static void broadcastJSONString(String json, String id) throws SteemInvalidTransactionException, SteemCommunicationException, SteemResponseException {
        LOGGER.trace("broadcastJSONString()");
        
        // Create a new apiWrapper with your config object.
        SteemJ steemJ = new SteemJ();
        
        ArrayList<AccountName> requiredPostingAuths = new ArrayList<>();
        requiredPostingAuths.add(SteemJConfig.getInstance().getDefaultAccount());

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(new CustomJsonOperation(null, requiredPostingAuths, id, json));

        SignedTransaction signedTransaction = new SignedTransaction(steemJ.getDynamicGlobalProperties().getHeadBlockId(), operations, null);
        signedTransaction.sign();

        steemJ.broadcastTransaction(signedTransaction);  
        
        //Util.sleep(1000); // do not stress the steem server
    }
}
