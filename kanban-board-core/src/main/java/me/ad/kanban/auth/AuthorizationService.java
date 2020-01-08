package me.ad.kanban.auth;

import me.ad.kanban.dto.ProjectDto;
import me.ad.kanban.entity.*;
import me.ad.kanban.exception.ForbiddenException;
import me.ad.kanban.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.Charset;
import java.util.Optional;

@Service
public class AuthorizationService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;
    private final TaskRepository taskRepository;
    private final StageRepository stageRepository;

    public AuthorizationService(UserRepository userRepository, ProjectRepository projectRepository, TeamRepository teamRepository, TaskRepository taskRepository, StageRepository stageRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.teamRepository = teamRepository;
        this.taskRepository = taskRepository;
        this.stageRepository = stageRepository;
    }

    private void ensureNotNull(Object obj) {
        if(obj == null) {
            throw new IllegalArgumentException("Object must not be null");
        }
    }

    public void ensureAuthUserIsOwnerOfProject(Authentication auth, String projectId) {
        ensureNotNull(projectId);
        Optional<User> userOpt = userRepository.findByUserId(auth.getName());
        userOpt.orElseThrow(() -> new IllegalArgumentException("User with Id: " + auth.getName() + " doesn't exist."));
        Optional<Project> projectOpt = projectRepository.findById(projectId);
        projectOpt.orElseThrow(() -> new IllegalArgumentException("Project with Id: " + projectId + " doesn't exist."));
        if(projectOpt.get().getOwner().getId().equals(userOpt.get().getId())) {
            // user is owner of project
        } else {
            throw new ForbiddenException("User is NOT authorized to perform this operation");
                    //"User is NOT authorized to perform this operation".getBytes(), Charset.defaultCharset());
        }
    }

    public void ensureAuthUserIsConcernedUser(Authentication auth, String userId) {
        ensureNotNull(userId);
        Optional<User> userOpt = userRepository.findByUserId(auth.getName());
        userOpt.orElseThrow(() -> new IllegalArgumentException("User with Id: " + auth.getName() + " doesn't exist."));
        if(userOpt.get().getId().equals(userId)) {
            // user is concerned user
        } else {
            throw new ForbiddenException("User is NOT authorized to perform this operation");
        }
    }

    public void ensureAuthUserIsOwnerOfTeam(Authentication auth, String teamId) {
        ensureNotNull(teamId);
        Optional<User> userOpt = userRepository.findByUserId(auth.getName());
        userOpt.orElseThrow(() -> new IllegalArgumentException("User with Id: " + auth.getName() + " doesn't exist."));
        Optional<Team> teamOpt = teamRepository.findById(teamId);
        teamOpt.orElseThrow(() -> new IllegalArgumentException("Team with Id: " + teamId + " doesn't exist."));
        if(teamOpt.get().getProject().getOwner().getId().equals(userOpt.get().getId())) {
            // user is owner of the project, to which the team belongs to
        } else {
            throw new ForbiddenException("User is NOT authorized to perform this operation");
            //"User is NOT authorized to perform this operation".getBytes(), Charset.defaultCharset());
        }
    }

    public void ensureAuthUserIsOwnerOfStage(Authentication auth, String stageId) {
        ensureNotNull(stageId);
        Optional<User> userOpt = userRepository.findByUserId(auth.getName());
        userOpt.orElseThrow(() -> new IllegalArgumentException("User with Id: " + auth.getName() + " doesn't exist."));
        Optional<Stage> stageOpt = stageRepository.findById(stageId);
        stageOpt.orElseThrow(() -> new IllegalArgumentException("Stage with Id: " + stageId + " doesn't exist."));
        if(stageOpt.get().getProject().getOwner().getId().equals(userOpt.get().getId())) {
            // user is owner of the project, to which the team belongs to
        } else {
            throw new ForbiddenException("User is NOT authorized to perform this operation");
            //"User is NOT authorized to perform this operation".getBytes(), Charset.defaultCharset());
        }
    }

    public void ensureAuthUserIsOwnerOfTask(Authentication auth, String taskId) {
        ensureNotNull(taskId);
        Optional<User> userOpt = userRepository.findByUserId(auth.getName());
        userOpt.orElseThrow(() -> new IllegalArgumentException("User with Id: " + auth.getName() + " doesn't exist."));
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        taskOpt.orElseThrow(() -> new IllegalArgumentException("Task with Id: " + taskId + " doesn't exist."));
        if(taskOpt.get().getOwner().getId().equals(userOpt.get().getId())) {
            // user is owner of the task
        } else {
            throw new ForbiddenException("User is NOT authorized to perform this operation");
            //"User is NOT authorized to perform this operation".getBytes(), Charset.defaultCharset());
        }
    }

}
