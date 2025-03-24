package com.floda.home.model;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

@Getter
@Setter
public class Image {
    private int width;
    private int height;
    private Pixel[][] pixels;
    private String fileName;
    private Path path;

    public Image(int width, int height, String fileName, Path path, Pixel[][] pixels) {
        this.width = width;
        this.height = height;
        this.fileName = fileName;
        this.path = path;
        this.pixels = pixels;
    }

    public void setPixels(int x, int y, Pixel pixel) {
        pixels[x][y] = pixel;
    }
}
