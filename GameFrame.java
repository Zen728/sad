package xyz.chengzi.aeroplanechess.view;

import xyz.chengzi.aeroplanechess.Client;
import xyz.chengzi.aeroplanechess.controller.GameController;
import xyz.chengzi.aeroplanechess.listener.GameStateListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Random;

public class GameFrame extends JFrame implements GameStateListener {
    private static final String[] PLAYER_NAMES = {"Yellow", "Blue", "Green", "Red"};

    private final JLabel statusLabel = new JLabel();
    public Point[] pieces = new Point[52 + 24 + 16 + 4];//棋盘上的全部点，前52个为普通点，再24个为最后一列点，再16个飞机场的点，再4个起步点
    public Point[] endpieces = new Point[24];//最后一列点
    public Point[] airport = new Point[16];//飞机场的点
    public Point[] waiting_area = new Point[4];//起步的格子
    public boolean waitingselect = false;
    public JLabel[] jarray;
    public int[] num = new int[4];
    public int dice;
    int dice2;

    public int turtle = 2;//0,1,2,3分别表示朝上右下左

    public GameFrame(GameController controller) throws Exception {
        //起步格子的位置
        waiting_area[0] = new Point(638, 512);
        waiting_area[1] = new Point(241, 648);
        waiting_area[2] = new Point(108, 254);
        waiting_area[3] = new Point(500, 122);

        //普通点的位置pieces
        //numbers数组只是作为测试，最后应该删掉
        JLabel[] numbers = new JLabel[52];
        {
            pieces[0] = new Point(469, 510);
            numbers[0] = new JLabel("0");
            numbers[0].setLocation(pieces[0]);
            numbers[0].setSize(20, 20);
            add(numbers[0]);

            for (int i = 1; i < 52; i++) {

                setNext(i);

                numbers[i] = new JLabel(i + "");
                numbers[i].setLocation(pieces[i]);
                numbers[i].setSize(20, 20);
                add(numbers[i]);


            }


        }

        //设置endpieces
        JLabel[] numbers1 = new JLabel[endpieces.length];
        {
            turtle = 0;
            int tx = 365;
            int ty = 378;
            endpieces[0] = new Point(tx, 575);
            endpieces[6] = new Point(170, ty);
            endpieces[12] = new Point(tx, 195 - 7);
            endpieces[18] = new Point(558, ty + 3);
            numbers1[0] = new JLabel("0");
            numbers1[0].setLocation(endpieces[0]);
            numbers1[0].setSize(20, 20);
            add(numbers1[0]);

            for (int i = 1; i < endpieces.length; i++) {
                numbers1[i] = new JLabel(i + "");
                numbers1[i].setSize(20, 20);

                if (i == 6 || i == 12 || i == 18) {
                    turtle = turnright(turtle);
                    numbers1[i].setLocation(endpieces[i]);
                    continue;
                }
                setNextEnd(i);
                numbers1[i].setLocation(endpieces[i]);

                add(numbers1[i]);


            }


        }
        {
            controller.registerListener(this);
            setTitle("Super Aeroplane Chess");
            setSize(772, 825);
            setLocationRelativeTo(null); // Center the window
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setLayout(null);

            statusLabel.setLocation(0, 700);
            statusLabel.setFont(statusLabel.getFont().deriveFont(18.0f));
            statusLabel.setSize(450, 22);
            add(statusLabel);


            DiceSelectorComponent diceSelectorComponent = new DiceSelectorComponent();
            diceSelectorComponent.setLocation(396, 700);
            add(diceSelectorComponent);

            // roll 按钮，覆写变成两个骰子
            JButton button = new JButton("roll");
            Random r = new Random();
            button.addActionListener((e) -> {
                if (diceSelectorComponent.isRandomDice()) {//为true时表示现在轮到玩家投了，而不是玩家已经投过，需要指定移动的棋子
                    int dice = controller.rollDice();
                    int dice2 = 1 + r.nextInt(6);
                    if (dice != -1) {
                        this.dice = dice;
                        this.dice2 = dice2;


                        //提示可以走的步数


                        num[0] = dice * dice2;
                        if (num[0] > 12) {
                            num[0] = 12;
                        }
                        num[1] = dice - dice2;
                        if (num[1] < 0) {
                            num[1] = -num[1];
                        }
                        num[2] = 0;
                        if (dice >= dice2 && (dice / (dice2 + 0.0)) % 1 == 0) {
                            num[2] = dice / dice2;
                        } else if (dice <= dice2 && (dice2 / (dice + 0.0)) % 1 == 0) {
                            num[2] = dice2 / dice;
                        }
                        num[3] = dice + dice2;
                        String s = "";
                        for (int i = 0; i < 3; i++) {
                            if (num[i] != 0) {
                                boolean alreadyDisplayed = false;
                                for (int j = 0; j < i; j++) {
                                    if (num[i] == num[j]) {
                                        alreadyDisplayed = true;
                                        break;
                                    }
                                }
                                if (!alreadyDisplayed) {
                                    s = s + num[i] + " ";
                                }
                            }

                        }
                        s = s + "or " + num[3] + " ";

                        statusLabel.setText(String.format("[%s] Rolled %d and %d,could go " + s + "steps",
                                PLAYER_NAMES[controller.getCurrentPlayer()], dice, dice2));
                        waitingselect = true;
                    } else {
                        JOptionPane.showMessageDialog(this, "You have already rolled the dice");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "You selected " + diceSelectorComponent.getSelectedDice());
                }
            });
            button.setLocation(668, 700);
            button.setFont(button.getFont().deriveFont(18.0f));
            button.setSize(90, 30);
            add(button);
        }

        {//填飞机场16个位置
            int tx = 551;
            int ty = 566;
            for (int i = 0; i < airport.length; i++) {
                if (i == 4)
                    tx -= 430;
                if (i == 8)
                    ty -= 433;
                if (i == 12)
                    tx += 430;
                airport[i] = new Point(tx + (i % 2) * 59, ty + ((i / 2) % 2) * 60);

            }


            //前面的airport数组应该已经把各点的位置都存好了，这时只需要new对应的图片作为JLabel对象，棋子就也好了
            jarray = new JLabel[airport.length];
            for (int i = 0; i < airport.length; i++) {
                String piece_name = "黄棋子.png";
                if (i / 4 == 1) {
                    piece_name = "蓝棋子.png";
                } else if (i / 4 == 2) {
                    piece_name = "绿棋子.png";
                } else if (i / 4 == 3) {
                    piece_name = "红棋子.png";
                }

                jarray[i] = new JLabel(new ImageIcon("C:\\Users\\Ld\\Desktop\\仅供参考\\CS102A-AeroplaneChess\\src\\xyz\\chengzi\\aeroplanechess\\view\\" + piece_name));//你最好换成其他图片。。
                jarray[i].setSize(40, 40);
                jarray[i].setLocation(airport[i]);
                jarray[i].setVisible(true);
                int finalI = i;
                jarray[i].addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e){
                        ImageIcon ll=new ImageIcon("C:\\Users\\Ld\\Desktop\\仅供参考\\CS102A-AeroplaneChess\\src\\xyz\\chengzi\\aeroplanechess\\view\\红棋子.png");
                        jarray[finalI].setIcon(ll);
                    }
                    public void mouseExited(MouseEvent e){
                        ImageIcon ll=new ImageIcon("C:\\Users\\Ld\\Desktop\\仅供参考\\CS102A-AeroplaneChess\\src\\xyz\\chengzi\\aeroplanechess\\view\\蓝棋子.png");
                        jarray[finalI].setIcon(ll);

                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {






                        if (waitingselect) {

                            int player = controller.getCurrentPlayer();
                            int k = getLocation(jarray[finalI]);
                            if ((dice == 6 || dice2 == 6) && k < pieces.length - 4 && k >= pieces.length - 4 - 16) {//对还停在飞机场的点
                                jarray[finalI].setLocation(waiting_area[player]);


                            } else {
                                if (k >= pieces.length - 4) {//对停在起步位置的点
                                    switch (player) {
                                        case 0:
                                            jarray[finalI].setLocation(pieces[49 - 1 + num[0]]);
                                            break;//先只能走固定步数，两骰子数目相乘的结果，最大为12
                                        case 1:
                                            jarray[finalI].setLocation(pieces[10 - 1 + num[0]]);
                                            break;
                                        case 2:
                                            jarray[finalI].setLocation(pieces[23 - 1 + num[0]]);
                                            break;
                                        case 3:
                                            jarray[finalI].setLocation(pieces[36 - 1 + num[0]]);
                                            break;

                                    }
                                } else if (k < pieces.length - 4 - 16 - 24) {//对在棋盘边上的点
                                    jarray[finalI].setLocation(pieces[k + num[0]]);//先只考虑第一个骰子
                                }


                            }//棋盘上的全部点，前52个为普通点，再24个为最后一列点，再16个飞机场的点，再4个起步点
                            waitingselect = false;
                            onPlayerStartRound(controller.nextPlayer());

                        }
                    }
                    //只有当投过骰子以后点击才有用(当前只针对 auto模式，未考虑manual模式)
                });
                add(jarray[i]);
            }

