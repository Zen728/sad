package xyz.chengzi.aeroplanechess;//视情况可以删掉这一句

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server {
    public static void main(String args[]) throws Exception{
       Server ser=new Server();
       String s="123Hello";//这里填要发送的字符串
       ser.send(s);



    }

    public void send(String s) throws Exception {
        int myport=4000;//本机端口，需要与Client类的端口保持一致
        int yourport=10031;//接收方客户端的端口
        DatagramSocket ds=new DatagramSocket(myport);
        DatagramPacket dp=new DatagramPacket(s.getBytes(),s.length(), InetAddress.getByName("10.21.66.160"),yourport);//目标客户端端口
        System.out.println("信息已发送,请等待下一位玩家操作...");
        ds.send(dp);
        ds.close();
    }

}
