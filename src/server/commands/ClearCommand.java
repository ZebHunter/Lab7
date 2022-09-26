package server.commands;

import common.data.Worker;
import common.exceptions.DatabaseHandlingException;
import common.exceptions.ManualDatabaseEditException;
import common.exceptions.PermissionDeniedException;
import common.exceptions.WrongAmouthOfElementsException;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutputer;

public class ClearCommand extends AbstractCommand{
    private final CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;
    public ClearCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("clear","", "очистить коллекцию");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmouthOfElementsException();
            for (Worker worker : collectionManager.getCollection()) {
                if (!worker.getOwner().equals(user)) throw new PermissionDeniedException();
                if (!databaseCollectionManager.checkWorkerUserId(worker.getID(), user)) throw new ManualDatabaseEditException();
            }
            databaseCollectionManager.clearCollection();
            collectionManager.clearStack();
            ResponseOutputer.appendln("Коллекция очищена!");
            return true;
        } catch (WrongAmouthOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appenderror("Произошла ошибка при обращении к базе данных!");
        } catch (PermissionDeniedException exception) {
            ResponseOutputer.appenderror("Недостаточно прав для выполнения данной команды!");
            ResponseOutputer.appendln("Принадлежащие другим пользователям объекты доступны только для чтения.");
        } catch (ManualDatabaseEditException exception) {
            ResponseOutputer.appenderror("Произошло прямое изменение базы данных!");
            ResponseOutputer.appendln("Перезапустите клиент для избежания возможных ошибок.");
        }
        return false;
    }
}
