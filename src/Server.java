import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class Server {
    ServerSocket serverSocket;
    AuthService authService;
    HashSet<ClientHandler> clients;

    public Server() {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            authService = new AuthService();
            clients = new HashSet<>();
            while (true){
                System.out.println("Waiting for connections");
                Socket socket = serverSocket.accept();
                System.out.println(socket+"connecting");
                new ClientHandler(this, socket);

            }

        } catch (IOException e) {
            throw new RuntimeException("SWW", e);
        }

    }

    public synchronized  boolean privateMessage (String name, String message){
        for (ClientHandler client : clients){
            if (name.equals(client.getName())) {
                client.sendMessage(message);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean inNickFree (String nickname) {
        for (ClientHandler client : clients){
            if (client.getName().equals(nickname)) return false;
        }
        return true;
    }

    public synchronized void broadcast (String message){
        for (ClientHandler clientHandler : clients){
            clientHandler.sendMessage(message);
        }
    }

    public synchronized void unsubscribe (ClientHandler client){
        clients.remove(client);
    }

    public synchronized void subscribe (ClientHandler client){
        clients.add(client);
    }


    public  AuthService getAuthService() {
        return authService;
    }
}
