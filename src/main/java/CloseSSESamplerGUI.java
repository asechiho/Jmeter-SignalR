import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;
import java.awt.*;

/**
 * @author aleksey
 * @since 5/9/17.
 **/

public class CloseSSESamplerGUI extends AbstractSamplerGui {

    public CloseSSESamplerGUI() {
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
        layoutPanel.add(requestSettingsPanel, "North");
        layoutPanel.add(SSESamplerGuiPanel.createAboutPanel(this));
        this.add(layoutPanel, "Center");
    }

    public String getStaticLabel() {
        return "SSE Connection Close";
    }

    public String getLabelResource() {
        return null;
    }

    public TestElement createTestElement() {
        CloseSSESampler element = new CloseSSESampler();
        this.configureTestElement(element);
        return element;
    }

    public void configure(TestElement element) {
        super.configure(element);
    }

    public void clearGui() {
        super.clearGui();
    }

    public void modifyTestElement(TestElement testElement) {
        this.configureTestElement(testElement);
    }
}
