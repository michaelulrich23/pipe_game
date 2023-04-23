package sk.stuba.fei.uim.oop.controls;

import lombok.Getter;
import sk.stuba.fei.uim.oop.board.Board;
import sk.stuba.fei.uim.oop.board.Direction;
import sk.stuba.fei.uim.oop.board.State;
import sk.stuba.fei.uim.oop.tile.LPipe;
import sk.stuba.fei.uim.oop.tile.StartEnd;
import sk.stuba.fei.uim.oop.tile.StraightPipe;
import sk.stuba.fei.uim.oop.tile.Tile;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameLogic extends UniversalAdapter {
    public static final int INITIAL_BOARD_SIZE = 8;
    private static final String RESTART = "RESTART";
    private static final String CHECK = "Check path";
    private JFrame mainGame;
    private Board currentBoard;
    private int currentBoardSize;
    @Getter
    private JLabel label;
    @Getter
    private JLabel boardSizeLabel;
    private int counter;

    public GameLogic(JFrame mainGame) {
        this.mainGame = mainGame;
        this.currentBoardSize = INITIAL_BOARD_SIZE;
        this.initializeBoard(this.currentBoardSize);
        this.mainGame.add(this.currentBoard);
        this.label = new JLabel();
        this.boardSizeLabel = new JLabel();
        this.counter = 1;
        this.updateLevelLabel();
        this.updateBoardSizeLabel();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if(this.currentBoardSize == ((JSlider) e.getSource()).getValue()){
            return;
        }
        this.currentBoardSize = ((JSlider) e.getSource()).getValue();
        this.updateBoardSizeLabel();
        this.gameRestart();
        this.mainGame.setFocusable(true);
        this.mainGame.requestFocus();
    }

    public void gameWon() {
        this.mainGame.remove(this.currentBoard);
        this.initializeBoard(this.currentBoardSize);
        this.mainGame.add(this.currentBoard);
        this.counter++;
        this.updateLevelLabel();
    }

    private void gameRestart() {
        this.mainGame.remove(this.currentBoard);
        this.initializeBoard(this.currentBoardSize);
        this.mainGame.add(this.currentBoard);
        this.counter = 1;
        this.updateLevelLabel();
        this.mainGame.revalidate();
        this.mainGame.repaint();
        this.mainGame.setFocusable(true);
        this.mainGame.requestFocus();
    }

    private void initializeBoard(int dimension) {
        this.currentBoard = new Board(dimension);
        this.currentBoard.addMouseMotionListener(this);
        this.currentBoard.addMouseListener(this);
    }

    private void updateLevelLabel() {
        this.label.setText("Level: " + this.counter);
        this.mainGame.revalidate();
        this.mainGame.repaint();
    }

    private void updateBoardSizeLabel() {
        this.boardSizeLabel.setText("CURRENT BOARD SIZE: " + this.currentBoardSize);
        this.mainGame.revalidate();
        this.mainGame.repaint();
    }

    private void check(){
        int startIndex = 0;
        for (int i = 0; i < this.currentBoard.getTiles().length; i++) {
            if(this.currentBoard.getTiles()[0][i] instanceof StartEnd) {
                startIndex = i;
                this.currentBoard.getTiles()[0][i].setCheck(true);
            }
            for (int j = 0; j < this.currentBoard.getTiles().length; j++) {
                this.currentBoard.getTiles()[i][j].setVisited(false);
            }
        }

        checkPath(this.currentBoard.getTiles(), 0, startIndex);
        this.mainGame.repaint();
    }

    private void checkPath(Tile[][] pipes, int x, int y) {
        if(pipes[x][y] == null){
            return;
        }

        pipes[x][y].setVisited(true);

        Direction direction = pipes[x][y].getEnd();
        int nextX = x;
        int nextY = y;

        if (direction == Direction.UP) {
            nextY++;
        } else if (direction == Direction.DOWN) {
            nextY--;
        } else if (direction == Direction.LEFT) {
            nextX--;
        } else if (direction == Direction.RIGHT) {
            nextX++;
        }

        if (nextX < 0 || nextX >= pipes.length || nextY < 0 || nextY >= pipes.length) {
            return;
        }
        Tile nextPipe = pipes[nextX][nextY];
        if (nextPipe == null || nextPipe.getState() == State.BLANK || nextPipe.isVisited()) {
            return;
        }

        if (nextPipe.getState() == State.END) {
            if (pipes[x][y].getState() == State.STRAIGHT) {
                if ((x < nextX || y < nextY) && pipes[x][y].getEnd().getOppositeDirection() == nextPipe.getEnd()) {
                    gameWon();
                    return;
                }
                if (y > nextY && pipes[x][y].getEndTwo().getOppositeDirection() == nextPipe.getEnd()) {
                    gameWon();
                    return;
                }
            } else if ((pipes[x][y].getEnd().getOppositeDirection() == nextPipe.getEnd()) || (pipes[x][y].getEndTwo().getOppositeDirection() == nextPipe.getEnd())){
                gameWon();
                return;
            }
        }
        if (nextPipe.getState() == State.STRAIGHT) {
            if(pipes[x][y].getEnd().getOppositeDirection() == nextPipe.getEnd()){
                nextPipe.setCheck(true);
                nextPipe.setEnd(nextPipe.getEndTwo());
            } else if(pipes[x][y].getEnd().getOppositeDirection() == nextPipe.getEndTwo()){
                nextPipe.setCheck(true);
                nextPipe.setEnd(nextPipe.getEnd());
            } else return;
        } else if (nextPipe.getState() == State.BENT) {
            Direction endOpposite = pipes[x][y].getEnd().getOppositeDirection();
            if (endOpposite == nextPipe.getEnd() || endOpposite == nextPipe.getEndTwo()) {
                Direction dir = (endOpposite == nextPipe.getEnd()) ? nextPipe.getEndTwo() : nextPipe.getEnd();
                int[] offset = dir.getOffset();
                int newX = nextX + offset[0];
                int newY = nextY + offset[1];
                nextPipe.setCheck(true);
                if (newX < 0 || newY < 0 || newX > this.currentBoardSize-1 || newY > this.currentBoardSize-1 || pipes[newX][newY].isVisited()) {
                    return;
                }
                nextPipe.setEnd(dir);
            }
            else return;
        }
        checkPath(pipes, nextX, nextY);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton b = (JButton) e.getSource();
        switch(b.getText()){
            case RESTART:
                this.gameRestart();
                break;
            case CHECK:
                this.check();
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_R:
                this.gameRestart();
                break;
            case KeyEvent.VK_ENTER:
                this.check();
                break;
            case KeyEvent.VK_ESCAPE:
                this.mainGame.dispose();
                System.exit(0);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Component current = this.currentBoard.getComponentAt(e.getX(), e.getY());
        if (!(current instanceof Tile)) {
            return;
        }
        ((Tile) current).setHighlight(true);
        this.currentBoard.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Component current = this.currentBoard.getComponentAt(e.getX(), e.getY());
        if (!(current instanceof Tile)) {
            return;
        }
        if(current instanceof StartEnd){
            ((StartEnd) current).rotateTile();
        }
        if(current instanceof StraightPipe){
            ((StraightPipe) current).rotateTile();
        }
        if(current instanceof LPipe){
            ((LPipe) current).rotateTile();
        }
        for (int i = 0; i < this.currentBoard.getTiles().length; i++) {
            for (int j = 0; j < this.currentBoard.getTiles().length; j++) {
                this.currentBoard.getTiles()[i][j].setCheck(false);
            }
        }
        ((Tile) current).setHighlight(true);
        this.currentBoard.repaint();
    }
}
