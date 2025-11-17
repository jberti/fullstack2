# Requirements Document

## Introduction

The current project structure has JPA entities scattered across two different directories: `adapters/output/entities/` and `adapters/output/repositories/entities/`. This creates confusion and violates the principle of keeping related components together. This feature will reorganize the entities into a single, consistent location following clean architecture principles.

## Requirements

### Requirement 1

**User Story:** As a developer, I want all JPA entities to be in a single, well-organized directory, so that I can easily find and maintain entity classes.

#### Acceptance Criteria

1. WHEN reviewing the project structure THEN all JPA entities SHALL be located in `adapters/output/entities/`
2. WHEN looking for entity classes THEN there SHALL be no entities in `adapters/output/repositories/entities/`
3. WHEN examining the codebase THEN all imports referencing entity classes SHALL be updated to reflect the new location

### Requirement 2

**User Story:** As a developer, I want the repository classes to import entities from the correct location, so that the code compiles and runs without errors.

#### Acceptance Criteria

1. WHEN repository classes reference entity classes THEN they SHALL import from `adapters/output/entities/` package
2. WHEN the application starts THEN all JPA mappings SHALL work correctly with the reorganized entities
3. WHEN running tests THEN all entity-related tests SHALL pass with the new structure

### Requirement 3

**User Story:** As a developer, I want the adapter classes to import entities from the correct location, so that the dependency injection and mapping work properly.

#### Acceptance Criteria

1. WHEN adapter classes reference entity classes THEN they SHALL import from `adapters/output/entities/` package
2. WHEN the application runs THEN all entity mappings in adapters SHALL function correctly
3. WHEN examining the code THEN there SHALL be no broken imports or compilation errors

### Requirement 4

**User Story:** As a developer, I want to remove the empty `repositories/entities/` directory, so that the project structure is clean and follows consistent organization principles.

#### Acceptance Criteria

1. WHEN the reorganization is complete THEN the `adapters/output/repositories/entities/` directory SHALL be removed
2. WHEN examining the project structure THEN there SHALL be no empty or unused directories
3. WHEN the cleanup is done THEN the project SHALL maintain a clear separation between repositories and entities