package server.commands;

import java.time.LocalDateTime;

import common.exceptions.WrongAmouthOfElementsException;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

public class InfoCommand extends AbstractCommand{
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info", "", "вывести информацию о коллекции");
        this.collectionManager = collectionManager;
    }

    
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmouthOfElementsException();
            LocalDateTime lastInitTime = collectionManager.getLastInitTime();
            String lastInitTimeString = (lastInitTime == null) ? "в данной сессии инициализации еще не происходило" :
                    lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString();

            ResponseOutputer.appendln("Сведения о коллекции:");
            ResponseOutputer.appendln(" Тип: " + collectionManager.collectionType());
            ResponseOutputer.appendln(" Количество элементов: " + collectionManager.collectionSize());
            ResponseOutputer.appendln(" Дата последней инициализации: " + lastInitTimeString);
            return true;
        } catch (WrongAmouthOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        }
        return false;
    }
}
