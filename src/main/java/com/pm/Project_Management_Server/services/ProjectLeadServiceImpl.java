package com.pm.Project_Management_Server.services;

import com.pm.Project_Management_Server.dto.ContactPersonDTO;
import com.pm.Project_Management_Server.dto.ProjectLeadDTO;
import com.pm.Project_Management_Server.dto.UserDTO;
import com.pm.Project_Management_Server.entity.ContactPerson;
import com.pm.Project_Management_Server.entity.Project;
import com.pm.Project_Management_Server.entity.ProjectLead;
import com.pm.Project_Management_Server.entity.Users;
import com.pm.Project_Management_Server.repositories.ContactPersonRepository;
import com.pm.Project_Management_Server.repositories.ProjectLeadRepository;
import com.pm.Project_Management_Server.repositories.ProjectRepository;
import com.pm.Project_Management_Server.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectLeadServiceImpl implements ProjectLeadService {

    private final ProjectLeadRepository projectLeadRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ContactPersonRepository contactPersonRepository;

    @Override
    public List<ProjectLeadDTO> getAllProjectLeads() {
        return projectLeadRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectLeadDTO getById(Long id) {
        ProjectLead lead = projectLeadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project lead not found"));
        return toDTO(lead);
    }




    @Override
    public void removeProjectLead(Long id) {
        if (!projectLeadRepository.existsById(id)) {
            throw new RuntimeException("Project lead not found");
        }
        projectLeadRepository.deleteById(id);
    }

    @Override
    public ProjectLeadDTO getByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        ProjectLead lead = project.getProjectLead();
        if (lead == null) {
            throw new RuntimeException("No lead assigned to this project");
        }

        return toDTO(lead);
    }


    @Override
    public ProjectLeadDTO addProjectLead(ProjectLeadDTO projectLeadDTO) {
        // 1. Fetch the user by ID
        Users user = userRepository.findById(projectLeadDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + projectLeadDTO.getUserId()));

        // 2. Create and populate the ProjectLead entity
        ProjectLead projectLead = new ProjectLead();
        projectLead.setUser(user);

        // 3. Save the ProjectLead entity
        ProjectLead savedLead = projectLeadRepository.save(projectLead);

        // 4. Map saved entity to DTO
        ProjectLeadDTO savedDTO = new ProjectLeadDTO();
        savedDTO.setId(savedLead.getId());
        savedDTO.setUserId(savedLead.getUser().getId());

        return savedDTO;
    }

    @Override
    public List<UserDTO> getAllProjectLeadUsers() {
        List<ProjectLead> leads = projectLeadRepository.findAll();

        return leads.stream()
                .map(ProjectLead::getUser)  // get Users from each ProjectLead
                .map(user -> {
                    UserDTO dto = new UserDTO();
                    dto.setId(user.getId());
                    dto.setUserName(user.getUserName());
                    dto.setEmail(user.getEmail());
                    // map any other fields needed
                    return dto;
                })
                .collect(Collectors.toList());
    }



    // 🔄 DTO Converter
    private ProjectLeadDTO toDTO(ProjectLead lead) {
        if (lead == null || lead.getUser() == null) {
            throw new IllegalArgumentException("ProjectLead or associated User is null");
        }

        return ProjectLeadDTO.builder()
                .id(lead.getId())
                .userId(lead.getUser().getId())
                .build();
    }

}
