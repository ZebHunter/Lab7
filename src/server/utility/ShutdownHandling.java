package server.utility;

import common.utility.Console;

public class ShutdownHandling {

    public static void addCollectionSavingHook() {
        Thread savingHook = new Thread(() ->
                Console.println("\nСервер принял спок.")
        );
        Runtime.getRuntime().addShutdownHook(savingHook);
    }
}

