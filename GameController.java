package xyz.chengzi.aeroplanechess.controller;

import xyz.chengzi.aeroplanechess.listener.GameStateListener;
import xyz.chengzi.aeroplanechess.listener.InputListener;
import xyz.chengzi.aeroplanechess.listener.Listenable;
import xyz.chengzi.aeroplanechess.model.ChessBoard;
import xyz.chengzi.aeroplanechess.model.ChessBoardLocation;
import xyz.chengzi.aeroplanechess.model.ChessPiece;
import xyz.chengzi.aeroplanechess.util.RandomUtil;
import xyz.chengzi.aeroplanechess.view.ChessBoardComponent;
import xyz.chengzi.aeroplanechess.view.ChessComponent;
import xyz.chengzi.aeroplanechess.view.GameFrame;
import xyz.chengzi.aeroplanechess.view.SquareComponent;

import java.util.ArrayList;
import java.util.List;

public class GameController implements InputListener, Listenable<GameStateListener> {
    private final List<GameStateListener> listenerList = new ArrayList<>();
    private final ChessBoardComponent view;
    private final ChessBoard model;
    private Integer steps;
    private Integer rolledNumber;
    private Integer rolledNumber1;
    private int currentPlayer;
    private int n=1;
    private ChessBoardLocation chessBoardLocation1;
    private ChessBoardLocation chessBoardLocation2;


    public GameController(ChessBoardComponent chessBoardComponent, ChessBoard chessBoard) {
        this.view = chessBoardComponent;
        this.model = chessBoard;
        view.registerListener(this);
        model.registerListener(view);
    }
    public void setN(int n){
       this.n=n;
    }

    public int getN() {
        return n;
    }

    public void setSteps(int n ){
        this.steps=n;
    }
    public ChessBoardComponent getView() {
        return view;
    }

    public ChessBoard getModel() {
        return model;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void initializeGame() {
        model.placeInitialPieces();
        rolledNumber = null;
        rolledNumber1=null;
        currentPlayer = 0;
        listenerList.forEach(listener -> listener.onPlayerStartRound(currentPlayer));
    }

    public int rollDice() {
        if (rolledNumber == null) {
            return rolledNumber = RandomUtil.nextInt(1, 6);
        } else {
            return -1;
        }
    }
    public int rollDice1() {
        if (rolledNumber1 == null) {
            return rolledNumber1 = RandomUtil.nextInt(1, 6);
        } else {
            return -1;
        }
    }
    public int nextPlayer() {
        rolledNumber = null;
        rolledNumber1=null;
        steps=null;
        currentPlayer = (currentPlayer + 1) % 4;
        return currentPlayer;
    }
    public void setLocation1(ChessBoardLocation location){
        chessBoardLocation1=location;
    }
    public void setLocation2(ChessBoardLocation location){
        chessBoardLocation2=location;
    }


    @Override
    public void onPlayerClickSquare(ChessBoardLocation location, SquareComponent component) {
        System.out.println("clicked " + location.getColor() + "," + location.getIndex());
    }

    @Override
    public void onPlayerClickChessPiece(ChessBoardLocation location, ChessComponent component) {
        if (steps != null) {
            ChessPiece piece = model.getChessPieceAt(location);
            if (piece.getPlayer() == currentPlayer) {
                if (rolledNumber+rolledNumber1>=10){
                    System.out.println(rolledNumber+rolledNumber1);
                    currentPlayer=(currentPlayer+3)%4;
                    n=n+1;
                    if (n==3){
                        model.backToHome(chessBoardLocation1,chessBoardLocation2);
                    }
                    else model.moveChessPiece(location,steps,(currentPlayer+1)%4);
                }
                else model.moveChessPiece(location,steps,currentPlayer);
                listenerList.forEach(listener -> listener.onPlayerEndRound(currentPlayer));
                nextPlayer();
                listenerList.forEach(listener -> listener.onPlayerStartRound(currentPlayer));
            }
        }
    }
    public boolean chessNotFly(){
        ChessBoardLocation region0 = new ChessBoardLocation(0,0);
        ChessBoardLocation region1 = new ChessBoardLocation(0,1);
        ChessBoardLocation region2 = new ChessBoardLocation(0,2);
        ChessBoardLocation region3 = new ChessBoardLocation(0,3);
        if (model.getChessPieceAt(region0)!=null&&model.getChessPieceAt(region1)!=null&&model.getChessPieceAt(region2)!=null&&model.getChessPieceAt(region3)!=null){
            return true;
        }
        return false;
    }
    @Override
    public void registerListener(GameStateListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void unregisterListener(GameStateListener listener) {
        listenerList.remove(listener);
    }
}
