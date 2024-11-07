package tn.esprit.spring.dto;

import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InstructorDTO implements Serializable {

    @NotNull(message = "Instructor ID cannot be null")
    private Long numInstructor;

    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

    @NotNull(message = "Date of hire cannot be null")
    private LocalDate dateOfHire;

    private int yearsOfService;

    // Constructor to initialize and set yearsOfService
    public InstructorDTO(Long numInstructor, String firstName, String lastName, LocalDate dateOfHire) {
        this.numInstructor = numInstructor;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfHire = dateOfHire;
        this.yearsOfService = calculateYearsOfService();
    }

    // Method to calculate years of service based on the current date and date of hire
    private int calculateYearsOfService() {
        if (dateOfHire != null) {
            return LocalDate.now().getYear() - this.dateOfHire.getYear();
        }
        return 0; // Return 0 if dateOfHire is null to avoid errors
    }

    // Set yearsOfService whenever dateOfHire is set
    public void setDateOfHire(LocalDate dateOfHire) {
        this.dateOfHire = dateOfHire;
        this.yearsOfService = calculateYearsOfService();
    }
}
