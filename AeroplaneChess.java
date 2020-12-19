package xyz.chengzi.aeroplanechess;

import xyz.chengzi.aeroplanechess.controller.GameController;
import xyz.chengzi.aeroplanechess.model.ChessBoard;
import xyz.chengzi.aeroplanechess.view.ChessBoardComponent;
import xyz.chengzi.aeroplanechess.view.GameFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AeroplaneChess {
    public static void main(String[] args) throws Exception{

            ChessBoardComponent chessBoardComponent = new ChessBoardComponent(900, 13, 6);//endDimension表示终点前的格数
            ChessBoard chessBoard = new ChessBoard(13, 6);
            GameController controller = new GameController(chessBoardComponent, chessBoard);


        GameFrame mainFrame = new GameFrame(controller);
        mainFrame.add(chessBoardComponent);
        mainFrame.setVisible(true);


//        mainFrame.jarray[4].setLocation(269,320 );

//        for (int i = 0; i < mainFrame.pieces.length; i++) {
//            mainFrame.jarray[0].setLocation(mainFrame.pieces[i]);
//           Thread.sleep(500);
//        }
           // controller.initializeGame(  );

    }

}
