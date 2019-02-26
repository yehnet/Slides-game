package Slides.GamePlay;

import Slides.Victory;
import javafx.util.Pair;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

public class Game extends JFrame implements ActionListener, MouseListener, KeyListener {
    //-----------------fields---------------------------
    private int _blockSize;
    private int _nMoves;
    private int _counter;
    private JLabel _timerLabel, _moves;
    private JButton _undo;
    private Stack<Point> _undoMoves;
    private SlidePanel _slides;
    private Timer _timer;
    //---------------constructor------------------------
    public Game(Pair[][] icons){//receives an array with both icons and original index after assigned order from CSV
        //intialize _timerLabel
        _timer = new Timer(1000,this);
        _counter = 0;
        //initialize variables
        _undoMoves = new Stack<>();
        int size = icons.length;
        _nMoves =0;
        //set window defaults
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        //manage graphic aspects
        _slides = new SlidePanel(icons);
        _blockSize = 400 / size;//tile size
        //create information bar
        JPanel infoPanel = infoPanelInit();
        //assemble window
        JSplitPane mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, _slides, infoPanel);
        add(mainPane);
        _slides.requestFocus();//allows keyboard control of slide panel
        _slides.addMouseListener(this);
        _slides.addKeyListener(this);
        setResizable(false);
        setVisible(true);
        pack();
        this.setLocation(300,50);
    }
    //---------------methods----------------------------
    private JPanel infoPanelInit(){
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        infoPanel.setPreferredSize(new Dimension(100,50));
        _undo = new JButton("UNDO");
        _undo.addActionListener(this);
        _undo.setFocusable(false);//fix arrow keys un-responsive issue
        infoPanel.add(_undo,BorderLayout.CENTER);
        _timerLabel = new JLabel("Time: 00:00 ");
        _moves = new JLabel("Moves: "+ _nMoves);
        infoPanel.add(_timerLabel,BorderLayout.WEST);
        infoPanel.add(_moves,BorderLayout.EAST);
        return infoPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {//_undo button
        if (! _timer.isRunning())
            _timer.start();

        if (e.getSource()== _undo & !_undoMoves.isEmpty()) {
            Point back = _undoMoves.pop();//takes previous location of empty tile from stack
            _slides.canMove((int) back.getX(),(int) back.getY());
            validate();//refreshes panel
        }
        if (e.getSource()!= _undo)
            _timerLabel.setText( getMinuteString( ++_counter) ) ;//else its _timerLabel action

        //game winning
        if(_slides.get_empty().getX() == _slides.get_size() -1 & _slides.get_empty().getY() == _slides.get_size()-1) {//if blank is bottom right
            if (_slides.isSolved()) {//endgame sequence- displays missing piece, prevents further actions
                _timer.stop();
                dispose();
                _slides.fullPic();
                _slides.set_empty(new Point(-2,-2));
                new Victory(getInfo(), _slides );
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (! _timer.isRunning())
            _timer.start();
        int col = e.getX() / _blockSize;//converts mouse coordinates to board index
        int row = e.getY() / _blockSize;
        if(_slides.canMove(col, row)) {
            _undoMoves.push(new Point(_slides.get_prev()));//pushes previous location of empty tile to stack for future _undo
            _nMoves++;//increases move count
            _moves.setText("Moves: "+ _nMoves);//refreshes move display
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {//arrow keys control
        if (! _timer.isRunning())
            _timer.start();
        int keyCode=e.getKeyCode();
        int emptyX = (int) _slides.get_empty().getX();
        int emptyY = (int) _slides.get_empty().getY();
        switch (keyCode) {
            case ( KeyEvent.VK_UP )://move tile up
                if (_slides.canMove(emptyX, emptyY + 1)) {
                    updateMoves(_slides.get_prev());
                }
                break;
            case ( KeyEvent.VK_DOWN )://move tile down
                if (_slides.canMove(emptyX, emptyY - 1)) {
                    updateMoves(_slides.get_prev());
                }
                break;
            case ( KeyEvent.VK_LEFT )://move tile left
                if (_slides.canMove(emptyX + 1, emptyY)) {
                    updateMoves(_slides.get_prev());
                }
                break;
            case ( KeyEvent.VK_RIGHT )://move tile right
                if (_slides.canMove(emptyX - 1, emptyY)) {
                    updateMoves(_slides.get_prev());
                }
                break;
            case ( KeyEvent.VK_Z )://ctrl + Z
                if (e.isControlDown() & !_undoMoves.isEmpty()) {//perform UNDO
                    Point back = _undoMoves.pop();
                    _slides.canMove((int) back.getX(), (int) back.getY());
                    validate();
                }
                break;
        }
    }

    private String getMinuteString( int seconds ) {
        StringBuffer timerText = new StringBuffer() ;
        int min = seconds / 60 ;
        int sec = seconds - ( min * 60 ) ;
        timerText.append("Time: ");
        if( min < 1 )
            timerText.append( 0 ) ;
        timerText.append( min ) ;
        timerText.append( ":") ;
        if( sec < 10 )
            timerText.append( 0 ) ;
        timerText.append( sec + " ") ;
        return timerText.toString() ;
    }

    private String getInfo(){
       return  "<html>Congrats!<br/>you finished the puzzle<br/>in " + getMinuteString(_counter) + " with " + _nMoves + " moves!</html>";
    }

    private void updateMoves(Point prev){
        _undoMoves.push(new Point(prev));
        _nMoves++;
        _moves.setText("Moves: " + _nMoves);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
