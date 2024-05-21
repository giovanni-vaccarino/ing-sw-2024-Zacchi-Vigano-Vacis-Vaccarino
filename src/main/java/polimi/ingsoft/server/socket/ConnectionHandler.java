package polimi.ingsoft.server.socket;

import polimi.ingsoft.client.common.VirtualView;
import polimi.ingsoft.server.common.Utils;
import polimi.ingsoft.server.common.VirtualMatchServer;
import polimi.ingsoft.server.controller.GameState;
import polimi.ingsoft.server.controller.MainController;
import polimi.ingsoft.server.controller.MatchController;
import polimi.ingsoft.server.controller.PlayerInitialSetting;
import polimi.ingsoft.server.enumerations.ERROR_MESSAGES;
import polimi.ingsoft.server.enumerations.PlayerColor;
import polimi.ingsoft.server.exceptions.*;
import polimi.ingsoft.server.model.*;
import polimi.ingsoft.server.socket.protocol.MessageCodes;
import polimi.ingsoft.server.socket.protocol.SocketMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

public class ConnectionHandler implements Runnable, VirtualView {
    private final Socket socket;
    private final MainController controller;
    private MatchController matchController = null;
    private final VirtualView view;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final SocketServer server;

    private final PrintStream logger;
    private String nickname = Utils.getRandomNickname();

    public ConnectionHandler(Socket socket, MainController controller, SocketServer server, PrintStream logger) throws IOException {
        this.socket = socket;
        this.controller = controller;
        this.server = server;
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.view = new ClientProxy(out);
        this.logger = logger;
        this.server.addClient(this, nickname);
    }

