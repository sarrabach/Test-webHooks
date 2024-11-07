package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.dto.InstructorDTO;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.services.IInstructorServices;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "\uD83D\uDC69\u200D\uD83C\uDFEB Instructor Management")
@RestController
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorRestController {

    private final IInstructorServices instructorServices;

    @Operation(description = "Add Instructor")
    @PostMapping("/add")
    public ResponseEntity<InstructorDTO> addInstructor(@RequestBody InstructorDTO instructorDTO) {
        if (instructorDTO == null || instructorDTO.getFirstName() == null || instructorDTO.getLastName() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Instructor instructor = convertToEntity(instructorDTO);
        Instructor savedInstructor = instructorServices.addInstructor(instructor);
        return ResponseEntity.ok(convertToDTO(savedInstructor));
    }

    @Operation(description = "Add Instructor and Assign To Course")
    @PutMapping("/addAndAssignToCourse/{numCourse}")
    public ResponseEntity<InstructorDTO> addAndAssignToInstructor(@RequestBody InstructorDTO instructorDTO, @PathVariable("numCourse") Long numCourse) {
        if (instructorDTO == null || instructorDTO.getFirstName() == null || instructorDTO.getLastName() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Instructor instructor = convertToEntity(instructorDTO);
        Instructor assignedInstructor = instructorServices.addInstructorAndAssignToCourse(instructor, numCourse);
        return ResponseEntity.ok(convertToDTO(assignedInstructor));
    }

    @Operation(description = "Retrieve all Instructors")
    @GetMapping("/all")
    public ResponseEntity<List<InstructorDTO>> getAllInstructors() {
        List<InstructorDTO> instructorDTOList = instructorServices.retrieveAllInstructors()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(instructorDTOList);
    }

    @Operation(description = "Update Instructor")
    @PutMapping("/update")
    public ResponseEntity<InstructorDTO> updateInstructor(@RequestBody InstructorDTO instructorDTO) {
        if (instructorDTO == null || instructorDTO.getNumInstructor() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Instructor instructor = convertToEntity(instructorDTO);
        Instructor updatedInstructor = instructorServices.updateInstructor(instructor);
        return ResponseEntity.ok(convertToDTO(updatedInstructor));
    }

    @Operation(description = "Assign Instructor to Course")
    @PostMapping("/{instructorId}/assign/{courseId}")
    public ResponseEntity<InstructorDTO> assignInstructorToCourse(@PathVariable("instructorId") Long instructorId, @PathVariable("courseId") Long courseId) {
        Instructor instructor = instructorServices.retrieveInstructor(instructorId);
        if (instructor == null) {
            return ResponseEntity.notFound().build();
        }
        Instructor assignedInstructor = instructorServices.addInstructorAndAssignToCourse(instructor, courseId);
        return ResponseEntity.ok(convertToDTO(assignedInstructor));
    }

    @Operation(description = "Get Instructors sorted by Seniority")
    @GetMapping("/sortedBySeniority")
    public ResponseEntity<List<InstructorDTO>> getInstructorsSortedBySeniority() {
        List<InstructorDTO> sortedInstructors = instructorServices.getInstructorsSortedBySeniority()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sortedInstructors);
    }

    @GetMapping("/yearsOfService/{id}")
    public ResponseEntity<Integer> getYearsOfService(@PathVariable Long id) {
        int yearsOfService = instructorServices.getYearsOfService(id);
        return ResponseEntity.ok(yearsOfService);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> removeInstructor(@PathVariable Long id) {
        instructorServices.removeInstructor(id);
        return ResponseEntity.noContent().build();
    }

    // Convert InstructorDTO to Instructor entity
    private Instructor convertToEntity(InstructorDTO instructorDTO) {
        if (instructorDTO == null) {
            return null;
        }
        Instructor instructor = new Instructor();
        instructor.setNumInstructor(instructorDTO.getNumInstructor());
        instructor.setFirstName(instructorDTO.getFirstName());
        instructor.setLastName(instructorDTO.getLastName());
        instructor.setDateOfHire(instructorDTO.getDateOfHire());
        return instructor;
    }

    // Convert Instructor entity to InstructorDTO
    private InstructorDTO convertToDTO(Instructor instructor) {
        if (instructor == null) {
            return null;
        }
        InstructorDTO dto = new InstructorDTO();
        dto.setNumInstructor(instructor.getNumInstructor());
        dto.setFirstName(instructor.getFirstName());
        dto.setLastName(instructor.getLastName());
        dto.setDateOfHire(instructor.getDateOfHire());
        return dto;
    }
}
