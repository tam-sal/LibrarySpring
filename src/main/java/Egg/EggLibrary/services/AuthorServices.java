package Egg.EggLibrary.services;

import Egg.EggLibrary.entities.Author;
import Egg.EggLibrary.exceptions.LibraryExceptions;
import Egg.EggLibrary.repositories.AuthorRepository;
import java.util.*;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorServices {

    @Autowired
    private AuthorRepository authorRepo;

    @Transactional
    public void createAuthor(String name) throws Exception {
        validateAuthor(name);
        Author author = new Author();
        author.setName(name);
        try {
            authorRepo.save(author);
        } catch (Exception e) {
            System.out.println("can't persist author: " + e.getMessage());
            throw e;
        }

    }
    
    @Transactional
    public List<Author> displayAuthors() {
        List<Author> authors = new ArrayList();
        try {
            authors = authorRepo.findAll();
            return authors;
        } catch (Exception e) {
            System.out.println("can't get authors from DB: " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void updateAuthor(String authorId, String name) throws Exception {
        validateAuthor(name);
        try {
            Optional<Author> resAuthor = authorRepo.findById(authorId);
            if (resAuthor.isPresent()) {
                Author a = resAuthor.get();
                a.setName(name);
                authorRepo.save(a);
                System.out.println("updated: " + a);
                
            }
        } catch (Exception e) {
            System.out.println("Can't update author: " + e.getMessage());
            throw e;
        }
    }
    
    public Author getOne(String id){
        return authorRepo.getOne(id);
    }
    
    @Transactional
    public void delete(String id){
        try {
            authorRepo.deleteById(id);
            
        } catch (Exception e) {
            System.out.println("Can't delete author: "+e.getMessage());
            throw e;
        }
    }

    private void validateAuthor(String name) throws LibraryExceptions {
        if (name == null || name.isEmpty()) {
            throw new LibraryExceptions("name can't be empty or null");
        }
    }

}
