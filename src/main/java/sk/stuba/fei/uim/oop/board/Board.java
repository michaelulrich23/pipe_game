package sk.stuba.fei.uim.oop.board;

import lombok.Getter;
import sk.stuba.fei.uim.oop.tile.LPipe;
import sk.stuba.fei.uim.oop.tile.StartEnd;
import sk.stuba.fei.uim.oop.tile.StraightPipe;
import sk.stuba.fei.uim.oop.tile.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class Board extends JPanel {
    private Node[][] nodes;
    private Tile[][] tiles;

    public Board(int dimension) {
        this.initializeBoard(dimension);
        this.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        this.setBackground(Color.GRAY);
    }

    public void initializeBoard(int dimension) {
        this.nodes = new Node[dimension][dimension];
        this.tiles = new Tile[dimension][dimension];
        this.setLayout(new GridLayout(dimension, dimension));
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.tiles[i][j] = new Tile(State.BLANK);
                this.nodes[i][j] = new Node(i, j);
                this.add(this.nodes[i][j]);
            }
        }
        generateRoute(dimension);
    }

    public void generateRoute(int dimension) {
        int startX = 0;
        int startY = ThreadLocalRandom.current().nextInt(0, dimension);
        int endX = dimension - 1;
        int endY = ThreadLocalRandom.current().nextInt(0, dimension);

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (i != 0) {
                    this.nodes[i][j].addNeighbour(this.nodes[i - 1][j]);
                }
                if (i != dimension - 1) {
                    this.nodes[i][j].addNeighbour(this.nodes[i + 1][j]);
                }
                if (j != 0) {
                    this.nodes[i][j].addNeighbour(this.nodes[i][j - 1]);
                }
                if (j != dimension - 1) {
                    this.nodes[i][j].addNeighbour(this.nodes[i][j + 1]);
                }
            }
        }

        this.nodes[endX][endY].setEnd(true);
        this.tiles[startX][startY] = new StartEnd(State.START);
        this.tiles[endX][endY] = new StartEnd(State.END);

        ArrayList<Node> path = new ArrayList<>();

        path = this.dfs(this.nodes[startX][startY], path);

        convertToTiles(path);
    }

    public void convertToTiles(ArrayList<Node> path) {
        for (Node node : path) {
            if (node.getEnter() == null || node.getExit() == null) {
                continue;
            }

            if (this.nodes[node.getXPos()][node.getYPos()].getEnter().getOppositeDirection() == this.nodes[node.getXPos()][node.getYPos()].getExit()) {
                this.tiles[node.getXPos()][node.getYPos()] = new StraightPipe(State.STRAIGHT);
            } else {
                this.tiles[node.getXPos()][node.getYPos()] = new LPipe(State.BENT);
            }
        }
        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles.length; j++) {
                this.remove(this.nodes[i][j]);
                this.add(this.tiles[i][j]);
            }
        }
    }

    public ArrayList<Node> dfs(Node currentNode, ArrayList<Node> path) {
        currentNode.setVisited(true);
        path.add(currentNode);

        if (currentNode.isEnd()) {
            return path;
        }

        for (Node neighbor : currentNode.getNeighbors()) {
            if (!neighbor.isVisited()) {
                Direction exitDirection = getDirection(currentNode, neighbor);
                Direction enterDirection = getDirection(neighbor, currentNode);

                currentNode.setExit(exitDirection);
                neighbor.setEnter(enterDirection);

                ArrayList<Node> result = dfs(neighbor, path);
                if (result != null) {
                    return result;
                }
            }
        }

        path.remove(path.size() - 1);
        return null;
    }

    public Direction getDirection(Node currentNode, Node neighbor) {
        int dx = neighbor.getXPos() - currentNode.getXPos();
        int dy = neighbor.getYPos() - currentNode.getYPos();
        Direction direction = null;
        if (dx == 1) {
            direction = Direction.RIGHT;
        } else if (dx == -1) {
            direction = Direction.LEFT;
        } else if (dy == 1) {
            direction = Direction.UP;
        } else if (dy == -1) {
            direction = Direction.DOWN;
        }
        return direction;
    }


}
