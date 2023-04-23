package sk.stuba.fei.uim.oop.board;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public Direction getOppositeDirection() {
        switch (this) {
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            case LEFT:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.LEFT;
        }
        return null;
    }

    public int[] getOffset() {
        int xOffset = 0;
        int yOffset = 0;

        switch (this) {
            case UP:
                yOffset = 1;
                break;
            case DOWN:
                yOffset = -1;
                break;
            case LEFT:
                xOffset = -1;
                break;
            case RIGHT:
                xOffset = 1;
                break;
        }

        return new int[]{xOffset, yOffset};
    }

}
