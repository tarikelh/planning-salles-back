package fr.dawan.calendarproject.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.dawan.calendarproject.dto.CenterDto;
import fr.dawan.calendarproject.services.CenterService;

@RestController
@RequestMapping("/api/centers")
public class CenterController {
	
	@Autowired
	private CenterService centerService;
	
	//GET
	@GetMapping(produces="application/json")
	public List<CenterDto> getAll(){
		return centerService.getAllCenter();
	}

}
