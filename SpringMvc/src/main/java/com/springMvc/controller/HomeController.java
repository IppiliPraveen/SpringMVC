package com.springMvc.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.springMvc.Constants.MvcConstants;
import com.springMvc.DAO.EmployDAO;
import com.springMvc.DAO.EmployDAOImpl;
import com.springMvc.model.Employ;
import com.springMvc.model.LogIn;
import com.springMvc.model.Messages;

@Controller
public class HomeController {
	
	@Autowired
	EmployDAO dao;
	 
	
	private final static Log LOG = LogFactory.getLog(EmployDAOImpl.class.getName());
	
	@RequestMapping(value="/")
	public ModelAndView home(ModelAndView model) throws IOException{
		model.setViewName("index");
	     return model;
	}
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login(Model model){
		 model.addAttribute("login", new LogIn());
		 return "login"; 
		 
	}
	
	@RequestMapping(value="/searchemploy", method = RequestMethod.GET)
	public String search(@ModelAttribute("employ") Employ employ){
		 return "SearchEmploy"; 
		 
	}
	
	@RequestMapping(value="/closeEmpSearch", method = RequestMethod.POST)
	public ModelAndView closeSearch(ModelAndView model){
		return new ModelAndView("redirect:/login");
		 
	}
	
	@RequestMapping(value="/closeupdate", method = RequestMethod.POST)
	public ModelAndView closeUpade(ModelAndView model){
		return new ModelAndView("redirect:/result");
		 
	}
	
	@RequestMapping(value="/closeEmpResults", method = RequestMethod.POST)
	public ModelAndView closeResults(ModelAndView model){
		return new ModelAndView("redirect:/searchemploy");
		 
	}
	
	@RequestMapping(value="/logout", method = RequestMethod.POST)
	public ModelAndView logout(ModelAndView model){
		return new ModelAndView("redirect:/login");
		 
	}
	
	@RequestMapping(value="/addemploy", method = RequestMethod.GET)
	public String addEmploy(Model model){
		Employ employ=new Employ();
		employ.setEmpNo(dao.genarateEmpID());
		model.addAttribute("employ", employ);
		return "AddEmploy";
		 
	}
	
	@RequestMapping(value="/newEmploy", method = RequestMethod.POST)
	public ModelAndView newEmploy(@ModelAttribute("employ") Employ employ,ModelAndView model){
		Messages msgs=new Messages();
		String sb="";
		boolean flag=true;
		if (employ.getName() == null || employ.getName() == "") {
			sb+="Please Enter Employ Name <br>";
			flag=false;
		}
		if (employ.getGender() == null || String.valueOf(employ.getGender()) == "") {
			sb+="Please Enter Employ Gender <br>";
			flag=false;
		}
		if (employ.getDept() == null || employ.getDept() == "") {
			sb+="Please Enter Employ Department <br>";
			flag=false;
		}
		if (employ.getDesig() == null || employ.getDesig() == "") {
			sb+="Please Enter Employ Designation <br>";
			flag=false;
		}
		msgs.setMsg(sb);
		if(!flag) {
			model.addObject("messages", msgs);
			employ.setEmpNo(dao.genarateEmpID());
			model.addObject("employ", employ);
			model.setViewName("AddEmploy");
			return model;
		}
		dao.addEmploy(employ);
		return new ModelAndView("redirect:/searchemploy");
		 
	}
	
	 @RequestMapping(value="/loginDetails", method = RequestMethod.POST)
	 public ModelAndView loginDetails(@ModelAttribute("login") LogIn login,ModelAndView model) {
		 Messages msgs=new Messages();
		 boolean isLogin=dao.login(login.getUser() ,login.getPassword());
		  if(isLogin) {
			  model.addObject("login", login);
			  model.addObject("messages", msgs);
			  return new ModelAndView("redirect:/searchemploy");
		  }
		  msgs.setMsg("Incorrect User Name or Password");
		  model.addObject("login", login);
		  model.addObject("messages", msgs);
		  model.setViewName("login");
		  return model;
	 }
	
	@RequestMapping(value="/result")
	public ModelAndView listEmploy(ModelAndView model,HttpServletRequest req, @RequestParam String clickedButton) throws IOException{
		  Messages msgs=new Messages();
			if (!"Reset".equals(clickedButton)) {

				List<Employ> listEmploy = null;
				String empNo = MvcConstants.NULL;
				
				if (req.getParameter("empNo") != null && !req.getParameter("empNo").equals("")) {
					
					empNo = req.getParameter("empNo");
					listEmploy = dao.searchEmploy(empNo);
				} else {
					
					listEmploy = dao.showEmploy();
				}
				
				if (listEmploy.size() > 0) {

					model.addObject("listEmploy", listEmploy);
					model.setViewName("home");

					return model;
				} else {
					msgs.setMsg("Incorrect Employ ID");
					model.addObject("messages", msgs);
					model.addObject("employ", new Employ());
					model.setViewName("SearchEmploy");
					return model;
				}
			}
			return new ModelAndView("redirect:/searchemploy");
	}
	@RequestMapping(value="/register", method = RequestMethod.GET)
	public String newlogin(Model model){
		 model.addAttribute("login", new LogIn());
		 return "Register"; 
		 
	}
	@RequestMapping(value="/registerUser", method = RequestMethod.POST)
	 public ModelAndView registerNewUser(@ModelAttribute("login") LogIn login) {
		 dao.newUserRegister(login);
		 return new ModelAndView("redirect:/login");
	 }
	@RequestMapping(value="/editemploy",method = RequestMethod.GET)
	public ModelAndView editEmploy(ModelAndView model,HttpServletRequest req,@RequestParam("empno") String empNo){
		List<Employ> listEmploy = null;
		Employ employ = new Employ();

		listEmploy = dao.searchEmploy(empNo);
		LOG.info("Employ object -------> "+listEmploy.get(0));
		employ = (Employ) listEmploy.get(0); 
		model.addObject("employ", employ);
		model.setViewName("UpdateEmploy");
		return model;

	}
	@RequestMapping(value="/updateEmploy",method = RequestMethod.POST)
	public ModelAndView UpdateEmploy(@ModelAttribute("employ") Employ employ){
		dao.updateEmploy(employ);
		return new ModelAndView("redirect:/searchemploy");

	}
}
