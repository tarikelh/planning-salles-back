package fr.dawan.calendarproject.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.ResourceDto;
import fr.dawan.calendarproject.entities.Resource;
import fr.dawan.calendarproject.mapper.ResourceMapper;
import fr.dawan.calendarproject.repositories.ResourceRepository;

@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

	@Autowired
	ResourceRepository resourceRepository;

	@Autowired
	private ResourceMapper resourceMapper;
	
	
	@Override
	public List<ResourceDto> getAllResources() {
		List<Resource> resource = resourceRepository.findAll();

		List<ResourceDto> result = new ArrayList<>();
		for (Resource r : resource) {
			result.add(resourceMapper.resourceToResourceDto(r));
		}

		return result;
	}

	@Override
	public List<ResourceDto> getAllResources(int page, int max, String search) {
		Pageable pagination = null;

		if (page > -1 && max > 0)
			pagination = PageRequest.of(page, max);
		else
			pagination = Pageable.unpaged();

		List<Resource> resource = resourceRepository.findAllByNameContaining(search, pagination).get()
				.collect(Collectors.toList());

		List<ResourceDto> result = new ArrayList<>();
		for (Resource r : resource) {
			result.add(resourceMapper.resourceToResourceDto(r));
		}

		return result;
	}

	@Override
	public List<ResourceDto> getResourceByRoomAvailability(boolean availability) {
		List<Resource> resource = resourceRepository.findByRoomAvailability(availability);

		List<ResourceDto> result = new ArrayList<>();
		for (Resource r : resource) {
			result.add(resourceMapper.resourceToResourceDto(r));
		}

		return result;
	}

	@Override
	public List<ResourceDto> getResourceByQuantity(int quantity) {
		List<Resource> resource = resourceRepository.findByQuantity(quantity);

		List<ResourceDto> result = new ArrayList<>();
		for (Resource r : resource) {
			result.add(resourceMapper.resourceToResourceDto(r));
		}

		return result;
	}

	@Override
	public List<ResourceDto> getResourceByQuantityRange(int value1, int value2) {
		List<Resource> resource = resourceRepository.findByQuantityRange(value1, value2);

		List<ResourceDto> result = new ArrayList<>();
		for (Resource r : resource) {
			result.add(resourceMapper.resourceToResourceDto(r));
		}

		return result;
	}

	@Override
	public ResourceDto getById(long id) {
		Optional<Resource> r = resourceRepository.findById(id);
		if (r.isPresent())
			return resourceMapper.resourceToResourceDto(r.get());
		return null;
	}

	@Override
	public CountDto count(String search) {
		return new CountDto(resourceRepository.countByNameContaining(search));
	}

	@Override
	public void deleteById(long id) {
		resourceRepository.deleteById(id);
		
	}

	@Override
	public ResourceDto saveOrUpdate(ResourceDto rDto) {

		if (rDto.getId() > 0 && !resourceRepository.findById(rDto.getId()).isPresent())
			return null;

		Resource r = resourceMapper.resourceDtoToResource(rDto);

		try {
			r = resourceRepository.saveAndFlush(r);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resourceMapper.resourceToResourceDto(r);
	}

}
