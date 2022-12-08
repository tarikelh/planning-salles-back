package fr.dawan.calendarproject.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.SkillDto;
import fr.dawan.calendarproject.services.SkillService;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

	@Autowired
	private SkillService skillService;

	// GET
	@GetMapping(produces = "application/json")
	public List<SkillDto> getAll() {
		return skillService.getAllSkills();
	}

	// GET
	@GetMapping(value = { "/pagination" }, produces = "application/json")
	public List<SkillDto> getAllPagination(
			@RequestParam(value = "page", defaultValue = "-1", required = false) int page,
			@RequestParam(value = "size", defaultValue = "-1", required = false) int size,
			@RequestParam(value = "search", defaultValue = "", required = false) String search) {
		return skillService.getAllSkills(page-1, size, search);
	}

	// COUNT
	@GetMapping(value = { "/count" }, produces = "application/json")
	public CountDto countFilter(@RequestParam(value = "search", defaultValue = "", required = false) String search) {
		return skillService.count(search);
	}

	// GET - id
	@GetMapping(value = "/{id}", produces = { "application/json", "application/xml" })
	public ResponseEntity<Object> getById(@PathVariable("id") long id) {
		SkillDto skill = skillService.getById(id);

		if (skill != null)
			return ResponseEntity.status(HttpStatus.OK).body(skill);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Skill with Id " + id + " Not Found");
	}

	// DELETE - supprimer
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<String> deleteById(@PathVariable(value = "id") long id) {
		try {
			skillService.deleteById(id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Skill with Id " + id + " Deleted");
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Skill with Id " + id + " Not Found");
		}
	}

	// POST - ajouter (ou modifier)
	@PostMapping(consumes = "application/json", produces = "application/json")
	public SkillDto save(@RequestBody SkillDto skill) {
		return skillService.saveOrUpdate(skill);
	}

	// PUT - modifier
	@PutMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> update(@RequestBody SkillDto skill) {
		SkillDto updatedSkill = skillService.saveOrUpdate(skill);

		if (updatedSkill != null)
			return ResponseEntity.status(HttpStatus.OK).body(updatedSkill);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Skill with Id " + skill.getId() + " Not Found");
	}
}
