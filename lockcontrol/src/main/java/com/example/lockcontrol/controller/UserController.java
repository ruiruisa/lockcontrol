package com.example.lockcontrol.controller;

import com.example.lockcontrol.bean.Friend;
import com.example.lockcontrol.bean.Game;
import com.example.lockcontrol.bean.MasterTasks;
import com.example.lockcontrol.bean.User;
import com.example.lockcontrol.service.GameService;
import com.example.lockcontrol.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
    public String handle(HttpSession session, Model model) {
        model.addAttribute("msg", session.getAttribute("msg"));
        return "login";
    }

    //登录
    @PostMapping("/userlogin")
    public String register(User user, HttpSession session, Model model, Integer attribute) {
        User loginUser = userService.loginUser(user.getEmail(), user.getPassword());
        if (loginUser != null) {
            switch (loginUser.getGrade()) {
                case 0:
                    session.setAttribute("user", loginUser);
                    return "redirect:/home";
                case 1:
                    session.setAttribute("user", loginUser);
                    return "redirect:/masterhome";
            }
        } else {
            model.addAttribute("msg", "邮箱或密码错误!");
            return "login";
        }
        return "login";
    }

    @GetMapping("/masterhome")
    public String masterHome(Model model){
        List<Game> telGame = gameService.findTelGame();
        List<MasterTasks> masterTasks = new LinkedList<>();
        for(Game game : telGame){
            MasterTasks masterTask = new MasterTasks();
            masterTask.setId(game.getId());
            User user = userService.findUser(game.getUserId());
            masterTask.setUserName(user.getName());
            if(game.getTasksuccess() == 0){
                masterTask.setStatus("未完成");
            }else {
                masterTask.setStatus("已完成");
            }
            masterTasks.add(masterTask);
        }
        model.addAttribute("game",masterTasks);
        return "masterhome";
    }

    @GetMapping("/home")
    public String home(HttpSession session,Model model){
        //查询时间

        User user = (User) session.getAttribute("user");
        Game game = gameService.findGameUser(user.getId());
        if(game != null){
            switch(game.getGameType()){
                case 0:
                    Date time = userService.getTime(user.getId());

                    model.addAttribute("date",time);
                    model.addAttribute("type",time);

                    break;
                case 1:
                    User master = userService.findUser(game.getMasterId());
                    model.addAttribute("date","由" + master.getName() + "控制中");
                    model.addAttribute("type","master");
                    break;
            }
        }else{
            model.addAttribute("date","暂无游戏");
            model.addAttribute("type",null);
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
        String CountTime = "" + year + "," + month + "," + day + ", " + hours + "," + minutes + "," + s + ",";
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
    public String postregister(User user,Model model){
        User user1 = userService.checkEmail(user.getEmail());
        if(user1 != null){
            model.addAttribute("msg","此邮箱已被注册!");
            return "login";
        }else{
            userService.register(user);
            return "login";
        }
    }
    //好友
    @GetMapping("/friend")
    public String friend(HttpSession session,Model model) {
        User user = (User) session.getAttribute("user");
        List<Friend> friends = userService.getFriend(user.getId());
        if (friends != null) {
            List<Friend> yesFriend = new LinkedList<>();
            List<Friend> noFriend = new LinkedList<>();
            for (Friend friend : friends) {
                if (friend.getYesorno() == 0) {
                    if (friend.getFriendId() == user.getId()) {
                        noFriend.add(friend);
                    }
                } else {
                    yesFriend.add(friend);
                }
            }

                model.addAttribute("num", noFriend.size());
                model.addAttribute("yesfriends", yesFriend);
                model.addAttribute("nofriends", noFriend);
                return "friend";
            }
            return "friend";
        }

    @PostMapping("/findfriend")
    public String findFriend(String name,Model model){
        List<User> user = userService.getUser(name);
        model.addAttribute("user",user);
        return "findfriend";
    }

    @GetMapping("/addfriend")
    public String addFriend(HttpSession session,Integer id){
        User user = (User) session.getAttribute("user");
        if(user.getId() != id){
            User friendUser = userService.findUser(id);
            Friend friend = new Friend();
            friend.setUserId(user.getId());
            friend.setFriendId(id);
            friend.setFriendName(friendUser.getName());
            userService.addFriend(friend);
        }

        return "redirect:/friend";
    }

    @GetMapping("/yesfriends")
    public String yesFriends(Integer userId,Integer friendId){
        userService.yesFriends(userId,friendId);
        return "redirect:/friend";
    }
    @GetMapping("/nofriends")
    public String noFriends(Integer userId,Integer friendId){
        userService.noFriends(userId, friendId);
        return "redirect:/friend";
    }

    @GetMapping("/delfriend")
    public String delFriend(HttpSession session,Integer delfriend){
        User user = (User) session.getAttribute("user");
        userService.noFriends(user.getId(),delfriend);
        return "redirect:/friend";
    }


}
