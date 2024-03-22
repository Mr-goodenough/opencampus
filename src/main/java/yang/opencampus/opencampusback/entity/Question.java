package yang.opencampus.opencampusback.entity;

import org.springframework.data.mongodb.core.annotation.Collation;

import jakarta.persistence.Id;
import lombok.Data;

@Collation("questions")
@Data
public class Question {
    @Id
    public String _id;
    public int teacherID;
    public String email;
    public String className;
    public String nickname;
    public String question;
    public Question(int teacherID, String email, String className, String nickname, String question) {
        this.teacherID = teacherID;
        this.email = email;
        this.className = className;
        this.nickname = nickname;
        this.question = question;
    }
}
