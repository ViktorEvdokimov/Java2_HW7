package Clients;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientInterface {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ClientInterface() {
        try {
            System.out.println("Trying connection to server");
            socket = new Socket("127.0.0.1", 8888);
            System.out.println("Connection to server " + socket+ " successful");
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(this::waitingInput).start();
            Scanner sc = new Scanner(System.in);
            while (true){
                try {
                    out.writeUTF(sc.nextLine());
                } catch (Exception e){
                    System.out.println("Connecting lost");
                    break;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("SWW", e);
        } finally {
            try {
                out.writeUTF("/end");
            } catch (IOException e) {
                throw new RuntimeException("SWW", e );
            }
        }

    }

    private void waitingInput(){
        boolean isWork = true;
        while (isWork){
            try {
                System.out.println(in.readUTF());
            } catch (IOException e) {
                isWork=false;
                throw new RuntimeException("Connecting lost",e);
            }

        }
    }
}
