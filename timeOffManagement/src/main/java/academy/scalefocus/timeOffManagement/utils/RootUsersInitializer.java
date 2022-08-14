package academy.scalefocus.timeOffManagement.utils;

import academy.scalefocus.timeOffManagement.model.User;
import academy.scalefocus.timeOffManagement.repository.UserRepository;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@ComponentScan
public class RootUsersInitializer implements SmartInitializingSingleton {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void afterSingletonsInstantiated() {
        if(userRepository.findByUsername("admin").isEmpty()){
            createRootAdmin();
        }
        if(userRepository.findByUsername("elPatron").isEmpty()){
            createCeo();
        }
    }

    public void createRootAdmin(){
        User rootAdmin = new User("admin","adminpass","admin","adminsson", null, true, "adming@adminmail.com");
        rootAdmin.setPassword(passwordEncoder.encode(rootAdmin.getPassword()));
        userRepository.save(rootAdmin);
    }

    public void createCeo(){
        User ceo = new User("elPatron","theBossPass","El","Patron",
                null, true, "ceo@gmail.com");
        ceo.setPassword(passwordEncoder.encode(ceo.getPassword()));
        userRepository.save(ceo);
    }
}