            for (int i = 52; i < pieces.length; i++) {
                if (i < 52 + endpieces.length) {
                    pieces[i] = endpieces[i - 52];
                } else if (i < 52 + endpieces.length + airport.length) {
                    pieces[i] = airport[i - 52 - 24];
                } else {
                    pieces[i] = waiting_area[i - 52 - 24 - 16];
                }
            }//棋盘上的全部点，前52个为普通点，再24个为最后一列点，再16个飞机场的点，再4个起步点


            //直接整一个棋盘图片作JLabel对象，当作背景图
            ImageIcon board_icon = new ImageIcon("C:\\Users\\Ld\\Desktop\\仅供参考\\CS102A-AeroplaneChess\\src\\xyz\\chengzi\\aeroplanechess\\view\\ChessBoard.png");//最好换张图！
            JLabel board = new JLabel(board_icon);
            board.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println(e.getX() + "," + e.getY());//任何一个JLabel都可以用这个方法，只需要改掉这一句，点击即可发生你想要发生的事。这句是为了点击时能够显示x，y坐标，方便你设置其他Point的位置
                }
            });

            board.setSize(772, 800);
            add(board);

        }

    }


    @Override
    public void onPlayerStartRound(int player) {
        statusLabel.setText(String.format("[%s] Please roll the dice", PLAYER_NAMES[player]));
    }

    @Override
    public void onPlayerEndRound(int player) {

    }

    public void setNext(int i) {
        int gap = 32;
        int txgap = 16;//经测算，三角转方格处x的增量
        int tygap = 39;//三角转方格处y的增量

        pieces[i] = new Point(pieces[i - 1]);
        switch (i % 13) {
            case 1:
                leftgo(turtle, pieces[i], txgap);
                forward(turtle, pieces[i], tygap);
                break;
            case 2:
                forward(turtle, pieces[i], gap);
                break;
            case 3:
                rightgo(turtle, pieces[i], txgap);
                forward(turtle, pieces[i], tygap);
                break;
            case 4:
                turtle = turnright(turtle);
                leftgo(turtle, pieces[i], txgap - 7);
                forward(turtle, pieces[i], gap + 4);
                break;
            case 5:
            case 6:
            case 7:
            case 8:
                forward(turtle, pieces[i], gap);
                break;
            case 9:
                forward(turtle, pieces[i], tygap);
                rightgo(turtle, pieces[i], txgap - 2);
                break;
            case 10:
                turtle = turnright(turtle);
                forward(turtle, pieces[i], tygap);
                leftgo(turtle, pieces[i], txgap);
                break;
            case 11:
                forward(turtle, pieces[i], gap);
                break;
            case 12:
                rightgo(turtle, pieces[i], txgap);
                forward(turtle, pieces[i], gap);
                break;
            default:
                turtle = turnleft(turtle);
                forward(turtle, pieces[i], 25);
                rightgo(turtle, pieces[i], 27);
                break;

        }

    }

    //后面几个方法里的i都是主方法里的turtle,表示当前海龟的朝向
    public int turnright(int i) {
        return (i + 1) % 4;
    }

    public int turnleft(int i) {
        return i - 1 < 0 ? 3 : (i - 1);
    }

    public void forward(int i, Point p, int distance) {
        switch (i) {
            case 0:
                p.setLocation(p.getX(), p.getY() - distance);
                break;
            case 1:
                p.setLocation(p.getX() + distance, p.getY());
                break;
            case 2:
                p.setLocation(p.getX(), p.getY() + distance);
                break;
            case 3:
                p.setLocation(p.getX() - distance, p.getY());
                break;
        }
    }

    public void leftgo(int i, Point p, int distance) {
        switch (i) {
            case 0:
                p.setLocation(p.getX() - distance, p.getY());
                break;
            case 1:
                p.setLocation(p.getX(), p.getY() - distance);
                break;
            case 2:
                p.setLocation(p.getX() + distance, p.getY());
                break;
            case 3:
                p.setLocation(p.getX(), p.getY() + distance);
                break;
        }
    }

    public void rightgo(int i, Point p, int distance) {
        switch (i) {
            case 0:
                p.setLocation(p.getX() + distance, p.getY());
                break;
            case 1:
                p.setLocation(p.getX(), p.getY() + distance);
                break;
            case 2:
                p.setLocation(p.getX() - distance, p.getY());
                break;
            case 3:
                p.setLocation(p.getX(), p.getY() - distance);
                break;
        }
    }

    public void setNextEnd(int i) {
        int gap = 32;
        endpieces[i] = new Point(endpieces[i - 1]);
        forward(turtle, endpieces[i], gap);


    }

    //取回它对应的格子序数
    public int getLocation(JLabel lab) {

        for (int i = 0; i < pieces.length; i++) {
            Point a = lab.getLocation();
            if (a.getX() == pieces[i].getX() && a.getY() == pieces[i].getY()) {
                return i;
            }
        }

        return -1;
    }


    //若有至少一个棋子出了飞机场，则true
    public boolean hasOut(int player) {
        int a = 4 * player;
        int b = 0;

        for (int i = 0; i < 4; i++) {

            JLabel lab = jarray[a + i];
            for (int j = 0; j < 4; j++) {
                Point p = airport[a + j];
                if (p.getX() == lab.getX() && p.getY() == lab.getY()) {
                    b++;
                }
            }


        }

        return b == 4 ? false : true;
    }
    public void updateInternet() throws Exception{
        Client c=new Client();
        c.receive();
    }

}
