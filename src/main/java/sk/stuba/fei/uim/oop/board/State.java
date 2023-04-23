package sk.stuba.fei.uim.oop.board;

public enum State {
    BLANK,
    START,
    END,
    STRAIGHT,
    BENT;

    public Direction getDirections(double angle) {
        if (angle == 0) return Direction.UP;
        if (angle == 90) return Direction.RIGHT;
        if (angle == 180) return Direction.DOWN;
        if (angle == 270) return Direction.LEFT;
        return null;
    }

}
