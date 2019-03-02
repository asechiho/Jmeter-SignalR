import javax.swing.*;
import java.awt.*;

/**
 * @author aleksey
 * @since 5/9/17.
 **/
public class OpenSSESamplerGuiPanel extends SSESamplerGuiPanel {

    public OpenSSESamplerGuiPanel() {
        this.init();
    }

    private void init() {
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, 1));
        boxPanel.setBorder(BorderFactory.createTitledBorder("Connection"));
        boxPanel.add(Box.createVerticalStrut(3));
        JPanel urlPanel = this.createUrlPanel();
        boxPanel.add(urlPanel);
        JPanel tokenPanel = this.createTokens();
        boxPanel.add(tokenPanel);

        this.setLayout(new BorderLayout());
        this.add(boxPanel, "North");
        this.add(createAboutPanel(this));
    }

    void clearGui() {
        super.clearGui();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(3);
        frame.setSize(800, 400);
        frame.getContentPane().add(new OpenSSESamplerGuiPanel());
        frame.setVisible(true);
    }

}
