package com.pm.Project_Management_Server.dto;

import com.pm.Project_Management_Server.entity.ResourceLevel;
import com.pm.Project_Management_Server.entity.ResourceLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceRequiredDTO {
    private Long id;
    private ResourceLevel level;  // jr, intermediate, sr, etc.
    private String expRange;        // Example: "2-4 years"
    private Integer quantity;
    private Long projectId;
}
