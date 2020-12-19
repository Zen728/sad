package xyz.chengzi.aeroplanechess;//视情况可以删掉这一句

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
    public static void main(String args[])throws  Exception{
        Client cli=new Client();
        while (true){
        cli.receive();
        }

    }


   public void receive() throws Exception{
       byte[] b=new byte[1024];
       int myport=10030;//本机的端口，可随意设置
       DatagramSocket ds=new DatagramSocket(myport);
       DatagramPacket dp=new DatagramPacket(b,1024,InetAddress.getByName( "10.21.10.18"),myport);
   //    DatagramPacket dp=new DatagramPacket(b,1024,myport);


           System.out.println("等待接收数据...");
           ds.receive(dp);
           System.out.println("收到信息！");
           String str = new String(dp.getData(), 0, dp.getLength())+"  From:"+dp.getAddress();
           System.out.println(str);
           ds.close();
       }

}
