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

public class TextFIle extends JFrame {
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
    public static void main(String[] args) {

        setUIFont();
        TextFIle frame = new TextFIle();

        frame.setVisible(true);
    }

    //Sets the global font for the text editor menu bar
    public static void setUIFont() {
        Font f = new Font("楷体", Font.PLAIN, 20);
        String[] names = {"Title", "Label", "CheckBox", "PopupMenu", "MenuItem", "CheckBoxMenuItem",
                "JRadioButtonMenuItem", "ComboBox", "Button", "Tree", "ScrollPane",
                "TabbedPane", "EditorPane", "TitledBorder", "Menu", "TextArea",
                "OptionPane", "MenuBar", "ToolBar", "ToggleButton", "ToolTip",
                "ProgressBar", "TableHeader", "Panel", "List", "ColorChooser",
                "PasswordField", "TextField", "Table", "Label", "Viewport",
                "RadioButtonMenuItem", "RadioButton", "DesktopPane", "InternalFrame", "JTextPane"
        };
        for (String item : names) {
            UIManager.put(item + ".font", f);
        }
    }

    //The main TextFIle that designs the text editor's window content
    public TextFIle() {
        openDia = new FileDialog(this, "Open(O)", FileDialog.LOAD);
        um = new UndoManager();
        //Design the entire UI, including size, name, layout
        setTitle("Text Editor");
        setBounds(300, 300, 700, 700);
        setJMenuBar(createJMenuBar());
        JScrollPane imgScrollPane = new JScrollPane(workArea);
        GridLayout gridLayout = new GridLayout(1, 2);
        //Add components to the boundary layout
        jPanelNorth.setLayout(gridLayout);
        jPanelNorth.add(jLabelDate);
        jPanelNorth.add(jLabelTime);
        workArea.getDocument().addUndoableEditListener(um);
        add(jPanelNorth, BorderLayout.NORTH);
        add(imgScrollPane, BorderLayout.CENTER);
        time().start();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    //Build the text editor's menu bar and add the desired function labels to it
    public JMenuBar createJMenuBar() {
        JMenuBar menubar = new JMenuBar();

        JMenu menuFile = new JMenu("File");
        JMenu menuEdit = new JMenu("Edit");
        JMenu menuAbout = new JMenu("Help");

        //File functions
        menuFile.add(newopen());
        menuFile.add(open());
        menuFile.addSeparator();
        menuFile.add(save());
        menuFile.add(print());
        menuFile.add(exit());

        menuEdit.add(search());
        //put them in the menubar
        menubar.add(menuFile);
        menubar.add(menuEdit);
        menubar.add(menuAbout);

        return menubar;
    }

    //the function to display time on the top of the text-editor
    private Timer time()
    {
        return new Timer(1000, e -> {
            long timemillis = System.currentTimeMillis();
            SimpleDateFormat date = new SimpleDateFormat("yyyy / MM / dd ");
            jLabelDate.setText("   Date:  " + date.format(new Date(timemillis)));
            SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss ");
            jLabelTime.setText("   Time:  " + time.format(new Date(timemillis)));
        });
    }


    //For the next is diff functions about File
    //open a new page
    public JMenuItem newopen()
    {
        JMenuItem newopen = new JMenuItem("New(N)", KeyEvent.VK_N);
        newopen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        newopen.addActionListener(e -> workArea.setDocument(new DefaultStyledDocument()));
        return newopen;

    }

    //open a file which already been written
    public JMenuItem open() {
        JMenuItem open = new JMenuItem("Open(O)", KeyEvent.VK_O);
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        open.addActionListener(arg0 -> {
            openDia.setVisible(true);
            String dirPath = openDia.getDirectory();
            String fileName = openDia.getFile();
            if (dirPath == null || fileName == null) {
                return;
            }
            workArea.setText("");
            workArea.setText(OPEN(dirPath, fileName));
        });
        return open;

    }

    //a function to help open
    public static String OPEN(String dirpath,String filename){
        File file0 = new File(dirpath,filename);
        char[] lines = new char[(int) file0.length()];
        try {
            FileReader fin=new FileReader(file0);
            try {
                fin.read(lines);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return new String(lines);
    }

    private JMenuItem save()
    {
        JMenuItem save = new JMenuItem("Save(S)", KeyEvent.VK_S);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        save.addActionListener(e -> {
            int i=filechooser.showSaveDialog(TextFIle.this);
            if(i==JFileChooser.APPROVE_OPTION)
            {
                File f=filechooser.getSelectedFile();
                try
                {
                    SAVE(f,workArea.getText());
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        return save;
    }

    //a function to help save
    public static void SAVE(File f,String Text){
        FileOutputStream out= null;
        try {
            out = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            assert out != null;
            out.write(Text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //here is function about print files
    private JMenuItem print()
    {
        JMenuItem print = new JMenuItem("Print(P)", KeyEvent.VK_P);
        print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
        print.addActionListener(e -> {
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            PrintService[] printService = PrintServiceLookup.lookupPrintServices(flavor, pras);
            PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
            PrintService service;
            service = ServiceUI.printDialog(null, 100, 100, printService, defaultService, flavor, pras);
            if (service != null)
            {
                DocPrintJob job = service.createPrintJob();
                DocAttributeSet das = new HashDocAttributeSet();
                Doc doc = new SimpleDoc(createJMenuBar().getMenu(0).getText().getBytes(), flavor, das);
                try {
                    job.print(doc, pras);
                } catch (PrintException printException) {
                    printException.printStackTrace();
                }
            }
        });
        return print;
    }

    //exit from the text-editor
    private JMenuItem exit()
    {
        JMenuItem exit = new JMenuItem("Exit(E)", KeyEvent.VK_E);
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
        exit.addActionListener(arg0 -> System.exit(0));
        return exit;
    }

    //to find words which you want to notice
    private JMenuItem search()
    {
        JMenuItem search = new JMenuItem("Search(F)", KeyEvent.VK_F);
        search.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
        search.addActionListener(e -> Find());
        return search;
    }

    //a function to help find
    public static int index(String str1,String str2) {
        JTextPane textPane1 = new JTextPane();
        textPane1.setText(str1);
        int index;
        textPane1.setCaretPosition(textPane1.getText().length());
        index = str1.lastIndexOf(str2, textPane1.getCaretPosition() - str2.length() - 1);
        return index;
    }

    //realize the search function and display the find interface
    private void Find()
    {
        final JDialog findDialog=new JDialog(this,"Find",false);
        Container con=findDialog.getContentPane();
        con.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JLabel findContentLabel=new JLabel("search:");
        final JTextField findText=new JTextField(15);
        JButton findNextButton=new JButton("search next one");
        ButtonGroup bGroup=new ButtonGroup();
        final JRadioButton upButton=new JRadioButton("up");
        final JRadioButton downButton=new JRadioButton("down");
        downButton.setSelected(true);
        bGroup.add(upButton);
        bGroup.add(downButton);
        JButton cancel=new JButton("cancel");
        cancel.addActionListener(e -> findDialog.dispose());

        findNextButton.addActionListener(e -> {
            int k;
            final String str1,str2;
            str1=workArea.getText();
            str2=findText.getText();

            if(upButton.isSelected())
            {
                k = str1.lastIndexOf(str2, workArea.getCaretPosition()-findText.getText().length()-1);
                if(k>-1)
                {
                    workArea.setCaretPosition(k);
                    workArea.select(k,k+str2.length());
                    workArea.setSelectedTextColor(Color.red);
                }
                else
                {   JOptionPane.showMessageDialog(null,"Cannot find anything!","Find",JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else if(downButton.isSelected())
            {
                if(workArea.getSelectedText()==null)
                    k=str1.indexOf(str2,workArea.getCaretPosition()+1);
                else
                    k=str1.indexOf(str2, workArea.getCaretPosition()-findText.getText().length()+1);
                if(k>-1)
                {
                    workArea.setCaretPosition(k);
                    workArea.select(k,k+str2.length());
                    workArea.setSelectedTextColor(Color.red);
                }
                else
                {   JOptionPane.showMessageDialog(null,"Cannot find anything!","Find",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        JPanel panel1=new JPanel();
        JPanel panel2=new JPanel();
        JPanel panel3=new JPanel();
        JTextPane textPane = new JTextPane();
        JPanel directionPanel=new JPanel();
        directionPanel.setBorder(BorderFactory.createTitledBorder("Direction"));
        textPane.setText(textPane.getText()+"begin from button 'up'");
        textPane.setBackground(Color.yellow);
        directionPanel.add(upButton);
        directionPanel.add(downButton);
        directionPanel.add(textPane);
        panel1.setLayout(new GridLayout(2,1,5,5));
        panel1.add(findNextButton);
        panel1.add(cancel);
        panel2.add(findContentLabel);
        panel2.add(findText);
        panel3.add(directionPanel);
        con.add(panel2);
        con.add(panel1);
        con.add(panel3);
        con.setLayout(new FlowLayout(FlowLayout.LEADING,10,10));
        findDialog.setSize(500,350);
        findDialog.setResizable(false);
        findDialog.setLocation(100,100);
        findDialog.setVisible(true);
    }


}
