package polimi.ingsoft.server.socket;

import polimi.ingsoft.client.rmi.VirtualView;
import polimi.ingsoft.server.controller.MainController;
import polimi.ingsoft.server.controller.MatchController;
import polimi.ingsoft.server.model.Message;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class SocketServer {
    private final int port;
    private final PrintStream logger;
    private final MainController controller;
    private final List<VirtualView> clients = new ArrayList<>();

    public SocketServer(int port, PrintStream logger, MainController controller) {
        this.port = port;
        this.logger = logger;
        this.controller = controller;
    }

    public void handleIncomingConnections() {
        logger.println("SOCKET: Starting server");
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket server;
        try {
            server = new ServerSocket(port);
            logger.println("SOCKET: Server ready");
            while (true) {
                try {
                    Socket socket = server.accept();
                    ConnectionHandler handler = new ConnectionHandler(socket, controller, this, logger);
                    clients.add(handler);
                    executor.submit(handler);
                } catch (IOException e) {
                    break;
                }
            }
            server.close();
        } catch (IOException ignored) { }
        executor.shutdown();
    }

    public void singleUpdateMatchesList(VirtualView client, List<Integer> matches) {
        // TODO fixare e usare granularità sul singolo client
        synchronized (this.clients) {
            try {
                client.showUpdateMatchesList(matches);
            } catch (IOException ignored) { }
        }
    }

    public void broadcastUpdateMatchesList(List<Integer> matches) throws IOException {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdateMatchesList(matches);
            }
        }
    }

    public void singleUpdateMatchCreate(VirtualView client, MatchController match) throws IOException {
        // TODO fixare e usare granularità sul singolo client
        synchronized (this.clients) {
            client.showUpdateMatchCreate(match);
        }
    }

    public void singleUpdateMatchJoin(VirtualView client, Boolean success) {
        // TODO fixare e usare granularità sul singolo client
        synchronized (this.clients) {
            try {
                client.showUpdateMatchJoin(success);
            } catch (IOException ignored) { }
        }
    }

    public void broadcastUpdatePublicBoard() {
        synchronized (this.clients) {
            for (var client : this.clients) {
                try {
                    client.showUpdatePublicBoard();
                } catch (IOException ignored) { }
            }
        }
    }

    public void broadcastUpdateBoard() {
        synchronized (this.clients) {
            for (var client : this.clients) {
                try {
                    client.showUpdateBoard();
                } catch (IOException ignored) { }
            }
        }
    }

    public void broadcastUpdateChat(Message message) {
        synchronized (this.clients) {
            for (var client : this.clients) {
                try {
                    client.showUpdateChat(message);
                } catch (IOException ignored) { }
            }
        }
    }
}
