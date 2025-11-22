<div align="center">

#  Quiz Trainer
### *Application de quiz Android – Kotlin, Compose, Ktor, Room*

<img src="docs/banner.png" width="80%" alt="Banner"/>

</div>

---

##  Description

"Quiz Trainer” est une application mobile Android développée en Kotlin avec Jetpack Compose.  
Elle permet à l’utilisateur de tester ses connaissances en informatique à travers un quiz dynamique alimenté par une API publique en ligne.

Le joueur répond à une série de 10 questions, puis découvre son score final.  
Les résultats sont enregistrés localement grâce à une base de données interne (Room), afin de pouvoir consulter son historique des scores même hors connexion.

Ce projet illustre les principales compétences du cours :
- création d’écrans en Compose
- gestion d’appels réseau avec Ktor
- stockage local avec Room
- architecture MVVM
- gestion d’état réactif avec ViewModel et Flow

---

##  Fonctionnalités principales

###  1. Accueil
- Lancer un nouveau quiz
- Accéder à l’historique des parties
- Interface épurée, moderne et responsive

###  2. Quiz (API + logique de jeu)
- Chargement des questions depuis **Open Trivia DB** via Ktor
- 4 choix mélangés aléatoirement
- Correction immédiate
- Bouton “Question suivante”
- Score mis à jour dynamiquement
- Explication / feedback en cas d'erreur

###  3. Résultat + Historique (Room)
- Le score de chaque partie est sauvegardé localement
- Historique trié du plus récent au plus ancien
- Affichage : date, score, catégorie, difficulté
- Consultation possible hors-ligne

---
##  API utilisée

**Nom :** [Open Trivia DB](https://opentdb.com/api_config.php)  
**Type :** 
- API publique et gratuite (aucune clé requise)
- Format JSON
- Aucun token nécessaire
- Exemple de requête :
https://opentdb.com/api.php?amount=10&category=18&difficulty=easy&type=multiple

---
## 🧱 Architecture MVVM

Architecture : MVVM (Model – View – ViewModel)

![Architecture MVVM du projet](images/architecture.png)


###  **UI Layer (Compose)**
- Aucune logique métier
- Observe les StateFlow du ViewModel
- Navigation Compose (Home → Quiz → Results → History)

###  **ViewModel Layer**
- Logique du quiz
- Gestion des états (loading, questions, score, erreurs)
- Expose l’historique
- Utilise `viewModelScope` + `StateFlow`

###  **Repository Layer**
- Point central d’accès aux données
- Combine API (Ktor) + Room

###  **Data Layer**
- **Ktor Client** → appel réseau + JSON
- **Room** → stockage local

---
## Structure du projet

app/
├── data/
│    ├── api/ (Ktor service)
│    ├── db/  (Room DAO + Entities + Database)
│    └── model/ (DTO + modèles domaine)
│
├── ui/
│    ├── home/
│    ├── quiz/
│    ├── result/
│    └── theme/
│
├── viewmodel/
└── MainActivity.kt

---

## Installation
1. Cloner le projet
2. Ouvrir dans Android Studio
3. Synchroniser Gradle
4. Lancer sur un émulateur ou téléphone physique

