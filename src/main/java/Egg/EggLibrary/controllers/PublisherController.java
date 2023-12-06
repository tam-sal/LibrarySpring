package Egg.EggLibrary.controllers;

import Egg.EggLibrary.services.PublisherServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/publisher")
public class PublisherController {

    @Autowired
    private PublisherServices publisherServ;

    @GetMapping("/register")
    public String register() {
        return "publisher_form.html";
    }

    @PostMapping("/registry")
    public String registry(@RequestParam String name) {
        try {
            publisherServ.createPublisher(name);
            System.out.println("Created Publisher: " + name);
            return "index.html";
        } catch (Exception e) {
            System.out.println("Error registering publisher: " + e.getMessage());
            return "publisher_form.html";
        }
    }

    @GetMapping("/list")
    public String list(ModelMap model) {
        try {
            model.addAttribute("publishers", publisherServ.displayPublishers());
            model.put("success", "successfully loaded publishers");
        } catch (Exception e) {
            System.out.println("publisher controller list method error:\n" + e.getMessage());
            model.put("error", e.getMessage());

        }
        return "publisher_list.html";

    }

    @GetMapping("/modify/{id}")
    public String modify(@PathVariable String id, ModelMap model) {
        try {
            model.put("publisher", publisherServ.getOne(id));
            return "modified_publisher.html";
        } catch (Exception e) {
            model.put("error", e.getMessage());
            model.put("publisher", publisherServ.getOne(id));
            return "modified_publisher.html";
        }
    }

    @PostMapping("/modify/{id}")
    public String modify(@PathVariable String id, ModelMap model, String name) {
        try {
            publisherServ.updatePublisher(id, name);
            return "redirect:../list";
        } catch (Exception e) {
            model.put("publisher", publisherServ.getOne(id));
            model.put("error", e.getMessage());
            return "modified_publisher.html";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, ModelMap model) {
        model.put("publisher", publisherServ.getOne(id));
        return "deleted_publisher.html";

    }

    @PostMapping("/delete/{id}")
    public String deletePublisher(@PathVariable String id, ModelMap model) {
        try {
            publisherServ.delete(id);
            return "redirect:../list";
        } catch (Exception e) {
            model.put("publisher", publisherServ.getOne(id));
            model.put("error", e.getMessage());
            return "deleted_publisher.html";
        }
    }

}
