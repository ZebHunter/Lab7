package common.interaction;

import java.io.Serializable;

public class Request implements Serializable {
    private String commandName;
    private String commandStringArgument;
    private Serializable commandObjectArgument;
    private User user;

    public Request(String commandName, String commandStringArgument, Serializable commandObjectArgument, User user) {
        this.commandName = commandName;
        this.commandStringArgument = commandStringArgument;
        this.commandObjectArgument = commandObjectArgument;
        this.user = user;
    }

    public Request(User user) {
        this("", "", null, user);
    }

    
    public String getCommandName() {
        return commandName;
    }

    
    public String getCommandStringArgument() {
        return commandStringArgument;
    }

    
    public Object getCommandObjectArgument() {
        return commandObjectArgument;
    }

    
    public boolean isEmpty() {
        return commandName.isEmpty() && commandStringArgument.isEmpty() && commandObjectArgument == null;
    }

    public User getUser(){
        return user;
    }

    @Override
    public String toString() {
        return "Request[" + commandName + ", " + commandStringArgument + ", " + commandObjectArgument + "]";
    }
}

