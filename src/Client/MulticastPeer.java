import java.net.*;
import java.io.*;
public class MulticastPeer{
    private Boolean flag = true;
    // args give message contents and destination multicast group (e.g. "228.5.6.7")
    MulticastSocket s = null;
    
    public MulticastPeer(String args[]){ 
        try {
            InetAddress group = InetAddress.getByName(args[1]);
            this.s = new MulticastSocket(6789);
            s.joinGroup(group);
            byte [] m = args[0].getBytes();
            DatagramPacket messageOut = new DatagramPacket(m, m.length, group, 6789);
            s.send(messageOut); 
            byte[] buffer = new byte[1000];
            new Thread(){
                @Override
                public void run(){
                    while(flag) {        // get messages from others in group
                        try{
                            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                            s.receive(messageIn);
                            System.out.println("Received:" + new String(messageIn.getData()));
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
            s.leaveGroup(group);        
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){System.out.println("IO: " + e.getMessage());
        }finally {if(s != null) s.close();}
    }  
        
    public void stop(){
        flag = false;
    }             
    
}