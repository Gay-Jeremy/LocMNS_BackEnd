INSERT INTO type_document ( libelle) VALUES
( 'Documentation technique'),
( 'Manuel utilisateur'),
( 'Facture'),
('Bon de livraison'),
('Fiche technique');

INSERT INTO categorie_modele (libelle) VALUES
('Ordinateur portable'),
('Ordinateur fixe'),
('Écran'),
('Imprimante'),
('Serveur'),
('Tablette'),
('Téléphone'),
('Switch réseau'),
('Routeur'),
('Projecteur');

INSERT INTO modele (libelle, categorie_modele_id) VALUES
('Dell Latitude 5540', 1),
('HP EliteBook 840 G10', 1),
('Lenovo ThinkPad T14', 1),
('Apple MacBook Pro 14', 1),
('Dell OptiPlex 7010', 2),
('HP ProDesk 600 G9', 2),
('Samsung 27" S27A600', 3),
('LG 24" 24MK430H', 3),
('HP LaserJet Pro M404dn', 4),
('Canon imageRUNNER 2630', 4),
('Dell PowerEdge R750', 5),
('HP ProLiant DL380', 5),
('Apple iPad Pro 12.9', 6),
('Samsung Galaxy Tab S9', 6),
('iPhone 14 Pro', 7),
('Cisco Catalyst 2960', 8),
('Netgear GS308', 8),
('Cisco RV340', 9),
('Epson EB-L200F', 10),
('BenQ MH560', 10);

INSERT INTO documentation (titre, url, format, type_document_id) VALUES
('Guide installation Dell Latitude 5540', 'https://docs.dell.com/latitude-5540-install', 'PDF',  1),
('Manuel utilisateur HP EliteBook 840', 'https://support.hp.com/elitebook840-manual', 'PDF', 2),
('Fiche technique Lenovo ThinkPad T14', 'https://psref.lenovo.com/thinkpad-t14', 'PDF',  5),
('Facture MacBook Pro 14 lot 2023', 'https://intranet.entreprise.fr/factures/mbp14-2023', 'PDF',  3),
('Guide administrateur Dell PowerEdge R750', 'https://docs.dell.com/poweredge-r750-admin', 'PDF',  1),
('Manuel HP LaserJet Pro M404dn', 'https://support.hp.com/laserjet-m404dn', 'PDF',  2),
('Fiche technique Cisco Catalyst 2960', 'https://cisco.com/catalyst-2960-datasheet', 'PDF',  5),
('Guide installation Epson EB-L200F', 'https://epson.com/ebl200f-install', 'PDF',  1);

INSERT INTO salle (libelle) VALUES
('Salle serveur RDC'),
('Bureau direction'),
('Open space 1er étage'),
('Open space 2ème étage'),
('Salle de réunion A'),
('Salle de réunion B'),
('Laboratoire informatique'),
('Accueil'),
('Comptabilité'),
('Ressources humaines');

INSERT INTO statut_materiel (libelle,code) VALUES
('Réception en cours', 'RECEPTION'),
('Dysfonctionnement', 'INCIDENT'),
('Panne', 'PANNE'),
('En maintenance', 'MAINTENANCE'),
('Hors service', 'HORS_SERVICE');

INSERT INTO materiel (num_serie, modele_id, salle_id, statut_materiel_id) VALUES
('PC 1', 1, 3, null),
('PC 2', 1, 3, 2),
('PC 3', 1, 4, null),
('PC 4', 1, 4, null),
('PC 5', 1, 4, null);


INSERT INTO composant (libelle) VALUES
('RAM 8 Go DDR4'),
('RAM 16 Go DDR4'),
('RAM 32 Go DDR4'),
('SSD 256 Go'),
('SSD 512 Go'),
('SSD 1 To'),
('Batterie 54Wh'),
('Batterie 86Wh'),
('Carte Wi-Fi Intel AX201'),
('Carte Wi-Fi Intel AX211'),
('Processeur Intel Core i5-1235U'),
('Processeur Intel Core i7-1265U'),
('Processeur Apple M2 Pro'),
('Processeur Intel Xeon Silver 4314'),
('GPU NVIDIA RTX 3050'),
('Webcam HD 720p'),
('Webcam FHD 1080p'),
('Lecteur d''empreintes digitales'),
('Module TPM 2.0'),
('Dock USB-C');


