package xyz.chengzi.aeroplanechess.model;

public class ChessPiece {
    private final int player;
    private final int chess;
    private int chessNumber;

    public ChessPiece( int player, int chess,int chessNumber){
        this.player = player;
        this.chess = chess;
        this.chessNumber = chessNumber;
    }
    public int getPlayer() {
        return player;
    }
    public int getChess(){
        return chess;
    }
    public int getChessNumber(){
        return chessNumber;
    }
    public void setChessNumber(int chessNumber){
    this.chessNumber=chessNumber;
    }
}
