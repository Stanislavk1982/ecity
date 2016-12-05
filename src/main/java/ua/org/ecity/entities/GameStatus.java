package ua.org.ecity.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GameStatus {

    EXISTS(0, "Game exists"),
    DOESNT_EXIST(1, "Game doesn't exist"),
    //    GAMEFINISHED(2, "Game is finished"),
    NOCITY(10, "There isn't city in the base"),
    CITYUSE(11, "The City was use in game"),
    WRONGCITYLETTER(12, "The City start on wrong letter"),
    WINNERPLAYER1(20, " Winner Player 1"),
    WINNERPLAYER2(21, " Winner Player 2"),
    USERREGISTEROK (31,"User registration OK "),
    USEREXIST(32,"The player exist "),
    USERPASSWORDINCORECT(33,"User doesn't enter password"),
    USERDOESNTENTERLOGIN(34,"User doesn't enter login");



    GameStatus(int code, String message) {
        this.code = code;
        this.message = message;

    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
