package Egg.EggLibrary.services;

import Egg.EggLibrary.entities.UserEntity;
import Egg.EggLibrary.enums.Role;
import Egg.EggLibrary.exceptions.LibraryExceptions;
import Egg.EggLibrary.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
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

@Service
public class UserServices implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Transactional
    public void register(String name, String email, String password, String password2) throws LibraryExceptions {

        validate(name, email, password, password2);

        try {
            UserEntity user = new UserEntity();
            user.setName(name);
            String hashed = new BCryptPasswordEncoder().encode(password);
            user.setPassword(hashed);
            user.setEmail(email);
            user.setRole(Role.USER);
            userRepo.save(user);

        } catch (Exception e) {
            throw new LibraryExceptions(e.getMessage());
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

}
