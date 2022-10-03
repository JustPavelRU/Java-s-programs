package Human;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import OneFilePrograms.Encryption;

public class Human implements Serializable {

    private static final String[] COUNTRIES = new String[254];
    private static final String DIR_PATH = "C:\\Users\\Petro\\IdeaProjects\\Learning\\src\\FreePractice\\Human\\";
    private static final String COUNTRIES_PATH = DIR_PATH + "Countries.txt";
    private static final Scanner SCANNER = new Scanner(System.in);
    private static ArrayList<Human> humanList = new ArrayList<>(10);

    {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(COUNTRIES_PATH))) {
            int i = 0;
            while (bufferedReader.ready()) {
                COUNTRIES[i++] = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.printf("\nФайл по пути %s не найден!\n", COUNTRIES_PATH);
        } catch (IOException e) {
            System.out.println("\nНеизвестная ошибка ввода-вывода!");
        }
    }

    private String name = "DEFAULT_NAME";
    private String country = "DEFAULT_COUNTRY";
    private String profession = "DEFAULT_PROFESSION";
    private int age = -1;

    public static void main(String[] args) {

        String cmd = "X";
        String subCmd;
        boolean isExitEntered = false;

        showCmd();

        while (!isExitEntered) {
            System.out.print("\nВыберите команду: ");
            try {
                switch (cmd = SCANNER.nextLine()) {
                    case "make": {
                        System.out.print("\nВведите данные человека(Имя, Страна, Профессия, Возраст) поочередно через Enter: ");
                        humanList.add(new Human(SCANNER.nextLine(), SCANNER.nextLine(), SCANNER.nextLine(), SCANNER.nextInt()));
                        SCANNER.nextLine();
                        break;
                    }
                    case "change": {
                        System.out.print("\nВведите поочередно номер человека и его новые данные(Имя, Страна, Профессия, Возраст) через Enter: ");
/*
FIX
*/
                        int index = SCANNER.nextInt();
                        SCANNER.nextLine();
                        humanList.set(index, new Human(SCANNER.nextLine(), SCANNER.nextLine(), SCANNER.nextLine(), SCANNER.nextInt()));
                        SCANNER.nextLine();
                        break;
                    }
                    case "showOne": {
                        System.out.print("\nВведите номер(а) человека, данные которого хотите вывести на экран(чтобы прекратить ввод введите \"stop\"): ");
                        while (!((subCmd = SCANNER.nextLine()).equals("stop"))) {
                            showHuman(subCmd);
                        }
                        break;
                    }
                    case "showAll": {
                        for (Human human : humanList) {
                            System.out.println(human.toString());
                        }
                        break;
                    }
                    case "showCmd": {
                        showCmd();
                        break;
                    }
                    case "showFiles": {
                        for (Path path : Files.newDirectoryStream(Path.of(DIR_PATH))) {
                            System.out.println(path.getFileName());
                        }
                    }
                    case "save": {
                        System.out.print("\nВведите имя файла(с расширением или без): ");
                        saveHumans(SCANNER.nextLine());
                        break;
                    }
                    case "load": {
                        System.out.print("\nВведите имя файла(с расширением или без): ");
                        loadHumans(SCANNER.nextLine());
                        break;
                    }
                    case "clearField": {
                        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                        break;
                    }
                    case "exit": {
                        isExitEntered = true;
                        break;
                    }
                    case "encryptOrDecrypt": {
                        System.out.print("\nВведите поочередно имя файла(без расширения) и пароль через Enter: ");
                        Encryption.encryptOrDecryptFile(Path.of(DIR_PATH + SCANNER.nextLine() + ".bin"), SCANNER.nextLine());
                        break;
                    }
                    default: {
                        throw new UnknownCommandException();
                    }
                }
            } catch (UnknownCommandException e) {
                System.out.println("\n" + e.getMessage());
            } catch (Exception e) {
                System.out.println("\n" + e.getMessage());
            }
        }
    }

    public Human(String name, String country, String profession, int age) {
        if (isAlphabeticFieldNormal(name)) this.name = name;
        if (isCountryNormal(country)) this.country = country;
        if (isAlphabeticFieldNormal(profession)) this.profession = profession;
            if (isAgeNormal(age)) this.age = age;

    }

    private boolean isAlphabeticFieldNormal(String name) {
        for (char ch : name.toCharArray()) {
            if (!Character.isAlphabetic(ch)) return false;
        }
        return true;
    }

    private boolean isCountryNormal(String country) {
        for (String str : COUNTRIES) {
            if (country.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAgeNormal(int age) {
        if (age > -1 && age < 120) return true;
        else return false;
    }

    public static void showHuman(String index) {
        for (char ch: index.toCharArray()) {
            if (!Character.isDigit(ch)) {
                System.out.println("\nОшибка, вам необходимо ввести число!");
                return;
            }
        }
        System.out.println("\n" + humanList.get(Integer.parseInt(index)).toString());
    }

    public static void saveHumans(String fileName) {
        if (fileName.contains("\\")) {
            System.out.println("\nВведите имя файла, а не путь!");
            return;
        }
        if (!fileName.endsWith(".bin")) {
            // Нет ли расширения в названни.
            if (fileName.lastIndexOf(".") == -1) {
                fileName += ".bin";
            } else {
                fileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".bin";
            }
        }

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(DIR_PATH + fileName))) {
            objectOutputStream.writeObject(humanList);
            System.out.println("Успешно сохранено по пути: " + DIR_PATH + fileName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loadHumans(String fileName) {
        if (fileName.contains("\\")) {
            System.out.println("\nВведите имя файла, а не путь!");
            return;
        }
        if (!fileName.endsWith(".bin")) {
            // Нет ли расширения в названни.
            if (fileName.lastIndexOf(".") == -1) {
                fileName += ".bin";
            } else {
                fileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".bin";
            }
        }

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(DIR_PATH + fileName))) {
            System.out.println("\nНастоящие объекты будут удалены, подвердить загрузку? Y/N");
            if (SCANNER.nextLine().equals("Y")) {
                humanList = (ArrayList<Human>) objectInputStream.readObject();
                System.out.println("Успешно загружено из пути: " + DIR_PATH + fileName);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void showCmd() {
        System.out.println("\nКоманды программы HUMANS_BASE 1.1 RELEASE");
        System.out.println("| make \t\t - создать человека \t\t\t\t| change     - изменить данные человека \t\t  |" +
                "\n| showOne \t - вывести данные человека на экран | showAll    - вывести данные всех людей на экран |" +
                "\n| showCmd \t - вывести команды на экран \t\t| showFiles  - вывести файлы на экран \t\t\t  |" +
                "\n| save \t\t - сохранить данные в файл \t\t\t| load \t\t - загрузить данные из файла \t\t  |" +
                "\n| clearField - очистить поле    \t\t\t\t| exit \t\t - выйти \t\t\t\t\t\t\t  |" +
                "\n| encryptOrDecrypt - зашифровать файл");
    }

    @Override
    public String toString() {
        return "| Порядковый номер: " + humanList.indexOf(this) + " \t| Имя: " + name + " \t| Страна: " + country + " \t| Профессия: " +
                profession + "\t| Возраст: " + age + " \t|";
    }
}

class UnknownCommandException extends IOException {
    UnknownCommandException() {
        super("Неизвестная команда!");
    }
}
