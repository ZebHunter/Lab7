package server.commands;

import common.exceptions.DatabaseHandlingException;
import common.exceptions.UserIsNotFoundException;
import common.exceptions.WrongAmouthOfElementsException;
import common.interaction.User;
import server.utility.DatabaseUserManager;
import server.utility.ResponseOutputer;

public class LoginCommand extends AbstractCommand{
    private DatabaseUserManager databaseUserManager;

    public LoginCommand(DatabaseUserManager databaseUserManager) {
        super("login", "", "внутренняя команда");
        this.databaseUserManager = databaseUserManager;
    }

    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmouthOfElementsException();
            if (databaseUserManager.checkUserByUsernameAndPassword(user)) ResponseOutputer.appendln("Пользователь " +
                    user.getUsername() + " авторизован.");
            else throw new UserIsNotFoundException();
            return true;
        } catch (WrongAmouthOfElementsException exception) {
            ResponseOutputer.appendln("Использование: эммм...эээ.это внутренняя команда...");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appenderror("Произошла ошибка при обращении к базе данных!");
        } catch (UserIsNotFoundException exception) {
            ResponseOutputer.appenderror("Неправильные имя пользователя или пароль!");
        }
        return false;
    }
}
