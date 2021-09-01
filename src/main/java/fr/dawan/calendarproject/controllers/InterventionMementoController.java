package fr.dawan.calendarproject.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.dawan.calendarproject.entities.InterventionMemento;
import fr.dawan.calendarproject.services.InterventionCaretaker;

@RestController
@RequestMapping("/api/intervention-memento/")
public class InterventionMementoController {
	
	@Autowired
	private InterventionCaretaker caretaker;
	
	@GetMapping(value = "/{page}/{size}", produces="application/json")
	public List<InterventionMemento> getAll(@PathVariable("page") int page, @PathVariable("size") int size) {
		return caretaker.getAllMemento(page, size);
	}
}
