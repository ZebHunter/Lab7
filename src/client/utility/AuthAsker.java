package client.utility;

import java.util.NoSuchElementException;
import java.util.Scanner;

import common.exceptions.MustBeNotEmptyException;
import common.exceptions.NotInDeclaredLimitsException;
import common.utility.Console;

public class AuthAsker {
    
    private Scanner userScanner;

    public AuthAsker(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    
    public String askLogin() {
        String login;
        while (true) {
            try {
                Console.println("Введите логин:");
                Console.print(Console.PS2);
                login = userScanner.nextLine().trim();
                if (login.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception) {
                Console.printError("Данного логина не существует!");
            } catch (MustBeNotEmptyException exception) {
                Console.printError("Имя не может быть пустым!");
            } catch (IllegalStateException exception) {
                Console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return login;
    }

    
    public String askPassword() {
        String password;
        while (true) {
            try {
                Console.println("Введите пароль:");
                Console.print(Console.PS2);
                password = userScanner.nextLine().trim();
                break;
            } catch (NoSuchElementException exception) {
                Console.printError("Неверный логин или пароль!");
            } catch (IllegalStateException exception) {
                Console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return password;
    }

    
    public boolean askQuestion(String question) {
        String finalQuestion = question + " (+/-):";
        String answer;
        while (true) {
            try {
                Console.println(finalQuestion);
                Console.print(Console.PS2);
                answer = userScanner.nextLine().trim();
                if (!answer.equals("+") && !answer.equals("-")) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Console.printError("Ответ не распознан!");
            } catch (NotInDeclaredLimitsException exception) {
                Console.printError("Ответ должен быть представлен знаками '+' или '-'!");
            } catch (IllegalStateException exception) {
                Console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return answer.equals("+");
    }
}
