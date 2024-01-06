import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Mouse {
    public int x,y;
    public Stack<Coordinate> decision;
    public List<Coordinate> solution;

    public Mouse(int x, int y){
        this.x = x;
        this.y = y;
        decision = new Stack<Coordinate>();
        solution = new ArrayList<Coordinate>();
    }
}
