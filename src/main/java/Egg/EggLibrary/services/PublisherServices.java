package Egg.EggLibrary.services;

import Egg.EggLibrary.entities.Publisher;
import Egg.EggLibrary.exceptions.LibraryExceptions;
import Egg.EggLibrary.repositories.PublisherRepository;
import java.util.*;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
public class PublisherServices {

    @Autowired
    private PublisherRepository publisherRepo;

    @Transactional
    public void createPublisher(String name) throws Exception {
        validatePublisher(name);

        Publisher publisher = new Publisher();
        publisher.setName(name);
        try {
            publisherRepo.save(publisher);
        } catch (Exception e) {
            System.out.println("can't persist author: " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    public List<Publisher> displayPublishers() {

        List<Publisher> publishers = new ArrayList();
        try {
            publishers = publisherRepo.findAll();
            return publishers;
        } catch (Exception e) {
            System.out.println("can't get publishers from DB: " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void updatePublisher(String id, String name) {

        try {
            Optional<Publisher> resPublisher = publisherRepo.findById(id);
            if (resPublisher.isPresent()) {
                Publisher p = resPublisher.get();
                p.setName(name);
                publisherRepo.save(p);
                System.out.println("Updated:"+p);
            }
        } catch (Exception e) {
            System.out.println("can't update publisher: " + e.getMessage());
            throw e;
        }
    }
    
    private void validatePublisher(String name) throws LibraryExceptions{
        if(name == null || name.isEmpty()){
            throw new LibraryExceptions("publish name can't be empty / null");
        }
        
    }
    
    

}
