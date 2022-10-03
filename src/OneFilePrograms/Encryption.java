package OneFilePrograms;

import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.io.IOException;

public class Encryption {

    private static final String CANCEL_OPERATION = "Отмена операции шифрования...";

    public static void main(String[] args) {
        encryptOrDecryptFile(Path.of("C:\\Directory\\SecretData.txt"), "EVA-01");
    }

    public static void encryptOrDecryptFile(Path path, String password) {
        try {
            // Разнообразные проверки.
            if (!Files.exists(path)) {
                System.out.println("Файл не существует! " + CANCEL_OPERATION);
                return;
            }
            if (Files.isDirectory(path)) {
                System.out.println("Вы пытаетесь зашифровать/расшифровать директорию! " + CANCEL_OPERATION);
                return;
            }

            // Создание суммы пароля.
            int passwordSum = 0;
            for (char ch : password.toCharArray()) {
                passwordSum += ch;
            }

            // Проверка на зашифрованность файла.
            if (new String(Files.readAllBytes(path)).endsWith("[ENCRYPTED]")) {
                // Чтение байтов из потока, удаление флажка [ENCRYPTED] и последующая расшифровка.
                System.out.println("Расшифрование файла...");
                byte[] fileBytes = Files.readAllBytes(path);
                fileBytes = Arrays.copyOf(fileBytes, (fileBytes.length - 11));
                for (int i = 0; i < fileBytes.length; i++) {
                    fileBytes[i] ^= passwordSum;
                }

                // Перезапись файла с расшифрованными данными.
                Files.write(path, fileBytes);
                System.out.println("Успешно расшифрован файл: " + path);
            } else {
                // Чтение байтов из потока и последующее шифрование.
                System.out.println("Шифрование файла...");
                byte[] fileBytes = Files.readAllBytes(path);
                for (int i = 0; i < fileBytes.length; i++) {
                    fileBytes[i] ^= passwordSum;
                }

                // Перезапись файла с зашифрованными данными.
                Files.write(path, fileBytes);
                Files.write(path, "[ENCRYPTED]".getBytes(), StandardOpenOption.APPEND);
                System.out.println("Успешно зашифрован файл: " + path);
            }
        } catch (IOException e) {
            System.out.println("Где-то перехватилось исключение ввода-вывода!");
            e.printStackTrace();
        }
    }

}
