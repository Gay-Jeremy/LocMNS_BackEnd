package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dao.MaterielDao;
import com.locmns.locmns_backend.model.Materiel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MaterielService {

    protected final MaterielDao materielDao;

    public Optional<Materiel> obtenirMaterielParId(Integer id) {
        return materielDao.findById(id);
    }

    public List<Materiel> obtenirTousLesMateriels() {
        return materielDao.findAll();
    }

    public List<Materiel> obtenirMaterielsDisponibles() { return materielDao.retourneListeMaterielDisponible(); }

    public List<Materiel> obtenirMaterielsDisponiblesSurPeriode(LocalDate dateDebut, LocalDate dateFin) {
        return materielDao.materielDisponiblePeriode(dateDebut, dateFin);
    }

}
