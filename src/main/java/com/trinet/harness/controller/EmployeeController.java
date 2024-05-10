package com.trinet.harness.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trinet.harness.domain.FFRedisDto;
import com.trinet.harness.repo.CacheDataRepo;
import com.trinet.harness.service.FeatureFlagsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trinet.harness.domain.Employee;
import com.trinet.harness.service.EmployeeService;

@RestController
public class EmployeeController {

	Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	@Autowired
	EmployeeService employeeService;

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

	@GetMapping("/employees/workflow")
	public ResponseEntity<String> addEmployee(@RequestParam("status") String status) {
		logger.info("GitHub Actions workflow response status " + status);

		if (!status.equals("success")) {
			// call the servcie and update cache
		}

		return ResponseEntity.ok("Triggered");
	}


}
