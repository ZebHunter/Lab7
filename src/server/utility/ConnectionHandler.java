package server.utility;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ForkJoinPool;

import common.interaction.Request;
import common.interaction.Response;
import common.interaction.ResponseCode;
import common.utility.Console;
import server.Server;

public class ConnectionHandler implements Runnable{
    private Server server;
    private Socket clientSocket;
    private CommandManager commandManager;
    private ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    public ConnectionHandler(Server server, Socket clientSocket, CommandManager commandManager) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.commandManager = commandManager;
    }

    
    @Override
    public void run() {
        Request userRequest = null;
        Response responseToUser = null;
        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream())) {
            do {
                userRequest = (Request) clientReader.readObject();
                responseToUser = forkJoinPool.invoke(new HandleRequestTask(userRequest, commandManager));
                Response finalResponseToUser = responseToUser;
                Request finalUserRequest = userRequest;
                Runnable task = () -> {
                    try {
                        clientWriter.writeObject(finalResponseToUser);
                        clientWriter.flush();
                    } catch (IOException exception) {
                        if (!finalUserRequest.getCommandName().equals("exit"))
                        Console.printError("Произошла ошибка при отправке данных на клиент!");
                    }
                };
                new Thread(task).start();


            } while (responseToUser.getResponseCode() != ResponseCode.CLIENT_EXIT);

        } catch (ClassNotFoundException exception) {
            Console.printError("Произошла ошибка при чтении полученных данных!");
        } catch (CancellationException exception) {
            Console.println("При обработке запроса произошла ошибка многопоточности!");
        } catch (IOException exception) {
            if (!userRequest.getCommandName().equals("exit"))
                Console.printError("Непредвиденный разрыв соединения с клиентом!");
        } finally {
            try {
                clientSocket.close();
                Console.println("Клиент отключен от сервера.");
            } catch (IOException exception) {
                Console.printError("Произошла ошибка при попытке завершить соединение с клиентом!");
            }
            server.releaseConnection();
        }
    }
}
