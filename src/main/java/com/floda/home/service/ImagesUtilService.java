package com.floda.home.service;

import com.floda.home.model.Image;
import com.floda.home.model.Pixel;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Path;

@Slf4j
public class ImagesUtilService {

    private final ImageService imageService = new ImageServiceImpl();

    public File colorToBW(File sourceImage) {
        log.info("Делаю ЧБ изображение");
        Image image = imageService.parseFileToImage(sourceImage);

        Pixel[][] pixels = image.getPixels();

        Image newImage = new Image(image.getWidth(), image.getHeight(), "new.jpg", Path.of("/"), image.getPixels());

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
        return imageService.save(newImage);
    }

    public void randomColors(Image image) {

    }

}
