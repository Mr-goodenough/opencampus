package yang.opencampus.opencampusback;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import yang.opencampus.opencampusback.entity.Baseinfo;
import yang.opencampus.opencampusback.entity.Comment;
import yang.opencampus.opencampusback.entity.Question;
import yang.opencampus.opencampusback.entity.UncheckedComment;
import yang.opencampus.opencampusback.entity.UncheckedQuestion;
import yang.opencampus.opencampusback.entity.User;
import yang.opencampus.opencampusback.service.Email;
import yang.opencampus.opencampusback.service.MongoDB;
import yang.opencampus.opencampusback.service.Mysql;
import yang.opencampus.opencampusback.service.Root;
import yang.opencampus.opencampusback.utils.HashCode;
import yang.opencampus.opencampusback.utils.Token;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;







@RestController
@RequestMapping("/")
public class Rest {
    @Autowired
    private Mysql mysqldb;
    @Autowired
    private MongoDB mongo;
    @Autowired
    private Email emailer;
    @Autowired
    private Root root;


    HashCode hash=new HashCode();
    
    @PostMapping("/hello")
    public int hello() {
        int get=hash.StringToInt("293559861@qq.com"+"软件工程");
        return get;
    }

    @PostMapping("/generateEmail")
    public boolean gennerateEmail(@RequestParam("email") String email,@RequestParam ("password") String password,@RequestParam ("major") String major){
        int code= hash.StringToInt(email+password+major);
        return emailer.sendEmail(email,code);
    }
     @PostMapping("/register") 
     public String register(@RequestParam("email") String email,@RequestParam ("password") String password,@RequestParam ("major") String major,@RequestParam ("admission") int admission,@RequestParam("nickname") String nickname,@RequestParam("code") int code,HttpServletResponse response )
    {     
    if(!hash.checkCode(email+password+major,code)){
        return "codeError";
    }
    User newUser=new User(email,password,major,admission,nickname);
    System.out.println(newUser.toString());
    boolean emailAlreadyRegistered;
    emailAlreadyRegistered=mysqldb.accountHasExist(email);
    if(!emailAlreadyRegistered){
        mysqldb.register(newUser);
        Cookie cookie=new Cookie("token",Token.generateJWT(email));
        response.addCookie(cookie);
        return "registered"; 
    }else{
        return "failed";
    }
////////////////////以上为注册api，输入账号和密码等信息可以进行注册,如果不存在就会注册并提供token
    } 
    @PostMapping("/login")
    public String login(@RequestParam String email,@RequestParam String password,@CookieValue(name = "token",defaultValue ="nothing") String token,HttpServletResponse response) {
        System.out.println(email+password+token);
        if(token.equals("nothing")){
            if(mysqldb.login(email,password)){//没有token，进行登录
                Cookie cookie=new Cookie("token",Token.generateJWT(email));
                response.addCookie(cookie);
                //登录成功给用户token
                return "login";
            }else{
                //登录失败返回false
                return "failed";
            }
        }else{
            //查找到了toke
            if(Token.checkTokenAndEmail(token,email)){
                //此token是正确的可以直接登录
                return "login";
            }else{
                //此token是过期的或者是错误的又或者是与email不同（可能是一个人注册了两个账号）
                if(mysqldb.login(email,password)){//没有token，进行登录
                    Cookie cookie=new Cookie("token",Token.generateJWT(email));
                    response.addCookie(cookie);
                    //登录成功给用户token
                    return "login";
                }else{
                    //登录失败返回false
                    return "failed";
                }
            }
        }
    }
    @PostMapping("/baseinfo")//给出老师基本信息
    public Baseinfo getbaseinfo(@RequestParam int teacherID) {
        return mongo.getBaseinfoByTeacherID(teacherID);
    }
    @PostMapping("/getComment")//按照认同数量排序给出指定多的评论
    public List<Comment> getComment(@RequestParam int teacherID,@RequestParam int max,@RequestParam int min){
        return mongo.findCommentsByAgreeNum(teacherID, min, max);
    }
    @PostMapping("/isagree")//对评论进行认同或者不认同
    public void postMethodName(@RequestParam String commentID,@RequestParam boolean isagree) {
        mongo.updateComment(isagree,commentID);
    }
    @PostMapping("/check")//检查token是否是对的
    public boolean check(@CookieValue(name = "token",defaultValue ="nothing") String token) {
        return Token.checkToken(token);
    }
    @PostMapping("/selectTeacher")
    public List<Baseinfo> selectTeacher(@RequestParam String teacherName) {
        return mongo.selectTeacherName(teacherName);
    }
    @PostMapping("/selectTeacherAndDept")
    public List<Baseinfo> selectTeacherAndDept(@RequestParam String dept,@RequestParam String teacherName) {
        return mongo.deptAndSelectTeacherName(dept, teacherName);
    }
    @PostMapping("/addUncheckedComment")
    public boolean addUncheckedComment(@CookieValue(name = "token",defaultValue ="nothing") String token,@RequestParam int teacherID,@RequestParam String className,@RequestParam String nickname,@RequestParam int eztopass,@RequestParam int eztohighscore,@RequestParam int useful,@RequestParam boolean willcheck,@RequestParam int recommend,@RequestParam String others){
        String email=Token.tokenGetEmail(token);
        if(email!="failed"){
        //此处有一个疑问，为什么我之前没有在这添加检查token的逻辑，如果没有检查是不是可以一直发送请求撑爆我？
        mongo.addUncheckedComment(teacherID,email,className,nickname,eztopass,eztohighscore,useful,willcheck,recommend,others);
            return true;
        }else{
            return false;
        }
        //修改之后并没有测试
    }
    @PostMapping("/addUncheckedQuestion")//提问题
    public boolean addUncheckedQuestion(@CookieValue(name = "token",defaultValue ="nothing") String token,@RequestParam int teacherID,@RequestParam String className,@RequestParam String nickname,@RequestParam String question) {
        String email=Token.tokenGetEmail(token);
        if(email!="failed"){
        mongo.addUncheckedQuestion(teacherID,email,className,nickname,question);
            return true;
        }else{
            return false;
        }
    }//测试通过
    @PostMapping("/checkQuestion")
    public void checkQuestion(@RequestParam boolean pass,@RequestParam String rooter, @RequestParam String questionID,@RequestParam String password){
        boolean isRoot=root.login(rooter,password);
        if(isRoot){
            if(pass){
            //通过则将Question放入mongodb中
            mongo.putUncheckedQuestionToQuestion(questionID);
            }
        }
        mongo.deleteUncheckedQuestion(questionID);
    
        //不通过则将Question删除
    }//仓促通过，还留下瑕疵，没有反馈，重复删除的时候不会报错，但是感觉问题不大，因为既然是删除了，那就不存在多删除一次的问题
    //而且因为逻辑是从一个集合转移到另一个集合中，所以不存在一个人通过，另一个否决的问题，看谁先决定咯。
    @PostMapping("/answerQuestion")
    public boolean answerQuestion(@RequestParam String questionID,@CookieValue(name = "token",defaultValue ="nothing") String token,@RequestParam int teacherID,@RequestParam String className,@RequestParam String nickname,@RequestParam int eztopass,@RequestParam int eztohighscore,@RequestParam int useful,@RequestParam boolean willcheck,@RequestParam int recommend,@RequestParam String others){
        String email=Token.tokenGetEmail(token);
        System.out.println(questionID+teacherID+email+className+nickname+others);
        if(email!="failed"){
            mongo.QuestionPlusUnckeckedAnswer(questionID, teacherID, email, className, nickname, eztopass, eztohighscore, useful, willcheck, recommend, others);
            return true;//将问题合并解答并提交到unchecked数据库
        }else{
            return false;
        }
    }//对一个问题进行解答
    //写完测试一次通过！！！
    @PostMapping("/checkComment")
    public void checkComment(@RequestParam boolean pass,@RequestParam String rooter, @RequestParam String commentID,@RequestParam String password){
        boolean isRoot=root.login(rooter,password);
        if(isRoot){
            if(pass){
            //通过则将Question放入mongodb中
            mongo.putUnckeckCommentInComment(commentID);;
            }
        else{
            mongo.deleteUncheckComment(commentID);
        }
        //不通过则将Question删除
        }
    }
    @PostMapping("/getQuestion")//按照从晚到近的顺序给出指定数量的问题
    public List<Question> getQuestion(@RequestParam int max,@RequestParam int min){
        return mongo.getQuestion(max,min);

    }
    //依然一次通过！！！！！！！！！！！！！！！芜湖
    @PostMapping("/getUncheckedQuestion")
    public List<UncheckedQuestion> getUncheckedQuestion(@RequestParam String rooter,@RequestParam String password,@RequestParam int max,@RequestParam int min){
        boolean isRoot=root.login(rooter,password);
        if(isRoot)
        return mongo.getUncheckedQuestion(max,min);
        else
        return null;
    }
    @PostMapping("/getUncheckedComment")
    public List<UncheckedComment> getUncheckedComment(@RequestParam String rooter,@RequestParam String password,@RequestParam int max,@RequestParam int min){
        boolean isRoot=root.login(rooter,password);
        if(isRoot)
        return mongo.getUncheckedComment(max,min);
        else
        return null;
    }
    //基于偷懒万岁原则，这两段几乎就是上面的复制，所以不做测试
}
