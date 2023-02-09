package fr.dawan.calendarproject.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.dawan.calendarproject.dto.CenterDto;
import fr.dawan.calendarproject.entities.Center;
import fr.dawan.calendarproject.mapper.CenterMapper;
import fr.dawan.calendarproject.repositories.CenterRepository;

@Service
@Transactional
public class CenterServiceImpl implements CenterService{

	@Autowired
	private CenterRepository centerRepository;
	@Autowired
	private CenterMapper centerMapper;
	
	@Override
	public List<CenterDto> getAllCenter() {
		List<Center> centers = centerRepository.findAll();

		List<CenterDto> result = new ArrayList<>();
		for (Center c : centers) {
			result.add(centerMapper.centerToCenterDto(c));
		}

		return result;
	}

}
