package yang.opencampus.opencampusback.entity;

import org.springframework.data.mongodb.core.annotation.Collation;

import jakarta.persistence.Id;
import lombok.Data;

@Collation("uncheckedQuestions")
@Data
public class UncheckedQuestion {

    
    public UncheckedQuestion(int teacherID, String email, String className, String nickname, String question,
            String teacherName, String department) {
        this.teacherID = teacherID;
        this.email = email;
        this.className = className;
        this.nickname = nickname;
        this.question = question;
        this.teacherName = teacherName;
        this.department = department;
    }
    @Id
    public String _id;
    public int teacherID;
    public String email;
    public String className;
    public String nickname;
    public String question;
    public String teacherName;
    public String department;

}
