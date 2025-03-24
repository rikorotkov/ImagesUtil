package com.floda.home.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pixel {
    private int a;
    private int r;
    private int g;
    private int b;

    public Pixel(int a, int r, int g, int b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }
}
