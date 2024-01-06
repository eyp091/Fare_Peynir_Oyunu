import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main {
    public static int width;
    public static int height;
    public static char[][] maze1;
    public static void main(String[] args) {
        try {
            //Java Swing arayüzü için görünüm ayarlarını değiştirmek için kullanılır.
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                create();
            }
        });

    }
    public static void create(){
        JFrame frame = new JFrame("Mouse Maze");
        ImageIcon icon = new ImageIcon("indir.png");
        frame.setIconImage(icon.getImage());
        final Maze maze = new Maze();
        frame.setSize(700, 700);
        //Frame'in ekranın merkezine yerleştirmek için kullanılır.
        frame.setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2)-264,
                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2)-330);
        maze.setSize(300, 300);
        Container content = frame.getContentPane();

        //Yeni conteiner oluştur.
        content.setLayout(new BorderLayout() );
        frame.add(maze,BorderLayout.CENTER);

        JPanel panel1 = new JPanel(new GridLayout());
        JPanel panel2 = new JPanel(new GridLayout());
        JPanel panel3 = new JPanel(new BorderLayout());
        JPanel finish = new JPanel(new BorderLayout());
        JButton pickFile = new JButton("Dosya Seç");
        final JLabel textFile = new JLabel(" ex: maze.txt ");

        //dosya seçe tıklandığında.
        pickFile.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                //Kullanıcının bilgisayarındaki dosyalara erişimine olanak tanır.
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        ".txt file", "txt");
                chooser.setFileFilter(filter);

                int returnVal = chooser.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        BufferedReader read = new BufferedReader(new FileReader(chooser.getSelectedFile()));
                        String rea = read.readLine();
                        String[] split = rea.split(" ");
                        width = Integer.valueOf(split[0]);
                        height = Integer.valueOf(split[1]);

                        String readline;
                        int num = 0;
                        maze1 = new char[width][height];
                        while((readline = read.readLine()) != null){
                            char[] ch = readline.toCharArray();
                            for(int i = 0;i < ch.length;i++){
                                maze1[i][num] = ch[i];
                            }
                            num++;
                        }
                        maze.setMaze(maze1);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    textFile.setText(chooser.getSelectedFile().getName());
                }else{

                }
            }

        });

        //Text fieldları oluşturma.
        final JTextField mouseText = new JTextField("x,y",15);
        final JTextField cheeseText = new JTextField("x,y",15);
        final JLabel error = new JLabel("HATA MESAJLARI!!!!!");

        //Butonları oluşturma.
        JButton setMouse = new JButton("Fareyi Ayarla");
        JButton setChees = new JButton("Peyniri Ayarla");
        JButton solve = new JButton("Çözümü Başlat");
        JButton traverse = new JButton("Çapraz Geçiş");

        //Panellere ekleme.
        panel1.add(pickFile);
        panel1.add(textFile);
        panel2.add(solve);
        panel2.add(traverse);
        panel3.add(error,BorderLayout.SOUTH);
        panel1.add(setMouse);
        panel1.add(mouseText);
        panel1.add(setChees);
        panel1.add(cheeseText);
        finish.add(panel1,BorderLayout.NORTH);
        finish.add(panel2,BorderLayout.CENTER);
        finish.add(panel3,BorderLayout.SOUTH);
        content.add(finish, BorderLayout.SOUTH);

        //Traverse tıklandığında.
        traverse.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                maze.TraverseMouse(0);
            }

        });
        //Başlatmaya tıkladığında.
        solve.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub

                maze.solve();

                error.setText(maze.navigation);

            }

        });
        //Peynir konumunu atama.
        setChees.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String cheesePosition = cheeseText.getText();
                String[] splitposition = cheesePosition.split(",");
                try{
                    int x = Integer.valueOf(splitposition[0]);
                    int y = Integer.valueOf(splitposition[1]);
                    if(x >= width || y >= height){
                        error.setText("those coordinates are not in range of your maze, try again, 0-"+ (width - 1)+",0-"+(height-1));
                    }else if(maze1[x][y] == '1'){
                        error.setText("you cannot place the mouse on a wall, try again");
                    }else{
                        maze.setCheese(x,y);
                        error.setText("coordinates set.");
                    }
                }catch(NumberFormatException e){
                    error.setText("You Must enter the cheese position \"x,y\" example: 10,20");
                }catch(ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                    error.setText("You Must enter the cheese position \"x,y\" example: 10,20");
                }

            }

        });
        //Fareynin konumunu atama.
        setMouse.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String mousePosition = mouseText.getText();
                String[] splitposition = mousePosition.split(",");
                try{
                    int x = Integer.valueOf(splitposition[0]);
                    int y = Integer.valueOf(splitposition[1]);
                    if(x >= width || y >= height){
                        error.setText("those coordinates are not in range of your maze, try again, 0-"+ (width - 1)+",0-"+(height-1));
                    }else if(maze1[x][y] == '1'){
                        error.setText("you cannot place the mouse on a wall, try again");
                    }else{
                        maze.setMouse(x, y);
                        error.setText("coordinates set.");
                    }
                }catch(NumberFormatException e){
                    error.setText("You Must enter the mouse position \"x,y\" example: 10,20");
                }catch(ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                    error.setText("You Must enter the mouse position \"x,y\" example: 10,20");
                }

            }

        });



        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

}