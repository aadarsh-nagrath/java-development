package com.codewhisperer.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectContext {
    private String projectPath;
    private String projectName;
    private String primaryLanguage;
    private List<String> sourceFiles;
    private Map<String, String> dependencies; // Framework/version info
    private String gitBranch;
    private String lastCommitMessage;
    private List<String> recentFiles; // Recently modified files
    private Map<String, Object> projectMetadata;
} 