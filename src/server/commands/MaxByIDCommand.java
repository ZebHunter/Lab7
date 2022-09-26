package server.commands;

import common.exceptions.WrongAmouthOfElementsException;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

public class MaxByIDCommand extends AbstractCommand{
    private CollectionManager collectionManager;

    public MaxByIDCommand(CollectionManager collectionManager){
        super("max_by_id","", "вывести любой объект из коллекции, значение поля id которого является максимальным");
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute(String argument, Object object, User user){
        try{
            if(!argument.isEmpty() || object != null) throw new WrongAmouthOfElementsException();
            if (collectionManager.getCollection().size() != 0){
            int index = collectionManager.getCollection().size();
            ResponseOutputer.appendln(collectionManager.getByIndex(index - 1).toString());
            return true;

            }   
            else ResponseOutputer.appendln("Коллекция пуста");
        }catch(WrongAmouthOfElementsException e){
            ResponseOutputer.appenderror("Использование (" + argument + ") в " + getName());
        }
    return false;
    }
}
