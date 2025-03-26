package com.floda.home;

import com.floda.home.service.ImagesUtilService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

@Slf4j
public class ApplicationImagesUtil {

    private static final ImagesUtilService service = new ImagesUtilService();

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            runApplication(scanner);
        } catch (Exception e) {
            log.error("Произошла ошибка: {}", e.getMessage(), e);
            System.out.println("Произошла непредвиденная ошибка. Подробности в логах.");
        }
    }

    private static void runApplication(Scanner scanner) {
        System.out.println("=== Консольное приложение для обработки изображений ===");

        boolean isRunning = true;
        while (isRunning) {
            printMainMenu();

            try {
                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1 -> processImageOperation("Черно-белое", scanner);
                    case 2 -> processImageOperation("Состаренное", scanner);
                    case 3 -> processImageOperation("Негатив", scanner);
                    case 4 -> processImageOperation("Размытое", scanner);
                    case 0 -> {
                        System.out.println("Работа приложения завершена");
                        isRunning = false;
                    }
                    default -> System.out.println("Неверная команда. Попробуйте снова.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите число от 0 до 4");
                scanner.nextLine();
            }
        }
    }

    private static void processImageOperation(String operationName, Scanner scanner) {
        System.out.println("\n=== Создание " + operationName + " изображения ===");

        File sourceFile = getInputFile(scanner, "Введите путь к исходному изображению: ");
        if (sourceFile == null) return;

        System.out.print("Введите имя нового файла (без расширения): ");
        String newFileName = scanner.nextLine().trim();
        if (newFileName.isEmpty()) {
            System.out.println("Имя файла не может быть пустым. Операция отменена.");
            return;
        }

        // Определяем директорию для сохранения (директория исходного файла по умолчанию)
        String sourceDir = sourceFile.getParent();

        // Запрашиваем путь для сохранения (если не указан, используем директорию исходного файла)
        System.out.print("Введите путь для сохранения (оставьте пустым для сохранения в исходной директории): ");
        String savePath = scanner.nextLine().trim();

        // Формируем полный путь
        String outputPath = (savePath.isEmpty() ? sourceDir : savePath) + File.separator +
                newFileName + ".jpg";
        File outputFile = new File(outputPath);

        // Проверяем возможность создания файла
        if (!checkOutputFile(outputFile, scanner)) {
            return;
        }

        try {
            long startTime = System.currentTimeMillis();
            File result = processImage(operationName, sourceFile, outputFile);
            long endTime = System.currentTimeMillis();

            System.out.println("\n" + operationName + " изображение успешно сохранено как: " + result.getName());
            System.out.println("Полный путь: " + result.getAbsolutePath());
            System.out.printf("Время обработки: %d мс\n", (endTime - startTime));
        } catch (Exception e) {
            log.error("Ошибка обработки изображения: {}", e.getMessage(), e);
            System.out.println("Не удалось обработать изображение. Подробности в логах.");
        }
    }

    private static boolean checkOutputFile(File outputFile, Scanner scanner) {
        // Проверяем существование файла
        if (outputFile.exists()) {
            System.out.println("Файл уже существует: " + outputFile.getAbsolutePath());
            System.out.print("Перезаписать? (y/n): ");
            if (!scanner.nextLine().equalsIgnoreCase("y")) {
                return false;
            }
        }

        // Проверяем возможность создания файла
        try {
            File parentDir = outputFile.getParentFile();
            if (!parentDir.exists()) {
                System.out.print("Директория не существует. Создать? (y/n): ");
                if (scanner.nextLine().equalsIgnoreCase("y")) {
                    if (!parentDir.mkdirs()) {
                        System.out.println("Не удалось создать директорию.");
                        return false;
                    }
                } else {
                    return false;
                }
            }

            // Пробуем создать временный файл для проверки
            if (!outputFile.createNewFile()) {
                System.out.println("Невозможно создать файл по указанному пути.");
                return false;
            }
            outputFile.delete(); // Удаляем временный файл
            return true;
        } catch (IOException e) {
            System.out.println("Ошибка при проверке пути: " + e.getMessage());
            return false;
        }
    }

    private static File processImage(String operationName, File source, File destination) throws Exception {
        return switch (operationName) {
            case "Черно-белое" -> service.colorToBW(source, destination);
            case "Состаренное" -> service.agedImage(source, destination);
            case "Негатив" -> service.negativeImage(source, destination);
            case "Размытое" -> service.blurImage(source, destination);
            default -> throw new IllegalArgumentException("Неизвестная операция: " + operationName);
        };
    }

    private static void printMainMenu() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("Главное меню обработки изображений");
        System.out.println("=".repeat(40));
        System.out.println("1. Конвертировать в черно-белое");
        System.out.println("2. Применить эффект старения");
        System.out.println("3. Создать негатив");
        System.out.println("4. Применить размытие");
        System.out.println("0. Выход");
        System.out.println("=".repeat(40));
        System.out.print("Ваш выбор: ");
    }

    private static File getInputFile(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) {
                System.out.println("Путь не может быть пустым. Попробуйте снова.");
                continue;
            }

            File file = new File(path);
            if (!file.exists() || !file.isFile()) {
                System.out.println("Файл не найден или недоступен: " + file.getAbsolutePath());
                System.out.print("Хотите попробовать снова? (y/n): ");
                if (!scanner.nextLine().equalsIgnoreCase("y")) {
                    return null;
                }
            } else {
                return file;
            }
        }
    }
}