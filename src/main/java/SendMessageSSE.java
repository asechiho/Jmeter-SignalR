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

public class SendMessageSSE extends SSESampler {
    private static final Logger log = LoggingManager.getLoggerForClass();

    public SendMessageSSE() {
    }

    @Override
    protected HubConnection prepareHubConnectClient(SampleResult var1) {

        this.ws_message = this.getMessage();
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
    protected Object doSample(HubConnection var1, final SampleResult var2) throws IOException{

        recMessage = new MessageReceivedHandler() {
            @Override
            public void onMessageReceived(JsonElement json) {
                var2.setResponseData(json.toString(), null);
            }
        };

        var1.received(recMessage);

        var1.send(ws_message);

        return var1;
    }


    protected void postProcessResponse(Object response, SampleResult result) {
        HubConnection frame = (HubConnection) response;

        result.setResponseCode("200");
        if(response != null){
            result.setResponseHeaders("OK\n" + ((HubConnection)response).getState().toString() + "\nconnectionToken=" + ((HubConnection)response).getConnectionToken() +
                    "\nconnectionId=" + ((HubConnection)response).getConnectionId() + "\nconnectionData=" + ((HubConnection)response).getConnectionData() +
                    "\ntime=" + System.currentTimeMillis());
        }
        result.setDataType("text");
    }

    protected void handleUnexpectedFrameException(SampleResult result) {
//        result.sampleEnd();
        this.getLogger().error("Unexpected frame type received");
        result.setResponseCode("Sampler error: unexpected frame type.");
        result.setResponseMessage("Received error");
//        log.error("Close request was not answered with close response, but " + e.getReceivedFrame());
//        result.setResponseCode("WebSocket error: unsuccesful close.");
//        result.setResponseMessage("WebSocket error: received not a close frame, but " + e.getReceivedFrame());
    }

    protected Logger getLogger() {
        return log;
    }

    protected String validateArguments() {
        return null;
    }

    public String getMessage() {
        return this.getPropertyAsString("message");
    }

    public void setMessage(String message) {
        this.setProperty("message", message);
    }
}
