package me.ad.kanban.service.impl;

import me.ad.kanban.config.CustomMessageProperties;
import me.ad.kanban.entity.Team;
import me.ad.kanban.filter.FilterBuilder;
import me.ad.kanban.repository.ProjectRepository;
import me.ad.kanban.repository.TeamRepository;
import me.ad.kanban.service.MapperService;
import me.ad.kanban.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {

    private final CustomMessageProperties message;
    private final MapperService mapperService;
    private final TeamRepository teamRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public TeamServiceImpl(CustomMessageProperties message, MapperService mapperService, TeamRepository teamRepository,
                           ProjectRepository projectRepository) {
        this.message = message;
        this.mapperService = mapperService;
        this.teamRepository = teamRepository;
        this.projectRepository = projectRepository;
    }

    //@Override
    public Long findTotalCount() {
        return teamRepository.count();
    }

    @Override
    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    @Override
    public Optional<Team> findById(String id) {
        return teamRepository.findById(id);
    }

    @Override
    public Team save(Team entity) {
        return teamRepository.save(entity);
    }

    @Override
    public void delete(Team entity) {
        teamRepository.delete(entity);
    }

    @Override
    public void deleteById(String id) {
        teamRepository.deleteById(id);
    }

    @Override
    public FilterBuilder<Team> filterBuilder() {
        return new FilterBuilder<>(message);
    }
}
