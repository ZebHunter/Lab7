package server.commands;

import common.data.Worker;
import common.exceptions.CollectionIsEmptyException;
import common.exceptions.DatabaseHandlingException;
import common.exceptions.ManualDatabaseEditException;
import common.exceptions.PermissionDeniedException;
import common.exceptions.WorkerNotFoundException;
import common.exceptions.WrongAmouthOfElementsException;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutputer;

public class RemoveByIDCommand extends AbstractCommand{
    private final CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public RemoveByIDCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("remove_by_id", "<ID>", "удалить элемент из коллекции по ID");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (stringArgument.isEmpty() || objectArgument != null) throw new WrongAmouthOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            Long id = Long.parseLong(stringArgument);
            Worker workerToRemove = collectionManager.getById(id);
            if (workerToRemove == null) throw new WorkerNotFoundException();
            if (!workerToRemove.getOwner().equals(user)) throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkWorkerUserId(workerToRemove.getID(), user))
                throw new ManualDatabaseEditException();
            databaseCollectionManager.deleteWorkerById(id);
            collectionManager.removefromStack(workerToRemove);
            ResponseOutputer.appendln("Рабочий успешно удален!");
            return true;
        } catch (WrongAmouthOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        } catch (NumberFormatException exception) {
            ResponseOutputer.appenderror("ID должен быть представлен числом!");
        } catch (WorkerNotFoundException exception) {
            ResponseOutputer.appenderror("Рабочего с таким ID в коллекции нет!");
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
