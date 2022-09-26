package server.utility;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import common.data.Worker;
import common.exceptions.DatabaseHandlingException;
import common.utility.Console;

public class CollectionManager {
    private Stack<Worker> stackWorker;
    private LocalDateTime creationDate;
    private LocalDateTime lastInitTime;
    private DatabaseCollectionManager databaseCollectionManager;

    

    public CollectionManager(DatabaseCollectionManager databaseCollectionManager){
        
        this.databaseCollectionManager = databaseCollectionManager;

        loadCollection();
    }

    public Stack<Worker> getCollection(){
        return stackWorker;
    }

    public LocalDateTime getCreationDate(){
        return creationDate;
    }

    public void setCollection(Stack<Worker> stackWoker){
        this.stackWorker = stackWoker;
    }

    public Worker getByID(int id){
        return stackWorker.stream().filter(x -> x.getID().equals(id)).findFirst().orElse(null);
    }

    public void addtoStack(Worker worker){
        stackWorker.add(worker);
    }

    public void removefromStack(Worker worker){
        stackWorker.remove(worker);
    }

    public void removeByID(Long id){
        for(Worker worker: stackWorker){
            if(worker.getID() == id) stackWorker.remove(worker);
        }
    }

    public void clearStack(){
        stackWorker.clear();
    }

    public String infoAboutCollection(){
        return "Тип - " + stackWorker.getClass() + "\n" +
                "Время создания - " + getCreationDate() + "\n" +
                "Размер - " + stackWorker.size();
    }

    public Worker getFirst() {
        return stackWorker.stream().findFirst().orElse(null);
    }


    public void replaceById(Long id, Worker newValue){
        newValue.setID(id);
        stackWorker
                .stream()
                .filter(worker -> worker.getID() == id)
                .findFirst()
                .ifPresent(worker -> stackWorker.set(stackWorker.indexOf(worker), newValue));
    
    }

    public void removeByIndex(int index){
        stackWorker.remove(index);
    }

    public Worker getByIndex(int index){
       return stackWorker.get(index);
    }

    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    
    public String collectionType() {
        return stackWorker.getClass().getName();
    }

    public int collectionSize() {
        return stackWorker.size();
    }

    public Worker getById(Long id) {
        return stackWorker.stream().filter(x -> x.getID().equals(id)).findFirst().orElse(null);
    }

    public List<Integer> getAllSalary(){
        return stackWorker.stream()
            .map(Worker::getSalary)
            .collect(Collectors.toList());
    }

    private void loadCollection(){
        try {
            stackWorker = databaseCollectionManager.getCollection();
            lastInitTime = LocalDateTime.now();
            Console.println("Коллекция загружена.");
        } catch (DatabaseHandlingException exception) {
            stackWorker = new Stack<>();
            Console.printError("Коллекция не может быть загружена!");
        }
    }

   
    

    public String showCollection() {
        if (stackWorker.isEmpty()) return "Коллекция пуста!";
        return stackWorker.stream().reduce("", (sum, m) -> sum += m + "\n\n", (sum1, sum2) -> sum1 + sum2).trim();
    }
}
