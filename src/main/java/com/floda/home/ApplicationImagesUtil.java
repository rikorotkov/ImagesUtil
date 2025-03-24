package com.floda.home;

import com.floda.home.service.ImagesUtilService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Scanner;

@Slf4j
public class ApplicationImagesUtil {

    private static final Scanner scanner = new Scanner(System.in);
    private static ImagesUtilService service = new ImagesUtilService();

    private static final File source = new File("123.jpg");

    public static void main(String[] args) {
        printMenu(scanner);
    }

    private static void printMenu(Scanner scanner) {
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("*".repeat(26));
            System.out.println("Выберите команду");
            System.out.println("1. Сделать ЧБ изображение");
            System.out.println("0. Выход");
            System.out.println("*".repeat(26));

            String strOption = scanner.nextLine();
            int option = Integer.parseInt(strOption);

            switch (option) {
                case 1:
                    System.out.println("1. Сделать ЧБ изображение.");
                    service.colorToBW(source);
                    break;
                case 0:
                    System.out.println("Работа приложения завершена");
                    isRunning = false;
                    return;
                default:
                    System.out.println("Вы ввели неверную команду.");
                    break;
            }
        }
        scanner.close();
    }
}