INSERT INTO categorie_utilisateur (libelle, code) VALUES
('Administrateur', 'ADMIN'),
('Enseignant', 'USER'),
('Étudiant', 'USER');


INSERT INTO promotion (libelle) VALUES
('BUT Informatique 1ère année'),
('BUT Informatique 2ème année'),
('BUT Informatique 3ème année');


INSERT INTO utilisateur (nom, prenom, email,categorie_utilisateur_id,promotion_id, password) VALUES
('Dupont', 'Jean', 'jean.dupont@entreprise.fr', 1, 1,'$2a$10$N6SDVhhWyff0judQiQpZDOrV8HtUYbNOcP0IgG1BRg3mR5YhDvgA2'),
('Martin', 'Sophie', 'sophie.martin@entreprise.fr', 2, 2, '$2a$10$N6SDVhhWyff0judQiQpZDOrV8HtUYbNOcP0IgG1BRg3mR5YhDvgA2'),
('Bernard', 'Lucas', 'lucas.bernard@entreprise.fr', 3, 3, '$2a$10$N6SDVhhWyff0judQiQpZDOrV8HtUYbNOcP0IgG1BRg3mR5YhDvgA2');


INSERT INTO statut_emprunt (libelle,code) VALUES
('Demande en cours','DEMANDE_EN_COURS'),
('Refuser','REFUSE'),
('Accepter','ACCEPTE'),
('Demande Prolongation','DEMANDE_PROLONGATION'),
('Demande Retour Anticipé','DEMANDE_RETOUR_ANTICIPE'),
('Prolongation Refusé', 'PROLONGATION_REFUSE'),
('Retour Anticipé Refusé', 'DEMANDE_RETOUR_ANTICIPE_REFUSE');


INSERT INTO emprunt (date_creation, date_debut, date_fin, date_prolongation, date_retour_anticipe, date_retour_reel, materiel_id, statut_emprunt_id, utilisateur_id) VALUES
-- PC 1 : emprunté sur la période, statut ACCEPTÉ (id=3) → EXCLU
('2026-05-01','2026-03-10','2026-04-20', null, null, null, 1, 3, 1),

-- PC 2 : emprunté sur la période, statut REFUSÉ (id=2) → INCLUS (car != 2 est faux, statut=2)
('2026-05-01','2026-03-10','2026-04-20', null, null, null, 2, 2, 1),

-- PC 3 : emprunté MAIS date_retour_reel renseigné → INCLUS (déjà rendu)
('2026-05-01','2026-03-10','2026-04-20', null, null, '2026-05-15', 3, 3, 1),

-- PC 4 : emprunté hors période (après) → INCLUS
('2026-05-01','2026-05-25','2026-06-05', null, null, null, 4, 3, 1),
-- PC 1 : 2ème emprunt → chevauchement partiel gauche (commence avant, finit pendant)
('2026-05-01','2026-05-08','2026-08-13', null, null, null, 1, 3, 2),

-- PC 3 : ré-emprunté après son retour (ne doit pas bloquer)
('2026-05-16','2026-05-16','2026-05-30', null, null, null, 3, 1, 2),

-- PC 4 : emprunté avec statut "Demande en cours" sur la période → EXCLU (statut 1 != 2)
('2026-05-01','2026-05-12','2026-05-18', null, null, null, 4, 1, 1),

-- PC 5 : emprunt dont la borne exacte touche la période (retour le 12 mai, demande le 12 mai)
('2026-05-01','2026-05-05','2026-05-12', null, null, null, 5, 3, 1);



INSERT INTO droit_emprunt(peut_emprunter,categorie_modele_id, categorie_utilisateur_id) VALUES
(true, 1, 1),
(true, 1, 2),
(true, 1, 3);


INSERT INTO composant_categorie_modele (categorie_modele_id, composant_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(2, 5),
(2, 6),
(3, 7),
(3, 8);


INSERT INTO caracteristique_materiel_composant (valeur, materiel_id, composant_id) VALUES
('16 Go', 1, 2),
('512 Go', 1, 5),
('Wi-Fi AX201', 1, 9),
('i7-1265U', 1, 12),
('16 Go', 2, 2),
('256 Go', 2, 4),
('i5-1235U', 2, 11);
