package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.controllers.InstructorRestController;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.services.IInstructorServices;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

 class InstructorControllerTest {

   @Mock
   private IInstructorServices instructorServices;

   @InjectMocks
   private InstructorRestController instructorRestController;

   private MockMvc mockMvc;

   private Instructor instructor;

   @BeforeEach
    void setup() {
      MockitoAnnotations.openMocks(this);
      mockMvc = standaloneSetup(instructorRestController).build();

      // Initialize a test instructor
      instructor = new Instructor();
      instructor.setNumInstructor(1L);
      instructor.setFirstName("John");
      instructor.setLastName("Doe");
   }

   @Test
    void testAddInstructor() throws Exception {
      when(instructorServices.addInstructor(any(Instructor.class))).thenReturn(instructor);

      mockMvc.perform(post("/instructor/add")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content("{\"firstName\":\"John\",\"lastName\":\"Doe\"}"))
              .andExpect(status().isOk())
              .andExpect(content().json("{\"numInstructor\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"}"));

      verify(instructorServices, times(1)).addInstructor(any(Instructor.class));
   }

   @Test
    void testAddAndAssignToInstructor() throws Exception {
      when(instructorServices.addInstructorAndAssignToCourse(any(Instructor.class), eq(1L))).thenReturn(instructor);

      mockMvc.perform(put("/instructor/addAndAssignToCourse/1")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content("{\"firstName\":\"John\",\"lastName\":\"Doe\"}"))
              .andExpect(status().isOk())
              .andExpect(content().json("{\"numInstructor\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"}"));

      verify(instructorServices, times(1)).addInstructorAndAssignToCourse(any(Instructor.class), eq(1L));
   }

   @Test
    void testGetAllInstructors() throws Exception {
      List<Instructor> instructors = Arrays.asList(instructor);

      when(instructorServices.retrieveAllInstructors()).thenReturn(instructors);

      mockMvc.perform(get("/instructor/all"))
              .andExpect(status().isOk())
              .andExpect(content().json("[{\"numInstructor\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"}]"));

      verify(instructorServices, times(1)).retrieveAllInstructors();
   }

   @Test
    void testUpdateInstructor() throws Exception {
      when(instructorServices.updateInstructor(any(Instructor.class))).thenReturn(instructor);

      mockMvc.perform(put("/instructor/update")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content("{\"numInstructor\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"}"))
              .andExpect(status().isOk())
              .andExpect(content().json("{\"numInstructor\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"}"));

      verify(instructorServices, times(1)).updateInstructor(any(Instructor.class));
   }

   @Test
    void testAssignInstructorToCourse() throws Exception {
      when(instructorServices.retrieveInstructor(1L)).thenReturn(instructor);
      when(instructorServices.addInstructorAndAssignToCourse(any(Instructor.class), eq(1L))).thenReturn(instructor);

      mockMvc.perform(post("/instructor/1/assign/1"))
              .andExpect(status().isOk())
              .andExpect(content().json("{\"numInstructor\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"}"));

      verify(instructorServices, times(1)).addInstructorAndAssignToCourse(any(Instructor.class), eq(1L));
   }

   @Test
    void testGetYearsOfService() throws Exception {
      when(instructorServices.getYearsOfService(1L)).thenReturn(5); // Example years of service

      mockMvc.perform(get("/instructor/yearsOfService/1"))
              .andExpect(status().isOk())
              .andExpect(content().string("5"));

      verify(instructorServices, times(1)).getYearsOfService(1L);
   }

   @Test
    void testGetInstructorsSortedBySeniority() throws Exception {
      List<Instructor> sortedInstructors = Arrays.asList(instructor);

      when(instructorServices.getInstructorsSortedBySeniority()).thenReturn(sortedInstructors);

      mockMvc.perform(get("/instructor/sortedBySeniority"))
              .andExpect(status().isOk())
              .andExpect(content().json("[{\"numInstructor\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"}]"));

      verify(instructorServices, times(1)).getInstructorsSortedBySeniority();
   }

   @Test
    void testRemoveInstructor() throws Exception {
      doNothing().when(instructorServices).removeInstructor(1L); // Mock the removeInstructor method

      mockMvc.perform(delete("/instructor/remove/1"))
              .andExpect(status().isNoContent()); // Expect HTTP 204 No Content

      verify(instructorServices, times(1)).removeInstructor(1L); // Verify that the service method was called
   }
}
