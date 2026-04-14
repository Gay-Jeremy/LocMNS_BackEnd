package com.locmns.locmns_backend.services;

import com.jeremy.demo_spring_locmns.dao.MaterielDao;
import com.jeremy.demo_spring_locmns.model.Materiel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MaterielService {

    protected final MaterielDao materielDao;

    // Optional pour éviter les NullPointerException et gérer les erreurs métier
    public Optional<Materiel> findById(Integer id) {
        return materielDao.findById(id);
    }

    public List<Materiel> findAll() {
        return materielDao.findAll();
    }

    public List<Materiel> findDisponible() { return materielDao.retourneListeMaterielDisponible(); }

    public List<Materiel> findDisponiblePeriode(LocalDate dateDebut, LocalDate dateFin) {
        return materielDao.materielDisponiblePeriode(dateDebut, dateFin);
    }


}
