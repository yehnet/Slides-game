package Slides;

import Slides.GamePlay.Game;
import javafx.util.Pair;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class Settings extends JFrame implements ActionListener, ListSelectionListener {
    //-----------------fields---------------------------
    private JButton _startButton, _randomPic, _chooseFile;
    private JRadioButton a,b,c;
    private JTextField f;
    private JList<String> _picList;
    private JLabel d, _imageLabel;
    private int _size;
    private ImageIcon[] _images;
    private Vector<String[][]>[] _order;
    private String[] picNames;
    private final JFileChooser _fileChooser = new JFileChooser();
    //---------------constructor------------------------
    public Settings(ImageIcon[] images, String[] picNames, Vector<String[][]>[] order){
        //set window defaults
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //initialize variables
        this._images =images;
        this._order=order;
        this.picNames=picNames;
        //filechooser initialization- default file types {.jpg & .jpeg}
        _fileChooser.setFileFilter(new FileFilter() {

            public String getDescription() {
                return "JPG Images (*.jpg)";
            }

            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    String filename = f.getName().toLowerCase();
                    return filename.endsWith(".jpg") || filename.endsWith(".jpeg") ;
                }
            }
        });

        buttonsInit();
        //option buttons panel
        JPanel startPanel = new JPanel();
        startPanel.add(_randomPic);
        startPanel.add(_startButton);
        startPanel.add(_chooseFile);
        //size options
        ButtonGroup btnGroup = new ButtonGroup();
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout());
        btnGroup.add(a);
        btnGroup.add(b);
        btnGroup.add(c);
        buttonPane.add(a);
        buttonPane.add(b);
        buttonPane.add(c);
        buttonPane.add(d);
        buttonPane.add(f);
        //image preview
        _imageLabel = new JLabel();
        _imageLabel.setPreferredSize(new Dimension(400,400));
        JSplitPane imagePane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buttonPane, _imageLabel);

        JScrollPane listPane = scrollListInit();

        //assemble window
        JPanel settingsPane = new JPanel();
        settingsPane.setLayout(new BorderLayout());
        settingsPane.add(startPanel,BorderLayout.SOUTH);
        settingsPane.add(listPane,BorderLayout.WEST);
        settingsPane.add(imagePane, BorderLayout.EAST);
        add(settingsPane, BorderLayout.CENTER);
        getContentPane().setPreferredSize(getPreferredSize());
        setResizable(false);
        setVisible(true);
        pack();
        this.setLocation(300,50);
    }
    //---------------methods----------------------------
    private void buttonsInit(){
        //options buttons
        _startButton = new JButton();
        _startButton.addActionListener(this);
        _randomPic = new JButton();
        _randomPic.addActionListener(this);
        _chooseFile = new JButton();
        _chooseFile.addActionListener(this);
        try {
            Image tImg = ImageIO.read(new File("icons\\microgr.jpg") );
            _startButton.setIcon(new ImageIcon(tImg.getScaledInstance(150,50,Image.SCALE_SMOOTH)));
            Image tImg2 = ImageIO.read(new File("icons\\microblue.jpg") );
            _randomPic.setIcon(new ImageIcon(tImg2.getScaledInstance(150,50,Image.SCALE_SMOOTH)));
            Image tImg3 = ImageIO.read(new File("icons\\micropr.jpg") );
            _chooseFile.setIcon(new ImageIcon(tImg3.getScaledInstance(150,50,Image.SCALE_SMOOTH)));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error:" + ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        Dimension btnDimension = new Dimension(150, 50);
        _startButton.setPreferredSize(btnDimension);
        _startButton.setMinimumSize(btnDimension);
        _randomPic.setPreferredSize(btnDimension);
        _randomPic.setMinimumSize(btnDimension);
        _chooseFile.setPreferredSize(btnDimension);
        _chooseFile.setMinimumSize(btnDimension);
        //size buttons
        _size=3;
        a = new JRadioButton("3x3");
        a.setSelected(true);
        a.addActionListener(this);
        b = new JRadioButton("4x4");
        b.addActionListener(this);
        c = new JRadioButton("5x5");
        c.addActionListener(this);
        d = new JLabel("Custom Size");
        f = new JTextField();
        f.setPreferredSize(new Dimension(30,20));
    }

    private JScrollPane scrollListInit(){
        //picture source list
        _picList = new JList<>(picNames);
        JScrollPane listPane = new JScrollPane(_picList);
        listPane.setPreferredSize(new Dimension(150,400));
        listPane.setMinimumSize(new Dimension(150,400)) ;
        _picList.addListSelectionListener(this);
        DefaultListSelectionModel m = new DefaultListSelectionModel();
        m.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m.setLeadAnchorNotificationEnabled(false);
        _picList.setSelectionModel(m);
        _picList.setSelectedIndex(0);
        return  listPane;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        //size selection
        if(e.getSource()==a) {
            _size = 3;
        }
        if(e.getSource()==b) {
            _size = 4;
        }
        if(e.getSource()==c) {
            _size = 5;
        }
        //generate icon array and assign order according to CSV
        if (e.getSource()==_startButton) {
            Pair[][] icons;
            String[][] order;
            int var;
            BufferedImage fullImage;
            if (f.getText().equals("")) {
                if (_order[_size - 3] != null && _order[_size - 3].size() > 0) {
                    var = (int) ( Math.random() * _order[_size - 3].size() );
                    order = _order[_size - 3].elementAt(var);
                } else {
                    JOptionPane.showMessageDialog(this, "no matching shuffle in CSV", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (!f.getText().equals("")) {//generate without CSV
                try {
                    _size = Integer.parseInt(f.getText());
                    order = new String[_size][_size];
                    for (int i = 0; i < _size * _size - 1; i++){
                        order[i / _size][i % _size] = Integer.toString(i + 1);//generate array, assign tile numbers to cells
                    }
                    order[_size - 1][_size - 1] = "0";//bottom right tile is empty
                    Point empty = new Point(_size - 1, _size - 1);//empty tile location
                    for (int j = 0; j < Math.pow(4, _size); j++){//perform 4^size swaps
                        int emptyX = (int) empty.getX();
                        int emptyY = (int) empty.getY();
                        int action = (int) ( Math.random() * 4 );//4 swap options
                        switch (action) {
                            case ( 0 )://up
                                if (emptyY > 0) {
                                    order[emptyY][emptyX] = order[emptyY - 1][emptyX];
                                    order[emptyY - 1][emptyX] = "0";
                                    empty.setLocation(emptyX, emptyY - 1);
                                }
                                break;
                            case ( 1 )://down
                                if (emptyY < _size - 1) {
                                    order[emptyY][emptyX] = order[emptyY + 1][emptyX];
                                    order[emptyY + 1][emptyX] = "0";
                                    empty.setLocation(emptyX, emptyY + 1);
                                }
                                break;
                            case ( 2 )://left
                                if (emptyX > 0) {
                                    order[emptyY][emptyX] = order[emptyY][emptyX - 1];
                                    order[emptyY][emptyX - 1] = "0";
                                    empty.setLocation(emptyX - 1, emptyY);
                                }
                                break;
                            case ( 3 )://right
                                if (emptyX < _size - 1) {
                                    order[emptyY][emptyX] = order[emptyY][emptyX + 1];
                                    order[emptyY][emptyX + 1] = "0";
                                    empty.setLocation(emptyX + 1, emptyY);
                                }
                                break;
                        }
                    }
                } catch (NumberFormatException e2) {
                    JOptionPane.showMessageDialog(this, "Please insert a valid number", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            } else {
                JOptionPane.showMessageDialog(this, "Couldn't load board configuration", "Error", JOptionPane.ERROR_MESSAGE);
                return;
                //no such board configuration exists
            }
            ImageIcon icon = _images[_picList.getSelectedIndex()];
            fullImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics g = fullImage.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            icons = cutImage(fullImage, order);
            if (icons != null) {
                dispose();
                new Game(icons);
            }
        }

    if (e.getSource()== _randomPic){//chooses random picture from list
        _picList.setSelectedIndex((int)(Math.random()*picNames.length));
    }

    if (e.getSource()== _chooseFile) {//uploads picture from file
        int returnVal = _fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = _fileChooser.getSelectedFile();
            try {
                BufferedImage fullImage = ImageIO.read(file);
                int var = (int) ( Math.random() * _order[_size - 3].size() );
                String[][]order = _order[_size-3].elementAt(var);
                Pair[][] icons = cutImage(fullImage, order);
                if (icons != null) {
                    new Game(icons);
                    dispose();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Couldn't load picture","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {//updates image displayed in panel from list
        _imageLabel.setIcon(new ImageIcon((new ImageIcon("sample_pictures\\" + _picList.getSelectedValue()))
                .getImage().getScaledInstance(400,400,Image.SCALE_SMOOTH)));
    }

    private Pair[][] cutImage(BufferedImage fullImage, String[][] order) {
        int blockSize = 400 / _size;
        int w = fullImage.getWidth();
        int h = fullImage.getHeight();
        BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale((double) 400 / w, (double) 400 / h);//scale down to size
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        scaled = scaleOp.filter(fullImage, scaled);
        Pair[][] icons = new Pair[_size][_size];
        for (int i = 0; i < _size; i++){
            for (int j = 0; j < _size; j++){
                BufferedImage icon;
                int offset = Integer.parseInt(order[i][j]) - 1;
                if (offset < 0) {
                    icon = scaled.getSubimage(blockSize * ( _size - 1 ), blockSize * ( _size - 1 ),
                            blockSize, blockSize);//crop image
                    icons[i][j] = new Pair<>(order[i][j], new ImageIcon(icon));
                } else {
                    icon = scaled.getSubimage(blockSize * ( offset % _size ), blockSize * ( offset / _size ),
                            blockSize, blockSize);//crop image
                    icons[i][j] = new Pair<>(order[i][j], new ImageIcon(icon));
                }
            }
        }
        return icons;
    }
}
