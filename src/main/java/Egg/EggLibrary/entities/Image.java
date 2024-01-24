
package Egg.EggLibrary.entities;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Image implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    private String mime;
    private String name;
    
    @Lob @Basic(fetch= FetchType.LAZY)
    private byte[] content;

    public Image() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
    
    
    
}
