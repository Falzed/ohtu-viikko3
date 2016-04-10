package ohtu;

import ohtu.data_access.InMemoryUserDao;
import ohtu.data_access.FileUserDAO;
import ohtu.io.ConsoleIO;
import ohtu.io.IO;
import ohtu.services.AuthenticationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Component
public class App {

    private IO io;
    private AuthenticationService auth;

    @Autowired
    public App(IO io, AuthenticationService auth) {
        this.io = io;
        this.auth = auth;
    }

    public String[] ask() {
        String[] userPwd = new String[2];
        userPwd[0] = io.readLine("username:");
        userPwd[1] = io.readLine("password:");
        return userPwd;
    }

    public void run() {
        while (true) {
            String command = io.readLine(">");
            if (command.isEmpty()) {
                break;
            }
            command(command);
        }
    }

    public void command(String command) {
        if (command.equals("new")) {
            createNew();
        } else if (command.equals("login")) {
            login();
        }
    }

    public void createNew() {
        String[] usernameAndPasword = ask();
        if (auth.createUser(usernameAndPasword[0], usernameAndPasword[1])) {
            io.print("new user registered");
        } else {
            io.print("new user not registered");
        }
    }

    public void login() {
        String[] usernameAndPasword = ask();
        if (auth.logIn(usernameAndPasword[0], usernameAndPasword[1])) {
            io.print("logged in");
        } else {
            io.print("wrong username or password");
        }
    }

    public static void main(String[] args) {
//        UserDao dao = new InMemoryUserDao();
//        IO io = new ConsoleIO();
//        AuthenticationService auth = new AuthenticationService(dao);
//        new App(io, auth).run();

        ApplicationContext ctx = new FileSystemXmlApplicationContext("src/main/resources/spring-context.xml");
        FileUserDAO fileUserDAO = (FileUserDAO) ctx.getBean("fileUserDAO");
        App application = ctx.getBean(App.class);
        application.run();
    }

    // testejä debugatessa saattaa olla hyödyllistä testata ohjelman ajamista
    // samoin kuin testi tekee, eli injektoimalla käyttäjän syötteen StubIO:n avulla
    //
    // UserDao dao = new InMemoryUserDao();  
    // StubIO io = new StubIO("new", "eero", "sala1nen" );   
    //  AuthenticationService auth = new AuthenticationService(dao);
    // new App(io, auth).run();
    // System.out.println(io.getPrints());
}
