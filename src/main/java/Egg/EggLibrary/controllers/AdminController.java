
package Egg.EggLibrary.controllers;

import Egg.EggLibrary.entities.UserEntity;
import Egg.EggLibrary.services.UserServices;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserServices userServ;
    
    @GetMapping("/dashboard")
    public String adminPanel(){
        return "panel.html";
    }
    
    @GetMapping("/users")
    public String list(ModelMap model){
        List<UserEntity> users = userServ.getAllUsers();
        model.addAttribute("users", users);
        return "user_list.html";
    }
    
    @GetMapping("/modify_role/{id}")
    public String switchRole(@PathVariable String id)throws Exception {
        userServ.changeRole(id);
        return "redirect:/admin/users";
    }
}
