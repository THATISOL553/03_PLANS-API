package in.insplan003.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.insplan003.entity.UserMaster;

public interface UserMasterRepo extends JpaRepository<UserMaster, Integer>{
	
	public UserMaster findByEmail(String email);
	
}
