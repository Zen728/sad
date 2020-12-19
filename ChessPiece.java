package xyz.chengzi.aeroplanechess.model;

import javax.swing.*;

public class ChessPiece extends JLabel {
    private final int player;


    public ChessPiece(int player) {
        this.player = player;

    }

    public int getPlayer() {
        return player;
    }
}
