package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.dto.InstructorDTO;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class InstructorServicesImpl implements IInstructorServices {

    private final IInstructorRepository instructorRepository;
    private final ICourseRepository courseRepository;

    @Override
    public Instructor addInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    @Override
    public List<Instructor> retrieveAllInstructors() {
        return instructorRepository.findAll();
    }

    @Override
    public Instructor updateInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    @Override
    public Instructor retrieveInstructor(Long numInstructor) {
        return instructorRepository.findById(numInstructor).orElse(null);
    }

    @Override
    public void removeInstructor(Long numInstructor) {
        if (instructorRepository.existsById(numInstructor)) {
            instructorRepository.deleteById(numInstructor);
        } else {
            throw new EntityNotFoundException("Instructor with ID " + numInstructor + " not found");
        }
    }

    @Override
    public Instructor addInstructorAndAssignToCourse(Instructor instructor, Long numCourse) {
        Course course = courseRepository.findById(numCourse)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + numCourse));

        Set<Course> courseSet = new HashSet<>();
        courseSet.add(course);
        instructor.setCourses(courseSet);

        return instructorRepository.save(instructor);
    }

    @Override
    public int getYearsOfService(Long numInstructor) {
        Instructor instructor = instructorRepository.findById(numInstructor).orElse(null);
        if (instructor == null) {
            return 0; // Return 0 if the instructor is not found
        }
        return instructor.getYearsOfService();
    }

    @Override
    public List<Instructor> getInstructorsSortedBySeniority() {
        return instructorRepository.findAll().stream()
                .sorted(Comparator.comparing(Instructor::getYearsOfService).reversed())
                .collect(Collectors.toList());
    }

    public InstructorDTO convertToDTO(Instructor instructor) {
        if (instructor == null) {
            return null; // Return null if instructor is null
        }
        InstructorDTO dto = new InstructorDTO();
        dto.setNumInstructor(instructor.getNumInstructor());
        dto.setFirstName(instructor.getFirstName());
        dto.setLastName(instructor.getLastName());
        dto.setDateOfHire(instructor.getDateOfHire());
        return dto;
    }

    public Instructor convertToEntity(InstructorDTO dto) {
        if (dto == null) {
            return null; // Return null if dto is null
        }
        Instructor instructor = new Instructor();
        instructor.setNumInstructor(dto.getNumInstructor());
        instructor.setFirstName(dto.getFirstName());
        instructor.setLastName(dto.getLastName());
        instructor.setDateOfHire(dto.getDateOfHire());
        return instructor;
    }
}
