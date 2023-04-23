package sk.stuba.fei.uim.oop.tile;

import sk.stuba.fei.uim.oop.board.State;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class StraightPipe extends Tile{

    public StraightPipe(State state) {
        super(state);
        this.angle = ThreadLocalRandom.current().nextInt(0, 2) * 90;
    }

    public void rotateTile(){
        this.angle += 90;
        this.angle = this.angle % 180;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(51,51,51));

        int[] xPoints = {0, 0, this.getWidth(), this.getWidth()};
        int[] yPoints = {this.getHeight()/3, this.getHeight()*2/3, this.getHeight()*2/3, this.getHeight()/3};

        int[][] result = convertingPoints(xPoints, yPoints);

        g.fillPolygon(result[0], result[1], 4);

        if(this.check){
            g.setColor(Color.BLUE);
            int[] xPointsWater = {0, 0, this.getWidth(), this.getWidth()};
            int[] yPointsWater = {this.getHeight()*4/9, this.getHeight()*5/9, this.getHeight()*5/9, this.getHeight()*4/9};
            int[][] resultWater = convertingPoints(xPointsWater, yPointsWater);
            g.fillPolygon(resultWater[0], resultWater[1], 4);
        }

        this.setEnd(this.state.getDirections(this.angle));
        this.setEndTwo(this.getEnd().getOppositeDirection());
    }
}
