package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.dto.InstructorDTO;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;
import tn.esprit.spring.services.InstructorServicesImpl;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstructorServicesImplTest {

    @Mock
    private IInstructorRepository instructorRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private InstructorServicesImpl instructorServices;

    private Instructor instructor;
    private Course course;
    private InstructorDTO instructorDto; // Renamed from instructorDTO to instructorDto

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize Instructor object
        instructor = new Instructor();
        instructor.setNumInstructor(1L);
        instructor.setFirstName("John");
        instructor.setLastName("Doe");
        instructor.setDateOfHire(LocalDate.now().minusYears(5)); // Setting the date of hire

        // Initialize Course object
        course = new Course();
        course.setNumCourse(1L);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT); // Use the enum value here

        // Initialize InstructorDTO object
        instructorDto = new InstructorDTO(); // Use renamed variable
        instructorDto.setNumInstructor(instructor.getNumInstructor());
        instructorDto.setFirstName(instructor.getFirstName());
        instructorDto.setLastName(instructor.getLastName());
        instructorDto.setDateOfHire(instructor.getDateOfHire());
    }

    @Test
    void testAddInstructor() {
        when(instructorRepository.save(instructor)).thenReturn(instructor);

        Instructor result = instructorServices.addInstructor(instructor);

        assertNotNull(result);
        assertEquals(instructor.getFirstName(), result.getFirstName());
        assertEquals(instructor.getLastName(), result.getLastName());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testRetrieveAllInstructors() {
        when(instructorRepository.findAll()).thenReturn(Arrays.asList(instructor));

        List<Instructor> instructors = instructorServices.retrieveAllInstructors();

        assertNotNull(instructors);
        assertEquals(1, instructors.size());
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void testUpdateInstructor() {
        when(instructorRepository.save(instructor)).thenReturn(instructor);

        Instructor updatedInstructor = instructorServices.updateInstructor(instructor);

        assertNotNull(updatedInstructor);
        assertEquals(instructor.getFirstName(), updatedInstructor.getFirstName());
        assertEquals(instructor.getLastName(), updatedInstructor.getLastName());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testRetrieveInstructor() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

        Instructor foundInstructor = instructorServices.retrieveInstructor(1L);

        assertNotNull(foundInstructor);
        assertEquals(instructor.getFirstName(), foundInstructor.getFirstName());
        assertEquals(instructor.getLastName(), foundInstructor.getLastName());
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    void testAddInstructorAndAssignToCourse() {
        // Mock behavior for CourseRepository
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor result = instructorServices.addInstructorAndAssignToCourse(instructor, 1L);

        assertNotNull(result);
        assertTrue(result.getCourses().contains(course));
        assertEquals(1, result.getCourses().size());

        // Verify interactions
        verify(courseRepository, times(1)).findById(1L);
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testGetYearsOfService() {
        int expectedYearsOfService = Period.between(instructor.getDateOfHire(), LocalDate.now()).getYears();
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

        int actualYearsOfService = instructorServices.getYearsOfService(1L);

        assertEquals(expectedYearsOfService, actualYearsOfService);
    }

    @Test
    void testGetInstructorsSortedBySeniority() {
        Instructor instructor1 = new Instructor();
        instructor1.setDateOfHire(LocalDate.now().minusYears(10));

        Instructor instructor2 = new Instructor();
        instructor2.setDateOfHire(LocalDate.now().minusYears(5));

        Instructor instructor3 = new Instructor();
        instructor3.setDateOfHire(LocalDate.now().minusYears(1));

        List<Instructor> instructors = Arrays.asList(instructor2, instructor3, instructor1);
        when(instructorRepository.findAll()).thenReturn(instructors);

        List<Instructor> sortedInstructors = instructorServices.getInstructorsSortedBySeniority();

        assertEquals(instructor1, sortedInstructors.get(0));
        assertEquals(instructor2, sortedInstructors.get(1));
        assertEquals(instructor3, sortedInstructors.get(2));
    }

    @Test
    void testGetInstructorsSortedBySeniority_Integration() {
        when(instructorRepository.findAll()).thenReturn(Arrays.asList(instructor));

        List<Instructor> sortedInstructors = instructorServices.getInstructorsSortedBySeniority();

        assertNotNull(sortedInstructors);
        assertFalse(sortedInstructors.isEmpty());
    }

    @Test
    void testGetYearsOfService_Integration() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

        int expectedYearsOfService = Period.between(instructor.getDateOfHire(), LocalDate.now()).getYears();
        int actualYearsOfService = instructorServices.getYearsOfService(1L);

        assertEquals(expectedYearsOfService, actualYearsOfService);
    }

    @Test
    void testConvertToDTO() {
        // Test pour la conversion d'un instructeur en DTO
        InstructorDTO convertedInstructorDto = instructorServices.convertToDTO(instructor); // Use renamed variable
        assertNotNull(convertedInstructorDto);
        assertEquals(instructor.getFirstName(), convertedInstructorDto.getFirstName());
        assertEquals(instructor.getLastName(), convertedInstructorDto.getLastName());
        assertEquals(instructor.getDateOfHire(), convertedInstructorDto.getDateOfHire());
    }

    @Test
    void testConvertToEntity() {
        // Test pour la conversion d'un DTO en instructeur
        Instructor convertedInstructor = instructorServices.convertToEntity(instructorDto); // Use renamed variable
        assertNotNull(convertedInstructor);
        assertEquals(instructorDto.getFirstName(), convertedInstructor.getFirstName());
        assertEquals(instructorDto.getLastName(), convertedInstructor.getLastName());
        assertEquals(instructorDto.getDateOfHire(), convertedInstructor.getDateOfHire());
    }

    @Test
    void testRemoveInstructor() {
        Long instructorIdToRemove = 1L;

        // Mock behavior to check if the instructor exists
        when(instructorRepository.existsById(instructorIdToRemove)).thenReturn(true);

        // Call the method to remove the instructor
        instructorServices.removeInstructor(instructorIdToRemove);

        // Verify that the delete method was called with the correct ID
        verify(instructorRepository, times(1)).deleteById(instructorIdToRemove);
    }

    @Test
    void testRemoveInstructor_NotFound() {
        Long instructorIdToRemove = 1L;

        // Mock behavior to simulate that the instructor does not exist
        when(instructorRepository.existsById(instructorIdToRemove)).thenReturn(false);

        // Verify that an exception is thrown when trying to remove a non-existing instructor
        assertThrows(EntityNotFoundException.class, () -> instructorServices.removeInstructor(instructorIdToRemove));
    }
}
