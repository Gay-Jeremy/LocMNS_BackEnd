package com.locmns.locmns_backend.mock;

import com.locmns.locmns_backend.repository.EmpruntRepository;
import com.locmns.locmns_backend.model.Emprunt;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MockEmpruntRepository implements EmpruntRepository {


    @Override
    public Optional<Emprunt> findById(Integer id) {
        if (id == 1) {
            Emprunt emprunt = new Emprunt();
            emprunt.setId(1);
            return Optional.of(emprunt);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer id) {
        return id == 1;
    }

    @Override
    public void deleteById(Integer id) {
        // on ne fait rien — pas de vraie base
    }

    @Override
    public <S extends Emprunt> S save(S entity) {
        return entity;
    }

    // ── Méthodes custom du DAO ──────────────────────────

    @Override
    public List<Emprunt> retourneListeEmpruntActif(String code) { return List.of(); }

    @Override
    public List<Emprunt> retourneListeEmpruntActifParEmail(String code, String email) {
        return List.of();
    }

    @Override
    public List<Emprunt> retourneListeDemandeEmprunt(List<String> code) { return List.of(); }

    @Override
    public List<Emprunt> retourneListeDemandeEmpruntParEmail(List<String> codes, String email) {
        return List.of();
    }

    @Override
    public List<Emprunt> retourneEmpruntRecent(LocalDate dateDebut) { return List.of(); }

    @Override
    public List<Emprunt> retourneEmpruntRecentParEmail(LocalDate dateDebut, String email) {
        return List.of();
    }

    @Override
    public List<Emprunt> retourneEmpruntEnRetard() { return List.of(); }

    @Override
    public List<Emprunt> retourneEmpruntEnRetardParEmail(String email) {
        return List.of();
    }

    @Override
    public List<Emprunt> retourneHistoriqueEmprunt() {
        return List.of();
    }

    @Override
    public List<Emprunt> retourneHistoriqueEmpruntParEmail(String email) {
        return List.of();
    }

    @Override
    public boolean existsEmpruntActifParMaterielId(int materielId) {
        return false;
    }

    @Override
    public boolean existsConflitEmpruntSurMaterielPourPeriode(int materielId, int empruntId, LocalDate dateDebut, LocalDate dateFin) {
        return false;
    }

    @Override
    public void deleteByMaterielId(int materielId) {

    }


    // ── Méthodes JpaRepository obligatoires ────────────

    @Override public void flush() {}
    @Override public <S extends Emprunt> S saveAndFlush(S entity) { return entity; }
    @Override public <S extends Emprunt> List<S> saveAllAndFlush(Iterable<S> entities) { return List.of(); }
    @Override public void deleteAllInBatch(Iterable<Emprunt> entities) {}
    @Override public void deleteAllByIdInBatch(Iterable<Integer> integers) {}
    @Override public void deleteAllInBatch() {}
    @Override public Emprunt getOne(Integer integer) { return null; }
    @Override public Emprunt getById(Integer integer) { return null; }
    @Override public Emprunt getReferenceById(Integer integer) { return null; }
    @Override public <S extends Emprunt> Optional<S> findOne(Example<S> example) { return Optional.empty(); }
    @Override public <S extends Emprunt> List<S> findAll(Example<S> example) { return List.of(); }
    @Override public <S extends Emprunt> List<S> findAll(Example<S> example, Sort sort) { return List.of(); }
    @Override public <S extends Emprunt> Page<S> findAll(Example<S> example, Pageable pageable) { return null; }
    @Override public <S extends Emprunt> long count(Example<S> example) { return 0; }
    @Override public <S extends Emprunt> boolean exists(Example<S> example) { return false; }
    @Override public <S extends Emprunt, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) { return null; }
    @Override public <S extends Emprunt> List<S> saveAll(Iterable<S> entities) { return List.of(); }
    @Override public List<Emprunt> findAll() { return List.of(); }
    @Override public List<Emprunt> findAllById(Iterable<Integer> integers) { return List.of(); }
    @Override public long count() { return 0; }
    @Override public void delete(Emprunt entity) {}
    @Override public void deleteAllById(Iterable<? extends Integer> integers) {}
    @Override public void deleteAll(Iterable<? extends Emprunt> entities) {}
    @Override public void deleteAll() {}
    @Override public List<Emprunt> findAll(Sort sort) { return List.of(); }
    @Override public Page<Emprunt> findAll(Pageable pageable) { return null; }
}