package com.example.lockcontrol.controller;

import com.example.lockcontrol.bean.*;
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
import java.util.*;

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
        Game game = gameService.findGameUser(user.getId());
        if(game != null){
            switch (game.getGameType()){
                case 0:
                    model.addAttribute("gamemsg","已有游戏，不能继续添加!");
                    //查询时间
                    Date date = new Date();
                    Date time = userService.getTime(user.getId());
                    String timeInterval = getTimeInterval(time, date);
                    model.addAttribute("date",timeInterval);
                    return "home";
                case 1:
                    model.addAttribute("gamemsg","已有游戏，不能继续添加!");
                    return "home";
            }
        }
        List<Friend> friends = userService.getFriend(user.getId());
        if(friends != null){
            List<Friend> yesFriend = new LinkedList<>();
            for(Friend friend : friends)
                if(friend.getYesorno()!=0){
                        yesFriend.add(friend);

                }
            model.addAttribute("yesfriends",yesFriend);
        }
        return "game";
    }

    @PostMapping("/tonewgame")
    public String tonewgame(NewGame newGame, HttpSession session, Model model){
        User user = (User)session.getAttribute("user");
        Game game = new Game();

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
        } else {
            game.setClean(0);
        }
        game.setId(user.getId());
        game.setGameType(0);
        game.setMasterId(0);
        gameService.toNewGame(game);
        session.setAttribute("game", game);
        if (game.getGameType()==0) {
            return "photoset";
        } else {
            return "masterset";
        }
    }

    @PostMapping("/choosemaster")
    public String chooseMaster(Integer gameId,HttpSession session){
        User user = (User)session.getAttribute("user");
        Game game = new Game();
        game.setGameType(1);
        Friend friend = userService.findFriendMessage(gameId);
        if(user.getId() == friend.getUserId()){
            game.setMasterId(friend.getFriendId());
            game.setUserId(user.getId());
        }else {
            game.setMasterId(user.getId());
            game.setUserId(friend.getFriendId());
        }
        game.setClean(0);
        Date date = new Date();
        game.setDatetime(date);
        gameService.masterToNewGame(game);
        session.setAttribute("game", game);
        return "photoset";
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
        Game game = gameService.findGameUser(user.getId());
        if(game != null){
            switch(game.getGameType()){
                case 0:
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
                case 1:
                    if(game.getOpenorclose() == 1){
                        model.addAttribute("gamemsg","主人未解锁!");
                        return "home";
                    }else {
                        Integer photo = gameService.getPhoto(user.getId());
                        model.addAttribute("num",photo);
                        return "lockon";
                    }

            }
        }
        model.addAttribute("gamemsg","暂无游戏!");
        return "home";

    }

    @GetMapping("/delgame")
    public String delGame(HttpSession session,Model model){
        User user = (User)session.getAttribute("user");
        Game game = gameService.findGameUser(user.getId());
        if(game.getMasterId()==1){
            if(game.getOpenorclose()==1){
                model.addAttribute("gamemsg","无法删除!");
                User master = userService.findUser(game.getMasterId());
                model.addAttribute("date","由" + master.getName() + "控制中");
                return "home";
            }else {
                gameService.delGame(user.getId());
                model.addAttribute("date","暂无游戏");
                return "home";
            }
        }
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

    @GetMapping("/withgame")
    public String withGame(HttpSession session,Model model){
        User user = (User)session.getAttribute("user");
        if(gameService.findGameUser(user.getId()) == null){
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

    @GetMapping("/control")
    public String control(HttpSession session,Model model){
        User user = (User)session.getAttribute("user");
        List<Game> masterGame = gameService.findMasterGame(user.getId());
        List<Control> controls = new ArrayList<>();
        for(Game game : masterGame){
            if(game.getOpenorclose()==1){
                Control control = new Control();
                User user1 = userService.findUser(game.getUserId());
                control.setId(user1.getName());
                //查询时间
                Date date = new Date();
                Date time = userService.getNTime(user.getId());
                String timeInterval = getTimeInterval(date, time);

                control.setDate(timeInterval);

                control.setGameId(game.getId());
                controls.add(control);
            }
        }
        model.addAttribute("controls",controls);
        return "control";
    }

    @GetMapping("/masterlockon")
    public String masterLockOn(Integer id){
        userService.masterLockOn(id);
        return "control";
    }

    @GetMapping("/addtask")
    public String addTask(Integer id,HttpSession session,Model model){
        session.setAttribute("gameid",id);
        if(gameService.findGameId(id).getTask() != null){
            model.addAttribute("gamemsg","已有任务不能继续添加!");
            return "home";
        }else {
            model.addAttribute("gamemsg","添加成功!");
            return "addtask";
        }
    }

    @PostMapping("/task")
    public String task(String text,Integer attribute,HttpSession session,Model model){
        User user = (User)session.getAttribute("user");
        Integer gameid = (Integer) session.getAttribute("gameid");
        Game game = gameService.findGameId(gameid);
        if(game != null){
            switch(attribute){
                case 0:
                    gameService.addTask(gameid,text);
                    model.addAttribute("text",text);
                    return "tasklook";
                case 1:
                    gameService.addTask(gameid,text);
                    gameService.setTaskTel(gameid);
                    return "tasklook";
            }
        }
        return "home";
    }

    @GetMapping("/looktask")
    public String lookTask(HttpSession session,Integer id,Model model){
        User user = (User)session.getAttribute("user");
        Game game = gameService.findGameId(id);
        String task = gameService.findTask(game.getId());
        model.addAttribute("text",task);
        if(game.getTasksuccess() == 0){
            model.addAttribute("color","text-danger");
            model.addAttribute("success","未完成!");
        }else {
            model.addAttribute("color","text-success");
            model.addAttribute("success","已完成!");
        }
        return "tasklook";
    }

    @GetMapping("/mytask")
    public String myTask(HttpSession session,Model model){
        User user = (User)session.getAttribute("user");
        Game game = gameService.findGameUser(user.getId());
        model.addAttribute("text",game.getTask());
        if(game.getTasksuccess() == 0){
            model.addAttribute("color","text-danger");
            model.addAttribute("success","未完成!");
        }else {
            model.addAttribute("color","text-success");
            model.addAttribute("success","已完成!");
        }
        return "tasklook";
    }

    @GetMapping("/deltask")
    public String delTask(HttpSession session,Model model){
        User user = (User)session.getAttribute("user");
        Game game = gameService.findGameMaster(user.getId());
        if(game.getUserId() == user.getId()){
            model.addAttribute("gamemsg","任务只能由主人删除!");
            return "home";
        }else {
            gameService.delTask(game.getId());
            return "tasklook";
        }
    }

    @GetMapping("/materyestask")
    public String materYesTask(Integer id){
        gameService.masterToTask(id);
        return "masterhome";
    }
}
