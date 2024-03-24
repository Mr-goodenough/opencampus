package yang.opencampus.opencampusback.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Data;

@Document(collection="question")
@Data
public class Question {
    @Id
    public String _id;
    public int teacherID;
    public String email;
    public String className;
    public String nickname;
    public String question;
    public String teacherName;
    public String department;
    //添加teacherName而不用再次查询是为了减少查询开销
    public Question(int teacherID, String email, String className, String nickname, String question, String teacherName,
            String department) {
        this.teacherID = teacherID;
        this.email = email;
        this.className = className;
        this.nickname = nickname;
        this.question = question;
        this.teacherName = teacherName;
        this.department = department;
    }
}
