package me.ad.kanban.bootstrap;

import me.ad.kanban.entity.*;
import me.ad.kanban.repository.*;
import me.ad.kanban.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final StageRepository stageRepository;
    private final TeamRepository teamRepository;
    private final TaskRepository taskRepository;

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    public DataLoader(ProjectRepository projectRepository, UserRepository userRepository, StageRepository stageRepository,
                      TeamRepository teamRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.stageRepository = stageRepository;
        this.teamRepository = teamRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadDemoData();
    }

    private void loadDemoData() {
        log.info("Loading Demo Data for the Kanban Application");
        // Create Demo Owner User
        User owner = new User();
        owner.setFirstName("John");
        owner.setLastName("Dao");
        owner.setUserId("john.d");
        User savedOwner = userRepository.save(owner);

        // Create Demo User
        User foo = new User();
        foo.setUserId("foo.b");
        foo.setFirstName("Foo");
        foo.setLastName("Bar");
        User savedFoo = userRepository.save(foo);

        User nick = new User();
        nick.setUserId("nick.f");
        nick.setFirstName("Nick");
        nick.setLastName("Furry");
        User savedNick = userRepository.save(nick);

        // Create Demo Project
        Project demoProject = new Project();
        demoProject.setName("Demo");
        demoProject.setDescription("Description for the Demo Project." +
                "\n It has 4 modules as part of Development team.");
        demoProject.setStartDate(LocalDate.now());
        demoProject.setEndDate(LocalDate.of(9999, 12, 31));
        demoProject.setOwner(savedOwner);
        Project savedDemoProject = projectRepository.save(demoProject);

        // Add stages to Demo demoProject
        Stage todo = new Stage();
        todo.setName("To-Do");
        todo.setProject(savedDemoProject);
        Stage savedTodo = stageRepository.save(todo);

        Stage inProgress = new Stage();
        inProgress.setName("In-Progress");
        inProgress.setProject(savedDemoProject);
        Stage savedInProgress = stageRepository.save(inProgress);

        Stage completed = new Stage();
        completed.setName("Completed");
        completed.setProject(savedDemoProject);
        Stage savedCompleted = stageRepository.save(completed);

        // Add Team to Demo Project
        Team ui = new Team();
        ui.setName("UI");
        ui.setProject(savedDemoProject);
        Team savedUi = teamRepository.save(ui);

        Team wf = new Team();
        wf.setName("WF");
        wf.setProject(savedDemoProject);
        Team savedWf = teamRepository.save(wf);

        Team mgmt = new Team();
        mgmt.setName("Management");
        mgmt.setProject(savedDemoProject);
        Team savedMgmt = teamRepository.save(mgmt);

        // Add owner to Management team
        savedOwner.getTeams().add(savedMgmt);
        savedMgmt.getUsers().add(savedOwner);
        userRepository.save(savedOwner);
        teamRepository.save(savedMgmt);

        // Add users to teams
        savedFoo.getTeams().add(savedUi);
        savedUi.getUsers().add(savedFoo);
        userRepository.save(savedFoo);
        teamRepository.save(savedUi);

        savedNick.getTeams().add(savedWf);
        savedWf.getUsers().add(savedNick);
        userRepository.save(savedNick);
        teamRepository.save(savedWf);

        // Create Tasks
        Task task1 = new Task();
        task1.setName("Load Demo Data in Startup");
        task1.setDescription("Need to Create Dummy data for Demo Project and" +
                " load it on application start up");
        task1.setPriority(50);
        task1.setCategory(savedTodo);
        task1.setTeam(savedWf);
        task1.setUser(savedNick);
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setName("Add Dto(s) for entities");
        task2.setDescription("Need to Create Dto classes for all entities");
        task2.setPriority(60);
        task2.setCategory(savedTodo);
        task2.setTeam(savedWf);
        task2.setUser(savedNick);
        taskRepository.save(task2);

        Task task3 = new Task();
        task3.setName("Create UI using Angular 7");
        task3.setDescription("Need to Create UI pages using Angular 7");
        task3.setPriority(30);
        task3.setCategory(savedInProgress);
        task3.setTeam(savedUi);
        task3.setUser(savedFoo);
        taskRepository.save(task3);
    }
}
