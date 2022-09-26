package client.utility;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;
import java.io.*;

import common.data.Coordinates;
import common.data.Person;
import common.data.Position;
import common.data.Status;
import common.exceptions.CommandUsageException;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.ScriptRecursionException;
import common.interaction.Request;
import common.interaction.ResponseCode;
import common.interaction.User;
import common.interaction.WorkerObject;
import common.utility.Console;


public class UserHandler {
private final int maxRewriteAttempts = 1;

private Scanner userScanner;
private Stack<File> scriptStack = new Stack<>();
private Stack<Scanner> scannerStack = new Stack<>();

public UserHandler(Scanner userScanner) {
    this.userScanner = userScanner;
}

public Request handle(ResponseCode serverResponseCode, User user) {
    String userInput;
    String[] userCommand;
    ProcessingCode processingCode;
    int rewriteAttempts = 0;
    try {
        do {
            try {
                if (fileMode() && (serverResponseCode == ResponseCode.ERROR))
                    throw new IncorrectInputInScriptException();
                while (fileMode() && !userScanner.hasNextLine()) {
                    userScanner.close();
                    userScanner = scannerStack.pop();
                    Console.println("Возвращаюсь к скрипту '" + scriptStack.pop().getName() + "'...");
                }
                if (fileMode()) {
                    userInput = userScanner.nextLine();
                    if (!userInput.isEmpty()) {
                        Console.println(userInput);
                    }
                } else {
                    userInput = userScanner.nextLine();
                }
                userCommand = (userInput.trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
            } catch (NoSuchElementException | IllegalStateException exception) {
                Console.println();
                Console.printError("Произошла ошибка при вводе команды!");
                userCommand = new String[]{"", ""};
                rewriteAttempts++;
                if (rewriteAttempts >= maxRewriteAttempts) {
                    Console.printError("Превышено количество попыток ввода!");
                    System.exit(0);
                }
            }
            processingCode = processCommand(userCommand[0], userCommand[1]);
        } while (processingCode == ProcessingCode.ERROR && !fileMode() || userCommand[0].isEmpty());
        try {
            if (fileMode() && (serverResponseCode == ResponseCode.ERROR || processingCode == ProcessingCode.ERROR))
                throw new IncorrectInputInScriptException();
            switch (processingCode) {
                case OBJECT:
                    WorkerObject worker = generateWorkerAdd();
                    return new Request(userCommand[0], userCommand[1], worker, user);
                case UPDATE_OBJECT:
                    WorkerObject wObject = generateWorkerUpdate();
                    return new Request(userCommand[0], userCommand[1], wObject, user);
                case SCRIPT:
                    File scriptFile = new File(userCommand[1]);
                    if (!scriptFile.exists()) throw new FileNotFoundException();
                    if (!scriptStack.isEmpty() && scriptStack.search(scriptFile) != -1)
                        throw new ScriptRecursionException();
                    scannerStack.push(userScanner);
                    scriptStack.push(scriptFile);
                    userScanner = new Scanner(scriptFile);
                    Console.println("Выполняю скрипт '" + scriptFile.getName() + "'...");
                    break;
            }
        } catch (FileNotFoundException exception) {
            Console.printError("Файл со скриптом не найден!");
        } catch (ScriptRecursionException exception) {
            Console.printError("Скрипты не могут вызываться рекурсивно!");
            throw new IncorrectInputInScriptException();
        }
    } catch (IncorrectInputInScriptException exception) {
        Console.printError("Выполнение скрипта прервано!");
        while (!scannerStack.isEmpty()) {
            userScanner.close();
            userScanner = scannerStack.pop();
        }
        scriptStack.clear();
        return new Request(user);
    }
    
    return new Request(userCommand[0], userCommand[1], null, user);
}

private ProcessingCode processCommand(String command, String commandArgument){
    try {
        switch (command) {
            case "":
                return ProcessingCode.ERROR;
            case "help":
                if (!commandArgument.isEmpty()) throw new CommandUsageException();
                break;
            case "info":
                if (!commandArgument.isEmpty()) throw new CommandUsageException();
                break;
            case "show":
                if (!commandArgument.isEmpty()) throw new CommandUsageException();
                break;
            case "add":
                if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                return ProcessingCode.OBJECT;
            case "update":
                if (commandArgument.isEmpty()) throw new CommandUsageException("<ID> {element}");
                return ProcessingCode.UPDATE_OBJECT;
            case "remove_by_id":
                if (commandArgument.isEmpty()) throw new CommandUsageException("<ID>");
                break;
            case "clear":
                if (!commandArgument.isEmpty()) throw new CommandUsageException();
                break;
            case "save":
                if (!commandArgument.isEmpty()) throw new CommandUsageException();
                break;
            case "execute_script":
                if (commandArgument.isEmpty()) throw new CommandUsageException("<file_name>");
                return ProcessingCode.SCRIPT;
            case "exit":
                if (!commandArgument.isEmpty()) throw new CommandUsageException();
                break;
            case "add_if_min":
                if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                return ProcessingCode.OBJECT;
            case "remove_last":
                if (!commandArgument.isEmpty()) throw new CommandUsageException();
                break;
            case "history":
                if (!commandArgument.isEmpty()) throw new CommandUsageException();
                break;
            case "print_field_ascending_salary":
                if (!commandArgument.isEmpty()) throw new CommandUsageException();
                break;
            case "max_by_id":
                if (!commandArgument.isEmpty()) throw new CommandUsageException();
                break;
            case "print_ascending":
                if (!commandArgument.isEmpty()) throw new CommandUsageException();
                break;
            case "server_exit":
                if (!commandArgument.isEmpty()) throw new CommandUsageException();
                break;
            default:
                Console.println("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
                return ProcessingCode.ERROR;
        }
    } catch (CommandUsageException exception) {
        if (exception.getMessage() != null) command += " " + exception.getMessage();
        Console.println("Использование: '" + command + "'");
        return ProcessingCode.ERROR;
    }
    return ProcessingCode.OK;
}

    private WorkerObject generateWorkerAdd() throws IncorrectInputInScriptException {
        Asker workerAsker = new Asker(userScanner);
        if(fileMode()) workerAsker.setUserMode();
        return new WorkerObject(
            workerAsker.askName(),
            workerAsker.askCoordinates(),
            workerAsker.askSalary(),
            workerAsker.askPosition(),
            workerAsker.askStatus(),
            workerAsker.askPerson()
        );
    }

    private WorkerObject generateWorkerUpdate() throws IncorrectInputInScriptException{
        Asker workerAsker = new Asker(userScanner);
        if (fileMode()) workerAsker.setUserMode();
        String name = workerAsker.askQuestion("Хотите изменить имя рабочего?") ?
                workerAsker.askName() : null;
        Coordinates coordinates = workerAsker.askQuestion("Хотите изменить координаты рабочего?") ?
                workerAsker.askCoordinates() : null;
        int salary = workerAsker.askQuestion("Хотите изменить зарплату?") ?
                workerAsker.askSalary() : -1;
        Position position = workerAsker.askQuestion("Хотите изменить позицию рабочего?") ?
                workerAsker.askPosition() : null;
        Status status = workerAsker.askQuestion("Хотите изменить статус рабочего?") ?
                workerAsker.askStatus() : null;

        Person person = workerAsker.askQuestion("Хотите изменить личные данные?") ?
            workerAsker.askPerson() : null;
        return new WorkerObject(name, coordinates, salary, position, status, person);

    }

private boolean fileMode() {
    return !scannerStack.isEmpty();
}
}

