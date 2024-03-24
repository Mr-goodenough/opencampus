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
    public void addUncheckedQuestion(int teacherID,String email,String className,String nickname,String question,String teacherName,String department){
        UncheckedQuestion newQuestion=new UncheckedQuestion(teacherID,email,className,nickname,question,teacherName,department);
        uncheckedQuestionRepository.save(newQuestion);
    }
    public void putUncheckedQuestionToQuestion(String QuestionID){
        UncheckedQuestion passedQuestion=uncheckedQuestionRepository.findBy_id(QuestionID);
        Question question=new Question(passedQuestion.getTeacherID(),passedQuestion.getEmail(),passedQuestion.getClassName(),passedQuestion.getNickname(),passedQuestion.getQuestion(),passedQuestion.getTeacherName(),passedQuestion.getDepartment());
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
    public void deleteUncheckComment(String CommentID)
    {
        //值得注意的是，因为审核的存在，会出现同时一个问题被两个解答的情况
        //这个时候查找questionID的时候会出现找不到的情况，即被删除了
        UncheckedComment needToDelete=uncheckedCommentRepository.findBy_id(CommentID);
        if(needToDelete !=null){
        uncheckedCommentRepository.delete(needToDelete);
        }else{
            System.out.println("已经删除了");
        }
    }   //删除没有通过审核的评论
    public void putUnckeckCommentInComment(String  commentID){
        UncheckedComment uncheckedComment=uncheckedCommentRepository.findBy_id(commentID);
        uncheckedCommentRepository.deleteBy_id(commentID);
        Comment comment=new Comment(
            uncheckedComment.getTeacherID(),
            uncheckedComment.getUserEmail(),
            uncheckedComment.getClassName(),
            uncheckedComment.getNickname(),
            uncheckedComment.getEZtoPass(),   // 修改此处，假设 getComment() 方法改为 getEZtoPass() 返回类型为 int
            uncheckedComment.getEZtoHighScore(),
            uncheckedComment.getUseful(),
            uncheckedComment.isWillCheck(),   // 修改此处，假设 isWillCheck() 方法返回类型为 boolean
            uncheckedComment.getRecommend(),
            uncheckedComment.getOthers()
        );
        commentRepository.save(comment);
    }//将通过审核的comment保存
    public void QuestionPlusUnckeckedAnswer(String QuestionID,int teacherid, String userEmail,String className, String nickname, int EZtoPass, int EZtoHighScore, int useful,
    boolean willCheck, int recommend, String others)
   {
        Question question=questionRepository.findBy_id(QuestionID);
        UncheckedComment answer=new UncheckedComment(teacherid,userEmail,className,nickname,EZtoPass,EZtoHighScore,useful,willCheck,recommend,others);
        answer.setOthers(question.getNickname()+"问如下问题："+question.getQuestion()+"    收到如下解答"+answer.getOthers());
        uncheckedCommentRepository.save(answer);
    }//将问题和回答合并并保存到unchecked中
    public List<Question> getQuestion(int max,int min){
        return mongoDBRepository.findQuestionBy_id(max, min);
    }
    public List<UncheckedComment> getUncheckedComment(int max,int min){
        return mongoDBRepository.findUncheckedCommentBy_id(max, min);
    }
    public List<UncheckedQuestion> getUncheckedQuestion(int max,int min){
        return mongoDBRepository.findUncheckedQuestionBy_id(max, min);
    }
}
