package xyz.chengzi.aeroplanechess.view;

import xyz.chengzi.aeroplanechess.controller.GameController;
import xyz.chengzi.aeroplanechess.listener.GameStateListener;
import xyz.chengzi.aeroplanechess.model.ChessBoard;
import xyz.chengzi.aeroplanechess.model.ChessBoardLocation;
import xyz.chengzi.aeroplanechess.model.ChessPiece;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GameFrame extends JFrame implements GameStateListener {
    private static final String[] PLAYER_NAMES = {"Yellow", "Blue", "Green", "Red"};
    private ChessBoard chessBoard;
    private final JLabel statusLabel = new JLabel();
    private JLabel statusLabel1 = new JLabel();
    private JLabel statusLabel2 = new JLabel();
    private JLabel statusLabel3 = new JLabel();
    private JLabel statusLabel4 = new JLabel();
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;

    public GameFrame(GameController controller) {
        controller.registerListener(this);
        chessBoard=controller.getModel();
        setTitle("2020 CS102A Project Demo");
        setSize(1300, 825);
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        statusLabel3.setLocation(780, 300);
        statusLabel3.setFont(statusLabel1.getFont().deriveFont(18.0f));
        statusLabel3.setSize(400, 20);
        add(statusLabel3);
        statusLabel4.setLocation(780, 330);
        statusLabel4.setFont(statusLabel1.getFont().deriveFont(18.0f));
        statusLabel4.setSize(400, 20);
        add(statusLabel4);
        statusLabel.setLocation(780, 30);
        statusLabel.setFont(statusLabel.getFont().deriveFont(18.0f));
        statusLabel.setSize(400, 20);
        add(statusLabel);
        statusLabel1.setLocation(780, 300);
        statusLabel1.setFont(statusLabel1.getFont().deriveFont(18.0f));
        statusLabel1.setSize(400, 20);
        add(statusLabel1);
        statusLabel2.setLocation(780, 330);
        statusLabel2.setFont(statusLabel2.getFont().deriveFont(18.0f));
        statusLabel2.setSize(400, 20);
        add(statusLabel2);


        DiceSelectorComponent diceSelectorComponent = new DiceSelectorComponent();
        diceSelectorComponent.setLocation(396, 758);
        add(diceSelectorComponent);
        JButton button = new JButton("roll");
        button.setLocation(780, 0);
        button.setFont(button.getFont().deriveFont(18.0f));
        button.setSize(90, 30);
        add(button);
        button1 = new JButton("+");
        button2 = new JButton("-");
        button3 = new JButton("*");
        button4 = new JButton("÷");
        button1.setLocation(780, 120);
        button1.setFont(button.getFont().deriveFont(18.0f));
        button1.setSize(90, 30);
        add(button1);
        button2.setLocation(780, 160);
        button2.setFont(button.getFont().deriveFont(18.0f));
        button2.setSize(90, 30);
        add(button2);
        button3.setLocation(780, 200);
        button3.setFont(button.getFont().deriveFont(18.0f));
        button3.setSize(90, 30);
        add(button3);
        button4.setLocation(780, 240);
        button4.setFont(button.getFont().deriveFont(18.0f));
        button4.setSize(90, 30);
        add(button4);
        button5 = new JButton("fight");
        button5.setLocation(780, 400);
        button5.setFont(button.getFont().deriveFont(18.0f));
        button5.setSize(90, 30);
        add(button5);
        button5.setVisible(false);
        button6 = new JButton("fly");
        button6.setLocation(780, 70);
        button6.setFont(button.getFont().deriveFont(18.0f));
        button6.setSize(90, 30);
        add(button6);
        AtomicInteger add = new AtomicInteger();
        AtomicInteger subtract = new AtomicInteger();
        AtomicInteger multiply= new AtomicInteger();
        AtomicInteger divide= new AtomicInteger();
        button.addActionListener((e) -> {
            int dice = controller.rollDice();
            int dice1 = controller.rollDice1();
            if (diceSelectorComponent.isRandomDice()) {
                if (dice != -1 || dice1 != -1) {
                    button1.setVisible(true);
                    button2.setVisible(true);
                    button3.setVisible(true);
                    button4.setVisible(true);
                    if (dice==6||dice1==6){
                        button6.setVisible(true);
                    }
                    add.set(dice + dice1);
                    subtract.set(Math.abs(dice - dice1));
                    multiply.set(dice * dice1);
                    if (dice/dice1==(double)dice/dice1) divide.set(dice / dice1);
                    if (dice1/dice==(double)dice1/dice) divide.set(dice1 / dice);
                    statusLabel.setText(String.format("[%s] Rolled a %d and a %d",
                            PLAYER_NAMES[controller.getCurrentPlayer()], dice, dice1));
                    button1.addActionListener((a) -> {
                        statusLabel1.setText(String.format("you choose %d steps,", add.get()));
                        statusLabel2.setText(String.format("please choose a chess to move"));
                        controller.setSteps(add.get());
                    });
                    button2.addActionListener((b) -> {
                        if (dice == dice1) {
                            statusLabel1.setText(String.format("it's 0."));
                            statusLabel2.setText(String.format("please choose another button"));
                        }
                        if (dice!=dice1) {
                            statusLabel1.setText(String.format("you choose to move %d steps,", subtract.get()));
                            statusLabel2.setText(String.format("please choose a chess to move"));
                            controller.setSteps(subtract.get());
                        }
                    });
                    button3.addActionListener((c) -> {
                        if (dice * dice1 <= 12) {
                            statusLabel1.setText(String.format("you choose to move %d steps,", multiply.get()));
                            controller.setSteps(multiply.get());
                        } else {
                            statusLabel1.setText(String.format("you choose to move 12 steps,"));
                            controller.setSteps(12);
                        }
                        statusLabel2.setText(String.format("please choose a chess to move"));
                    });
                    button4.addActionListener((d) -> {
                        if (dice % dice1 == 0) {
                            statusLabel1.setText(String.format("you choose to move %d steps,", divide.get()));
                            statusLabel2.setText(String.format("please choose a chess to move"));
                            controller.setSteps(divide.get());
                        }
                        if (dice1 % dice == 0) {
                            statusLabel1.setText(String.format("you choose to move %d steps,", divide.get()));
                            statusLabel2.setText(String.format("please choose a chess to move"));
                            controller.setSteps(divide.get());
                        }
                        if (dice % dice1 != 0 && dice1 % dice != 0) {
                            statusLabel1.setText(String.format("you can't move like this"));
                            statusLabel2.setText(String.format("please choose another button"));
                        }

                    });

                } else {
                    JOptionPane.showMessageDialog(this, "You have already rolled the dice");
                }
            } else {
                JOptionPane.showMessageDialog(this, "You selected " + diceSelectorComponent.getSelectedDice());
                int a= Integer.parseInt(diceSelectorComponent.getSelectedDice().toString());
                controller.setSteps(a);
            }

        });
    }
    public void fight(ChessBoardLocation src,ChessBoardLocation dest2,int m,int n){
        System.out.println("m");
        button5.setVisible(true);
            statusLabel3.setText(String.format("[%s] Rolled a %d", PLAYER_NAMES[chessBoard.getGridAt(src).getPiece().getPlayer()], m));
            statusLabel4.setText(String.format("[%s] Rolled a %d", PLAYER_NAMES[chessBoard.getGridAt(dest2).getPiece().getPlayer()], n));
            chessBoard.setM(m);
            chessBoard.setN(n);
        statusLabel4.setText(String.format("[%s] Please roll the dice", PLAYER_NAMES[chessBoard.getGridAt(src).getPiece().getPlayer()]));
    }
    @Override
    public void onPlayerStartRound(int player) {
        button1.setVisible(false);
        button2.setVisible(false);
        button3.setVisible(false);
        button4.setVisible(false);
        button6.setVisible(false);
        statusLabel1.setText("");
        statusLabel2.setText("");
        statusLabel.setText(String.format("[%s] Please roll the dice", PLAYER_NAMES[player]));
    }

    @Override
    public void onPlayerEndRound(int player) {

    }
}
