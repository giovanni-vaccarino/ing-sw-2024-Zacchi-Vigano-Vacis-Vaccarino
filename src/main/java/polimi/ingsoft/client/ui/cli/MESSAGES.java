package polimi.ingsoft.client.ui.cli;

public enum MESSAGES {
    CHOOSE_PROTOCOL_LIST("Before starting, choose which communication protocol you want to use: "),
    CHOOSE_PROTOCOL("Choose your protocol: "),
    WELCOME("Welcome to CODEX, choose your nickname: \n"),
    NICKNAME_UPDATED("Your nickname was successfully set"),
    UNABLE_TO_UPDATE_NICKNAME("This nickname has already been used"),
    CHOOSE_MATCH("Choose your match: "),
    CHOOSE_NICKNAME("Choose your nickname: "),
    CHOOSE_NUMPLAYERS("Choose how many players should the game have: "),
    JOINING_MATCH("Joining match..."),
    JOINED_MATCH("Succesfully joined match"),
    CREATED_MATCH("Successfully create match"),


    /*QUESTI LI HO AGGIUNTI IO ANDRE-SIMO*/
    CHOOSE_ACTION("COMMAND>"),

    HELPMAIN("HERE'S WHAT YOU CAN DO !\nBOARD:ENDER THE BOARD MENU\nPUBLICBOARD: ENTER THE PUBLICBOARD MENU\nCHAT: ACCESS CHAT MENU!\nMESSAGE: CHAT PRIVATELY WITH A PLAYER\nHELP: PRINT THIS LIST AGAIN!"),
    HELPBOARD(""),
    HELPCHAT("");

    private final String value;

    MESSAGES(String value) {
            this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
