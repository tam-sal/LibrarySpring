
package Egg.EggLibrary.controllers;

import Egg.EggLibrary.entities.UserEntity;
import Egg.EggLibrary.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/image")
public class ImageController {
    @Autowired
    UserServices userServ;
    
    @GetMapping("/profile/{id}")
    public ResponseEntity<byte[]> userImage (@PathVariable String id) throws Exception {
        
        UserEntity user = userServ.getOne(id);
        byte[] img = user.getImage().getContent();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        
        return new ResponseEntity<>(img, headers, HttpStatus.OK);
    }
}
