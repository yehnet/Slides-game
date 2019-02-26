package Slides;

import Slides.GamePlay.SlidePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Victory extends JFrame implements ActionListener {
    private JButton mainMenu, newGame;

    //---------------constructor------------------------
    public Victory(String details,SlidePanel fullPic){

        //-----------------fields---------------------------
        JPanel winPanel = new JPanel();
        winPanel.setLayout(new BorderLayout());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        mainMenu = new JButton();
        Dimension btnDimension = new Dimension(150, 50);
        mainMenu.setPreferredSize(btnDimension);
        mainMenu.setMinimumSize(btnDimension);
        newGame = new JButton();
        newGame.setPreferredSize(btnDimension);
        newGame.setMinimumSize(btnDimension);
        try {
            Image tImg = ImageIO.read(new File("icons\\backbtn1.jpg") );
            mainMenu.setIcon(new ImageIcon(tImg.getScaledInstance(150,50,Image.SCALE_SMOOTH)));
            Image tImg2 = ImageIO.read(new File("icons\\backbtn2.jpg") );
            newGame.setIcon(new ImageIcon(tImg2.getScaledInstance(150,50,Image.SCALE_SMOOTH)));
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        buttonsPanel.add(mainMenu);
        buttonsPanel.add(newGame);
        mainMenu.addActionListener(this);
        newGame.addActionListener(this);

        JLabel info = new JLabel(details);

        fullPic.setPreferredSize(new Dimension(400,400));
        winPanel.add(info, BorderLayout.NORTH);
        winPanel.add(fullPic, BorderLayout.CENTER);
        winPanel.add(buttonsPanel, BorderLayout.SOUTH);
        winPanel.setPreferredSize(winPanel.getPreferredSize());
        winPanel.validate();

        add(winPanel);
        getContentPane().setPreferredSize(winPanel.getPreferredSize());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        pack();
        this.setLocation(300,50);
    }
    //---------------methods----------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == mainMenu) {//start game
            dispose();
            new Menu();
        }
        if(e.getSource() == newGame) {//exit game
            dispose();
            Menu props = new Menu();
            new Settings(props.get_images(),props.get_picNames(),props.get_order());
            props.dispose();
        }

    }
}
