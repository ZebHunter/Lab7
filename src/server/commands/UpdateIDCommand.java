package server.commands;

import java.time.LocalDateTime;

import common.data.Coordinates;
import common.data.Person;
import common.data.Position;
import common.data.Worker;
import common.exceptions.CollectionIsEmptyException;
import common.exceptions.DatabaseHandlingException;
import common.exceptions.ManualDatabaseEditException;
import common.exceptions.PermissionDeniedException;
import common.exceptions.WorkerNotFoundException;
import common.exceptions.WrongAmouthOfElementsException;
import common.interaction.User;
import common.interaction.WorkerObject;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutputer;

public class UpdateIDCommand extends AbstractCommand{
    private final CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;
    public UpdateIDCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("update", "<ID> {element}", "обновить значение элемента коллекции по ID");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

  
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (stringArgument.isEmpty() || objectArgument == null) throw new WrongAmouthOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();

            Long id = Long.parseLong(stringArgument);
            if (id <= 0) throw new NumberFormatException();
            Worker oldWorker = collectionManager.getById(id);
            if (oldWorker == null) throw new WorkerNotFoundException();
            if (!oldWorker.getOwner().equals(user)) throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkWorkerUserId(oldWorker.getID(), user))throw new ManualDatabaseEditException();
            WorkerObject newWorker = (WorkerObject)objectArgument;

            databaseCollectionManager.updateWorkerById(id,newWorker);

            String name = newWorker.getName() == null ? oldWorker.getName() : newWorker.getName();
            Coordinates coordinates = newWorker.getCoordinates() == null ? oldWorker.getCoordinates() : newWorker.getCoordinates();
            LocalDateTime creationDate = oldWorker.getCreationDate();
            int salary = newWorker.getSalary() == -1 ? oldWorker.getSalary() : newWorker.getSalary();
            Position position = newWorker.getPosition() == null ? oldWorker.getPosition() : newWorker.getPosition();
            common.data.Status status = newWorker.getStatus() == null ? oldWorker.getStatus() : newWorker.getStatus();
            Person person = newWorker.getPerson() == null ? oldWorker.getPerson() : newWorker.getPerson();

            collectionManager.removefromStack(oldWorker);
            collectionManager.addtoStack(new Worker(id, name, coordinates, creationDate, salary, position, status, person, user));
            ResponseOutputer.appendln("Рабочий успешно изменен!");
            return true;
        } catch (WrongAmouthOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        } catch (NumberFormatException exception) {
            ResponseOutputer.appenderror("ID должен быть представлен положительным числом!");
        } catch (WorkerNotFoundException exception) {
            ResponseOutputer.appenderror("Лабораторной работы с таким ID в коллекции нет!");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("Переданный клиентом объект неверен!");
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
