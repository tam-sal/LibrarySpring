
package Egg.EggLibrary.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    
    @GetMapping("/")
    public String index(){
        return "index.html";
    }
    // Adding a default fallback for any non-matching route
    @RequestMapping("/**")
    public String fallback(){
        return "redirect:/";
    }
}
