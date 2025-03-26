package com.floda.home.service;

import com.floda.home.model.Image;
import com.floda.home.model.Pixel;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ImageServiceImpl implements ImageService {

    List<Image> images = new ArrayList<>();

    @Override
    public File save(Image image, File outputFile) {
        BufferedImage imageModelToFile = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        Pixel[][] pixels = image.getPixels();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Pixel pixel = pixels[x][y];
                int argb = (pixel.getA() << 24) | (pixel.getR() << 16) | (pixel.getG() << 8) | pixel.getB();
                imageModelToFile.setRGB(x, y, argb);
            }
        }

        try {
            ImageIO.write(imageModelToFile, "jpg", outputFile);
            return outputFile;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сохранения файла", e);
        }
    }

    @Override
    public Image parseFileToImage(File file) {
        try {
            BufferedImage image = ImageIO.read(file);

            int width = image.getWidth();
            int height = image.getHeight();

            Image parseJPGToModel = new Image(width, height, file.getName(), Path.of(file.getPath()), new Pixel[width][height]);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int pixel = image.getRGB(x, y);

                    int a = (pixel >> 24) & 0xff;
                    int r = (pixel >> 16) & 0xff;
                    int g = (pixel >> 8) & 0xff;
                    int b = (pixel) & 0xff;

                    parseJPGToModel.setPixels(x, y, new Pixel(a, r, g, b));
                }
            }
            log.info("Перевожу изображение из файла в модель");
            images.add(parseJPGToModel);
            return parseJPGToModel;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
