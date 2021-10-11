package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.PDFTextStripper;
import org.odftoolkit.odfdom.doc.OdfTextDocument;

import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextFIle extends JFrame
{
    public JFileChooser filechooser = new JFileChooser();
    public FileDialog openDia;
    public static StyledDocument doc = new DefaultStyledDocument();
    public static JTextPane workArea = new JTextPane(doc);
    public static JTextPane testArea = new JTextPane();
    public JLabel jLabelDate = new JLabel("Date");
    public JLabel jLabelTime = new JLabel("Time");
    public JPanel jPanelNorth = new JPanel();
    public UndoManager um;
    public PDDocument initfile;
    public static String readfile;


    //Main function that sets the implementation of the page
    public static void main(String[] args)
    {

        setUIFont();
        TextFIle frame = new TextFIle();

        frame.setVisible(true);
    }
    //Sets the global font for the text editor menu bar
    public static void setUIFont()
    {
        Font f = new Font("楷体",Font.PLAIN,20);
        String[] names ={ "Title","Label", "CheckBox", "PopupMenu","MenuItem", "CheckBoxMenuItem",
                "JRadioButtonMenuItem","ComboBox", "Button", "Tree", "ScrollPane",
                "TabbedPane", "EditorPane", "TitledBorder", "Menu", "TextArea",
                "OptionPane", "MenuBar", "ToolBar", "ToggleButton", "ToolTip",
                "ProgressBar", "TableHeader", "Panel", "List", "ColorChooser",
                "PasswordField","TextField", "Table", "Label", "Viewport",
                "RadioButtonMenuItem","RadioButton", "DesktopPane", "InternalFrame", "JTextPane"
        };
        for (String item : names) {
            UIManager.put(item+ ".font",f);
        }
    }
    //The main TextFIle that designs the text editor's window content
    public TextFIle()
    {
        openDia = new FileDialog(this,"Open(O)",FileDialog.LOAD);
        um = new UndoManager();
        //Design the entire UI, including size, name, layout
        setTitle("Text Editor");
        setBounds(300,300,700,700);
        setJMenuBar(createJMenuBar());
        JScrollPane imgScrollPane = new JScrollPane(workArea);
        GridLayout gridLayout = new GridLayout(1, 2);
        //Add components to the boundary layout
        jPanelNorth.setLayout(gridLayout);
        jPanelNorth.add(jLabelDate);
        jPanelNorth.add(jLabelTime);
        workArea.getDocument().addUndoableEditListener(um);
        add(jPanelNorth, BorderLayout.NORTH);
        add(imgScrollPane,BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    //Build the text editor's menu bar and add the desired function labels to it
    public JMenuBar createJMenuBar()
    {
        JMenuBar menubar=new JMenuBar();

        JMenu menuFile=new JMenu("File");
        JMenu menuEdit=new JMenu("Edit");
        JMenu menuAbout=new JMenu("Help");


        //put them in the menubar
        menubar.add(menuFile);
        menubar.add(menuEdit);
        menubar.add(menuAbout);

        return menubar;
    }



}
