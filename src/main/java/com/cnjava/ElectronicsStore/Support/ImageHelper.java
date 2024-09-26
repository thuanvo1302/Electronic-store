package com.cnjava.ElectronicsStore.Support;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
public class ImageHelper {

	public String addImage(String imageUrls, List<MultipartFile> imageFiles) throws Exception {
        for (MultipartFile multipartFile : imageFiles) {
            if (!multipartFile.isEmpty()) {
                String originalFileName = multipartFile.getOriginalFilename();
                Path uploadPath = Paths.get(
                		ResourceUtils
						.getFile("classpath:" + "static/uploads/")
						.getAbsolutePath()
						+ File.separator + originalFileName
        		);

                if (!Files.exists(uploadPath))
                	Files.write(uploadPath, multipartFile.getBytes());
                else
                	FileCopyUtils.copy(multipartFile.getBytes(), uploadPath.toFile());

                String imageUrl = ";/uploads/" + originalFileName;
                imageUrls += imageUrl;
            }
        }
        return imageUrls;
	}
	public String removeImages (List<String> deleteImages, String imageUrls) {
		if (deleteImages != null && !deleteImages.isEmpty()) {
        	String[] imageUrlsArray = imageUrls.split(";");
            List<String> updatedImageUrls = new ArrayList<>();
            
            for (String url : imageUrlsArray) {
                if (!deleteImages.contains(url)) {
                    updatedImageUrls.add(url);
                }
            }
            imageUrls = String.join(";", updatedImageUrls);
            return imageUrls;
        } else return "No Images In to-Delete List";
	}
}




















