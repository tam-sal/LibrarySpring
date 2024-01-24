package Egg.EggLibrary.controllers;

import Egg.EggLibrary.entities.UserEntity;
import Egg.EggLibrary.exceptions.LibraryExceptions;
import Egg.EggLibrary.services.UserServices;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private UserServices userservices;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/register")
    public String register() {
        return "register.html";
    }

    @PostMapping("/register")
    public String registry(@RequestParam String name,
            @RequestParam String email, @RequestParam MultipartFile image, @RequestParam String password,
            @RequestParam String password2, ModelMap model) {
        try {
            userservices.register(name, email, password, password2, image);
            model.put("success", "user successfully registered");
            return "index.html";
        } catch (LibraryExceptions ex) {
            model.put("error", ex.getMessage());
            model.put("name", name);
            model.put("email", email);
            return "register.html";
        }
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap model) {

        if (error != null) {
            model.put("error", "Incorrect email or password");
        }

        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/start")
    public String start(HttpSession session) {
        UserEntity logged = (UserEntity) session.getAttribute("usersession");
        if (logged.getRole().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        return "start.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/profile")
    public String profile(ModelMap model, HttpSession session) {
        UserEntity u = (UserEntity) session.getAttribute("usersession");
        model.put("user", u);
        return "modified_user.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/profile/{id}")
    public String update(
            @PathVariable String id,
            @RequestParam String name,
            @RequestParam String email,
            MultipartFile image,
            @RequestParam String password,
            @RequestParam String password2,
            ModelMap model
    ) {

        try {
            userservices.update(id, name, email, password, password2, image);
            model.put("success", "successfully updated " + name + "'s profile!");
            return "start.html";
        } catch (LibraryExceptions e) {
            model.put("error", e.getMessage());
            model.put("name", name);
            model.put("email", email);
            return "redirect:/profile";
        }
    }

    // Adding a default fallback for any non-matching route
//    @RequestMapping("/**")
//    public String fallback() {
//        return "redirect:/";
//    }
}
