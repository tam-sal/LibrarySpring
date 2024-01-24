package Egg.EggLibrary.controllers;

import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrController implements ErrorController {

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView renderErrorPage(HttpServletRequest httpReq) {

        ModelAndView errorPage = new ModelAndView("error");
        String message = "";
        int code = getErrorCode(httpReq);

        switch (code) {
            case 400: {
                message = "Page doesn't exist";
                break;
            }
            case 403: {
                message = "Access denied. Only users with permissions";
                break;
            }
            case 401: {
                message = "Not authorized";
                break;
            }
            case 404: {
                message = "Not found";
                break;
            }
            case 500: {
                message = "Server Error";
                break;
            }
        }
        errorPage.addObject("code", code);
        errorPage.addObject("message", message);
        return errorPage;
    }

    private int getErrorCode(HttpServletRequest httpReq) {
        String statusCode = "javax.servlet.error.status_code";
        return (Integer) httpReq.getAttribute(statusCode);
    }
    
    public String getErrorPath() {
        return "/error.html";
    }
}
