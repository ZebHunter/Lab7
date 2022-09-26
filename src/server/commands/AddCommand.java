package server.commands;

import common.exceptions.DatabaseHandlingException;
import common.exceptions.WrongAmouthOfElementsException;
import common.interaction.User;
import common.interaction.WorkerObject;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutputer;

public class AddCommand extends AbstractCommand{
    private final CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;
    public AddCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("add", "{element}", "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument == null) throw new WrongAmouthOfElementsException();
            WorkerObject workerObject = (WorkerObject) objectArgument;
            collectionManager.addtoStack(databaseCollectionManager.insertWorker(workerObject, user));
            ResponseOutputer.appendln("Рабочий успешно добавлена!");
            return true;
        } catch (WrongAmouthOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException e) {
            ResponseOutputer.appenderror("Произошла ошибка при обращении к базе данных!");
        }
        return false;
    }
}
