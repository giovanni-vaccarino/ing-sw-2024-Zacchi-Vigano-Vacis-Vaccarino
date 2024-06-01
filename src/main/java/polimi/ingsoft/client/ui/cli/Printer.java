package polimi.ingsoft.client.ui.cli;

import polimi.ingsoft.server.model.*;

import java.io.PrintStream;
import java.util.ArrayList;

public class Printer {
    private final PrintStream out;

    public Printer(PrintStream out) {
        this.out = out;
    }
    public void printFromMain(String argument){
        //System.out.print(MESSAGES.CLS.getValue()); prova per verificare che il cls funzioni
        if (argument.toLowerCase().equals(Arguments.Argument.HELP.getValue())) {
            out.println(MESSAGES.HELPMAIN.getValue());
        }else out.println(MESSAGES.ERROR.getValue());
    }
    public void printFromBoard(Board board, PlayerHand hand, String argument, QuestCard playerQuest){
        out.print(MESSAGES.CLS.getValue());
        if(argument==null)ClientBoard.printBoard(board);
        else if(argument.equals(BoardArgument.UP.getValue()))ClientBoard.printBoard(board,BoardArgument.UP);
        else if(argument.equals(BoardArgument.DOWN.getValue()))ClientBoard.printBoard(board,BoardArgument.DOWN);
        else if(argument.equals(BoardArgument.LEFT.getValue()))ClientBoard.printBoard(board,BoardArgument.LEFT);
        else if(argument.equals(BoardArgument.RIGHT.getValue()))ClientBoard.printBoard(board,BoardArgument.RIGHT);
        else if(argument.equals(BoardArgument.UPRIGHT.getValue()))ClientBoard.printBoard(board,BoardArgument.UPRIGHT);
        else if(argument.equals(BoardArgument.UPLEFT.getValue()))ClientBoard.printBoard(board,BoardArgument.UPLEFT);
        else if(argument.equals(BoardArgument.DOWNLEFT.getValue()))ClientBoard.printBoard(board,BoardArgument.DOWNLEFT);
        else if(argument.equals(BoardArgument.DOWNRIGHT.getValue()))ClientBoard.printBoard(board,BoardArgument.DOWNRIGHT);
        if(hand!=null)ClientHand.print(hand,playerQuest);
        if(argument!=null&&argument.toLowerCase().equals(Arguments.Argument.HELP.getValue()))out.println(MESSAGES.HELPBOARD.getValue());
        else out.println(MESSAGES.ERROR.getValue() );
    }
    public void printFromPublicBoard(PublicBoard publicBoard, PlayerHand hand, String argument, QuestCard playerQuest){
        out.print(MESSAGES.CLS.getValue());
        ClientPublicBoard.printPublicBoard(publicBoard.getPublicBoardResource(),publicBoard.getPublicBoardGold(),publicBoard.getPublicBoardQuest());
        ClientHand.print(hand,playerQuest);
        if(argument.toLowerCase().equals(Arguments.Argument.HELP.getValue()))out.println(MESSAGES.HELPCLIENTBOARD.getValue());
        else if(argument.toLowerCase().equals(PublicBoardArguments.GETCARD.getValue()))out.println(MESSAGES.HELPGETCARDTYPE.getValue());
        else if(argument.toLowerCase().equals(PublicBoardArguments.GOLD.getValue())||
                argument.toLowerCase().equals(PublicBoardArguments.RESOURCE.getValue()))
            out.println(MESSAGES.HELPGETCARDPLACE.getValue());
        else out.println(MESSAGES.ERROR.getValue() );
    }
    public void printInitialCardChoice(InitialCard initialCard){
        out.print(MESSAGES.HELP_INITIAL_CARD_CHOICE.getValue());
        ClientHand.initialPrint(initialCard);
    }
    public void printQuestCardChoice(QuestCard quest1, QuestCard quest2){
        out.print(MESSAGES.HELP_QUEST_CARD_CHOICE.getValue());
        ArrayList<QuestCard> quests=new ArrayList<>();
        quests.add(quest1);
        quests.add(quest2);
        ClientPublicBoard.printInitialQuests(quests);
    }
}
