package com.maisprati.bike.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.maisprati.bike.entities.Bike;
import com.maisprati.bike.repositories.BikeRepository;
import com.maisprati.bike.services.exceptions.DatabaseException;
import com.maisprati.bike.services.exceptions.ResourceNotFoundException;

@Service
public class BikeService {

	@Autowired
	private BikeRepository repository;

	public List<Bike> findAll() {
		return repository.findAll();
	}

	public Bike findById(Long id) {
		Optional<Bike> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}

	// Operações DB

	// insere
	public Bike insert(Bike obj) {
		return repository.save(obj);

	}

	// delete
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	// atualizar

	public Bike update(Long id, Bike obj) {
		try {
			Bike entity = repository.getOne(id);// prepara o obj monitorado para mexer e depois alterar no DB
			updateData(entity, obj);
			return repository.save(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(Bike entity, Bike obj) {
		entity.setDescription(obj.getDescription());
		entity.setModel(obj.getModel());
		entity.setPricePaid(obj.getPricePaid());
		entity.setPurchaseDate(obj.getPurchaseDate());

	}
}
