package Egg.EggLibrary.entities;

import java.io.Serializable;
import java.time.*;
import javax.persistence.*;

@Entity
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long isbn;

    @Column(unique = true)
    private String title;

    private Integer copies;

    //@Temporal(TemporalType.DATE)
    private LocalDate registeredAt;

    @ManyToOne
    private Author author;

    @ManyToOne
    private Publisher publisher;

    public Book() {
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) throws Exception {
        if (copies < 1) {
            throw new Exception("copies must be positive");
        }
        this.copies = copies;
    }

    public LocalDate getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDate registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "Book{" + "isbn=" + isbn + ", title=" + title + ", copies=" + copies + ", registeredAt=" + registeredAt + ", author=" + author.getName() + ", publisher=" + publisher.getName() + '}';
    }
    
    

    

}
