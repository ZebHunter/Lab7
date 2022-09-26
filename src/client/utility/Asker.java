package client.utility;

import common.exceptions.MustBeNotEmptyException;
import common.exceptions.NotInDeclaredLimitsException;
import common.data.*;
import common.exceptions.*;
import common.utility.Console;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Asker {
    Scanner userScanner;
    private boolean scriptMode;

    public Asker(Scanner userScanner, boolean scriptMode){
        this.userScanner = userScanner;
        this.scriptMode = scriptMode;
    }

    public Asker(Scanner userScanner){
        this.userScanner = userScanner;
        scriptMode = false;
    }

    public Scanner getUserScanner(){
        return userScanner;
    }

    public void setUserScanner(Scanner userScanner){
        this.userScanner = userScanner;
    }

    public void scriptMode(){
        scriptMode = true;
    }

    public void setUserMode(){
        scriptMode = false;
    }


    public String askName() throws IncorrectInputInScriptException{
        String name;
        while(true){
            Console.print(Console.PS2);
            Console.print("Напишите имя: ");
            try{
                name = userScanner.nextLine().trim();
                if (scriptMode) Console.println(name);
                if (name.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (MustBeNotEmptyException e){
                Console.printError("Имя не может быть пустым");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NoSuchElementException e) {
                Console.printError("Имя не может быть загружено");
                if (scriptMode) throw new IncorrectInputInScriptException();
                if(!userScanner.hasNext()) {
                    Console.printError("Ctrl-D Caused exit!");
                    System.exit(0);
                }
            } catch (IllegalStateException e) {
                Console.printError("Неизвестная ошибка!");
                System.exit(0);
            }
        }
        return name;
    }

    private int askX() throws IncorrectInputInScriptException{
        int x;
        while(true){
            try{
                Console.print(Console.PS2);
                Console.print("Введите координату x: ");
                String s = userScanner.nextLine().trim();
                if(scriptMode) Console.println(s);
                x = Integer.parseInt(s);
                break;
            } catch(NoSuchElementException e){
                Console.printError("x не может быть загружен");
                if (scriptMode) throw new IncorrectInputInScriptException();
                if (!userScanner.hasNext()){
                    Console.printError("Ошибка");
                    System.exit(0);
                }
            } catch (NumberFormatException e) {
                Console.printError("x должно быть целым");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Console.printError("Неизвестная ошибка!");
                System.exit(0);
            }
        }
        return x;
    }

    private Integer askY() throws IncorrectInputInScriptException{
        Integer y;
        while (true){
            try{
                Console.print(Console.PS2);
                Console.print("Введите координату y: ");
                String s = userScanner.nextLine().trim();
                if(scriptMode) Console.println(s);
                y = Integer.parseInt(s);
                break;
            } catch(NoSuchElementException e){
                Console.printError("y не может быть загружен");
                if (scriptMode) throw new IncorrectInputInScriptException();
                if(!userScanner.hasNext()) {
                    Console.printError("Ctrl-D Caused exit!");
                    System.exit(0);
                }
            } catch (NumberFormatException e) {
                Console.printError("x должно быть целым");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Console.printError("Неизвестная ошибка!");
                System.exit(0);
            }
        }
        return y;
    }

    public Coordinates askCoordinates() throws IncorrectInputInScriptException{
        int x;
        Integer y;
        x = askX();
        y = askY();
        return new Coordinates(x, y);
    }

    public LocalDate askCreationDay(){
        while (true){
            return LocalDate.now();
        }
    }

    public int askSalary() throws IncorrectInputInScriptException{
        int salary;
        while(true){
            try{
                Console.print(Console.PS2);
                Console.print("Введите зарплату: ");
                String s = userScanner.nextLine().trim();
                if(scriptMode) Console.println(s);
                salary = Integer.parseInt(s);
                if (salary <= 0) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException e) {
                Console.printError("Зарплата не может быть загружена");
                if (scriptMode) throw new IncorrectInputInScriptException();
                if(!userScanner.hasNext()) {
                    Console.printError("Ctrl-D Caused exit!");
                    System.exit(0);
                }
            } catch (NumberFormatException e) {
                Console.printError("Зарплата должна быть целым значением");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Console.printError("Неизвестная ошибка!");
                System.exit(0);
            } catch (NotInDeclaredLimitsException e) {
                Console.printError("Зарплата должна быть положительной и больше 0");
                if (scriptMode) throw new IncorrectInputInScriptException();
            }

        }
        return salary;
    }

    public Position askPosition() throws IncorrectInputInScriptException{
        Position position;
        while (true){
            try{
                Console.println("Выберете категорию: " + Position.nameList());
                Console.print(Console.PS2);
                Console.print("Выберете тип: ");
                String s = userScanner.nextLine().trim();
                if (scriptMode) Console.println(s);
                if (s.equals("")) return null;
                position = Position.valueOf(s.toUpperCase());
                break;
            }catch (NoSuchElementException exception) {
                Console.printError("Тип не определен");
                if (scriptMode) throw new IncorrectInputInScriptException();
                if(!userScanner.hasNext()) {
                    Console.printError("Ctrl-D Caused exit!");
                    System.exit(0);
                }
            } catch (IllegalArgumentException exception) {
                Console.printError("There is no similar type in category");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Console.printError("Unexpected error!");
                System.exit(0);
            }
        }
        return position;
    }

    public Status askStatus() throws IncorrectInputInScriptException{
        Status status;
        while (true){
            try{
                Console.println("Выберете категорию: " + Status.nameList());
                Console.print(Console.PS2);
                Console.print("Выберете тип: ");
                String s = userScanner.nextLine().trim();
                if (scriptMode) Console.println(s);
                if (s.equals("")) return null;
                status = Status.valueOf(s.toUpperCase());
                break;
            }catch (NoSuchElementException exception) {
                Console.printError("Type can't be recognized");
                if (scriptMode) throw new IncorrectInputInScriptException();
                if(!userScanner.hasNext()) {
                    Console.printError("Ctrl-D Caused exit!");
                    System.exit(0);
                }
            } catch (IllegalArgumentException exception) {
                Console.printError("There is no similar type in category");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Console.printError("Unexpected error!");
                System.exit(0);
            }
        }
        return status;
    }

    private LocalDateTime askBirthday() throws IncorrectInputInScriptException, DateTimeParseException {
        LocalDateTime birthday;
        while (true){
            try{
                Console.print(Console.PS2);
                Console.print("Введите дату рождения: ");
                String date = userScanner.nextLine();
                if (date.equals("")) throw new MustBeNotEmptyException();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                LocalDateTime parse = LocalDateTime.parse(date, formatter);
                birthday = parse;
                break;

            }catch(DateTimeParseException e){
                Console.printError("Некорректная дата");
            } catch (MustBeNotEmptyException e){
                Console.printError("Имя не может быть пустым");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NoSuchElementException e) {
                Console.printError("The name can't be loaded or recognized");
                if (scriptMode) throw new IncorrectInputInScriptException();
                if(!userScanner.hasNext()) {
                    Console.printError("Ctrl-D Caused exit!");
                    System.exit(0);
                }
            } catch (IllegalStateException e) {
                Console.printError("Unexpected error!");
                System.exit(0);
            }
        }
        return birthday;
    }


    private Color askHairColor() throws IncorrectInputInScriptException{
        Color color;
        while (true){
            try{
                Console.println("Выберете категорию: " + Color.nameList());
                Console.print(Console.PS2);
                Console.print("Выберете тип: ");
                String s = userScanner.nextLine().trim();
                if (scriptMode) Console.println(s);
                if (s.equals("")) return null;
                color = Color.valueOf(s.toUpperCase());
                break;
            }catch (NoSuchElementException exception) {
                Console.printError("Type can't be recognized");
                if (scriptMode) throw new IncorrectInputInScriptException();
                if(!userScanner.hasNext()) {
                    Console.printError("Ctrl-D Caused exit!");
                    System.exit(0);
                }
            } catch (IllegalArgumentException exception) {
                Console.printError("There is no similar type in category");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Console.printError("Unexpected error!");
                System.exit(0);
            }
        }
        return color;
    }

    private EyeColor askEyeColor() throws IncorrectInputInScriptException{
        EyeColor color;
        while (true){
            try{
                Console.println("Выберете категорию: " + EyeColor.nameList());
                Console.print(Console.PS2);
                Console.print("Выберете тип: ");
                String s = userScanner.nextLine().trim();
                if (scriptMode) Console.println(s);
                if (s.equals("")) return null;
                color = EyeColor.valueOf(s.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                Console.printError("Type can't be recognized");
                if (scriptMode) throw new IncorrectInputInScriptException();
                if(!userScanner.hasNext()) {
                    Console.printError("Ctrl-D Caused exit!");
                    System.exit(0);
                }
            } catch (IllegalArgumentException exception) {
                Console.printError("There is no similar type in category");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Console.printError("Unexpected error!");
                System.exit(0);
            }
        }
        return color;
    }

    private Country askNationality() throws IncorrectInputInScriptException{
        Country nationality;
        while (true){
            try{
                Console.println("Выберете категорию: " + Country.nameList());
                Console.print(Console.PS2);
                Console.print("Выберете тип: ");
                String s = userScanner.nextLine().trim();
                if (scriptMode) Console.println(s);
                if (s.equals("")) return null;
                nationality = Country.valueOf(s.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                Console.printError("Type can't be recognized");
                if (scriptMode) throw new IncorrectInputInScriptException();
                if(!userScanner.hasNext()) {
                    Console.printError("Ctrl-D Caused exit!");
                    System.exit(0);
                }
            } catch (IllegalArgumentException exception) {
                Console.printError("There is no similar type in category");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Console.printError("Unexpected error!");
                System.exit(0);
            }
        }
        return nationality;
    }

    private Double askLocationX() throws IncorrectInputInScriptException{
        Double x;
        while(true){
            try{
                Console.print(Console.PS2);
                Console.print("Введите координату x: ");
                String s = userScanner.nextLine().trim();
                if(scriptMode) Console.println(s);
                x = Double.parseDouble(s);
                break;
            } catch(NoSuchElementException e){
                Console.printError("The Y axis can't be loaded or recognized");
                if (scriptMode) throw new IncorrectInputInScriptException();
                if(!userScanner.hasNext()) {
                    Console.printError("Ctrl-D Caused exit!");
                    System.exit(0);
                }
            } catch (NumberFormatException e) {
                Console.printError("The Y axis have to be an Float value");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Console.printError("Unexpected error!");
                System.exit(0);
            }
        }
        return x;
    }

    private Long askLocationY() throws IncorrectInputInScriptException{
        Long y;
        while(true){
            try{
                Console.print(Console.PS2);
                Console.print("Введите координату y: ");
                String s = userScanner.nextLine().trim();
                if(scriptMode) Console.println(s);
                y = Long.parseLong(s);
                break;
            } catch(NoSuchElementException e){
                Console.printError("The Y axis can't be loaded or recognized");
                if (scriptMode) throw new IncorrectInputInScriptException();
                if(!userScanner.hasNext()) {
                    Console.printError("Ctrl-D Caused exit!");
                    System.exit(0);
                }
            } catch (NumberFormatException e) {
                Console.printError("The Y axis have to be an Float value");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Console.printError("Unexpected error!");
                System.exit(0);
            }
        }
        return y;
    }

    private float askLocationZ() throws IncorrectInputInScriptException{
        float z;
        while(true){
            try{
                Console.print(Console.PS2);
                Console.print("Введите координату z: ");
                String s = userScanner.nextLine().trim();
                if(scriptMode) Console.println(s);
                z = Float.parseFloat(s);
                break;
            } catch(NoSuchElementException e){
                Console.printError("The Y axis can't be loaded or recognized");
                if (scriptMode) throw new IncorrectInputInScriptException();
                if(!userScanner.hasNext()) {
                    Console.printError("Ctrl-D Caused exit!");
                    System.exit(0);
                }
            } catch (NumberFormatException e) {
                Console.printError("The Y axis have to be an Float value");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Console.printError("Unexpected error!");
                System.exit(0);
            }
        }
        return z;
    }

    private String askLocationName(){
        String name;
        while(true){
            Console.print(Console.PS2);
            Console.print("Напишите название: ");
            name = userScanner.nextLine().trim();
            if (scriptMode) Console.println(name);
            break;
        }
        return name;
    }

    private Location askLocation() throws IncorrectInputInScriptException{
        Double x = askLocationX();
        Long y = askLocationY();
        float z = askLocationZ();
        String name = askLocationName();
        if (name.equals("")) return null;
        return new Location(x, y, z, name);
    }

    public Person askPerson() throws IncorrectInputInScriptException{
        LocalDateTime birthday = askBirthday();
        Color color = askHairColor();
        EyeColor eyeColor = askEyeColor();
        Country nationality = askNationality();
        Location location = askLocation();
        return new Person(birthday, eyeColor, color, nationality, location);
    }

    public boolean askQuestion(String question) throws IncorrectInputInScriptException {
        String finalQuestion = question + " (+/-):";
        String answer;
        while (true) {
            try {
                Console.println(finalQuestion);
                Console.print(Console.PS2);
                answer = userScanner.nextLine().trim();
                if (scriptMode) Console.println(answer);
                if (!answer.equals("+") && !answer.equals("-")) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Console.printError("Ответ не распознан!");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Console.printError("Ответ должен быть представлен знаками '+' или '-'!");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return answer.equals("+");
    }
}
