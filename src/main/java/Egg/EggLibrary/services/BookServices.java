package Egg.EggLibrary.services;

import Egg.EggLibrary.entities.*;
import Egg.EggLibrary.exceptions.LibraryExceptions;
import Egg.EggLibrary.repositories.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServices {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private PublisherRepository publisherRepository;

    @Transactional
    public void createBook(Long isbn, String title, Integer copies, String authorId, String publisherId) throws Exception {
        validateAttributes(isbn, title, copies, authorId, publisherId);
        try {
            Author author = authorRepository.findById(authorId).get();
            Publisher publisher = publisherRepository.findById(publisherId).get();

            Book book = new Book();
            LocalDate today = LocalDate.now();
            book.setIsbn(isbn);
            book.setTitle(title);
            book.setCopies(copies);
            book.setRegisteredAt(today);
            book.setAuthor(author);
            book.setPublisher(publisher);
            bookRepository.save(book);
        } catch (Exception e) {
            System.out.println("Can't persist book: " + e.getMessage());
            throw e;
        }
    }

    public List<Book> displayBooks() {
        List<Book> books = new ArrayList();
        try {
            books = bookRepository.findAll();
            return books;
        } catch (Exception e) {
            System.out.println("can't get books from DB: " + e.getMessage());
            throw e;
        }
    }

    public void updateBookById(Long isbn, String title, Integer copies, String authorId, String publisherId) throws Exception {
        validateAttributes(isbn, title, copies, authorId, publisherId);
        try {
            Optional<Book> resBook = bookRepository.findById(isbn);
            Optional<Author> resAuthor = authorRepository.findById(authorId);
            Optional<Publisher> resPublisher = publisherRepository.findById(publisherId);

            if (resBook.isPresent() && resAuthor.isPresent() && resPublisher.isPresent()) {
                Author author = resAuthor.get();
                Publisher publisher = resPublisher.get();
                Book book = resBook.get();
                book.setAuthor(author);
                book.setPublisher(publisher);
                book.setCopies(copies);
                book.setTitle(title);
                bookRepository.save(book);
                System.out.println("update: " + book);
            }

        } catch (Exception e) {
            System.out.println("Can't update book: " + e.getMessage());
            throw e;
        }
    }

    private void validateAttributes(Long isbn, String title, Integer copies, String authorId, String publisherId) throws LibraryExceptions {

        if (isbn == null) {
            throw new LibraryExceptions("ISBN can't be null");
        }
        if ( title == null || title.isEmpty()) {
            throw new LibraryExceptions("title can't be empty string or null");
        }
        if (copies < 1 || copies == null) {
            throw new LibraryExceptions("copies must be a positive value");
        }
        if (authorId == null || publisherId == null || authorId.isEmpty() || publisherId.isEmpty() ) {
            throw new LibraryExceptions("id can't be null or empty string");
        }
    }

}
