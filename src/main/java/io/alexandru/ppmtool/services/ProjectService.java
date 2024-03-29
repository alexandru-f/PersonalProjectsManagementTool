package io.alexandru.ppmtool.services;

import io.alexandru.ppmtool.domain.User;
import io.alexandru.ppmtool.exceptions.ProjectNotFoundException;
import io.alexandru.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.alexandru.ppmtool.domain.Backlog;
import io.alexandru.ppmtool.domain.Project;
import io.alexandru.ppmtool.exceptions.ProjectIdException;
import io.alexandru.ppmtool.repositories.BacklogRepository;
import io.alexandru.ppmtool.repositories.ProjectRepository;

import java.security.Principal;

public class ProjectService {

	private final ProjectRepository projectRepository;

	private final BacklogRepository backlogRepository;

	private final UserRepository userRepository;

	public ProjectService(ProjectRepository projectRepository, BacklogRepository backlogRepository, UserRepository userRepository) {
		this.projectRepository = projectRepository;
		this.backlogRepository = backlogRepository;
		this.userRepository = userRepository;
	}

	public Project saveOrUpdateProject(Project project, String username) {


		if (project.getId() != null) {
			Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());

			if (existingProject != null && (!existingProject.getProjectLeader().equals(username))) {
				throw new ProjectNotFoundException("Project not found in your account");
			} else if (existingProject == null) {
				throw new ProjectNotFoundException("Project with ID '" + project.getProjectIdentifier() + "' cannot be updated because it doesn't exist");
			}
		}

		try {
			User user = userRepository.findByUsername(username);
			project.setUser(user);
			project.setProjectLeader(username);
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			
			if (project.getId() == null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			}
			
			if(project.getId() != null) {
				project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
			}
			
			return projectRepository.save(project);
			
		} catch (Exception e) {
			throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() +"' already exists");
		}
	
	}
	
	public Project findProjectByIdentifier(String projectId, String username) {
		
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if (project == null) {
			throw new ProjectIdException("Project ID '"+ projectId.toUpperCase() +"'does not exist");
		}

		if (!project.getProjectLeader().equals(username)) {
			throw new ProjectNotFoundException("Project not found in your account");
		}

		return project;
	}
	
	public Iterable<Project> findAllProjects(String username) {
		return projectRepository.findAllByProjectLeader(username);
	}
	
	public void deleteProjectByIdentifier(String projectId, String username) {
		projectRepository.delete(findProjectByIdentifier(projectId, username));
	}
	
}