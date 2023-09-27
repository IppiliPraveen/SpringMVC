package com.springMvc.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.springMvc.Constants.MvcConstants;
import com.springMvc.model.Employ;
import com.springMvc.model.Gender;
import com.springMvc.model.LogIn;
import com.springMvc.model.Messages;

public class EmployDAOImpl implements EmployDAO{
	
	private final static Log LOG = LogFactory.getLog(EmployDAOImpl.class.getName());
	
	private JdbcTemplate jdbcTemplate;
	
	public EmployDAOImpl(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public List<Employ> showEmploy() {
		
		LOG.info(" ------> Entering showEmploy() method");
		String sql = "SELECT * FROM Employ WHERE status='a'";
		LOG.info("Query ::::::: " + sql);
	    List<Employ> listEmploy = jdbcTemplate.query(sql, new RowMapper<Employ>() {
	 
	        @Override
	        public Employ mapRow(ResultSet rs, int rowNum) throws SQLException {
	            Employ employ = new Employ();
	            employ.setEmpNo(rs.getString("empno"));;
	            employ.setName(rs.getString("name"));
	            employ.setGender(Gender.valueOf(rs.getString("gender")));
	            employ.setDept(rs.getString("dept"));
	            employ.setDesig(rs.getString("desig"));
	            employ.setSalary(rs.getInt("basic"));
	            employ.setStatus(rs.getString("status"));
	            LOG.info("Status --------> "+employ.getStatus());
	            return employ;
	        }
	 
	    });
	    LOG.info(" ------> Ending showEmploy() method");
	    return listEmploy;
	}

	@Override
	public List<Employ> searchEmploy(String empno) {
		// TODO Auto-generated method stub
		LOG.info(" ------> Starting searchEmploy() method");
		LOG.info("Empno ::::::: "+empno);
		String sql="SELECT * FROM EMPLOY WHERE empno='"+empno+"' AND status='a'";
		List<Employ> employ=jdbcTemplate.query(sql, new RowMapper<Employ>() {

			@Override
			public Employ mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				Employ emp=new Employ();
				emp.setEmpNo(rs.getString("empno"));
				emp.setName(rs.getString("name"));
				emp.setGender(Gender.valueOf(rs.getString("gender")));
				emp.setDept(rs.getString("dept"));
				emp.setDesig(rs.getString("desig"));
	            emp.setSalary(rs.getInt("basic"));
	            emp.setStatus(rs.getString("status"));
	            LOG.info("Status --------> "+emp.getStatus());
	            return emp;
			}
			
		});
		return employ;
	}
	
	@Override
	public Employ getEmploy(String empNo) {
		String sql = "select * from EMPLOY where empno=?";
	    return jdbcTemplate.query(sql,new Object[] {empNo}, new ResultSetExtractor<Employ>() {
	        public Employ extractData(ResultSet rs) throws SQLException,
	                DataAccessException {
	            if (rs.next()) {
	                Employ employ = new Employ();
		            employ.setEmpNo(rs.getString("empno"));
		            employ.setName(rs.getString("name"));
		            employ.setGender(Gender.valueOf(rs.getString("gender")));
		            employ.setDept(rs.getString("dept"));
		            employ.setSalary(rs.getInt("basic"));;
		            return employ;
	            }
	 
	            return null;
	        }
	 
	    });
	}

	@Override
	public void updateEmploy(Employ employ) {
		String cmd = "Update employ set name=?, Gender=?, Dept=?, Desig=?, " + " Basic=?, status=? WHERE Empno=?";
		
		LOG.info("ID     ---------------> "+employ.getEmpNo());
		LOG.info("Name   ---------------> "+employ.getName());
		LOG.info("Gender ---------------> "+employ.getGender().toString());
		LOG.info("Dept   ---------------> "+employ.getDept());
		LOG.info("Desig  ---------------> "+employ.getDesig());
		LOG.info("Salary ---------------> "+employ.getSalary());
		LOG.info("Status ---------------> "+employ.getStatus());

		jdbcTemplate.update(cmd, employ.getName(), employ.getGender().toString(), employ.getDept(), employ.getDesig(),
				employ.getSalary(), employ.getStatus(), employ.getEmpNo());

	}

	@Override
	public void addEmploy(Employ employ) {
		String cmd = "insert into employ(empno,name,gender,dept,desig,"
				+ "basic, status) values(?,?,?,?,?,?, 'a')";
		jdbcTemplate.update(cmd, employ.getEmpNo(),employ.getName(),
				 employ.getGender().toString(), 
				 employ.getDept(),employ.getDesig(),employ.getSalary());	
	}

	@Override
	public boolean login(String user, String password) {
		// TODO Auto-generated method stub
		LOG.info(" ------> Entering login() method");
		int count=0;
		if (user != null && password != null) {

			 String sql = "SELECT count(*) FROM login WHERE userName='" + user + "' AND password='" + password + "'";
			 LOG.info("Query ::::::: " + sql);
			 
			 count = jdbcTemplate.queryForObject(sql, Integer.class);
			 LOG.info("Count ::::::: " + count);
		}
		LOG.info(" ------> Ending login() method");
	    if(count==1) {
	    	
	    	return true;
	    }
	    Messages msgs=new Messages();
	    msgs.setMsg("Incorrect User Name or Password");
	    return false;
		
	}

	@Override
	public String genarateEmpID() {
		
		LOG.info(" ------> Entering genarateEmpID() method");
		int count=MvcConstants.ZERO;
		String sql = "SELECT COUNT(empno) FROM Employ";
		LOG.info("Query ------> " + sql);
		
	    count = jdbcTemplate.queryForObject(sql, Integer.class)+1; 
	    LOG.info("Count ------> " + count);
	    if (count < 10) {
	    	LOG.info(" < 10 ------> Ending genarateEmpID() method");
	        return "EHYD00" + count;
	    } else if (count < 100) {
	    	LOG.info(" < 100 ------> Ending genarateEmpID() method");
	        return "EHYD0" + count;
	    } else if (count < 1000) {
	    	LOG.info(" < 1000 ------> Ending genarateEmpID() method");
	        return "EHYD" + count;
	    }
	    LOG.info(" ------> Ending genarateEmpID() method");
	    return "";
	}

	@Override
	public void newUserRegister(LogIn login) {
		// TODO Auto-generated method stub
		String cmd = "insert into login(userName,password) values(?,?)";
		jdbcTemplate.update(cmd, login.getUser(),login.getPassword());	
	}
		
}
