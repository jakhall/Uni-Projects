package aurora.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Aurora Client - Creates the connection to the server.
 */

public class AuroraClient extends Thread {

    private String ip = "86.13.177.165";
    private final int port = 10777;
    private boolean isConnected = false;
    private Socket clientSocket = null;


    public AuroraClient() {

    }

    public AuroraClient(String ip) {
        this.ip = ip;
    }

    public boolean isConnected(){
        return isConnected;
    }

    public Socket getSocket(){
        return clientSocket;
    }


    //Shutdown - closes the client socket and sets isConnected to false.

    public void shutdown() {
        System.out.println("Client: Disconnecting..");
        isConnected = false;
        try {
            clientSocket.close();
            clientSocket = null;
            System.out.println("Client: Disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Client: Disconnect Failed.");
        }

    }


    //Run - obtains the socket connection from the server if exists, sets isConnected to true.

    public void run() {
        Socket socket = null;
        System.out.println("Client: Connecting..");

        try {
            clientSocket = new Socket(ip, port);
            System.out.println("Client: Connected.");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        isConnected = true;

        return;
    }
}


