import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

import java.awt.*;
/**
 * @author aleksey
 * @since 5/9/17.
 **/
public class OpenSSESamplerGUI extends AbstractSamplerGui {

    private OpenSSESamplerGuiPanel settingsPanel;

    public OpenSSESamplerGUI() {
        this.init();
    }

    private void init() {
        this.setLayout(new BorderLayout(0, 5));
        this.setBorder(this.makeBorder());
        this.add(this.makeTitlePanel(), "North");
        this.settingsPanel = new OpenSSESamplerGuiPanel();
        this.add(this.settingsPanel, "Center");
    }

    public void clearGui() {
        super.clearGui();
        this.settingsPanel.clearGui();
    }

    public String getStaticLabel() {
        return "ServerSentEvent(SSE) Open Connection";
    }

    public String getLabelResource() {
        return null;
    }

    public TestElement createTestElement() {
        OpenSSESampler element = new OpenSSESampler();
        this.configureTestElement(element);
        return element;
    }

    public void configure(TestElement element) {
        super.configure(element);
        if(element instanceof OpenSSESampler) {
            OpenSSESampler sampler = (OpenSSESampler)element;
            this.settingsPanel.protocolSelector.setToolTipText(sampler.getProtocol());
            this.settingsPanel.urlField.setText(sampler.getHost());
            this.settingsPanel.hubNameField.setText(sampler.getHubName());
            this.settingsPanel.hubMethodNameField.setText(sampler.getHubNameMethod());

        }

    }

    public void modifyTestElement(TestElement element) {
        this.configureTestElement(element);
        if(element instanceof OpenSSESampler) {
            OpenSSESampler sampler = (OpenSSESampler)element;
            sampler.setProtocol(this.settingsPanel.protocolSelector.getToolTipText());
            sampler.setHost(this.settingsPanel.urlField.getText());
            sampler.setHubName(this.settingsPanel.hubNameField.getText());
            sampler.setHubNameMethod(this.settingsPanel.hubMethodNameField.getText());
        }

    }
}
