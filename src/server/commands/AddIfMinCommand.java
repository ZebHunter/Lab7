package server.commands;

import common.data.Worker;
import common.exceptions.DatabaseHandlingException;
import common.exceptions.WrongAmouthOfElementsException;
import common.interaction.User;
import common.interaction.WorkerObject;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutputer;

public class AddIfMinCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public AddIfMinCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("add_if_min", "{element}", "добавить новый элемент, если его значение меньше, чем у наименьшего");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument == null) throw new WrongAmouthOfElementsException();
            WorkerObject wObject = (WorkerObject) objectArgument;
            Worker worker = databaseCollectionManager.insertWorker(wObject, user);
            if (collectionManager.collectionSize() == 0 || worker.compareTo(collectionManager.getFirst()) < 0) {
                collectionManager.addtoStack(worker);
                ResponseOutputer.appendln("Солдат успешно добавлен!");
                return true;
            } else ResponseOutputer.appenderror("Значение солдата больше, чем значение наименьшего из солдат!");
        } catch (WrongAmouthOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appenderror("Произошла ошибка при обращении к базе данных!");
        }
        return false;
    }
}
