public class Coordinate {
    public int x,y;

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object obj){
        boolean sameSame = false;

        if(obj != null && obj instanceof Coordinate){
            if(this.x == ((Coordinate)obj).x && this.y == ((Coordinate)obj).y){
                sameSame = true;
            }
        }
        return sameSame;
    }
}
