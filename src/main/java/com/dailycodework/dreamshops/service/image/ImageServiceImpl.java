package com.dailycodework.dreamshops.service.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dailycodework.dreamshops.dto.ImageDto;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Image;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.ImageRepository;
import com.dailycodework.dreamshops.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
  private final ImageRepository imageRepository;
  private final ProductRepository productRepository;

  @Override
  public Image getImageById(Long id) {
    return imageRepository
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException("Image not found"));
  }

  @Override
  public void deleteImageById(Long id) {
    imageRepository
        .findById(id)
        .ifPresentOrElse(
            imageRepository::delete,
            () -> new ResourceNotFoundException("Image not found"));
  }

  @Override
  public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
    Product product = productRepository
      .findById(productId)
      .orElseThrow(() -> new ResourceNotFoundException("Product Non Existent"));
    List<ImageDto> imageDtos = new ArrayList<>();
    for (MultipartFile file : files) {
      try {
        Image image = new Image();

        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());

        image.setImage(new SerialBlob(file.getBytes()));

        image.setProduct(product);

        // We return an URL instead of the image itself
        image.setDownloadUrl(downloadUrlBuilder(image.getId()));
        Image savedImage = imageRepository.save(image);

        // Making sure the saved image in db have the correct id in the db
        savedImage.setDownloadUrl(downloadUrlBuilder(savedImage.getId()));
        imageRepository.save(savedImage);

        ImageDto imageDto = new ImageDto();
        imageDto.setId(savedImage.getId());
        imageDto.setFileName(savedImage.getFileName());

        imageDto.setDownloadUrl(savedImage.getDownloadUrl());
        imageDtos.add(imageDto);
      } catch (IOException | SQLException e) {
        throw new RuntimeException(e.getMessage());
      }
    }
    return imageDtos;
  }

  private String downloadUrlBuilder(Long imageId) {
    return "/api/v1/images/image/download/" + imageId;
  }

  @Override
  public void updateImage(MultipartFile file, Long imageId) {
    Image image = getImageById(imageId);
    try {
      image.setFileName(file.getOriginalFilename());
      image.setFileType(file.getContentType());
      image.setImage(new SerialBlob(file.getBytes()));
      imageRepository.save(image);
    } catch (IOException | SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
