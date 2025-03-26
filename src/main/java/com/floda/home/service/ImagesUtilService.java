package com.floda.home.service;

import com.floda.home.model.Image;
import com.floda.home.model.Pixel;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Path;

@Slf4j
public class ImagesUtilService {

    private final ImageService imageService = new ImageServiceImpl();

    public File colorToBW(File sourceImage, File destinationImage) {
        log.info("Делаю ЧБ изображение");
        Image image = imageService.parseFileToImage(sourceImage);
        Pixel[][] pixels = image.getPixels();
        String newFileName = "BW_" + sourceImage.getName();
        Image newImage = new Image(image.getWidth(), image.getHeight(), newFileName, Path.of("/"), image.getPixels());

        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[x].length; y++) {
                Pixel pixel = pixels[x][y];

                int r = pixel.getR();
                int g = pixel.getG();
                int b = pixel.getB();

                int gray = (int) (0.299 * r + 0.587 * g + 0.114 * b);

                pixel.setR(gray);
                pixel.setG(gray);
                pixel.setB(gray);

                newImage.setPixels(x, y, pixel);
            }
        }
        return imageService.save(newImage, destinationImage);
    }

    public File agedImage(File sourceImage, File destinationImage) {
        log.info("Делаю эффект старины");
        Image image = imageService.parseFileToImage(sourceImage);
        Pixel[][] pixels = image.getPixels();
        String newFileName = "aged_" + sourceImage.getName();
        Image newImage = new Image(image.getWidth(), image.getHeight(), newFileName, Path.of("/"), pixels);

        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[x].length; y++) {
                Pixel pixel = pixels[x][y];

                int r = pixel.getR();
                int g = pixel.getG();
                int b = pixel.getB();

                // Случайный шум
                int noise = (int) (Math.random() * 50);
                int agedR = r + noise;
                int agedG = g + noise;
                int agedB = b - noise;

                // Ограничиваем значения до 0-255
                agedR = Math.min(Math.max(agedR, 0), 255);
                agedG = Math.min(Math.max(agedG, 0), 255);
                agedB = Math.min(Math.max(agedB, 0), 255);

                pixel.setR(agedR);
                pixel.setG(agedG);
                pixel.setB(agedB);

                newImage.setPixels(x, y, pixel);
            }
        }
        return imageService.save(newImage, destinationImage);
    }

    public File negativeImage(File sourceImage, File destinationImage) {
        log.info("Делаю негатив");
        Image image = imageService.parseFileToImage(sourceImage);
        Pixel[][] pixels = image.getPixels();
        String newFileName = "negative_" + sourceImage.getName();
        Image newImage = new Image(image.getWidth(), image.getHeight(), newFileName, Path.of("/"), pixels);

        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[x].length; y++) {
                Pixel pixel = pixels[x][y];

                int r = pixel.getR();
                int g = pixel.getG();
                int b = pixel.getB();

                int negativeR = 255 - r;
                int negativeG = 255 - g;
                int negativeB = 255 - b;

                pixel.setR(negativeR);
                pixel.setG(negativeG);
                pixel.setB(negativeB);

                newImage.setPixels(x, y, pixel);
            }
        }
        return imageService.save(newImage, destinationImage);
    }

    public File blurImage(File sourceImage, File destinationImage) {
        log.info("Делаю эффект замыливания");
        Image image = imageService.parseFileToImage(sourceImage);
        Pixel[][] pixels = image.getPixels();
        String newFileName = "blur_" + sourceImage.getName() + ".jpg";

        Pixel[][] blurredPixels = new Pixel[image.getWidth()][image.getHeight()];

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int sumR = 0, sumG = 0, sumB = 0;
                int count = 0;

                // Суммируем значения пикселей в области 3x3 с проверкой границ
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int nx = x + i;
                        int ny = y + j;

                        // Проверка на границы массива пикселей
                        if (nx >= 0 && nx < image.getWidth() && ny >= 0 && ny < image.getHeight()) {
                            Pixel pixel = pixels[nx][ny];
                            sumR += pixel.getR();
                            sumG += pixel.getG();
                            sumB += pixel.getB();
                            count++;
                        }
                    }
                }

                // Усредняем значения (делим на реальное количество пикселей)
                int avgR = sumR / count;
                int avgG = sumG / count;
                int avgB = sumB / count;

                blurredPixels[x][y] = new Pixel(pixels[x][y].getA(), avgR, avgG, avgB);
            }
        }

        Image newImage = new Image(image.getWidth(), image.getHeight(), newFileName, Path.of("/"), blurredPixels);
        return imageService.save(newImage, destinationImage);
    }
}
