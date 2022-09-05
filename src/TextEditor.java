import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class TextEditor extends JFrame implements ActionListener {

    JTextArea textArea;
    JScrollPane scrollPane;
    JSpinner fontSizeSpinner;
    JButton fontColorButton;
    JComboBox<String> fontPickerBox;
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem newFileItem;
    JMenuItem openFileItem;
    JMenuItem saveFileItem;
    JMenuItem printFileItem;
    JMenuItem exitAppItem;

    TextEditor() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Your text editor!");
        this.setSize(500, 680);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(new Color(0x333333));

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Calibri", Font.PLAIN,25));
        textArea.setBackground(new Color(0x494949));
        textArea.setForeground(Color.WHITE);
        Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
        textArea.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        textArea.setEditable(true);
        textArea.setToolTipText("Click to type.");
        textArea.setCaretColor(Color.LIGHT_GRAY);

        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 550));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);


        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setPreferredSize(new Dimension(50, 25));
        fontSizeSpinner.setValue(20);
        fontSizeSpinner.addChangeListener(e -> textArea.setFont(new Font(textArea.getFont().getFamily(),
                Font.PLAIN, (Integer) fontSizeSpinner.getValue())));
        fontSizeSpinner.setToolTipText("Change the size of the text.");

        fontColorButton = new JButton("Text color");
        fontColorButton.addActionListener(this);
        fontColorButton.setToolTipText("Change the color of the text.");
        fontColorButton.setBackground(new Color(0x606060));
        fontColorButton.setForeground(Color.WHITE);

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontPickerBox = new JComboBox<>(fonts);
        fontPickerBox.addActionListener(this);
        fontPickerBox.setSelectedItem("Calibri");
        fontPickerBox.setToolTipText("Change the font.");

        // ------ MENU BAR ------

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        newFileItem = new JMenuItem("New file");
        openFileItem = new JMenuItem("Open file");
        saveFileItem = new JMenuItem("Save file");
        printFileItem = new JMenuItem("Print file");
        exitAppItem = new JMenuItem("Exit");

        newFileItem.addActionListener(this);
        openFileItem.addActionListener(this);
        saveFileItem.addActionListener(this);
        printFileItem.addActionListener(this);
        exitAppItem.addActionListener(this);

        fileMenu.add(newFileItem);
        fileMenu.add(openFileItem);
        fileMenu.add(saveFileItem);
        fileMenu.add(printFileItem);
        fileMenu.add(exitAppItem);
        menuBar.add(fileMenu);

        menuBar.setBackground(new Color(0xC0C0C0));

        // ------ MENU BAR ------

        this.setJMenuBar(menuBar);
        this.add(fontSizeSpinner);
        this.add(fontColorButton);
        this.add(fontPickerBox);
        this.add(scrollPane);
        this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == fontColorButton) {
            Color color = JColorChooser.showDialog(null, "Choose a color.", Color.WHITE);
            textArea.setForeground(color);
        }

        if (e.getSource() == fontPickerBox) {
            textArea.setFont(new Font((String)fontPickerBox.getSelectedItem(),
                    Font.PLAIN, textArea.getFont().getSize()));
        }

        // ------ MENU OPTIONS ALGORITHM ------

        if (e.getSource() == newFileItem) {
            textArea.setText("");
        }
        if (e.getSource() == openFileItem) {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("Desktop"));
            FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("Text Files", "txt", "HTML files");
            chooser.setFileFilter(fileFilter);

            int response = chooser.showOpenDialog(null);

            if (response == JFileChooser.APPROVE_OPTION) {
                File file = new File(chooser.getSelectedFile().getAbsolutePath());
                Scanner fileIn = null;

                try {
                    fileIn = new Scanner(file);
                    if (file.isFile()) {
                        while (fileIn.hasNextLine()) {
                            String line = fileIn.nextLine() + "\n";
                            textArea.append(line);
                        }
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } finally {
                    assert fileIn != null;
                    fileIn.close();
                }
            }
        }

        // Set file extension manually (.txt, .html, etc.);

        if (e.getSource() == saveFileItem) {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("Desktop"));

            int response = chooser.showSaveDialog(null);

            if (response == JFileChooser.APPROVE_OPTION) {
                File file;
                file = new File(chooser.getSelectedFile().getAbsolutePath());

                try (PrintWriter fileOut = new PrintWriter(file)) {
                    fileOut.println(textArea.getText());
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }

        if (e.getSource() == printFileItem) {
            try {
                textArea.print();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (e.getSource() == exitAppItem) {
            System.exit(0);
        }

        // ------ MENU OPTIONS ALGORITHM ------

    }
}
