package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dao.EmpruntDao;
import com.locmns.locmns_backend.model.Emprunt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpruntService {

    protected final EmpruntDao empruntDao;

    public List<Emprunt> findListeEmpruntActif(){ return empruntDao.retourneListeEmpruntActif(); }

    public List<Emprunt> findListeEmprunt(){ return empruntDao.retourneListeDemande(); }

    public List<Emprunt> findDemandeEmprunt(){ return empruntDao.retourneListeDemande(); }

    public List<Emprunt> findEmpruntPanne(){ return empruntDao.retourneListePanne(); }

    public List<Emprunt> findEmpruntRecent(){ return empruntDao.retourneEmpruntRecent(); }

}
