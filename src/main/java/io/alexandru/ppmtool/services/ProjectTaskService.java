package io.alexandru.ppmtool.services;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.alexandru.ppmtool.domain.Backlog;
import io.alexandru.ppmtool.domain.Project;
import io.alexandru.ppmtool.domain.ProjectTask;
import io.alexandru.ppmtool.exceptions.ProjectNotFoundException;
import io.alexandru.ppmtool.repositories.BacklogRepository;
import io.alexandru.ppmtool.repositories.ProjectRepository;
import io.alexandru.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {
	
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectService projectService;

	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

			//PTs to be added to a specific project, not null project, so backlog exists
			Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
			
			//Set the backlog to the projectTask
			projectTask.setBacklog(backlog);
			
			//Project Identifier and the ID of the task within the project #IDPRO-1 - Project Sequence
			Integer BacklogSequence = backlog.getPTSequence();
			
			//Update THE backlog sequence
			BacklogSequence++;
			
			backlog.setPTSequence(BacklogSequence);
			
			//Add sequence to the project task
			projectTask.setProjectSequence(backlog.getProjectIdentifier() + "-" + backlog.getPTSequence());
			projectTask.setProjectIdentifier(backlog.getProjectIdentifier());


			//SET Initial Status - when priority is null
			if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
				projectTask.setStatus("TO_DO");
			}

			//SET Initial priority - when priority is null
			if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
				projectTask.setPriority(3);
			}

			//return projectTask
			return projectTaskRepository.save(projectTask);

	}

	public Iterable<ProjectTask> findProjectById(String backlog_id, String username) {
		
//		Project project = projectRepository.findByProjectIdentifier(backlog_id);
//
//		if (project == null) {
//			throw new ProjectNotFoundException("Project with ID '" + backlog_id + "' does not exist");
//		}

		projectService.findProjectByIdentifier(backlog_id, username);

		return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
	}
	
	public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username) {

		// Make sure we are searching on the right backlog
		projectService.findProjectByIdentifier(backlog_id, username);


		// Make sure that our tasks exists
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
		
		if (projectTask == null) {
			throw new ProjectNotFoundException("Project Task '"+ pt_id +"' not found");
		}
		
		// Make sure that the backlog/project id in the path corresponds to the right project
		if (!projectTask.getProjectIdentifier().equals(backlog_id.toUpperCase()))  {
			throw new ProjectNotFoundException("Project Task '"+ pt_id +"' does not exist in project "+ backlog_id);
		}

		return projectTask;
	}

	//Update Project Task
	public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username) {
		// Get projectTask from DB
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id,username);
		// Switch project task from db with updated project task
		projectTask = updatedTask;

		return projectTaskRepository.save(projectTask);
	}
	
	//Delete a project task
	public void deletePTByProjectSequence(String backlog_id, String pt_id, String username) {
//		Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
//		if (backlog == null) {
//			throw new ProjectNotFoundException("Project with ID '"+ pt_id +"' was not found");
//		}
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
		projectTaskRepository.delete(projectTask);
	}
	
}
