package client;

import client.utility.*;
import common.exceptions.ConnectionErrorException;
import common.exceptions.MustBeNotEmptyException;
import common.exceptions.NotInDeclaredLimitsException;
import common.exceptions.WrongAmouthOfElementsException;
import common.utility.Console;

import java.util.*;

public class ClientApp {
    private static final int RECONNECTION_TIMEOUT = 5 * 1000;
    private static final int MAX_RECONNECTION_ATTEMPTS = 5;

    private static String host;
    private static int port;

    private static boolean initializeConnectionAddress(String[] hostAndPortArgs) {
        try {
            if (hostAndPortArgs.length != 2) throw new WrongAmouthOfElementsException ();
            host = hostAndPortArgs[0];
            port = Integer.parseInt(hostAndPortArgs[1]);
            if (port < 0) throw new NotInDeclaredLimitsException();
            return true;
        } catch (WrongAmouthOfElementsException exception) {
            String jarName = new java.io.File(ClientApp.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            Console.println("Использование: 'java -jar " + jarName + " <host> <port>'");
        } catch (NumberFormatException exception) {
            Console.printError("Порт должен быть представлен числом!");
        } catch (NotInDeclaredLimitsException exception) {
            Console.printError("Порт не может быть отрицательным!");
        }
        return false;
    }

    public static void main(String[] args) throws ConnectionErrorException, NotInDeclaredLimitsException, MustBeNotEmptyException, NullPointerException {
        if (!initializeConnectionAddress(args)) return;
        Scanner userScanner = new Scanner(System.in);
        AuthHandler authHandler = new AuthHandler(userScanner);
        UserHandler userHandler = new UserHandler(userScanner);
        Client client = new Client(host, port, RECONNECTION_TIMEOUT, MAX_RECONNECTION_ATTEMPTS, userHandler, authHandler);
        client.run();
        userScanner.close();
    }
}