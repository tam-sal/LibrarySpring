package Egg.EggLibrary.controllers;

import Egg.EggLibrary.services.PublisherServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String list(ModelMap model){
        try {
            model.addAttribute("publishers", publisherServ.displayPublishers());
            model.put("success", "successfully loaded publishers");
        } catch (Exception e) {
            System.out.println("publisher controller list method error:\n"+e.getMessage());
            model.put("error", e.getMessage());
            
        }
        return "publisher_list.html";
        
    }

}
