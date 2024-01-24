package Egg.EggLibrary.services;

import Egg.EggLibrary.entities.Image;
import Egg.EggLibrary.entities.UserEntity;
import Egg.EggLibrary.enums.Role;
import Egg.EggLibrary.exceptions.LibraryExceptions;
import Egg.EggLibrary.repositories.ImageRepository;
import Egg.EggLibrary.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServices implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ImageServices ImageServ;

    @Transactional
    public void register(String name, String email, String password, String password2, MultipartFile image) throws LibraryExceptions {

        validate(name, email, password, password2);

        try {
            UserEntity user = new UserEntity();
            user.setName(name);
            String hashed = new BCryptPasswordEncoder().encode(password);
            user.setPassword(hashed);
            user.setEmail(email);
            user.setRole(Role.USER);
            Image img = ImageServ.save(image);
            user.setImage(img);
            userRepo.save(user);

        } catch (Exception e) {
            throw new LibraryExceptions(e.getMessage());
        }

    }

    @Transactional
    public UserEntity getOne(String id) throws Exception {

        Optional<UserEntity> res = userRepo.findById(id);
        if (res.isPresent()) {
            UserEntity user = res.get();
            return user;
        }
        throw new LibraryExceptions("user not found");

    }

    @Transactional
    public void update(String id, String name, String email, String password, String password2, MultipartFile image) throws LibraryExceptions {
        validate(name, email, password, password2);
        Optional<UserEntity> res = userRepo.findById(id);

        if (res.isPresent()) {
            UserEntity u = res.get();
            u.setName(name);
            u.setEmail(email);
            u.setPassword(new BCryptPasswordEncoder().encode(password));
            u.setRole(Role.USER);
            String imgID = null;
            if (u.getImage() != null) {
                imgID = u.getImage().getId();
            }
            Image img = ImageServ.update(image, imgID);
            u.setImage(img);
            userRepo.save(u);

        } else {
            throw new LibraryExceptions("Could't update user data");
        }
    }

    @Transactional
    public List<UserEntity> getAllUsers() {
        try {
            return userRepo.findAll();
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional
    public void changeRole(String id) throws Exception {
        try {
            UserEntity user = getOne(id);
            if(user.getRole().toString().equals("ADMIN")){
                user.setRole(Role.USER);
            }
            else{
                user.setRole(Role.ADMIN);
            }
        } catch (Exception e) {
            e.getMessage();
            throw e;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity u = userRepo.findByEmail(email);

        if (u != null) {
            List<GrantedAuthority> permissions = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + u.getRole().toString());
            permissions.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usersession", u);
            return new User(u.getEmail(), u.getPassword(), permissions);
        } else {
            return null;
        }
    }

    private void validate(String name, String email, String password, String password2) throws LibraryExceptions {
        if (name == null || email == null || password == null || password2 == null
                || name.isEmpty() || email.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            throw new LibraryExceptions("all fields are required");
        }
        if (password.length() < 5) {
            throw new LibraryExceptions("password length must exceed 5 characters");
        }
        if (!password.equals(password2)) {
            throw new LibraryExceptions("password must be the same value in both fields");
        }
    }

}
