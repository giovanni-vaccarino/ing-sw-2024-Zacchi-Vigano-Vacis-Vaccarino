package polimi.ingsoft.server.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import polimi.ingsoft.server.exceptions.MatchExceptions.ColorAlreadyPickedException;
import polimi.ingsoft.server.factories.PublicBoardFactory;
import polimi.ingsoft.server.model.player.PlayerColor;
import polimi.ingsoft.server.model.publicboard.PublicBoard;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {
    private MatchController matchController;
    private GameState gameState;

    @BeforeEach
    void setUp() {
        PublicBoard publicBoard = PublicBoardFactory.createPublicBoard();
        matchController = new MatchController(System.out, 1, 3, publicBoard, new ChatController());
        gameState = matchController.getGameState();
    }
    @Test
    void getCurrentPlayer() {
        gameState.getCurrentPlayer();
    }

    @Test
    void getFirstPlayerIndex() {
        gameState.getFirstPlayerIndex();
    }

    @Test
    void getGamePhase() {
        gameState.getGamePhase();
    }

    @Test
    void getCurrentInitialStep() {
        gameState.getCurrentInitialStep();
    }

    @Test
    void getCurrentTurnStep() {
        gameState.getCurrentTurnStep();
    }

    @Test
    void getCurrentPlayerNickname() {
        gameState.getCurrentPlayerNickname();
    }

    @Test
    void updateState() {
        gameState.updateState();
    }

    @Test
    void updateInitialStep() {
    }

    @Test
    void updateTurnStep() {
    }

    @Test
    void validateMove() {
    }

    @Test
    void validateInitialChoice() {
    }

    @Test
    void checkColorAvailability() {
    }

    @Test
    void goToNextPlayer() {
        //gameState.goToNextPlayer();
    }

    @Test
    void getWinners() {
        gameState.getWinners();
    }

    @Test
    void testClone() {
        gameState.clone();
    }
}