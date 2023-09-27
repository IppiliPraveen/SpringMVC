package com.springMvc.DAO;

import java.util.List;

import com.springMvc.model.Employ;
import com.springMvc.model.LogIn;


public interface EmployDAO {
	
	List<Employ> showEmploy();
	
	List<Employ> searchEmploy(String empno);
	
	void updateEmploy(Employ employ);
	
	void addEmploy(Employ employ);
	
	boolean login(String user, String password);
	
	String genarateEmpID();
	
	Employ getEmploy(String empNo);
	
	void newUserRegister(LogIn login);

}
