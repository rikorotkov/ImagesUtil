package com.floda.home.service;

import com.floda.home.model.Image;

import java.io.File;

public interface ImageService {

    File save(Image image);

    Image parseFileToImage(File file);

}
