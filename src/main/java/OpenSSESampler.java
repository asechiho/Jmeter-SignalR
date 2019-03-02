import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
/**
 * @author aleksey
 * @since 5/9/17.
 **/
public class OpenSSESampler extends SSESampler {

    @SuppressWarnings("deprecation")
    private static final Logger log = LoggingManager.getLoggerForClass();

    @Override
    protected HubConnection prepareHubConnectClient(SampleResult var1) {
        this.dispose((HubConnection) threadLocalCachedConnection.get());

        ws_uri = this.getProtocol()+"://" + this.getHost();
        ws_hubName = this.getHubName();
        ws_name_receive_method = this.getHubNameMethod();
        ws_asp_net_token = this.getAspToken();
        ws_request_token = this.getRequestToken();
        HubConnection connect = new HubConnection(ws_uri);
        HubProxy proxy = connect.createHubProxy(ws_hubName);

        Class<String> message = (Class<String>) new String("").getClass();

        proxy.on(ws_name_receive_method, receive, message);

        return connect;
    }

    @Override
    protected Object doSample(HubConnection var1, SampleResult var2) {
        return null;
    }

    @Override
    protected Logger getLogger() {
        return null;
    }

    public String getHost() {
        return this.getPropertyAsString("host");
    }

    public void setHost(String host) {
        this.setProperty("host", host);
    }

    public String getHubName() {
        return this.getPropertyAsString("hubName", "b360hub");
    }

    public void setHubName(String hubName) {
        this.setProperty("hubName", hubName);
    }

    public String getHubNameMethod() {
        return this.getPropertyAsString("hubNameMethod");
    }

    public void setHubNameMethod(String hubNameMethod) {
        this.setProperty("hubNameMethod", hubNameMethod);
    }

    public String getAspToken() {
        return this.getPropertyAsString("AspToken");
    }

    public void setAspToken(String aspToken) {
        this.setProperty("AspToken", aspToken);
    }

    public String getRequestToken() {
        return this.getPropertyAsString("RequestToken");
    }

    public void setRequestToken(String requestToken) {
        this.setProperty("RequestToken", requestToken);
    }


}
