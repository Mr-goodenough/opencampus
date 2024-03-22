package yang.opencampus.opencampusback.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import yang.opencampus.opencampusback.entity.Rooter;
import yang.opencampus.opencampusback.repository.RooterRepository;

//这注定成为一个遗憾，我因为时间关系不能写一个完整的登录方式，只能用最粗糙的方式处理审核人员的验证问题
@Service
public class Root {

    private RooterRepository rooterRepository;
    public Root(RooterRepository rooterRepository){
        this.rooterRepository=rooterRepository;
    }

    public boolean login(String rooter,String password){
        Optional<Rooter> userOptional = rooterRepository.findByName(rooter);
        
        // 检查是否找到了用户并验证密码
        if (userOptional.isPresent()) {
            Rooter root = userOptional.get();
            if (root.getPassword().equals(password)) {
                // 登录成功
                System.out.println("登录成功！");
                return true;
            } else {
                // 密码错误
                System.out.println("密码错误！");
                return false;
            }
        } else {
            // 用户不存在
            System.out.println("用户不存在！"+String.valueOf(rooter));
            return false;
        }
    }
}
