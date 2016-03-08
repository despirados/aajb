package aajb;

import aajb.domain.user.User;
import aajb.repository.ParentRepository;
import aajb.domain.school.Parent;
import aajb.domain.user.UserProfileType;
import aajb.repository.UserRepository;
import aajb.service.SecurityService;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by ayed.h on 23/02/2016.
 */
@SpringBootApplication
@ComponentScan("aajb")
public class InitUserDatabaseApp implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private static SecurityService securityService;

    public static void main(String[] args) {
        SpringApplication.run(InitUserDatabaseApp.class, args);
        UserRepository userRepository = applicationContext.getBean(UserRepository.class);

        Environment environment = applicationContext.getEnvironment();
        securityService = applicationContext.getBean(SecurityService.class);

        String initUsersData = environment.getProperty("users.initData");
        Arrays.asList(initUsersData.split(";")).stream().filter(user ->
                userRepository.findByLogin(user.split(",")[2])==null) .forEach(userString -> {
            String[] userSplit = userString.split(",");
            UserProfileType[] types = null;
            if (userSplit.length>4){
                types = getTypes(Arrays.copyOfRange(userSplit,5,userSplit.length));
            }
            User user = createUser(userSplit[0], userSplit[1], userSplit[2], userSplit[3], userSplit[4], types);
            userRepository.save(user);
        });

        SpringApplication.exit(applicationContext);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        InitUserDatabaseApp.applicationContext = applicationContext;
    }

    private static UserProfileType[] getTypes(String[] tab) {
        if (tab==null){
            return null;
        }
        List<UserProfileType> types = new ArrayList<>();
        for(String t:tab) {
            switch (t) {
                case "ADMIN":
                    types.add(UserProfileType.ADMIN);break;
                case "USER":
                    types.add(UserProfileType.USER);break;
            }
        }
        return types.toArray(new UserProfileType[types.size()]);
    }

    private static User createUser(String firstName,String lastName,String login, String password, String email,
                                   UserProfileType... types) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setLogin(login);
        user.setPassword(securityService.encryptPassword(password));
        user.setEmail(email);
        user.setActive(true);
        if (types!=null && types.length>0) {
            user.setUserProfiles(new HashSet<>());
            for(UserProfileType type:types) {
                user.getUserProfiles().add(type);
            }
        }
        return user;
    }
}
