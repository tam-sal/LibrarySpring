package Egg.EggLibrary.controllers;

import Egg.EggLibrary.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookServices bookServ;
    @Autowired
    private AuthorServices authorServ;
    @Autowired
    private PublisherServices publisherServ;

    @GetMapping("/register")
    public String register(ModelMap model) {
        model.addAttribute("authors", authorServ.displayAuthors());
        model.addAttribute("publishers", publisherServ.displayPublishers());
        return "book_form.html";
    }

    @PostMapping("/registry")
    public String registry(@RequestParam(required = false) Long isbn, @RequestParam String title,
            @RequestParam(required = false) Integer copies, @RequestParam String authorId,
            @RequestParam String publisherId, ModelMap model) throws Exception {
        try {
            bookServ.createBook(isbn, title, copies, authorId, publisherId);
            model.put("success", "Book has been successfully created");

        } catch (Exception e) {
            System.out.println("Book registry err: " + e.getMessage());
            model.put("error", e.getMessage());
            model.addAttribute("authors", authorServ.displayAuthors());
            model.addAttribute("publishers", publisherServ.displayPublishers());
            return "book_form.html";
        }
        return "index.html";
    }

    @GetMapping("/list")
    public String list(ModelMap model) {
        try {
            model.addAttribute("books", bookServ.displayBooks());
            model.put("success", "sucessfully loaded books");
            return "book_list.html";
        } catch (Exception e) {
            model.put("error", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/modify/{isbn}")
    public String update(@PathVariable Long isbn, ModelMap model) {

        model.put("book", bookServ.getOne(isbn));
        model.addAttribute("authors", authorServ.displayAuthors());
        model.addAttribute("publishers", publisherServ.displayPublishers());

        return "modified_book.html";

    }

    @PostMapping("/modify/{isbn}")
    public String update(@PathVariable Long isbn, String title, Integer copies, String authorId, String publisherId, ModelMap model) {
        try {
            model.addAttribute("authors", authorServ.displayAuthors());
            model.addAttribute("publishers", publisherServ.displayPublishers());
            bookServ.updateBookById(isbn, title, copies, authorId, publisherId);
            return "redirect:../list";
        } catch (Exception e) {
            model.put("book", bookServ.getOne(isbn));
            model.put("error", e.getMessage());
            model.addAttribute("authors", authorServ.displayAuthors());
            model.addAttribute("publishers", publisherServ.displayPublishers());
            return "modified_book.html";
        }
    }

    @GetMapping("/delete/{isbn}")
    public String delete(@PathVariable Long isbn, ModelMap model) {
        model.put("book", bookServ.getOne(isbn));
        return "deleted_book.html";
    }

    @PostMapping("/delete/{isbn}")
    public String deleteBook(@PathVariable Long isbn, ModelMap model) {
        try {
            bookServ.deleteBook(isbn);
            return "redirect:../list";
        } catch (Exception e) {
            model.put("book", bookServ.getOne(isbn));
            model.put("error", e.getMessage());
            return "deleted_book.html";
        }
    }

}
