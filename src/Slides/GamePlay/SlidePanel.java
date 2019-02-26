package Slides.GamePlay;import javafx.util.Pair;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class SlidePanel extends JPanel {
    //-----------------fields---------------------------
    private Pair[][] _icons;
    private int _size;

    public void set_empty(Point _empty) {
        this._empty = _empty;
    }

    private Point _empty, _prev = new Point();

    //---------------constructor------------------------
    public SlidePanel(Pair[][] icons) {//receives an array with both icons and original index after assigned order from CSV
        super(new GridLayout(icons.length, icons.length));
        //initialize variables
        _icons = icons;
        _size = _icons.length;
        int blockSize = 400 / _size;
        //generate tile board
        JLabel tile;
        for (int i = 0; i < _size; i++){
            for (int j = 0; j < _size; j++){
                if (_icons[i][j].getKey().toString().equals("0")) {
                    tile = new JLabel();
                    _empty = new Point(j, i);
                }
                else
                    tile = new JLabel((ImageIcon)_icons[i][j].getValue());
                tile.setPreferredSize(new Dimension(blockSize, blockSize));
                tile.setBorder(new LineBorder(Color.black));
                add(tile);
            }
        }
        //initialize window
        setFocusable(true);
        requestFocus();
        setMinimumSize(new Dimension(_size * blockSize, _size * blockSize));
        setPreferredSize(new Dimension(_size * blockSize, _size * blockSize));

    }
    //---------------methods----------------------------
    boolean canMove(int col, int row) {//checks if a move is legal and if so performs swap.
        int newX = (int)_empty.getX();
        int newY = (int)_empty.getY();
        if ( (col>=0 & col< _size & row>=0 & row< _size) &&//index is within range
                ( ( newX == col + 1 | newX == col - 1 ) & newY == row ) |
                    (( newY == row + 1 | newY == row - 1 ) & newX == col ) ) {//index is bordering empty tile
            //performs swap
            ImageIcon icon = (ImageIcon) _icons[row][col].getValue();//pull icon to be swapped with empty tile
            ((JLabel)getComponent(newY * _size + newX)).setIcon(icon);//update icon on empty tile
            Pair tmp = _icons[newY][newX];//swap index array values according to tile swap
            _icons[newY][newX] = _icons[row][col];
            _icons[row][col] = tmp;
            _prev.setLocation(_empty);//store previous empty tile location
            _empty.setLocation(col, row);//update empty tile location
            ((JLabel)getComponent(row * _size + col)).setIcon(null);//remove icon from new empty tile1
            return true;
        }
        return false;
    }

    boolean isSolved(){//checks icons array to match indexes
        for (int i = 0; i< _size; i++){
            for (int j = 0; j< _size; j++){
                int index = i * _size + j + 1;
                if (index <_size*_size & !_icons[i][j].getKey().equals(Integer.toString (index)))//compares data index to position
                   return false;
            }
        }
        return true;
    }

    Point get_empty() {
        return _empty;
    }

    int get_size() {
        return _size;
    }

    Point get_prev() {
        return _prev;
    }

    void fullPic(){
        this.removeAll();
        int blockSize = 400 / _size;
        JLabel tile;
        for (int i = 0; i < _size; i++){
            for (int j = 0; j < _size; j++){
                tile = new JLabel((ImageIcon)_icons[i][j].getValue());
                tile.setPreferredSize(new Dimension(blockSize, blockSize));
                tile.setBorder(new LineBorder(Color.black));
                add(tile);
            }
        }
    }

}
