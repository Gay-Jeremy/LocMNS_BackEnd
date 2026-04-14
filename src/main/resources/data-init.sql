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

INSERT INTO modele (libelle) VALUES
('Dell Latitude 5540'),
('HP EliteBook 840 G10'),
('Lenovo ThinkPad T14'),
('Apple MacBook Pro 14'),
('Dell OptiPlex 7010'),
('HP ProDesk 600 G9'),
('Samsung 27" S27A600'),
('LG 24" 24MK430H'),
('HP LaserJet Pro M404dn'),
('Canon imageRUNNER 2630'),
('Dell PowerEdge R750'),
('HP ProLiant DL380'),
('Apple iPad Pro 12.9'),
('Samsung Galaxy Tab S9'),
('iPhone 14 Pro'),
('Cisco Catalyst 2960'),
('Netgear GS308'),
('Cisco RV340'),
('Epson EB-L200F'),
('BenQ MH560');

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

INSERT INTO statut_materiel (libelle) VALUES
('Réception en cours'),
('En maintenance'),
('Hors service');

INSERT INTO materiel (num_serie,modele_id,salle_id,statut_materiel_id) VALUES
('PC 1', 1, 3, null),
('PC 2', 1, 3,2 ),
('PC 3', 1, 4, null);


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




INSERT INTO categorie_utilisateur (libelle) VALUES
('Administrateur'),
('Enseignant'),
('Étudiant');


INSERT INTO promotion (libelle) VALUES
('BUT Informatique 1ère année'),
('BUT Informatique 2ème année'),
('BUT Informatique 3ème année');


INSERT INTO utilisateur (nom, prenom, email,categorie_utilisateur_id,promotion_id) VALUES
('Dupont', 'Jean', 'jean.dupont@entreprise.fr', 1, 1),
('Martin', 'Sophie', 'sophie.martin@entreprise.fr', 2, 2),
('Bernard', 'Lucas', 'lucas.bernard@entreprise.fr', 3, 3);


INSERT INTO statut_emprunt (libelle) VALUES
('Demande en cours'),
('Refusé'),
('Accepté'),
('Demande Prolongation'),
('Demande Retour Anticipé'),
('Panne Signalé'),
('Prolongation Refusé'),
('Retour Anticipé Refusé');

INSERT INTO emprunt (date_creation, date_debut, date_fin, date_prolongation, date_retour_anticipe, date_retour_reel, id, materiel_id, statut_emprunt_id, utilisateur_id) VALUES
('2026-05-01','2026-05-01','2026-06-01',null,null,null,1,1,3,1),
('2026-01-12', '2026-01-20', '2026-02-20', null,        null,         null,         2,  2, 4, 2),
('2026-01-15', '2026-01-18', '2026-02-18', null,        null,         '2026-02-10', 3,  3, 5, 3),
('2026-01-20', '2026-01-25', '2026-02-25', '2026-03-01',null,         null,         4,  1, 6, 1),
('2026-02-01', '2026-02-05', '2026-03-05', null,        null,         null,         5,  2, 1, 2),
('2026-02-10', '2026-02-12', '2026-03-12', null,        '2026-02-28', '2026-02-28', 6,  3, 5, 3),
('2026-02-15', '2026-02-20', '2026-03-20', null,        null,         null,         7,  1, 4, 2),
('2026-03-01', '2026-03-05', '2026-04-05', '2026-04-20',null,         null,         8,  2, 6, 1),
('2026-03-10', '2026-03-15', '2026-04-15', null,        null,         '2026-04-01', 9,  3, 5, 3),
('2026-03-20', '2026-03-25', '2026-04-25', null,        null,         null,         10, 1, 1, 2);



INSERT INTO droit_emprunt_categorie_utilisateur_categorie_modele (peut_emprunter,categorie_modele_id, categorie_utilisateur_id) VALUES
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
