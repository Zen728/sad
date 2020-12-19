package xyz.chengzi.aeroplanechess.listener;

public class gamelis implements GameStateListener {
    @Override
    public void onPlayerEndRound(int player) {
        System.out.println("已结束");
    }
    public void onPlayerStartRound(int player) {
        System.out.println("已开始");
    }
}
