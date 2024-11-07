package tn.esprit.spring.dto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.services.IInstructorServices;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "\uD83D\uDC69\u200D\uD83C\uDFEB Instructor Management")
@RestController
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorRestControllerDTO {

    private final IInstructorServices instructorServices;

    // Ajout d'un nouvel instructeur
    @Operation(description = "Add Instructor")
    @PostMapping("/add")
    public ResponseEntity<InstructorDTO> addInstructor(@Valid @RequestBody InstructorDTO instructorDTO) {
        Instructor instructor = convertToEntity(instructorDTO);
        Instructor savedInstructor = instructorServices.addInstructor(instructor);
        return ResponseEntity.ok(convertToDTO(savedInstructor));
    }

    // Ajout et assignation d'un instructeur à un cours
    @Operation(description = "Add Instructor and Assign To Course")
    @PutMapping("/addAndAssignToCourse/{numCourse}")
    public ResponseEntity<InstructorDTO> addAndAssignToInstructor(@Valid @RequestBody InstructorDTO instructorDTO, @PathVariable Long numCourse) {
        Instructor instructor = convertToEntity(instructorDTO);
        Instructor assignedInstructor = instructorServices.addInstructorAndAssignToCourse(instructor, numCourse);
        return ResponseEntity.ok(convertToDTO(assignedInstructor));
    }

    // Récupération de tous les instructeurs
    @Operation(description = "Retrieve all Instructors")
    @GetMapping("/all")
    public ResponseEntity<List<InstructorDTO>> getAllInstructors() {
        List<InstructorDTO> instructorDTOList = instructorServices.retrieveAllInstructors()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(instructorDTOList);
    }

    // Mise à jour d'un instructeur
    @Operation(description = "Update Instructor")
    @PutMapping("/update")
    public ResponseEntity<InstructorDTO> updateInstructor(@Valid @RequestBody InstructorDTO instructorDTO) {
        Instructor instructor = convertToEntity(instructorDTO);
        Instructor updatedInstructor = instructorServices.updateInstructor(instructor);
        return ResponseEntity.ok(convertToDTO(updatedInstructor));
    }

    // Assignation d'un instructeur à un cours
    @Operation(description = "Assign Instructor to Course")
    @PostMapping("/{instructorId}/assign/{courseId}")
    public ResponseEntity<InstructorDTO> assignInstructorToCourse(@PathVariable Long instructorId, @PathVariable Long courseId) {
        Instructor instructor = instructorServices.retrieveInstructor(instructorId);
        Instructor assignedInstructor = instructorServices.addInstructorAndAssignToCourse(instructor, courseId);
        return ResponseEntity.ok(convertToDTO(assignedInstructor));
    }

    // Récupération des instructeurs triés par ancienneté
    @Operation(description = "Get Instructors sorted by Seniority")
    @GetMapping("/sortedBySeniority")
    public ResponseEntity<List<InstructorDTO>> getInstructorsSortedBySeniority() {
        List<InstructorDTO> sortedInstructors = instructorServices.getInstructorsSortedBySeniority()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sortedInstructors);
    }

    // Récupération des années de service d'un instructeur
    @Operation(description = "Get Years of Service for Instructor")
    @GetMapping("/yearsOfService/{id}")
    public ResponseEntity<Integer> getYearsOfService(@PathVariable Long id) {
        int yearsOfService = instructorServices.getYearsOfService(id);
        return ResponseEntity.ok(yearsOfService);
    }

    // Suppression d'un instructeur
    @Operation(description = "Remove Instructor")
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> removeInstructor(@PathVariable Long id) {
        instructorServices.removeInstructor(id);
        return ResponseEntity.noContent().build();
    }

    // Méthode de conversion de DTO en entité Instructor
    private Instructor convertToEntity(InstructorDTO instructorDTO) {
        if (instructorDTO == null) return null;
        Instructor instructor = new Instructor();
        instructor.setNumInstructor(instructorDTO.getNumInstructor());
        instructor.setFirstName(instructorDTO.getFirstName());
        instructor.setLastName(instructorDTO.getLastName());
        instructor.setDateOfHire(instructorDTO.getDateOfHire());
        return instructor;
    }

    // Méthode de conversion de Instructor en DTO
    private InstructorDTO convertToDTO(Instructor instructor) {
        if (instructor == null) return null;
        return new InstructorDTO(
                instructor.getNumInstructor(),
                instructor.getFirstName(),
                instructor.getLastName(),
                instructor.getDateOfHire(),
                instructor.getYearsOfService()
        );
    }
}
