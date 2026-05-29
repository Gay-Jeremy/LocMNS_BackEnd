package com.locmns.locmns_backend.security;

import com.locmns.locmns_backend.model.Utilisateur;
import com.locmns.locmns_backend.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilisateurDetailsService implements UserDetailsService {

    protected final UtilisateurRepository utilisateurRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Utilisateur> optionalUtilisateur = utilisateurRepository.findByEmail(email);

        if(optionalUtilisateur.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }

        return new UtilisateurDetails(optionalUtilisateur.get());

    }
}
