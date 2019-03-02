import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;
import java.awt.*;

/**
 * @author aleksey
 * @since 5/9/17.
 **/

public class SendMessageSamplerGUI extends AbstractSamplerGui {
    private JTextField message;

    public SendMessageSamplerGUI() {
        this.init();
    }

    private void init() {
        this.setLayout(new BorderLayout(0, 5));
        this.setBorder(this.makeBorder());
        this.add(this.makeTitlePanel(), "North");
        JPanel layoutPanel = new JPanel();
        layoutPanel.setLayout(new BorderLayout());
        JPanel requestSettingsPanel = new JPanel(new FlowLayout(0));
        requestSettingsPanel.setBorder(BorderFactory.createTitledBorder("Data (close frame)"));
        requestSettingsPanel.add(new JLabel("Request message: "));
        this.message = new JTextField();
        this.message.setColumns(50);
        requestSettingsPanel.add(this.message);
        layoutPanel.add(requestSettingsPanel, "North");
        layoutPanel.add(SSESamplerGuiPanel.createAboutPanel(this));
        this.add(layoutPanel, "Center");
    }

    public String getStaticLabel() {
        return "SSE Send Message";
    }

    public String getLabelResource() {
        return null;
    }

    public TestElement createTestElement() {
        SendMessageSSE element = new SendMessageSSE();
        this.configureTestElement(element);
        return element;
    }

    public void configure(TestElement element) {
        super.configure(element);
        if(element instanceof SendMessageSSE) {
            SendMessageSSE sampler = (SendMessageSSE)element;
            this.message.setText(sampler.getMessage());
        }

        super.configure(element);
    }

    public void clearGui() {
        super.clearGui();
        //this.message.setText("");
    }

    public void modifyTestElement(TestElement testElement) {
        this.configureTestElement(testElement);
        if(testElement instanceof SendMessageSSE) {
            SendMessageSSE sampler = (SendMessageSSE)testElement;
            sampler.setMessage(this.message.getText());
        }

    }
}
