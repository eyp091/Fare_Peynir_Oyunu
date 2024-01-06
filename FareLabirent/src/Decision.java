public class Decision {
    public boolean up, down, left, right;
    public int x,y;

    public Decision(int x, int y){
        up = down = left = right = false;
        this.x = x;
        this.y = y;
    }

    public Decision(boolean up, boolean down, boolean left, boolean right){
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }

    public void setUp(boolean up){
        this.up = up;
    }

    public void setDown(boolean down){
        this.down = down;
    }

    public void setLeft(boolean left){
        this.left = left;
    }

    public void setRight(boolean right){
        this.right = right;
    }

    public String toString(){
        return x + "x--y" + y;
    }
}
