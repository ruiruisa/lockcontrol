package com.example.lockcontrol.controller;

import com.example.lockcontrol.bean.Game;
import com.example.lockcontrol.bean.NewGame;
import com.example.lockcontrol.bean.User;
import com.example.lockcontrol.service.GameService;
import com.example.lockcontrol.service.UserService;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Calendar;
import java.util.Date;

import static com.example.lockcontrol.controller.UserController.getTimeInterval;

/**
 * @author ruiruisa
 * @create 2022-05-14 19:45
 */
@Controller
public class GameController {
    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;


    @GetMapping("/game")
    public String game(HttpSession session,Model model){
        User user = (User)session.getAttribute("user");
        if(gameService.findGame(user.getId()) != null){
            model.addAttribute("gamemsg","已有游戏，不能继续添加!");
            //查询时间
            Date date = new Date();
            Date time = userService.getTime(user.getId());
            String timeInterval = getTimeInterval(time, date);
            model.addAttribute("date",timeInterval);
            return "home";
        }else {

            return "game";
        }
    }

    @PostMapping("/tonewgame")
    public String tonewgame(NewGame newGame, HttpSession session, Model model){
        User user = (User)session.getAttribute("user");
        Game game = new Game();
        if (("photo").equals(newGame.getType())) {
            Date date = new java.sql.Date(new java.util.Date().getTime());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);//设置起时间
            calendar.add(Calendar.MONTH, newGame.getMonth());
            calendar.add(Calendar.DATE, newGame.getDay());
            calendar.add(Calendar.HOUR, newGame.getHour());
            date = calendar.getTime();
            game.setDatetime(date);
            game.setUserId(user.getId());
            if (("yes").equals(newGame.getClean())) {
                game.setClean(newGame.getCleannum());
            }else{
                game.setClean(0);
            }
            game.setId(user.getId());
            gameService.toNewGame(game);
            session.setAttribute("game", game);
            return "photoset";
        } else {
            return "numset";
        }
    }


    @PostMapping("/photoon")
    public String photoOn(int photo,HttpSession session){
        Game game = (Game) session.getAttribute("game");

            gameService.setPhoto(photo,game.getUserId());

        return "redirect:/home";
    }

    @GetMapping("/gameclean")
    public String clean(HttpSession session,Model model){
        User user = (User)session.getAttribute("user");
        model.addAttribute("cleannum",gameService.findClean(user.getId()));
        return "clean";
    }
    //开始清洁
    @GetMapping ("/startclean")
    public String startClean(HttpSession session,Model model){
        User user = (User)session.getAttribute("user");
        Integer clean = gameService.findClean(user.getId());
        if(clean != 0){
            gameService.cutClean(clean-1,user.getId());
            Integer photo = gameService.getPhoto(user.getId());
            model.addAttribute("num",photo);
            return "cleanon";
        }else {
            model.addAttribute("cleanmsg","暂无清洁次数!");
            return "clean";
        }
    }
    @GetMapping("/stopclean")
    public String stopClean(HttpSession session){
        User user = (User)session.getAttribute("user");
        return "photoset";
    }

    @GetMapping("/lockon")
    public String lockOn(HttpSession session,Model model){
        User user = (User)session.getAttribute("user");
        //查询时间
        Date date = new Date();
        Date time = userService.getTime(user.getId());
        if(date.getTime() >= time.getTime()){
            Integer photo = gameService.getPhoto(user.getId());
            model.addAttribute("num",photo);
            return "lockon";
        }else{
            model.addAttribute("gamemsg","未到解锁时间!");
            //查询时间
            Date date1 = new Date();
            Date time1 = userService.getTime(user.getId());
            String timeInterval = getTimeInterval(time1, date1);
            model.addAttribute("date",timeInterval);
            return "home";
        }

    }

    @GetMapping("/delgame")
    public String delGame(HttpSession session,Model model){
        User user = (User)session.getAttribute("user");
        gameService.delGame(user.getId());
        model.addAttribute("date","暂无游戏");
        return "home";
    }

    @GetMapping("/open")
    public String open(HttpSession session,Model model){
        User user = (User)session.getAttribute("user");
        Integer open = gameService.findOpen(user.getId());
        if(open != 0){
            model.addAttribute("open",open);
            return "ononlock";
        }else {
            model.addAttribute("gamemsg","暂无钥匙!");
            return "home";
        }
    }
    @GetMapping("/buykeys")
    public String buyKeys(){
        return "buykeys";
    }
    @GetMapping("/keylockOn")
    public String keylockOn(HttpSession session,Model model){
        User user = (User)session.getAttribute("user");
        Integer open = gameService.findOpen(user.getId());
        open--;
        gameService.lockOn(open,user.getId());
        Integer photo = gameService.getPhoto(user.getId());
        model.addAttribute("num",photo);
        return "lockon";
    }
    @GetMapping("/friend")
    public String friend(){
        return "friend";
    }

    @GetMapping("/withgame")
    public String withGame(HttpSession session,Model model){
        User user = (User)session.getAttribute("user");
        if(gameService.findGame(user.getId()) == null){
            model.addAttribute("gamemsg","请添加游戏");
            return "home";
        }else {
            model.addAttribute("url","www.lockcontrol.xyz/find" + user.getId());
            return "withgame";
        }
    }

    //其他未登录用户添加时间
    @GetMapping("/find*")
    public String find(HttpServletRequest request,Model model){
        //获取端口号
        String url = String.valueOf(request.getRequestURL());
        String[]  url1 = url.split("find");
        Integer newurl = Integer.valueOf(url1[url1.length-1]);

        //查询时间
        Date date = new Date();
        Date time = userService.getTime(newurl);

        Calendar c = Calendar.getInstance();
        c.setTime(time);
        c.add(Calendar.HOUR_OF_DAY,1);
        Date time1 = c.getTime();

        gameService.addTimeForOtherUsers(time1,newurl);
        //查询时间
        Date newtime = userService.getTime(newurl);
        String timeInterval = getTimeInterval(newtime, date);
        model.addAttribute("date",timeInterval);

        return "find";
    }


}
