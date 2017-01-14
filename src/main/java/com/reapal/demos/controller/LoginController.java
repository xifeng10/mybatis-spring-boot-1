package com.reapal.demos.controller;

import com.reapal.demos.model.User;
import com.reapal.demos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Created by jack-cooper on 2017/1/14.
 */
@Controller
public class LoginController extends BaseController{

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public ModelAndView login() {
        // 1、收集参数、验证参数
        // 2、绑定参数到命令对象
        // 3、将命令对象传入业务对象进行业务处理
        // 4、选择下一个页面
        ModelAndView mv = new ModelAndView();
        // 添加模型数据 可以是任意的POJO对象
        mv.addObject("message", "welcome login repal.com");
        // 设置逻辑视图名，视图解析器会根据该名字解析到具体的视图页面
        mv.setViewName("login");
        return mv;
    }

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
        return mv;
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ModelAndView signIn(User user, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView();
        List<User> users = userService.queryListByWhere(user);
        if(users.size() == 1){
            users.get(0).setPassword(null);
            session.setAttribute("user",users.get(0));
            redirectAttributes.addFlashAttribute("message", "welcome to reapal");
            mv.setViewName("redirect:/index");
        }else{
            redirectAttributes.addFlashAttribute("message", "login error");
            mv.setViewName("redirect:/login");
        }
        return mv;
    }

    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public ModelAndView register(User user, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("register");
        return mv;
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public ModelAndView signup(User user, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView();
        User u = new User();
        u.setUsername(user.getUsername());
        List<User> users = userService.queryListByWhere(u);
        if(users.size() == 0){
            userService.save(user);
            mv.addObject("message", "register success");
            mv.setViewName("login");
        }else{
            redirectAttributes.addFlashAttribute("message", "username has been used ");
            mv.setViewName("redirect:/register");
        }
        return mv;
    }

    @RequestMapping(value = "/signout",method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView signout() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("message", "signout repal.com");
        mv.setViewName("redirect:/login");
        logger.info("===============退出系统=================");
        session.invalidate();
        return mv;
    }

}
