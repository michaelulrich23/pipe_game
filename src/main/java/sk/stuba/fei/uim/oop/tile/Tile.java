package sk.stuba.fei.uim.oop.tile;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

import sk.stuba.fei.uim.oop.board.Direction;
import sk.stuba.fei.uim.oop.board.State;

public class Tile extends JPanel {
    @Setter
    private boolean highlight;
    protected double angle;
    @Getter
    protected State state;
    @Getter
    @Setter
    protected Direction end;
    @Getter
    @Setter
    protected Direction endTwo;
    @Setter
    @Getter
    protected boolean visited;
    @Setter
    protected boolean check;


    public Tile(State state) {
        this.highlight = false;
        this.angle = 0;
        this.state = state;
        this.visited = false;
        this.end = null;
        this.endTwo = null;
        this.check = false;
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setBackground(Color.LIGHT_GRAY);
    }

    private void rotateRectangle(Point[] rectangle, Point location, double angle) {
        double sinAngle = Math.sin(angle);
        double cosAngle = Math.cos(angle);

        for (Point point : rectangle) {
            point.setLocation((point.getX() - location.getX()), (point.getY() - location.getY()));
            double newX = point.getX() * cosAngle - point.getY() * sinAngle;
            double newY = point.getX() * sinAngle + point.getY() * cosAngle;
            point.setLocation(newX, newY);
            point.setLocation((point.getX() + location.getX()), (point.getY() + location.getY()));
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (highlight) {
            g.setColor(Color.BLUE);
            ((Graphics2D) g).setStroke(new BasicStroke(5));
            g.drawRect(2, 2, this.getWidth() - 4, this.getHeight() - 4);
            highlight = false;
        }
    }

    protected int[][] convertingPoints(int[] xPoints, int[] yPoints) {
        Point location = new Point();
        location.setLocation(getWidth() / 2, getHeight() / 2);
        Point[] point = new Point[xPoints.length];

        for (int i = 0; i < xPoints.length; i++) {
            point[i] = new Point();
            point[i].setLocation(xPoints[i], yPoints[i]);
        }

        rotateRectangle(point, location, Math.toRadians(this.angle));

        for (int i = 0; i < xPoints.length; i++) {
            xPoints[i] = (int) point[i].getX();
            yPoints[i] = (int) point[i].getY();
        }

        int[][] result = new int[2][xPoints.length];
        for (int i = 0; i < xPoints.length; i++) {
            result[0][i] = xPoints[i];
            result[1][i] = yPoints[i];
        }

        return result;
    }

}
