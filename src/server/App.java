package server;

import server.commands.AddCommand;
import server.commands.AddIfMinCommand;
import server.commands.ClearCommand;
import server.commands.ExercuteScriptCommand;
import server.commands.ExitCommand;
import server.commands.HelpCommand;
import server.commands.HistoryCommand;
import server.commands.InfoCommand;
import server.commands.LoginCommand;
import server.commands.MaxByIDCommand;
import server.commands.PrintAscending;
import server.commands.PrintFieldAscendingSalaryCommand;
import server.commands.RegisterCommand;
import server.commands.RemoveByIDCommand;
import server.commands.RemoveLastCommand;
import server.commands.ShowCommand;
import server.commands.UpdateIDCommand;
import server.utility.CollectionManager;
import server.utility.CommandManager;
import server.utility.DatabaseCollectionManager;
import server.utility.DatabaseHandler;
import server.utility.DatabaseUserManager;
import server.utility.ShutdownHandling;

public class App {
    private static final int MAX_CLIENTS = 1000;
    private static int PORT = 1335;
    private static String databaseUsername = "s335134";
    private static String databaseAddress = "jdbc:postgresql://pg:5432/studs";
    private static String databasePassword = "caau#2135";

    public static void main(String[] args) {
        DatabaseHandler databaseHandler = new DatabaseHandler(databaseAddress, databaseUsername, databasePassword);
        DatabaseUserManager databaseUserManager = new DatabaseUserManager(databaseHandler);
        DatabaseCollectionManager databaseCollectionManager = new DatabaseCollectionManager(databaseHandler, databaseUserManager);
        CollectionManager collectionManager = new CollectionManager(databaseCollectionManager);
        CommandManager commandManager = new CommandManager(
                new AddCommand(collectionManager, databaseCollectionManager),
                new AddIfMinCommand(collectionManager, databaseCollectionManager),
                new ClearCommand(collectionManager, databaseCollectionManager),
                new ExercuteScriptCommand(),
                new ExitCommand(),
                new HelpCommand(),
                new HistoryCommand(),
                new InfoCommand(collectionManager),
                new LoginCommand(databaseUserManager),
                new MaxByIDCommand(collectionManager),
                new PrintAscending(collectionManager),
                new PrintFieldAscendingSalaryCommand(collectionManager),
                new RegisterCommand(databaseUserManager),
                new RemoveByIDCommand(collectionManager, databaseCollectionManager),
                new RemoveLastCommand(collectionManager, databaseCollectionManager),
                new ShowCommand(collectionManager),
                new UpdateIDCommand(collectionManager, databaseCollectionManager)
        );

        ShutdownHandling.addCollectionSavingHook();
        Server server = new Server(PORT, MAX_CLIENTS, commandManager);
        server.run();
        databaseHandler.closeConnection();
    }
}