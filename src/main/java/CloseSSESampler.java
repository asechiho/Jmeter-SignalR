

import com.google.gson.JsonElement;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.IOException;

/**
 * @author aleksey
 * @since 5/9/17.
 **/

public class CloseSSESampler extends SSESampler {
    private static final Logger log = LoggingManager.getLoggerForClass();

    public CloseSSESampler() {
    }

    @Override
    protected HubConnection prepareHubConnectClient(SampleResult var1) {
        HubConnection hbClient = (HubConnection)threadLocalCachedConnection.get();
        if(hbClient == null) {
            log.warn("There is no connection; nothing to close.");
            var1.setSamplerData("No request sent.");
            var1.setResponseMessage("No connection; nothing to close.");
            return null;
        } else {
            return hbClient;
        }
    }

    @Override
    protected Object doSample(HubConnection var1, SampleResult var2) throws IOException{
        short closeStatus = 1000;
        String reason = "sampler requested close";
        var2.setSamplerData("Requested connection close with status " + closeStatus + " and reason \'" + reason + "\'.");
        recMessage = new MessageReceivedHandler() {
            @Override
            public void onMessageReceived(JsonElement json) {
                var2.setResponseData(json.toString(), null);
            }
        };

        var1.received(recMessage);

        var1.stop();

        this.ws_state_close = 1;

        return var1;
    }


    protected void postProcessResponse(Object response, SampleResult result) {
        HubConnection frame = (HubConnection) response;

        result.setResponseMessage("Connection closed.");
        result.setResponseCode("200");
        if(response != null){
            result.setResponseHeaders("OK\n" + ((HubConnection)response).getState().toString() + "\nconnectionToken=" + ((HubConnection)response).getConnectionToken() +
                    "\nconnectionId=" + ((HubConnection)response).getConnectionId() + "\nconnectionData=" + ((HubConnection)response).getConnectionData() +
                    "\ntime=" + System.currentTimeMillis());
        }
        result.setDataType("text");
    }

    protected void handleUnexpectedFrameException(SampleResult result) {
//        log.error("Close request was not answered with close response, but " + e.getReceivedFrame());
//        result.setResponseCode("WebSocket error: unsuccesful close.");
//        result.setResponseMessage("WebSocket error: received not a close frame, but " + e.getReceivedFrame());
        this.getLogger().error("Unexpected frame type received");
        result.setResponseCode("Sampler error: unexpected frame type.");
        result.setResponseMessage("Received error");
    }

    protected Logger getLogger() {
        return log;
    }

    protected String validateArguments() {
        return null;
    }
}
