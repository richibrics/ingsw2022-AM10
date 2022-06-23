package it.polimi.ingsw.network;

public class NetworkConstants {
    public static int INITIAL_STILL_ALIVE_TIMER_VALUE = 10;

    public static String HANDSHAKE_STRING = "hi";
    public static int PORT = 12987;
    public static int SLEEP_TIME_IN_MILLISECONDS = 100;
    public static int SLEEP_TIME_RECEIVE_MESSAGE_IN_MILLISECONDS = 30;
    public static int SOCKET_SO_TIMEOUT_IN_MILLISECONDS = 500;
    public static int TIME_BETWEEN_HANDSHAKE_MESSAGES_IN_MILLISECONDS = 1000;
    public static String MESSAGE_FOR_WINNERS = "Congratulations, you are the winner!\n";
    public static String MESSAGE_FOR_LOSERS = "You are a loser!\n";

    public static final int TIME_TO_SLEEP_BETWEEN_LOOP_CHECK_IN_MILLISECONDS = 10;
}
