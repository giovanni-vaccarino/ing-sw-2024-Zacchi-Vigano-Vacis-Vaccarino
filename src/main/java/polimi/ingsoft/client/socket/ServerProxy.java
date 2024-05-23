package polimi.ingsoft.client.socket;

import polimi.ingsoft.client.common.VirtualView;
import polimi.ingsoft.server.common.VirtualServer;
import polimi.ingsoft.server.socket.protocol.MessageCodes;
import polimi.ingsoft.server.socket.protocol.NetworkMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ServerProxy implements VirtualServer {
    private final ObjectOutputStream out;

    public ServerProxy(ObjectOutputStream out) {
        this.out = out;
    }

    @Override
    public void setNickname(VirtualView client, String nickname) throws IOException {
        NetworkMessage message = new NetworkMessage(MessageCodes.SET_NICKNAME_REQUEST, nickname);
        out.writeObject(message);
        out.flush();
    }

    @Override
    public void getMatches(VirtualView client) throws IOException {
        NetworkMessage message = new NetworkMessage(MessageCodes.MATCHES_LIST_REQUEST, null);
        out.writeObject(message);
        out.flush();
    }

    @Override
    public void createMatch(String nickname, Integer requiredNumPlayers) throws IOException {
        NetworkMessage message = new NetworkMessage(MessageCodes.MATCH_CREATE_REQUEST, requiredNumPlayers);
        out.writeObject(message);
        out.flush();
    }

    @Override
    public void joinMatch(String nickname, Integer matchId) throws IOException {
        NetworkMessage message = new NetworkMessage(
                MessageCodes.MATCH_JOIN_REQUEST,
                matchId
        );
        out.writeObject(message);
        out.flush();
    }

    @Override
    public void reJoinMatch(Integer matchId, String nickname) throws IOException {

    }
}
