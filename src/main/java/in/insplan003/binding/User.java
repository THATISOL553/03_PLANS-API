package in.insplan003.binding;

import java.time.LocalDate;

import lombok.Data;

@Data
public class User {
	
	private String FullName;
	
	private String email;
	
	private Long mobile;
	
	private String gender;
	
	private LocalDate date;
	
	private Long ssn;
}
