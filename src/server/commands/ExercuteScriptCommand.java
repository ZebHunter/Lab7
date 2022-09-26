package server.commands;

import common.exceptions.WrongAmouthOfElementsException;
import common.interaction.User;
import server.utility.ResponseOutputer;

public class ExercuteScriptCommand extends AbstractCommand{
    public ExercuteScriptCommand() {
        super("execute_script", "<file_name>", "исполнить скрипт из указанного файла");
    }

    
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (stringArgument.isEmpty() || objectArgument != null) throw new WrongAmouthOfElementsException();
            return true;
        } catch (WrongAmouthOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        }
        return false;
    }
}
