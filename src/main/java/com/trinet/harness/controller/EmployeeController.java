package com.trinet.harness.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.trinet.harness.domain.Employee;
import com.trinet.harness.service.EmployeeService;

@RestController
public class EmployeeController {

	EmployeeService employeeService;
	
	EmployeeController(EmployeeService employeeService){
		this.employeeService = employeeService;
	}

	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> employees() {

		List<Employee> empList = employeeService.getEmployees();
		if (empList == null || empList.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(empList);
	}

	
	@PostMapping("/employees")
	public ResponseEntity<String> addEmployee(@RequestBody Employee emp) {

		employeeService.save(emp);

		return ResponseEntity.ok("created successfully");
	}
	
	
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable("id") Integer id) {

		employeeService.delete(id);

		return ResponseEntity.ok("deleted successfully");
	}
	
	 
}
