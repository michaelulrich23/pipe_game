package sk.stuba.fei.uim.oop.board;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

@Getter
public class Node extends JPanel {
    private int xPos;
    private int yPos;
    @Setter
    private boolean isEnd;
    @Setter
    private boolean visited;
    private ArrayList<Node> neighbours;
    @Setter
    private Direction enter;
    @Setter
    private Direction exit;

    public Node(int x, int y){
        this.xPos = x;
        this.yPos = y;
        this.isEnd = false;
        this.visited = false;
        this.enter = null;
        this.exit = null;
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setBackground(Color.LIGHT_GRAY);
        this.neighbours = new ArrayList<>();
    }

    public void addNeighbour(Node node) {
        this.neighbours.add(node);
    }

    public Collection<Node> getNeighbors() {
        Collection<Node> all = new ArrayList<>(this.neighbours);
        List<Node> shuffled = new ArrayList<>(all);
        Collections.shuffle(shuffled);
        return shuffled;
    }

}
