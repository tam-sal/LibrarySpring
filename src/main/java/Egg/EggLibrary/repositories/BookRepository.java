
package Egg.EggLibrary.repositories;

import Egg.EggLibrary.entities.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{

    
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) = :title")
    public Book findByTitle(@Param("title") String title);
    
    @Query("SELECT b FROM Book b WHERE LOWER(b.author.name) = :name")
    public List<Book> findByAuthor(@Param("name") String name);
}