    @Override
    public void run() {
        try {
            SocketMessage item;

            while ((item = (SocketMessage) in.readObject()) != null) {
                MessageCodes type = item.type;
                Object payload = item.payload;
                logger.println("SOCKET: Received request with type: " + type.toString());
                logger.println("SOCKET: And payload: " + payload);

                // Read message and perform action
                try {
                    switch (type) {
                        case CONNECT -> this.addClient();
                        case SET_NICKNAME_REQUEST -> {
                            String nickname = (String) payload;
                            try {
                                this.server.setNicknameForClient(this.nickname, nickname);
                            } catch (NicknameNotAvailableException exception) {
                                this.reportError(ERROR_MESSAGES.NICKNAME_NOT_AVAILABLE);
                            }
                            this.nickname = nickname;
                            this.server.singleUpdateNickname(this);
                        }
                        case MATCHES_LIST_REQUEST -> {
                            List<Integer> matches = controller.getMatches();
                            this.server.singleUpdateMatchesList(this, matches);
                        }
                        case MATCH_JOIN_REQUEST -> {
                            int id = (Integer) payload;
                            try {
                                controller.joinMatch(id, nickname);
                                this.server.singleUpdateMatchJoin(this);

                                MatchController matchController = controller.getMatch(id);
                                // Set match controller for usage later
                                this.matchController = matchController;
                                List<String> nicknames = matchController.getNamePlayers();
                                this.server.lobbyUpdatePlayerJoin(nicknames);
                            } catch (MatchAlreadyFullException exception) {
                                this.reportError(ERROR_MESSAGES.MATCH_IS_ALREADY_FULL);
                            } catch (MatchNotFoundException exception) {
                                this.reportError(ERROR_MESSAGES.MATCH_DOES_NOT_EXIST);
                            }
                        }
                        case MATCH_CREATE_REQUEST -> {
                            int numberForPlayers = (int) payload;
                            int id = controller.createMatch(numberForPlayers);
                            this.server.singleUpdateMatchCreate(this, id);
                            List<Integer> matches = controller.getMatches();
                            this.server.broadcastUpdateMatchesList(matches);
                            // It is client's responsibility to join the match right after
                        }
                        case SET_COLOR_REQUEST -> {
                            PlayerColor color = (PlayerColor) payload;

                            try {
                                matchController.setPlayerColor(nickname, color);
                                PlayerInitialSetting settings = matchController.getPlayerInitialSettingByNickname(nickname).orElse(null);
                                this.server.matchUpdateGameState(
                                        matchController.getMatchId(),
                                        matchController.getGameState()
                                );
                                this.server.singleUpdateInitialSettings(
                                        this,
                                        color,
                                        settings.getIsInitialFaceUp(),
                                        settings.getQuestCard()
                                );
                            } catch (NullPointerException exception) {
                                this.reportError(ERROR_MESSAGES.PLAYER_IS_NOT_IN_A_MATCH);
                            } catch (WrongGamePhaseException exception) {
                                this.reportError(ERROR_MESSAGES.WRONG_GAME_PHASE);
                            } catch (WrongStepException exception) {
                                this.reportError(ERROR_MESSAGES.WRONG_STEP);
                            } catch (ColorAlreadyPickedException exception) {
                                this.reportError(ERROR_MESSAGES.COLOR_ALREADY_PICKED);
                            } catch (InitalChoiceAlreadySetException exception) {
                                this.reportError(ERROR_MESSAGES.INITIAL_SETTING_ALREADY_SET);
                            }
                        }
                        case SET_INITIAL_CARD_REQUEST -> {
                            Boolean isInitialCardFacingUp = (Boolean) payload;

                            try {
                                matchController.setFaceInitialCard(nickname, isInitialCardFacingUp);
                                PlayerInitialSetting settings = matchController.getPlayerInitialSettingByNickname(nickname).orElse(null);
                                this.server.matchUpdateGameState(
                                        matchController.getMatchId(),
                                        matchController.getGameState()
                                );
                                this.server.singleUpdateInitialSettings(
                                        this,
                                        settings.getColor(),
                                        isInitialCardFacingUp,
                                        settings.getQuestCard()
                                );
                            } catch (NullPointerException exception) {
                                this.reportError(ERROR_MESSAGES.PLAYER_IS_NOT_IN_A_MATCH);
                            } catch (WrongGamePhaseException exception) {
                                this.reportError(ERROR_MESSAGES.WRONG_GAME_PHASE);
                            } catch (WrongStepException exception) {
                                this.reportError(ERROR_MESSAGES.WRONG_STEP);
                            } catch (InitalChoiceAlreadySetException exception) {
                                this.reportError(ERROR_MESSAGES.INITIAL_SETTING_ALREADY_SET);
                            }
                        }
                        case SET_QUEST_CARD_REQUEST -> {
                            QuestCard questCard = (QuestCard) payload;

                            try {
                                matchController.setQuestCard(nickname, questCard);
                                PlayerInitialSetting settings = matchController.getPlayerInitialSettingByNickname(nickname).orElse(null);
                                this.server.matchUpdateGameState(
                                        matchController.getMatchId(),
                                        matchController.getGameState()
                                );
                                this.server.singleUpdateInitialSettings(
                                        this,
                                        settings.getColor(),
                                        settings.getIsInitialFaceUp(),
                                        questCard
                                );
                            } catch (NullPointerException exception) {
                                this.reportError(ERROR_MESSAGES.PLAYER_IS_NOT_IN_A_MATCH);
                            } catch (WrongGamePhaseException exception) {
                                this.reportError(ERROR_MESSAGES.WRONG_GAME_PHASE);
                            } catch (WrongStepException exception) {
                                this.reportError(ERROR_MESSAGES.WRONG_STEP);
                            } catch (InitalChoiceAlreadySetException exception) {
                                this.reportError(ERROR_MESSAGES.INITIAL_SETTING_ALREADY_SET);
                            }
                        }
                        case MATCH_DRAW_REQUEST -> {
                            SocketMessage.DrawCardPayload drawCardPayload = (SocketMessage.DrawCardPayload) payload;
                            String deckType = drawCardPayload.deckType();
                            PlaceInPublicBoard.Slots slot = drawCardPayload.slot();

                            Player player = matchController.getPlayerByNickname(nickname)
                                    .orElse(null);

                            try {
                                MixedCard card = matchController.drawCard(player, deckType, slot);
                                player.addToHand(card);
                                this.server.singleUpdatePlayerHand(this, player.getHand());
                                this.server.matchUpdateGameState(
                                        matchController.getMatchId(),
                                        matchController.getGameState()
                                );
                                this.server.matchUpdatePublicBoard(
                                        matchController.getMatchId(),
                                        matchController.getPublicBoard()
                                );
                            } catch (NullPointerException exception) {
                                this.reportError(ERROR_MESSAGES.UNKNOWN_ERROR);
                            } catch (WrongGamePhaseException exception) {
                                this.reportError(ERROR_MESSAGES.WRONG_GAME_PHASE);
                            } catch (WrongStepException exception) {
                                this.reportError(ERROR_MESSAGES.WRONG_STEP);
                            } catch (WrongPlayerForCurrentTurnException exception) {
                                this.reportError(ERROR_MESSAGES.WRONG_PLAYER_TURN);
                            }
                        }
                        case MATCH_PLACE_REQUEST -> {
                            SocketMessage.PlaceCardPayload placeCardPayload = (SocketMessage.PlaceCardPayload) payload;
                            MixedCard card = placeCardPayload.card();
                            Coordinates coordinates = placeCardPayload.coordinates();
                            Boolean isFacingUp = placeCardPayload.isFacingUp();

                            Player player = matchController.getPlayerByNickname(nickname)
                                    .orElse(null);

                            try {
                                matchController.placeCard(player, card, coordinates, isFacingUp);
                                PlayedCard playedCard = player.getBoard().getCard(coordinates);
                                this.server.singleUpdatePlayerHand(this, player.getHand());
                                this.server.matchUpdateGameState(
                                        matchController.getMatchId(),
                                        matchController.getGameState()
                                );
                                this.server.matchUpdatePlayerBoard(
                                        matchController.getMatchId(),
                                        nickname,
                                        coordinates,
                                        playedCard
                                );
                            } catch (NullPointerException exception) {
                                this.reportError(ERROR_MESSAGES.UNKNOWN_ERROR);
                            } catch (WrongGamePhaseException exception){
                                this.reportError(ERROR_MESSAGES.WRONG_GAME_PHASE);
                            } catch (WrongStepException exception){
                                this.reportError(ERROR_MESSAGES.WRONG_STEP);
                            } catch (WrongPlayerForCurrentTurnException exception){
                                this.reportError(ERROR_MESSAGES.WRONG_PLAYER_TURN);
                            }
                        }
                        default -> {
                            logger.println("SOCKET: [INVALID MESSAGE]");
                            this.reportError(ERROR_MESSAGES.UNKNOWN_COMMAND);
                        }
                    }
                } catch (ClassCastException exception) {
                    this.reportError(ERROR_MESSAGES.UNKNOWN_COMMAND);
                }
            }

            // Shutdown socket
            in.close();
            out.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void addClient() {
        synchronized (this.server.clients) {
            this.server.clients.put(nickname, this);
        }
    }

    @Override
    public void showNicknameUpdate() throws IOException {
        logger.println("SOCKET: Sending nickname update");
        synchronized (this.view) {
            this.view.showNicknameUpdate();
        }
    }

    @Override
    public void showUpdateLobbyPlayers(List<String> players) throws IOException {
        logger.println("SOCKET: Sending lobby player updates: " + players);
        synchronized (this.view) {
            this.view.showUpdateLobbyPlayers(players);
        }
    }

    @Override
    public void showUpdateMatchesList(List<Integer> matches) throws IOException {
        logger.println("SOCKET: Sending matches list update: " + matches.toString());
        synchronized (this.view) {
            this.view.showUpdateMatchesList(matches);
        }
    }

    @Override
    public void showUpdateMatchJoin() {
        logger.println("SOCKET: Sending match join update");
        synchronized (this.view) {
            try {
                this.view.showUpdateMatchJoin();
            } catch (IOException ignore) { }
        }
    }

    @Override
    public void showUpdateMatchCreate(Integer matchId) throws IOException {
        logger.println("SOCKET: Sending match create update: " + matchId);
        synchronized (this.view) {
            this.view.showUpdateMatchCreate(matchId);
        }
    }

    @Override
    public void showUpdateChat(Message message) {
        synchronized (this.view) {
            try {
                this.view.showUpdateChat(message);
            } catch (IOException ignore) { }
        }
    }

    @Override
    public void showUpdateInitialSettings(PlayerColor color, Boolean isFacingUp, QuestCard questCard) {
        synchronized (this.view) {
            try {
                this.view.showUpdateInitialSettings(color, isFacingUp, questCard);
            } catch (IOException ignore) { }
        }
    }

    @Override
    public void showUpdateGameState(GameState gameState) {
        synchronized (this.view) {
            try {
                this.view.showUpdateGameState(gameState);
            } catch (IOException ignore) { }
        }
    }

    @Override
    public void showUpdatePlayerHand(PlayerHand<MixedCard> playerHand) {
        synchronized (this.view) {
            try {
                this.view.showUpdatePlayerHand(playerHand);
            } catch (IOException ignore) { }
        }
    }

    @Override
    public void showUpdatePublicBoard(PublicBoard publicBoard) {
        synchronized (this.view) {
            try {
                this.view.showUpdatePublicBoard(publicBoard);
            } catch (IOException ignore) { }
        }
    }

    @Override
    public void showUpdateBoard(String nickname, Coordinates coordinates, PlayedCard playedCard) {
        synchronized (this.view) {
            try {
                this.view.showUpdateBoard(nickname, coordinates, playedCard);
            } catch (IOException ignore) { }
        }
    }

    @Override
    public void reportError(ERROR_MESSAGES errorMessage) {
        synchronized (this.view) {
            try {
                this.view.reportError(errorMessage);
            } catch (IOException ignore) { }
        }
    }

    @Override
    public void setMatchControllerServer(VirtualMatchServer matchServer) { }
}
