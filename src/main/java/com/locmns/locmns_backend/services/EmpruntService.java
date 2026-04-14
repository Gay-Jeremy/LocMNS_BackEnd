package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dao.EmpruntDao;
import com.locmns.locmns_backend.model.Emprunt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class EmpruntService {

    protected final EmpruntDao empruntDao;
    
    public Optional<Emprunt> obtenirEmpruntsParId(Integer id){ return empruntDao.findById(id); }
    
    public List<Emprunt> obtenirTousLesEmprunts(){ return empruntDao.findAll(); }

    public List<Emprunt> obtenirEmpruntsActifs(){ return empruntDao.retourneListeEmpruntActif(); }

    public List<Emprunt> obtenirDemandesEmprunt(){ return empruntDao.retourneListeDemandeEmprunt(); }

    public List<Emprunt> obtenirPannesSignalees(){ return empruntDao.retourneListePanne(); }

    public List<Emprunt> obtenirEmpruntsRecents(){ return empruntDao.retourneEmpruntRecent(); }

}
