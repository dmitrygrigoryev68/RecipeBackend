package de.cocktail.service;
import de.cocktail.model.Cocktail;
import de.cocktail.repository.CocktailRepository;
import de.exeption.FileStorageException;
import de.exeption.MyFileNotFoundException;
import de.exeption.NotFoundCocktail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

@Service
public class CocktailImageServiceImpl implements CocktailImageService{

    @Autowired
    CocktailRepository cocktailRepository;

        private final Path fileStorageLocation=

         Paths.get("C:\\Users\\black82\\Documents\\GitHub\\Recipe Doker\\RecipeBackend\\src\\main\\resources\\image")
                    .toAbsolutePath().normalize();
        public String storeFile(MultipartFile file) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            try {
                if(fileName.contains("..")) {
                    throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
                }

                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                return fileName;
            } catch (IOException ex) {
                throw new FileStorageException("Could not store file " + fileName + ". Please try again!");
            }
        }

        public Resource loadFileAsResource(String fileName) {
            try {
                Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
                Resource resource = new UrlResource(filePath.toUri());
                if(resource.exists()) {
                    return resource;
                } else {
                    throw new MyFileNotFoundException("File not found " + fileName);
                }
            } catch (MalformedURLException ex) {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        }
        public void setImageToCocktail(Long id,String uri,String fileName) {
            Optional <Cocktail> byId = cocktailRepository.findById(id);
            if (byId.isPresent()) {
                Cocktail cocktail = byId.get();
                cocktail.getImage().setPatch(uri);
                cocktail.getImage().setTitle(fileName);
                cocktailRepository.save(cocktail);
            }
            else throw new NotFoundCocktail("This cocktail does not exist");

        }

    }