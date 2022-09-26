package server.commands;

import common.interaction.User;

public interface CommandAble {
    String getDescription();
    String getUsage();
    String getName();
    boolean execute(String argument, Object object, User user);
}
