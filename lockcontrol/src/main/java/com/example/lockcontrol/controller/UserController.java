package com.example.lockcontrol.controller;

import com.example.lockcontrol.bean.User;
import com.example.lockcontrol.service.GameService;
import com.example.lockcontrol.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author ruiruisa
 * @create 2022-05-17 20:15
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private GameService gameService;

    @GetMapping("/")
    public String handle(HttpSession session, Model model){
        model.addAttribute("msg",session.getAttribute("msg"));
        return "login";
    }

    //登录
    @PostMapping("/userlogin")
    public String register(User user, HttpSession session){
        User loginUser = userService.loginUser(user.getEmail(), user.getPassword());
        if(loginUser != null){
            session.setAttribute("user",loginUser);
            return "redirect:/home";
        }else{
            session.setAttribute("msg","邮箱或密码错误!");
            return "redirect:/login";
        }
    }
    @GetMapping("/home")
    public String home(HttpSession session,Model model){
        //查询时间

        User user = (User) session.getAttribute("user");
        if(gameService.findGame(user.getId()) != null){
            Date date = new Date();
            Date time = userService.getTime(user.getId());
            String timeInterval = getTimeInterval(time, date);
            model.addAttribute("date",timeInterval);
        }else{
            model.addAttribute("date","暂无游戏");
        }


        return "home";
    }
    public static String getTimeInterval(Date currentTime, Date firstTime) {
        // 得到的时间差值, 微秒级别
        long diff = currentTime.getTime() - firstTime.getTime();
        Calendar currentTimes = dataToCalendar(currentTime);//当前系统时间转Calendar类型
        Calendar firstTimes = dataToCalendar(firstTime);//查询的数据时间转Calendar类型
        int year = currentTimes.get(Calendar.YEAR) - firstTimes.get(Calendar.YEAR);//获取年
        int month = currentTimes.get(Calendar.MONTH) - firstTimes.get(Calendar.MONTH);
        int day = currentTimes.get(Calendar.DAY_OF_MONTH) - firstTimes.get(Calendar.DAY_OF_MONTH);
        if (month < 0||(month==0&&year>0)) {
            month = month + 12;//获取月
            year--;
        }
        if (day < 0||(day==0&&month>0)) {
            month -= 1;
            currentTimes.add(Calendar.MONTH, -1);
            day = day + currentTimes.getActualMaximum(Calendar.DAY_OF_MONTH);//获取日
        }

        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);//获取时
        long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);//获取分钟
        long s = (diff / 1000 - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60);//获取秒
        if(day>=1){day--;}
        String CountTime = "" + year + "年" + month + "月" + day + "天 " + hours + "时" + minutes + "分" + s + "秒";
        return CountTime;
    }

    // Date类型转Calendar类型
    public static Calendar dataToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    //注册
    @GetMapping("/register")
    public String register(){
        return "register";
    }
    @PostMapping("/register")
    public String postregister(User user){
        userService.register(user);
        return "login";
    }


}
