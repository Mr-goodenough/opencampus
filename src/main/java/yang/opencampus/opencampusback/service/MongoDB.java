package yang.opencampus.opencampusback.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Service;

import yang.opencampus.opencampusback.entity.Baseinfo;
import yang.opencampus.opencampusback.entity.Comment;
import yang.opencampus.opencampusback.entity.Question;
import yang.opencampus.opencampusback.entity.UncheckedComment;
import yang.opencampus.opencampusback.entity.UncheckedQuestion;
import yang.opencampus.opencampusback.repository.BaseinfoRepository;
import yang.opencampus.opencampusback.repository.CommentRepository;
import yang.opencampus.opencampusback.repository.MongoDBRepository;
import yang.opencampus.opencampusback.repository.QuestionRepository;
import yang.opencampus.opencampusback.repository.UncheckedCommentRepository;
import yang.opencampus.opencampusback.repository.UncheckedQuestionRepository;

@Service
@EnableMongoRepositories
public class MongoDB {
    private BaseinfoRepository baseinfoRepository;
    private CommentRepository commentRepository;
    private UncheckedCommentRepository uncheckedCommentRepository;
    private UncheckedQuestionRepository uncheckedQuestionRepository;
    private QuestionRepository questionRepository;

    public MongoDB(BaseinfoRepository baseinfoRepository,CommentRepository commentRepository,UncheckedCommentRepository uncheckedCommentRepository,UncheckedQuestionRepository uncheckedQuestionRepository,QuestionRepository questionRepository){
        this.baseinfoRepository = baseinfoRepository;
        this.commentRepository=commentRepository;
        this.uncheckedCommentRepository=uncheckedCommentRepository;
        this.uncheckedQuestionRepository=uncheckedQuestionRepository;
        this.questionRepository=questionRepository;
    }
    
    public Baseinfo getBaseinfoByTeacherID(int id){
        return baseinfoRepository.getBaseinfoByTeacherID(id);
    }

    @Autowired
    private MongoDBRepository mongoDBRepository;
    public void updateComment(boolean isAgree,String commentID){
        mongoDBRepository.updateComment(isAgree, commentID);
    }
    public List<Comment> findCommentsByAgreeNum(int teacherID,int n,int m){
        return mongoDBRepository.findCommentsByAgreeNum(teacherID, n, m);
    }
    public List<Baseinfo> selectTeacherName(String teacherName){
        if(!teacherName.equals(""))
        return mongoDBRepository.findByNameContaining(teacherName);
        else{
            List<Baseinfo> nulllist =new ArrayList<Baseinfo>();
            return nulllist;
        }
    }
    public List<Baseinfo> deptAndSelectTeacherName(String dept,String teacherName){
        if(!teacherName.equals(""))
        return mongoDBRepository.findByNameContainingAndDept(dept,teacherName);
        else{
            List<Baseinfo> nulllist =new ArrayList<Baseinfo>();
            return nulllist;
        }
    }
    public void addComment(int teacherid, String userEmail,String className, String nickname, int EZtoPass, int EZtoHighScore, int useful,
    boolean willCheck, int recommend, String others){
        Comment newComment=new Comment(teacherid,userEmail,className,nickname,EZtoPass,EZtoHighScore,useful,willCheck,recommend,others);
        commentRepository.save(newComment);
    }
    public void addUncheckedComment(int teacherid, String userEmail,String className, String nickname, int EZtoPass, int EZtoHighScore, int useful,
    boolean willCheck, int recommend, String others){
        UncheckedComment newComment=new UncheckedComment(teacherid,userEmail,className,nickname,EZtoPass,EZtoHighScore,useful,willCheck,recommend,others);
        uncheckedCommentRepository.save(newComment);
    }
    public void addUncheckedQuestion(int teacherID,String email,String className,String nickname,String question){
        UncheckedQuestion newQuestion=new UncheckedQuestion(teacherID,email,className,nickname,question);
        uncheckedQuestionRepository.save(newQuestion);
    }
    public void putUncheckedQuestionToQuestion(String QuestionID){
        System.out.println(QuestionID);
        UncheckedQuestion passedQuestion=uncheckedQuestionRepository.findBy_id(QuestionID);
        Question question=new Question(passedQuestion.getTeacherID(),passedQuestion.getEmail(),passedQuestion.getClassName(),passedQuestion.getNickname(),passedQuestion.getQuestion());
        questionRepository.save(question);
    }
    public void deleteUncheckedQuestion(String QuestionID){
        UncheckedQuestion needToDelete=uncheckedQuestionRepository.findBy_id(QuestionID);
        if(needToDelete !=null){
        uncheckedQuestionRepository.delete(needToDelete);
        }else{
            System.out.println("已经删除了");
        }
    }
}
