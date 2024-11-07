package tn.esprit.spring.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // Enables an all-args constructor for full initialization
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Instructor implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long numInstructor;
	String firstName;
	String lastName;
	LocalDate dateOfHire;

	// Making "courses" private and transient
	@OneToMany // Assuming Course has a field named 'instructor'
	transient Set<Course> courses;

	/**
	 * Calculates the years of service based on the date of hire.
	 * Returns 0 if the date of hire is null.
	 *  Returns 0 if the date of hire .
	 */
	public int getYearsOfService() {
		return (dateOfHire != null) ? Period.between(dateOfHire, LocalDate.now()).getYears() : 0;
	}
}
