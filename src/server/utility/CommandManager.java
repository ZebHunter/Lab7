package server.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import common.exceptions.HistoryIsEmptyException;
import common.interaction.User;
import common.utility.Console;
import server.commands.CommandAble;

public class CommandManager {

    private ReadWriteLock historyLocker = new ReentrantReadWriteLock();
    private ReadWriteLock collectionLocker = new ReentrantReadWriteLock();

    private final int SizeOfHistory = 5;
    private final String[] commandHistory = new String[SizeOfHistory];
    private final List<CommandAble> commands;
    private final CommandAble addCommand;
    private final CommandAble addIfMinCommand;
    private final CommandAble clearCommand;
    private final CommandAble exercuteScriptCommand;
    private final CommandAble exitCommand;
    private final CommandAble helpCommand;
    private final CommandAble historyCommand;
    private final CommandAble infoCommand;
    private final CommandAble loginCommand;
    private final CommandAble maxByIDCommand;
    private final CommandAble printAscending;
    private final CommandAble printFieldAscendingSalary;
    private final CommandAble registerCommand;
    private final CommandAble removeByIDCommand;
    private final CommandAble removeLastCommand;
    private final CommandAble showCommand;
    private final CommandAble updateIDCommand;

    public CommandManager(CommandAble addCommand, CommandAble addIfMinCommand,CommandAble clearCommand, CommandAble exercuteScriptCommand, CommandAble exitCommand, CommandAble helpCommand, CommandAble historyCommand, CommandAble infoCommand, CommandAble loginCommand, CommandAble maxByIDCommand, CommandAble printAscending, CommandAble printFieldAscendingSalary, CommandAble registerCommand, CommandAble removeByIDCommand, CommandAble removeLastCommand, CommandAble showCommand, CommandAble updateIDCommand){
        this.addCommand = addCommand;
        this.addIfMinCommand = addIfMinCommand;
        this.clearCommand = clearCommand;
        this.exercuteScriptCommand = exercuteScriptCommand;
        this.exitCommand = exitCommand;
        this.helpCommand = helpCommand;
        this.historyCommand = historyCommand;
        this.infoCommand = infoCommand;
        this.loginCommand = loginCommand;
        this.maxByIDCommand = maxByIDCommand;
        this.printAscending = printAscending;
        this.printFieldAscendingSalary = printFieldAscendingSalary;
        this.registerCommand = registerCommand;
        this.removeByIDCommand = removeByIDCommand;
        this.removeLastCommand = removeLastCommand;
        this.showCommand = showCommand;
        this.updateIDCommand = updateIDCommand;

        commands = new ArrayList<>(Arrays.asList(addCommand, addIfMinCommand, clearCommand, exercuteScriptCommand, exitCommand, helpCommand, historyCommand, infoCommand, loginCommand, maxByIDCommand, printAscending, printFieldAscendingSalary, registerCommand, removeByIDCommand, removeLastCommand, showCommand, updateIDCommand));
    }

    public void addToHistory(String commandToStore, User user) {
        historyLocker.writeLock().lock();
        try {
            for (CommandAble command : commands) {
                if (command.getName().equals(commandToStore)) {
                    for (int i = SizeOfHistory - 1; i > 0; i--) {
                        commandHistory[i] = commandHistory[i - 1];
                    }
                    commandHistory[0] = commandToStore + " (" + user.getUsername() + ')';
                }
            }
        } finally {
            historyLocker.writeLock().unlock();
        }
    }

    public boolean noSuchCommand(String command){
        Console.println("Команда '" + command + "не найдена. Попробуйте написать 'help'." );
        return false;
    }

    public boolean help(String stringArgument, Object objectArgument, User user){
        if (helpCommand.execute(stringArgument, objectArgument, user)) {
            for (CommandAble command : commands) {
                ResponseOutputer.appendtable(command.getName() + " " + command.getUsage(), command.getDescription());
            }
            return true;
        } else return false;
    }

    public boolean info(String stringArgument, Object objectArgument, User user) {
        collectionLocker.readLock().lock();
        try {
            return infoCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }

    public boolean show(String stringArgument, Object objectArgument, User user) {
        collectionLocker.readLock().lock();
        try {
            return showCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }

    public boolean add_if_min(String argument, Object object, User user) {
        collectionLocker.writeLock().lock();
        try {
            return addIfMinCommand.execute(argument, object, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    public boolean add(String stringArgument, Object objectArgument, User user){
        collectionLocker.writeLock().lock();
        try {
            return addCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    public boolean max_by_id(String argument, Object object, User user){
        collectionLocker.readLock().lock();
        try {
            return maxByIDCommand.execute(argument, object, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }

    public boolean update(String stringArgument, Object objectArgument, User user){
        collectionLocker.writeLock().lock();
        try {
            return updateIDCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    public boolean remove_last(String argument, Object object, User user) {
        collectionLocker.writeLock().lock();
        try{
        return removeLastCommand.execute(argument, object, user);
        } finally{
            collectionLocker.writeLock().unlock();
        }
    }

    public boolean removeById(String stringArgument, Object objectArgument, User user)  {
        collectionLocker.writeLock().lock();
        try {
            return removeByIDCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    public boolean clear(String stringArgument, Object objectArgument, User user)  {
        collectionLocker.writeLock().lock();
        try {
            return clearCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }


    public boolean exit(String stringArgument, Object objectArgument, User user)  {
        return exitCommand.execute(stringArgument, objectArgument, user);
    }

    public boolean executeScript(String stringArgument, Object objectArgument, User user)  {
        return exercuteScriptCommand.execute(stringArgument, objectArgument, user);
    }

    public boolean printAscending(String stringArgument, Object objectArgument, User user)  {
        collectionLocker.readLock().lock();
        try {
            return printAscending.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.readLock().lock();
        }
    }

    public boolean printFieldAscendingSalary(String argument, Object object, User user) {
        collectionLocker.readLock().lock();
        try {
            return printFieldAscendingSalary.execute(argument, object, user);
        } finally {
            collectionLocker.readLock().lock();
        }
    }
    


    public boolean history(String stringArgument, Object objectArgument, User user)  {
        if (historyCommand.execute(stringArgument, objectArgument, user)) {
            historyLocker.readLock().lock();
            try {
                if (commandHistory.length == 0) throw new HistoryIsEmptyException();
                ResponseOutputer.appendln("Последние использованные команды:");
                for (String command : commandHistory) {
                    if (command != null) ResponseOutputer.appendln(" " + command);
                }
                return true;
            } catch (HistoryIsEmptyException exception) {
                ResponseOutputer.appendln("Ни одной команды еще не было использовано!");
            } finally {
                historyLocker.readLock().unlock();
            }
        }
        return false;
    }

    public boolean login(String stringArgument, Object objectArgument, User user)  {
        return loginCommand.execute(stringArgument, objectArgument, user);
    }

    public boolean register(String stringArgument, Object objectArgument, User user)  {
        return registerCommand.execute(stringArgument, objectArgument, user);
    }

    @Override
    public String toString() {
        return "CommandManager (helper class for working with commands)";
    }
}
