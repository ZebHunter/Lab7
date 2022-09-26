package server.commands;

import java.util.ArrayList;
import java.util.Collections;

import common.data.Worker;
import common.exceptions.WrongAmouthOfElementsException;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

public class PrintAscending extends AbstractCommand{
    private CollectionManager collectionManager;

    public PrintAscending(CollectionManager collectionManager){
        super("print_ascending","", "выводит элементы коллекции в порядке возрастания");
        this.collectionManager = collectionManager;
    }

    public boolean execute(String argument, Object object, User user){
        try{
            if (!argument.isEmpty() || object != null) throw new WrongAmouthOfElementsException();
        ArrayList<Worker> copyofCollection = new ArrayList<>(collectionManager.getCollection());
        Collections.sort(copyofCollection);
        for(Worker worker: copyofCollection){
            ResponseOutputer.appendln(worker.toString());
        }
        return true;
    } catch(WrongAmouthOfElementsException e){
        ResponseOutputer.appenderror("Нет аргументов в " + getName());
    }
    return false;
    }
}
