import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;

public class Maze extends JComponent{
    public char[][] maze = null;
    public Rectangle[][] rects = null;
    public boolean mousebool = false;
    public boolean cheeseBool = false;
    public boolean backtrack = false;
    public Mouse mouse;
    public Cheese cheese;

    public Coordinate startingPoint;
    public boolean first = true;
    public boolean solv = true;
    public int lastx,lasty;
    public String navigation = "working!!!";

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(maze != null){
            for(int i = 0;i < maze.length;i++){
                for(int j = 0;j < maze[0].length;j++)
                    if(maze[i][j] == '1'){
                        g.setColor(Color.BLACK);
                        g.fillRect(rects[i][j].x, rects[i][j].y, rects[i][j].width, rects[i][j].height);
                        g.setColor(Color.WHITE);
                        g.drawRect(rects[i][j].x, rects[i][j].y, rects[i][j].width, rects[i][j].height);
                    }
                    else if(maze[i][j] == '0'){
                        g.setColor(Color.white);
                        g.fillRect(rects[i][j].x, rects[i][j].y, rects[i][j].width, rects[i][j].height);
                        g.setColor(Color.BLACK);
                        g.drawRect(rects[i][j].x, rects[i][j].y, rects[i][j].width, rects[i][j].height);
                    }
            }
            if(mousebool){
                g.setColor(Color.RED);

                g.fillOval(rects[mouse.x][mouse.y].x, rects[mouse.x][mouse.y].y, rects[mouse.x][mouse.y].width, rects[mouse.x][mouse.y].height);
            }
            if(cheeseBool){
                g.setColor(Color.ORANGE);
                g.fillOval(rects[cheese.x][cheese.y].x, rects[cheese.x][cheese.y].y, rects[cheese.x][cheese.y].width, rects[cheese.x][cheese.y].height);
            }
        }

    }
    public void setMaze(char[][] maze){
        mousebool = false;
        this.maze = maze;
        rects = new Rectangle[maze.length][maze[0].length];
        for(int i = 0;i < rects.length;i++){
            for(int j = 0;j < rects[0].length;j++){
                rects[i][j] = new Rectangle(i * (getWidth()/rects.length),j * (getHeight()/rects[0].length)
                        ,getWidth()/rects.length,getHeight()/rects[0].length);
            }
        }
        repaint();
    }
    public void setMouse(int x,int y){
        mousebool = true;
        mouse = new Mouse(x,y);
        startingPoint = new Coordinate(x,y);
        mouse.solution.add(new Coordinate(x,y));
        mouse.decision.push(new Coordinate(x,y));
        first = true;

        repaint();
    }

    public void setCheese(int x, int y){
        cheeseBool = true;
        cheese = new Cheese(x,y);
        startingPoint = new Coordinate(x,y);
        first = true;
        repaint();
    }
    public void solve(){

        if(mouse.x > 0 && mouse.x < maze.length - 1 && mouse.y > 0 && mouse.y < maze[0].length - 1 && mouse.decision.size() > 0){

            System.out.println(wasHere(new Coordinate(mouse.x,mouse.y - 1)) + "north");
            if (!(maze[mouse.x][mouse.y - 1] == '1') && !wasHere(new Coordinate(mouse.x,mouse.y - 1))) mouse.decision.push(new Coordinate(mouse.x,mouse.y - 1));

            System.out.println(wasHere(new Coordinate(mouse.x + 1,mouse.y)) + "east");
            if (!(maze[mouse.x + 1][mouse.y] == '1') && !wasHere(new Coordinate(mouse.x + 1,mouse.y))) mouse.decision.push(new Coordinate(mouse.x + 1,mouse.y));

            if (!(maze[mouse.x - 1][mouse.y] == '1') && !wasHere(new Coordinate(mouse.x - 1,mouse.y))) mouse.decision.push(new Coordinate(mouse.x - 1,mouse.y));

            if(!(maze[mouse.x][mouse.y + 1] == '1') && !wasHere(new Coordinate(mouse.x,mouse.y + 1))) mouse.decision.push(new Coordinate(mouse.x,mouse.y + 1));

            Coordinate coord = mouse.decision.pop();

            mouse.x = coord.x;
            mouse.y = coord.y;

            mouse.solution.add(coord);

            ActionListener taskPerformer = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    solve();

                }
            };
            javax.swing.Timer t = new javax.swing.Timer( 250, taskPerformer);
            t.setRepeats(false);
            t.start();

            if (mouse.x == cheese.x && mouse.y == cheese.y){
                System.out.println("fare peyniri buldu");
                t.stop();
                JOptionPane.showMessageDialog(null, "Oyun bitti!", "Oyun Durumu", JOptionPane.INFORMATION_MESSAGE);
            }


            repaint();
        }else{
            solv = false;
            if(mouse.decision.size() == 0){
                mouse.solution.clear();
                navigation = "there is no solution";
            }else{
                mouse.solution.clear();
                findPath(startingPoint.x,startingPoint.y);
                navigation = "maze complete";
            }
        }


    }
    public boolean findPath(int x,int y){
        if(x < 0 || y < 0 || x > maze.length - 1 || y > maze[0].length - 1)return false;

        if(!(x > 0 && x < maze.length - 1 && y > 0 && y < maze[0].length - 1) && maze[x][y] == '0')
        {
            mouse.solution.add(new Coordinate(x,y));
            return true;
        }
        if(maze[x][y] == '1') return false;

        mouse.solution.add(new Coordinate(x,y));
        if(!mouse.solution.contains(new Coordinate(x,y - 1)))
            if(findPath(x,y-1) == true) return true;
        if(!mouse.solution.contains(new Coordinate(x+1,y)))
            if(findPath(x + 1,y) == true) return true;
        if(!mouse.solution.contains(new Coordinate(x,y+1)))
            if(findPath(x,y + 1)== true) return true;
        if(!mouse.solution.contains(new Coordinate(x-1,y)))
            if(findPath(x - 1,y)== true) return true;
        mouse.solution.remove(new Coordinate(x,y));
        return false;
    }

    public boolean wasHere(Coordinate coord){
        return mouse.solution.contains(coord);
    }
    public void TraverseMouse(int x){
        if(x < mouse.solution.size()){
            Coordinate coord = mouse.solution.get(x);
            mouse.x = coord.x;
            mouse.y = coord.y;
            final int xt = x+=1;
            ActionListener taskPerformer = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    TraverseMouse(xt);

                }
            };
            javax.swing.Timer t = new javax.swing.Timer( 250, taskPerformer);
            t.setRepeats(false);
            t.start();
        }
        repaint();

    }
    public String Navigation(){
        return navigation;
    }
}
