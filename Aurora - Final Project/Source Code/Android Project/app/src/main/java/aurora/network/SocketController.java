package aurora.network;

import java.net.Socket;

/**
 * Socket Controller - stores the socket connection.
 */

public class SocketController extends Thread {


    Response response = null;
    Socket clientSocket = null;

    public SocketController(Socket socket){
        clientSocket = socket;
    }

    public void run(){
        while(response == null){

        }
        response = null;
    }

}
