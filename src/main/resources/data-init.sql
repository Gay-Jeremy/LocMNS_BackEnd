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


-- Le libellé décrit l'état de l'emprunt (« Accepté »), affiché dans les listes.
-- Le texte des actions (« Accepter ») est défini par le frontend à partir du code.
INSERT INTO statut_emprunt (libelle,code) VALUES
('Demande en cours','DEMANDE_EN_COURS'),
('Refusé','REFUSE'),
('Accepté','ACCEPTE'),
('Prolongation demandée','DEMANDE_PROLONGATION'),
('Retour anticipé demandé','DEMANDE_RETOUR_ANTICIPE'),
('Prolongation refusée', 'PROLONGATION_REFUSE'),
('Retour anticipé refusé', 'DEMANDE_RETOUR_ANTICIPE_REFUSE'),
('Prolongation acceptée', 'PROLONGATION_ACCEPTE'),
('Retour anticipé accepté', 'DEMANDE_RETOUR_ANTICIPE_ACCEPTE');


-- ============================================================
-- Jeu d'essai pour la disponibilité et la création d'un emprunt
-- Période de référence utilisée pour tester la disponibilité : du 2027-06-01 au 2027-06-10
-- ============================================================
INSERT INTO materiel (num_serie, modele_id, salle_id, statut_materiel_id) VALUES
('PC 1', 1, 3, null),   -- Aucun emprunt, aucun statut technique -> DISPONIBLE
('PC 2', 1, 3, 2),      -- Statut technique "Dysfonctionnement", aucun emprunt -> INDISPONIBLE
('PC 3', 1, 4, null),   -- Emprunt ACCEPTE chevauchant totalement la période -> INDISPONIBLE
('PC 4', 1, 4, null),   -- Emprunt REFUSE sur les mêmes dates -> DISPONIBLE
('PC 5', 1, 4, null),   -- Emprunt ACCEPTE mais déjà rendu (date_retour_reel renseignée) -> DISPONIBLE
('PC 6', 1, 4, null),   -- Emprunt ACCEPTE chevauchement partiel (commence avant, finit pendant) -> INDISPONIBLE
('PC 7', 1, 4, null),   -- Emprunt ACCEPTE chevauchement partiel (commence pendant, finit après) -> INDISPONIBLE
('PC 8', 1, 4, null),   -- Emprunt ACCEPTE totalement hors période -> DISPONIBLE
('PC 9', 1, 4, null),   -- Emprunt ACCEPTE dont la fin touche exactement le début de la période testée -> INDISPONIBLE
('PC 10', 1, 4, null);  -- Demande d'emprunt en attente (DEMANDE_EN_COURS) sur la période -> INDISPONIBLE

INSERT INTO emprunt (date_creation, 
                    date_debut, 
                    date_fin, 
                    date_prolongation, 
                    date_retour_anticipe, 
                    date_retour_reel, 
                    materiel_id, 
                    statut_emprunt_id, 
                    utilisateur_id) VALUES

-- PC 3 : chevauchement total avec la période de référence -> doit être EXCLU des disponibles
('2027-05-20','2027-06-02','2027-06-08', null, null, null, 3, 3, 1),

-- PC 4 : mêmes dates que PC3, mais emprunt REFUSE (id=2) -> doit rester DISPONIBLE malgré le chevauchement
('2027-05-20','2027-06-02','2027-06-08', null, null, null, 4, 2, 1),

-- PC 5 : emprunt ACCEPTE mais déjà rendu -> doit rester DISPONIBLE, le retour effectif libère le matériel
('2027-05-20','2027-06-02','2027-06-08', null, null, '2027-06-05', 5, 3, 1),

-- PC 6 : commence avant la période demandée, se termine pendant -> chevauchement partiel, doit être EXCLU
('2027-05-20','2027-05-28','2027-06-03', null, null, null, 6, 3, 1),

-- PC 7 : commence pendant la période demandée, se termine après -> chevauchement partiel, doit être EXCLU
('2027-05-20','2027-06-09','2027-06-15', null, null, null, 7, 3, 1),

-- PC 8 : entièrement terminé avant le début de la période testée -> doit rester DISPONIBLE
('2027-05-01','2027-05-10','2027-05-18', null, null, null, 8, 3, 1),

-- PC 9 : se termine exactement le jour où débute la période testée -> teste la borne inclusive (<=/>=), doit être EXCLU
('2027-05-15','2027-05-25','2027-06-01', null, null, null, 9, 3, 1),

-- PC 10 : demande encore en attente de validation (statut DEMANDE_EN_COURS, id=1) -> doit être EXCLU, une demande non tranchée bloque aussi
('2027-06-01','2027-06-03','2027-06-07', null, null, null, 10, 1, 1);



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
