package com.dailycodework.dreamshops.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dailycodework.dreamshops.dto.ImageDto;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Image;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.image.ImageService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/images")
public class ImageController {
  private final ImageService imageService;

  @PostMapping("/upload")
  public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {
    try {
      List<ImageDto> imageDtos = imageService.saveImages(files, productId);
      return ResponseEntity.ok(new ApiResponse("Upload success!", imageDtos));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Upload Failed!", e.getMessage()));
    }
  }

  @PutMapping("/{imageId}")
  public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {
    try {
      Image image = imageService.getImageById(imageId);
      if (image != null) {
        return ResponseEntity.ok(new ApiResponse("Update success", null));
      }
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
    return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Update failed", HttpStatus.INTERNAL_SERVER_ERROR));
  }

  @GetMapping("/{imageId}/download")
  public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
    Image image = imageService.getImageById(imageId);
    ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
    return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename=\"" + image.getFileName() + "\"")
        .body(resource);
  }

  @DeleteMapping("/{imageId}/delete")
  public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
    try {
      Image image = imageService.getImageById(imageId);
      if (image != null) {
        imageService.deleteImageById(imageId);
        return ResponseEntity.ok(new ApiResponse("Delete success", null));
      }
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
    return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Update failed", INTERNAL_SERVER_ERROR));
  }

}
