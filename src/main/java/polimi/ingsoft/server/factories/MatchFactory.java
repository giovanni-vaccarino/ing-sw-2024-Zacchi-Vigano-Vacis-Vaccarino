package polimi.ingsoft.server.factories;

import polimi.ingsoft.server.controller.ChatController;
import polimi.ingsoft.server.controller.MatchController;
import polimi.ingsoft.server.model.publicboard.PublicBoard;

import java.io.PrintStream;

public class MatchFactory {
    public static MatchController createMatch(PrintStream out, int matchId, int requestedNumPlayers) {
        PublicBoard publicBoard = PublicBoardFactory.createPublicBoard();
        ChatController chatController = ChatControllerFactory.createChatController();
        return new MatchController(out, matchId, requestedNumPlayers, publicBoard, chatController);
    }
}

