package server.commands;

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

public class RemoveLastCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    DatabaseCollectionManager databaseCollectionManager;

    public RemoveLastCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager){
        super("remove_last","", "Удалить последний элемент из коллекции");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    @Override
    public boolean execute(String argument, Object object, User user){
        try{
            if (!argument.isEmpty() || object != null) throw new WrongAmouthOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            int index = collectionManager.getCollection().size();
            if (collectionManager.getByIndex(index - 1) == null) throw new WorkerNotFoundException();
            if (!collectionManager.getByIndex(index - 1).getOwner().equals(user)) throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkWorkerUserId(collectionManager.getByIndex(index - 1).getID(), user))
                throw new ManualDatabaseEditException();
            databaseCollectionManager.deleteWorkerById(collectionManager.getByIndex(index - 1).getID());
            collectionManager.removeByIndex(index - 1);
            ResponseOutputer.appendln("Последний рабочий удален");
            return true;
        }  catch (WrongAmouthOfElementsException exception) {
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
