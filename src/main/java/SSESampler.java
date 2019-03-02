import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.http.CookieCredentials;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.log.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

/**
 * @author aleksey
 * @since 5/9/17.
 **/
@SuppressWarnings({"ALL", "Since15"})
public abstract class SSESampler extends AbstractSampler{

    protected static final ThreadLocal<HubConnection> threadLocalCachedConnection = new ThreadLocal();
    public HeaderManager headerManager;
    protected CookieCredentials cookies;
    protected String state;
    protected String protocol;
    protected SubscriptionHandler1<String> receive;
    protected MessageReceivedHandler recMessage;
    protected microsoft.aspnet.signalr.client.Logger logs;
    protected static String ws_name_receive_method;
    protected static String ws_uri;
    protected static String ws_request_token;
    protected static String ws_asp_net_token;
    protected static String ws_hubName;
    protected static short ws_state_close;
    protected static String ws_message;

    public SSESampler() {
    }


    protected abstract HubConnection prepareHubConnectClient(SampleResult var1);

    public SampleResult sample(Entry entry) {
        Logger log = this.getLogger();
        SampleResult result = new SampleResult();
        result.setSampleLabel(this.getName());
        String validationError = null;
        Map<String, String> error = new HashMap<>();
        if (validationError != null) {
            result.setResponseCode("Sampler error");
            result.setResponseMessage("Sampler error: " + validationError);
            return result;
        } else {

            HubConnection wsClient = this.prepareHubConnectClient(result);
            if (wsClient == null) return result;
            else if (this.headerManager != null) {
                Map<String, String> gotNewConnection = this.convertHeaders(this.headerManager);
                Request request = new Request("GET");
                request.setHeaders(gotNewConnection);

                cookies = new CookieCredentials();
                cookies.addCookie("__RequestVerificationToken", ws_request_token);
                cookies.addCookie(".AspNet.ApplicationCookie", ws_asp_net_token);
                wsClient.setCredentials(cookies);
                logs = wsClient.getLogger();

                result.setRequestHeaders((String) gotNewConnection.entrySet().stream().map((e) -> {
                    return (String) e.getKey() + ": " + (String) e.getValue();
                }).collect(Collectors.joining("\n")));

            }

            boolean gotNewConnection1 = false;
            result.sampleStart();

            try {

                if(wsClient.getConnectionId() == null){
                    wsClient.start();

                    while(!wsClient.start().isDone());

                    result.connectEnd();
                    ws_state_close = 0;
                    gotNewConnection1 = true;

                    if (wsClient.getState().toString() == "Disconnected") {

                        result.setResponseCode("500");
                        result.setResponseMessage("Error");
                        result.setResponseHeaders((String) error.entrySet().stream().map((header) -> {
                            return (String) header.getKey() + ": " + (String) header.getValue();
                        }).collect(Collectors.joining("\n")));
                        threadLocalCachedConnection.set(wsClient);
                        return result;
                    }

                }

                Object response = this.doSample(wsClient, result);

                result.sampleEnd();
                result.setSamplerData("Connect URL:\n" + wsClient.getUrl() + (!gotNewConnection1 ? "\n(using existing connection)" : "") + "\n");
                if (gotNewConnection1) {
                    result.setResponseCode("200");
                    result.setResponseHeaders((String) error.entrySet().stream().map((header) -> {
                        return (String) header.getKey() + ": " + (String) header.getValue();
                    }).collect(Collectors.joining("\n")));
                    log.info("sse open for " + wsClient.getConnectionId() + ": state = " + wsClient.getState());

                } else {
                    result.setResponseCodeOK();
                }

                this.postProcessResponse(response, result);
                result.setSuccessful(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (gotNewConnection1) {
                threadLocalCachedConnection.set(wsClient);
            }
            if(ws_state_close == 1){
                threadLocalCachedConnection.set((HubConnection)null);
                wsClient = null;
            }
            return result;
        }
    }

    protected abstract Object doSample(HubConnection var1, SampleResult var2) throws IOException, InterruptedException;

    protected void postProcessResponse(Object response, SampleResult result) {
    }

    protected void handleUnexpectedFrameException(SampleResult result) {
        result.sampleEnd();
        this.getLogger().error("Unexpected frame type received");
        result.setResponseCode("Sampler error: unexpected frame type.");
        result.setResponseMessage("Received error");
    }

    public void addTestElement(TestElement el) {
        if(el instanceof HeaderManager) {
            this.headerManager = (HeaderManager)el;
        } else {
            super.addTestElement(el);
        }

    }

    protected void dispose(HubConnection HubClient) {
        if(HubClient != null) {
            this.getLogger().debug("Closing streams for existing event-stream connection");
            HubClient.stop();
        }
        this.state = "STOP";
    }

    private Map<String, String> convertHeaders(HeaderManager headerManager) {
        HashMap headers = new HashMap();

        for(int i = 0; i < headerManager.size(); ++i) {
            Header header = headerManager.get(i);
            headers.put(header.getName(), header.getValue());
        }
        return headers;
    }

    protected abstract Logger getLogger();

    protected void setProtocol(String protocol){
        this.protocol = protocol;
    }

    protected String getProtocol(){
        return this.protocol == null ? "http" : this.protocol;
    }
}
