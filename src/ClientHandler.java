import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private final Server server;
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    private String name;

    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            this.in=new DataInputStream(socket.getInputStream());
            this.out=new DataOutputStream(socket.getOutputStream());
            new Thread(()->{
                try {
                    authentication();
                    readMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException("SWW", e);
        }


    }

    private void readMessage () throws IOException{
        while (true){
            String strFromClient = in.readUTF();
            if (strFromClient.startsWith("/w")){
                String[] parts = strFromClient.split("\\s");
                StringBuilder sb = new StringBuilder();
                for (int i=2; i< parts.length; i++){
                    sb.append(parts[i]);
                    sb.append(" ");
                }
                sb.setLength(sb.length()-1);
                if (server.privateMessage(parts[1], sb.toString())) sendMessage(name + " get your message.");
                else sendMessage("Incorrect private message.");
            } else if (strFromClient.equals("/end")) return;
            else server.broadcast(String.format("%s: %s", name, strFromClient));
        }
    }

    private void authentication() throws IOException{
        while (true){
            sendMessage("Start your message -auth to login");
            String inputData = in.readUTF();
            if (inputData.startsWith("-auth")){
                String[] parts = inputData.split("\\s");
                name = server.getAuthService().getNickByLoginPuss(parts[1], parts[2]);
                if (name!=null) {
                    if (server.inNickFree(name)){
                        sendMessage("Login successful");
                        server.broadcast(name+ " login");
                        server.subscribe(this);
                        return;
                    } else sendMessage("This login busy");
                } else sendMessage("Incorrect login or password");
            } else sendMessage("You should start your message -auth to login");
        }
    }

    private void closeConnection () {
        server.unsubscribe(this);
        server.broadcast(name + " logout");
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException("SWW", e);
        }
    }

    public void sendMessage (String message){
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }


}
