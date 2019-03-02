import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.util.regex.Pattern;
/**
 * @author aleksey
 * @since 5/9/17.
 **/
@SuppressWarnings("ALL")
public class SSESamplerGuiPanel extends JPanel{

    public static final Pattern DETECT_JMETER_VAR_REGEX = Pattern.compile("\\$\\{\\w+\\}");
    protected JComboBox<String> protocolSelector;
    protected JTextField urlField;
    protected JTextField hubNameField;
    protected JTextField hubMethodNameField;
    protected JTextArea aspTokenField;
    protected JTextArea requestTokenField;
    protected JLabel hubMethodNameLabel;
    protected JLabel hubNameLabel;
    protected JLabel urlLabel;
    protected JLabel aspTokenLabel;
    protected JLabel requestTokenLabel;

    public SSESamplerGuiPanel() {
    }

    void clearGui() {
        this.protocolSelector.setSelectedItem("http");
        this.urlField.setText("");
        this.hubNameField.setText("");
        this.hubMethodNameField.setText("");
        this.aspTokenField.setText("");
        this.requestTokenField.setText("");
    }

    protected JPanel createUrlPanel() {
        JPanel urlPanel = new JPanel();
        urlPanel.setLayout(new BoxLayout(urlPanel, 0));
        urlPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0), BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Server"), BorderFactory.createEmptyBorder(3, 5, 5, 0))));
        this.protocolSelector = new JComboBox(new String[]{"http", "https"});
        urlPanel.add(this.protocolSelector);
        urlPanel.add(Box.createHorizontalStrut(10));
        this.urlLabel = new JLabel("Host ");
        urlPanel.add(this.urlLabel);
        this.urlField = new JTextField();
        this.urlField.setColumns(15);
        this.urlField.setMaximumSize(new Dimension(2147483647, this.urlField.getMinimumSize().height));
        urlPanel.add(this.urlField);
        this.hubNameLabel = new JLabel("Hub name ");
        urlPanel.add(this.hubNameLabel);
        this.hubNameField = new JTextField();
        this.hubNameField.setColumns(10);
        this.hubNameField.setMaximumSize(new Dimension(214748, this.hubNameField.getMinimumSize().height));
        urlPanel.add(this.hubNameField);
        this.hubMethodNameLabel = new JLabel("Hub method name ");
        urlPanel.add(this.hubMethodNameLabel);
        this.hubMethodNameField = new JTextField();
        this.hubMethodNameField.setColumns(20);
        this.hubMethodNameField.setMaximumSize(new Dimension(2147483647, this.hubMethodNameField.getMinimumSize().height));
        urlPanel.add(this.hubMethodNameField);

        return urlPanel;
    }

    protected JPanel createTokens(){

        JPanel urlPanel = new JPanel();
        urlPanel.setLayout(new BoxLayout(urlPanel, 1));
        urlPanel.setSize(200, 400);
        urlPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0), BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Tokens"), BorderFactory.createEmptyBorder(3, 5, 5, 0))));

        this.aspTokenLabel = new JLabel("Asp.net application token ");
        urlPanel.add(this.aspTokenLabel);
        this.aspTokenField = new JTextArea();
        this.aspTokenField.setRows(2);
        this.aspTokenField.setLineWrap(true);
        this.aspTokenField.setWrapStyleWord(true);


        urlPanel.add( new JScrollPane(this.aspTokenField));
        this.requestTokenLabel = new JLabel("Request verification token ");
        urlPanel.add(this.requestTokenLabel);
        this.requestTokenField = new JTextArea();
        this.requestTokenField.setRows(2);
        urlPanel.add(new JScrollPane(this.requestTokenField));
        return urlPanel;

    }

    protected void addIntegerRangeCheck(JTextField input, int min, int max) {
        this.addIntegerRangeCheck(input, min, max, (JLabel)null);
    }

    protected void addIntegerRangeCheck(final JTextField input, final int min, final int max, final JLabel errorMsgField) {
        input.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                SSESamplerGuiPanel.this.checkIntegerInRange(e.getDocument(), min, max, input, errorMsgField);
            }

            public void removeUpdate(DocumentEvent e) {
                SSESamplerGuiPanel.this.checkIntegerInRange(e.getDocument(), min, max, input, errorMsgField);
            }

            public void changedUpdate(DocumentEvent e) {
                SSESamplerGuiPanel.this.checkIntegerInRange(e.getDocument(), min, max, input, errorMsgField);
            }
        });
    }

    private boolean checkIntegerInRange(Document doc, int min, int max, JTextField field, JLabel errorMsgField) {
        boolean ok = false;
        boolean isNumber = false;

        try {
            String literalContent = this.stripJMeterVariables(doc.getText(0, doc.getLength()));
            if(literalContent.trim().length() > 0) {
                int value = Integer.parseInt(literalContent);
                ok = value >= min && value <= max;
                isNumber = true;
            } else {
                ok = true;
            }
        } catch (NumberFormatException var10) {
            ;
        } catch (BadLocationException var11) {
            ;
        }

        if(field != null) {
            if(ok) {
                field.setForeground(Color.BLACK);
                if(errorMsgField != null) {
                    errorMsgField.setText("");
                }
            } else {
                field.setForeground(Color.RED);
                if(isNumber && errorMsgField != null) {
                    errorMsgField.setText("Value must >= " + min + " and <= " + max);
                }
            }
        }

        return ok;
    }

    protected String stripJMeterVariables(String data) {
        return DETECT_JMETER_VAR_REGEX.matcher(data).replaceAll("");
    }

    static JPanel createAboutPanel(final JComponent parent) {
        JPanel aboutPanel = new JPanel();
        aboutPanel.setLayout(new FlowLayout(2));
        JLabel aboutLabel = new JLabel("<html>SSE Samplers plugin. Test build. <u>Sechiho, Aleksey</u></html>");
        aboutPanel.add(aboutLabel);
        return aboutPanel;
    }

}
