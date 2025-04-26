package com.gestionecole.service;

import com.gestionecole.model.Cours;
import com.gestionecole.model.Professeur;
import com.gestionecole.repository.CoursRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoursService {

    private final CoursRepository coursRepository;

    public CoursService(CoursRepository coursRepository) {
        this.coursRepository = coursRepository;
    }

    public List<Cours> getAllCours() {
        return coursRepository.findAll();
    }

    public Optional<Cours> getCoursById(Long id) {
        return coursRepository.findById(id);
    }

    public Cours saveCours(Cours cours) {
        return coursRepository.save(cours);
    }

    public void deleteCours(Long id) {
        coursRepository.deleteById(id);
    }

    public List<Cours> getCoursByProfesseur(Professeur professeur) {
        return coursRepository.findByProfesseur(professeur);
    }

}