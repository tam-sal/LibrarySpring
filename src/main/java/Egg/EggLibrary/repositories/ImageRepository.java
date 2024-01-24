
package Egg.EggLibrary.repositories;

import Egg.EggLibrary.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ImageRepository extends JpaRepository<Image, String>{
    
}
