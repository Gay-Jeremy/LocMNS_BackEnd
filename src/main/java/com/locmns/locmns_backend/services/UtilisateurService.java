package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.model.Utilisateur;
import com.locmns.locmns_backend.repository.UtilisateurDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final PasswordEncoder encoder ;

    private final UtilisateurDao utilisateurDao;

    public void insert(Utilisateur utilisateur) {
        utilisateur.setId(null);
        utilisateur.setPassword(encoder.encode(utilisateur.getPassword()));
        utilisateurDao.save(utilisateur);
    }

}
