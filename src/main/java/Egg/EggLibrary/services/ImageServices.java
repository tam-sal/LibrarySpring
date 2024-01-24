package Egg.EggLibrary.services;

import Egg.EggLibrary.entities.Image;
import Egg.EggLibrary.exceptions.LibraryExceptions;
import Egg.EggLibrary.repositories.ImageRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServices {

    @Autowired
    private ImageRepository imageRepo;

    public Image save(MultipartFile file) throws LibraryExceptions {

        if (file != null) {
            try {
                Image img = new Image();
                img.setName(file.getName());
                img.setMime(file.getContentType());
                img.setContent(file.getBytes());
                return imageRepo.save(img);

            } catch (Exception e) {
                System.err.println(e.getMessage());
                throw new LibraryExceptions("Can't save image");
            }
        }
        return null;
    }

    public Image update(MultipartFile file, String idImage) throws LibraryExceptions {
        if (idImage != null && file != null) {
            try {
                Image img = new Image();
                Optional<Image> res = imageRepo.findById(idImage);
                if (res.isPresent()) {
                    img = res.get();
                    img.setMime(file.getContentType());
                    img.setContent(file.getBytes());
                    img.setName(file.getName());
                    return imageRepo.save(img);
                }

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
}
