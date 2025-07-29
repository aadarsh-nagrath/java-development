package com.codewhisperer.service;

import com.codewhisperer.model.ProjectContext;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectScannerService {

    public ProjectContext scanProject(String projectPath) {
        try {
            Path path = Paths.get(projectPath);
            if (!Files.exists(path)) {
                throw new IllegalArgumentException("Project path does not exist: " + projectPath);
            }

            ProjectContext context = ProjectContext.builder()
                    .projectPath(projectPath)
                    .projectName(path.getFileName().toString())
                    .primaryLanguage(detectPrimaryLanguage(path))
                    .sourceFiles(scanSourceFiles(path))
                    .dependencies(extractDependencies(path))
                    .gitBranch(getGitBranch(path))
                    .lastCommitMessage(getLastCommitMessage(path))
                    .recentFiles(getRecentFiles(path))
                    .projectMetadata(extractProjectMetadata(path))
                    .build();

            log.info("Project scanned successfully: {}", context.getProjectName());
            return context;

        } catch (Exception e) {
            log.error("Error scanning project at path: {}", projectPath, e);
            throw new RuntimeException("Failed to scan project", e);
        }
    }

    private String detectPrimaryLanguage(Path projectPath) {
        Map<String, Integer> languageCounts = new HashMap<>();
        
        try {
            Files.walkFileTree(projectPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    String fileName = file.toString().toLowerCase();
                    if (fileName.endsWith(".java")) languageCounts.merge("java", 1, Integer::sum);
                    else if (fileName.endsWith(".py")) languageCounts.merge("python", 1, Integer::sum);
                    else if (fileName.endsWith(".js") || fileName.endsWith(".ts")) languageCounts.merge("javascript", 1, Integer::sum);
                    else if (fileName.endsWith(".go")) languageCounts.merge("go", 1, Integer::sum);
                    else if (fileName.endsWith(".rs")) languageCounts.merge("rust", 1, Integer::sum);
                    else if (fileName.endsWith(".cpp") || fileName.endsWith(".c")) languageCounts.merge("c++", 1, Integer::sum);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            log.warn("Error detecting language for project: {}", projectPath, e);
        }

        return languageCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("unknown");
    }

    private List<String> scanSourceFiles(Path projectPath) {
        List<String> sourceFiles = new ArrayList<>();
        Set<String> sourceExtensions = Set.of(".java", ".py", ".js", ".ts", ".go", ".rs", ".cpp", ".c", ".h", ".hpp");
        
        try {
            Files.walkFileTree(projectPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    String fileName = file.toString().toLowerCase();
                    if (sourceExtensions.stream().anyMatch(fileName::endsWith)) {
                        sourceFiles.add(projectPath.relativize(file).toString());
                    }
                    return FileVisitResult.CONTINUE;
                }
                
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    String dirName = dir.getFileName().toString();
                    if (dirName.equals("target") || dirName.equals("node_modules") || 
                        dirName.equals(".git") || dirName.equals("build")) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            log.warn("Error scanning source files: {}", e.getMessage());
        }
        
        return sourceFiles.stream().limit(100).collect(Collectors.toList()); // Limit to avoid overwhelming
    }

    private Map<String, String> extractDependencies(Path projectPath) {
        Map<String, String> dependencies = new HashMap<>();
        
        // Check for pom.xml (Maven)
        Path pomXml = projectPath.resolve("pom.xml");
        if (Files.exists(pomXml)) {
            dependencies.put("buildTool", "maven");
            // Could parse pom.xml for actual dependencies
        }
        
        // Check for build.gradle
        Path buildGradle = projectPath.resolve("build.gradle");
        if (Files.exists(buildGradle)) {
            dependencies.put("buildTool", "gradle");
        }
        
        // Check for package.json
        Path packageJson = projectPath.resolve("package.json");
        if (Files.exists(packageJson)) {
            dependencies.put("buildTool", "npm");
        }
        
        // Check for requirements.txt
        Path requirementsTxt = projectPath.resolve("requirements.txt");
        if (Files.exists(requirementsTxt)) {
            dependencies.put("buildTool", "pip");
        }
        
        return dependencies;
    }

    private String getGitBranch(Path projectPath) {
        try {
            Repository repository = Git.open(projectPath.toFile()).getRepository();
            return repository.getBranch();
        } catch (IOException e) {
            log.debug("Could not get git branch for project: {}", projectPath, e);
            return "unknown";
        }
    }

    private String getLastCommitMessage(Path projectPath) {
        try {
            Git git = Git.open(projectPath.toFile());
            return git.log().setMaxCount(1).call().iterator().next().getFullMessage();
        } catch (IOException | GitAPIException e) {
            log.debug("Could not get last commit message for project: {}", projectPath, e);
            return "unknown";
        }
    }

    private List<String> getRecentFiles(Path projectPath) {
        List<String> recentFiles = new ArrayList<>();
        try {
            Files.walkFileTree(projectPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (attrs.lastModifiedTime().toMillis() > System.currentTimeMillis() - 86400000) { // Last 24 hours
                        recentFiles.add(projectPath.relativize(file).toString());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            log.warn("Error getting recent files: {}", e.getMessage());
        }
        return recentFiles.stream().limit(10).collect(Collectors.toList());
    }

    private Map<String, Object> extractProjectMetadata(Path projectPath) {
        Map<String, Object> metadata = new HashMap<>();
        
        // Get project size
        try {
            long size = Files.walk(projectPath)
                    .filter(Files::isRegularFile)
                    .mapToLong(p -> {
                        try {
                            return Files.size(p);
                        } catch (IOException e) {
                            return 0L;
                        }
                    })
                    .sum();
            metadata.put("projectSizeBytes", size);
        } catch (IOException e) {
            log.warn("Error calculating project size: {}", e.getMessage());
        }
        
        // Check for common config files
        metadata.put("hasReadme", Files.exists(projectPath.resolve("README.md")));
        metadata.put("hasGitignore", Files.exists(projectPath.resolve(".gitignore")));
        metadata.put("hasDockerfile", Files.exists(projectPath.resolve("Dockerfile")));
        
        return metadata;
    }
} 