package Slides;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;
import java.util.Vector;

public class Menu extends JFrame implements ActionListener{

    public static void main(String[] args) {
        new Menu();
    }

    //-----------------fields---------------------------
    private JButton _startButton, _exitButton;
    private ImageIcon[] _images;
    private String[] _picNames;
    private Vector<String[][]>[] _order;
    private JLabel background;
    //---------------constructor------------------------
    public Menu(){
        super("poopex hw3");
        //initialize display panel
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        displayInit();
        pullFiles();

        //assemble window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel1.add(_startButton,BorderLayout.CENTER);
        panel1.add(_exitButton, BorderLayout.SOUTH);
        add(background);
        background.add(panel1,BorderLayout.SOUTH);
        setResizable(false);
        setVisible(true);
        pack();
        this.setLocation(400,50);

    }
    //---------------methods----------------------------
    private void displayInit(){
        //initialize background
        background = new JLabel(new ImageIcon("icons\\back2.jpg"));
        background.setLayout(new BorderLayout());
        //initilize buttons
        _startButton = new JButton();
        Dimension btnDimension = new Dimension(150, 50);
        _startButton.setPreferredSize(btnDimension);
        _startButton.setMinimumSize(btnDimension);
        // startButton.setFont(new Font("Arial" , Font.BOLD, 30));
        _startButton.addActionListener(this);
        _exitButton = new JButton();
        // exitButton.setFont(new Font("Arial" , Font.BOLD, 30));
        _exitButton.setPreferredSize(btnDimension);
        _exitButton.setMinimumSize(btnDimension);
        _exitButton.addActionListener(this);
        try {
            Image tImg = ImageIO.read(new File("icons\\gr5.jpg"));
            _startButton.setIcon(new ImageIcon(tImg));
            Image tImg2 = ImageIO.read(new File("icons\\red5.jpg"));
            _exitButton.setIcon(new ImageIcon(tImg2));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error:" + ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(ex.toString());
        }
    }

    private void pullFiles(){
        //generate image list and arrays from files
        _picNames = new String[Objects.requireNonNull(new File("sample_pictures").listFiles()).length];
        _images = new ImageIcon[Objects.requireNonNull(new File("sample_pictures").listFiles()).length];
        int i = 0;
        for (File file: Objects.requireNonNull(new File("sample_pictures").listFiles())){
            _picNames[i] = file.getName();//full size image
            _images[i] = new ImageIcon("sample_pictures\\" + _picNames[i]);
            i++;
        }
        _order = new Vector[3];
        for (int n=0;n<3;n++)
            _order[n]=new Vector<>();
        //reads CSV file to extract board setup options
        try {
            Scanner inputCSV = new Scanner(new File("boards.csv"));
            int size=-1;
            int k=0;
            while (inputCSV.hasNext()){
                String line = inputCSV.next();
                if(line.length()==1) {//size definition- new array
                    size = Integer.parseInt(line);
                    k=0;
                    _order[size-3].add(new String[size][size]);
                }
                else {//line order
                    _order[size-3].elementAt(_order[size-3].size()-1)[k] = line.split(",");//add to last array added
                    k++;
                }
            }
        } catch (FileNotFoundException e) {//exception handling
            JOptionPane.showMessageDialog(this, "Error:" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == _startButton) {//start game
            dispose();
            new Settings(_images, _picNames, _order);
        }
        if(e.getSource() == _exitButton)//exit game
           dispose();

    }

    public ImageIcon[] get_images() {
        return _images;
    }

    public String[] get_picNames() {
        return _picNames;
    }

    public Vector<String[][]>[] get_order() {
        return _order;
    }
}
