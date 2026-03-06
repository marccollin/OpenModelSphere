# OpenModelSphere

OpenModelSphere est l'un des outils de modélisation open-source les plus complets pour la conception de systèmes d'information. 
Il couvre la modélisation de données (relationnelle et physique), la modélisation de processus d'affaires et la modélisation UML.

![OpenModelSphere](https://github.com/marccollin/OpenModelSphere/blob/gradle/oms-4.png)



## Caractéristiques Principales
- Modélisation de Données : Support complet des schémas conceptuels, logiques et physiques (ERD).

- Modélisation UML : Diagrammes de classes, de cas d'utilisation, de séquence, etc.

- Modélisation de Processus (BPM) : Pour cartographier les flux de travail de l'entreprise.

- Indépendance DBMS : Génération de code DDL pour les bases de données majeures (Oracle, PostgreSQL, MySQL, SQL Server, etc.).

- Architecture Java : Multiplateforme (Windows, Linux, macOS).

## Changement effectué

Le projet utilise désormait gradle au lieu de ant.
Passage à java 11
Les fichiers java sont passé en UTF 8
la gestion du classhpath a changé entre les version de java, le code a été modifié

## Constatation

Le logiciel démarre sous Linux et est utilisable.
Il y a des erreurs à l'occassion en console, mais je n'ai pas été en mesure de faire rouler la version original afin de pouvoir comparer.


## Compilation

L'application peut être lancé via le module distribution avec la tache run.
