package server.commands;

import java.util.ArrayList;

import common.exceptions.WrongAmouthOfElementsException;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

public class PrintFieldAscendingSalaryCommand extends AbstractCommand{
    private CollectionManager collectionManager;

    public PrintFieldAscendingSalaryCommand(CollectionManager collectionManager){
        super("print_field_ascending_salary","", "выводит значения полей salary");
        this.collectionManager = collectionManager;
    }

    public boolean execute(String argument, Object object, User user){
        try{
            if(!argument.isEmpty() || object != null) throw new WrongAmouthOfElementsException();
            ArrayList<Integer> salary = new ArrayList<>(collectionManager.getAllSalary());
            salary.sort(null);
            ResponseOutputer.appendln(salary);
            return true;
        }catch(WrongAmouthOfElementsException e){
            ResponseOutputer.appenderror("Нет аргументов в " + getName());
        }
        return false;
    }
}
