package Egg.EggLibrary.controllers;

import Egg.EggLibrary.services.AuthorServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private AuthorServices authorServ;

    @GetMapping("/register")
    public String register() {
        return "author_form.html";
    }

    @PostMapping("/registry")
    public String registery(@RequestParam String name, ModelMap model) {

        try {
            authorServ.createAuthor(name);
            model.put("success", "author successfully created");
        } catch (Exception ex) {
            model.put("error", ex.getMessage());
            System.out.println(ex.getMessage());
            return "author_form.html";
        }
        return "index.html";
    }

    @GetMapping("/list")
    public String list(ModelMap model) {
        try {
            model.addAttribute("authors", authorServ.displayAuthors());
            model.put("success", "sucessfully loaded authors");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.put("error", e.getMessage());
        }
        return "author_list.html";
    }

    @GetMapping("/modify/{id}")
    public String modify(@PathVariable String id, ModelMap model) {
        model.put("author", authorServ.getOne(id));
        return "modified_author.html";
    }

    @PostMapping("/modify/{id}")
    public String modify(@PathVariable String id, String name, ModelMap model) throws Exception {
        try {
            authorServ.updateAuthor(id, name);
            return "redirect:../list";

        } catch (Exception e) {
            model.put("author", authorServ.getOne(id));
            model.put("error", e.getMessage());
            return "modified_author.html";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, ModelMap model) {
        model.put("author", authorServ.getOne(id));
        return "deleted_author.html";
    }

    @PostMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable String id, ModelMap model) {
        try {
            authorServ.delete(id);
            System.out.println("Author deleted successfully");
            return "redirect:../list";
        } catch (Exception e) {
            model.put("author", authorServ.getOne(id));
            model.put("error", e.getMessage());
            return "modified_author.html";
        }
    }

}
