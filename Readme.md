# 🎯 Cartel Management API

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

**🤖 API REST moderne avec Intelligence Artificielle Mistral AI intégrée**

*Simulation éducative d'un système de gestion de cartel fictif avec plus de 50 endpoints documentés*


---

## Environnement déployé

*Swagger UI accessible (attention ça peut prendre du temps à charger) :*
- https://cartelapi.onrender.com/swagger-ui/index.html

## 🌟 Points forts

- 🤖 **Intelligence Artificielle** : Mistral AI intégré pour suggestions, analyses et chat
- 🔐 **Sécurité JWT** : Authentification robuste avec 4 niveaux de rôles
- 📊 **50+ Endpoints** : API REST complète avec pagination et filtres
- 📚 **Documentation Swagger** : Interface interactive pour tester l'API
- 🐳 **Docker Ready** : Déploiement en un clic
- 🧪 **Tests inclus** : Collection Postman + tests d'intégration
- ⚡ **Performance** : Optimisé avec cache et pagination

## 🎮 Démonstration

### 🤖 Chat avec l'IA Mistral
```bash
POST /api/ai/chat
{
  "message": "What are the best strategies for expanding territory safely?"
}
```

### 📊 Suggestions intelligentes d'opérations
```bash
GET /api/ai/operation-suggestions?territory=Miami&riskLevel=5
```

### 💹 Analyses de marché automatisées
```bash
GET /api/ai/product-analysis?productId=1
```

---

## 🏗 Architecture

```
🎯 Cartel Management API
├── 🔐 Authentication (JWT + Rôles)
├── 👥 User Management (CRUD + Recherche)
├── 📦 Product Catalog (Types, Prix, Pureté)
├── 🎯 Operations Planning (Risques, Participants)
├── 💰 Transactions (Rapports financiers)
├── 🏭 Warehouse Management (Géolocalisation)
├── 📋 Inventory Tracking (Alertes stock)
├── 💬 Secure Messaging (Chiffrement)
└── 🤖 AI Services (Mistral AI)
    ├── Suggestions d'opérations
    ├── Analyses de marché
    ├── Évaluation des risques
    ├── Chat interactif
    └── Prévisions financières
```

## 🚀 Installation rapide

### Prérequis
- Java 17+
- Gradle 7+
- Clé API Mistral AI ([Obtenir ici](https://console.mistral.ai/))

### 1. Clone et configuration
```bash
git clone https://github.com/votre-username/cartel-management-api.git
cd cartel-management-api

# Configuration Mistral AI
echo "spring.ai.mistralai.api-key=YOUR_MISTRAL_API_KEY" >> src/main/resources/application.properties
```

### 2. Démarrage
```bash
./gradlew bootRun
```

### 3. Vérification
```bash
curl http://localhost:8080/actuator/health
# ✅ {"status":"UP"}
```

**🎉 API disponible sur http://localhost:8080**
**📚 Documentation Swagger : http://localhost:8080/swagger-ui.html**

## 🛠 Technologies

| Catégorie | Technologies |
|-----------|-------------|
| **Backend** | Java 17, Spring Boot 3.1.5, Spring Security |
| **Base de données** | H2 (dev), MySQL (prod), Spring Data JPA |
| **Intelligence Artificielle** | Mistral AI API, Spring AI |
| **Documentation** | Swagger/OpenAPI 3, Spring Doc |
| **Sécurité** | JWT, BCrypt, Rôles RBAC |
| **Outils** | Gradle, Lombok, Docker |
| **Tests** | JUnit 5, Spring Boot Test, Postman |

## 🎯 Fonctionnalités principales

### 🔐 Authentification & Sécurité
- **JWT Tokens** avec expiration configurable
- **4 niveaux de rôles** : USER, LIEUTENANT, BOSS, ADMIN
- **Sécurisation par endpoints** avec Spring Security
- **Chiffrement des mots de passe** avec BCrypt

### 👥 Gestion des utilisateurs
- CRUD complet avec validation
- Recherche et filtres avancés (rôle, territoire, code)
- Gestion des territoires et codes secrets
- Pagination sur tous les listings

### 📦 Catalogue des produits
- Types de produits : POWDER, HERBS, PILLS, LIQUID, SYNTHETIC
- Gestion prix, pureté, origine, caractéristiques
- Recherche multi-critères (type, prix, pureté, pays)
- Disponibilité et gestion du cycle de vie

### 🎯 Planification d'opérations
- Création et suivi des opérations
- Gestion des participants et leaders
- Évaluation des risques (1-10)
- Calcul des profits estimés/réels
- Statuts : PLANNED, IN_PROGRESS, COMPLETED, FAILED, CANCELLED

### 💰 Transactions & Finance
- Enregistrement des échanges commerciaux
- Calculs automatiques (quantité × prix)
- Rapports financiers par période
- Analyses des profits par vendeur/acheteur

### 🏭 Entrepôts & Inventaire
- Gestion des entrepôts avec géolocalisation
- Suivi des stocks en temps réel
- Alertes de stock bas automatiques
- Réservation de quantités

### 💬 Messagerie sécurisée
- Communication interne entre membres
- Chiffrement optionnel des messages
- Gestion des priorités et types
- Boîte de réception et historique

## 🤖 Fonctionnalités IA

### 🎯 Suggestions d'opérations intelligentes
L'IA analyse l'historique et propose des opérations personnalisées :
```json
{
  "territory": "Miami",
  "riskLevel": 5,
  "suggestions": "Operation Moonrise - Expansion stratégique dans le secteur sud...",
  "contextOperations": 12
}
```

### 📊 Analyses de marché
Évaluation automatique des produits avec recommandations :
```json
{
  "productName": "Premium White",
  "analysis": "Position concurrentielle forte, recommandation d'ajustement prix...",
  "currentPrice": 250.00
}
```

### ⚠️ Évaluation des risques
Assessment intelligent des opérations :
```json
{
  "operationName": "Operation Sunrise",
  "riskAssessment": "Niveau de risque modéré, recommandations de mitigation...",
  "currentRiskLevel": 6
}
```

### 💬 Chat interactif
Assistant IA pour conseils stratégiques :
```bash
POST /api/ai/chat
{
  "message": "Comment optimiser nos profits ce trimestre ?"
}
```

### 📈 Prévisions financières
Projections basées sur l'historique :
```json
{
  "forecastPeriod": "6 months",
  "forecast": "Croissance projetée de 15-20% basée sur 45 transactions...",
  "historicalDataPoints": 45
}
```

## 📊 Endpoints principaux

### 🔐 Authentification
```http
POST /api/auth/register     # Inscription
POST /api/auth/login        # Connexion
GET  /api/auth/me          # Utilisateur courant
```

### 🤖 Intelligence Artificielle ⭐
```http
GET  /api/ai/operation-suggestions  # Suggestions IA
GET  /api/ai/product-analysis      # Analyse marché
GET  /api/ai/risk-assessment       # Évaluation risques
POST /api/ai/chat                  # Chat interactif
GET  /api/ai/financial-forecast    # Prévisions financières
```

### 👥 Utilisateurs (Admin/Boss)
```http
GET  /api/users            # Liste paginée
POST /api/users            # Créer utilisateur
GET  /api/users/{id}       # Détails
PUT  /api/users/{id}       # Modifier
```

### 📦 Produits
```http
GET  /api/products         # Catalogue complet
POST /api/products         # Ajouter produit
GET  /api/products/type/{type}     # Par type
GET  /api/products/search  # Recherche
```

### 🎯 Opérations
```http
GET  /api/operations       # Toutes les opérations
POST /api/operations       # Planifier opération
GET  /api/operations/status/{status}  # Par statut
GET  /api/operations/high-risk/{level} # Haut risque
```

**📚 Documentation complète : [Swagger UI](http://localhost:8080/swagger-ui.html)**

## 🔒 Sécurité & Rôles

| Rôle | Permissions |
|------|-------------|
| **USER** | Consultation, messages, participation aux opérations |
| **LIEUTENANT** | + Gestion opérations, inventaire, transactions |
| **BOSS** | + Gestion utilisateurs, produits, analyses financières |
| **ADMIN** | Accès complet système, configuration |

### Comptes prédéfinis (développement)
```
admin / admin123        → ADMIN
boss1 / admin123        → BOSS
lieutenant1 / admin123  → LIEUTENANT
user1 / admin123        → USER
```

## 🧪 Tests & Validation

### Collection Postman incluse
- **50+ requêtes** prêtes à l'emploi
- **Tests automatisés** avec validation des réponses
- **Token JWT** géré automatiquement
- **Données de test** prédéfinies

### Tests d'intégration
```bash
./gradlew test
```

### Validation manuelle
```bash
# Health check
curl http://localhost:8080/actuator/health

# Test authentification
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Test IA (avec token)
curl -X GET "http://localhost:8080/api/ai/operation-suggestions?territory=Miami" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## 📈 Monitoring

### Health checks intégrés
- **Application** : `/actuator/health`
- **Base de données** : Vérification connexion auto
- **IA Mistral** : Validation clé API

### Métriques exportables
- **Prometheus** : `/actuator/prometheus`
- **Métriques JVM** : Mémoire, threads, GC
- **Métriques custom** : Nombre d'opérations, transactions

### Logs structurés
```
2024-06-26 10:30:15 [INFO ] com.cartel.service.AIService - AI suggestion generated for territory: Miami
2024-06-26 10:30:16 [DEBUG] com.cartel.security.JwtUtil - JWT token validated for user: testuser
```

## 🏆 Fonctionnalités remarquables

- ✅ **Intelligence Artificielle** intégrée (Mistral AI)
- ✅ **50+ endpoints** REST documentés
- ✅ **Authentification JWT** robuste
- ✅ **4 niveaux de rôles** utilisateur
- ✅ **Pagination** sur tous les listings
- ✅ **Recherche et filtres** avancés
- ✅ **Validation** automatique des données
- ✅ **Gestion d'erreurs** centralisée
- ✅ **Tests d'intégration** complets
- ✅ **Collection Postman** fournie
- ✅ **Documentation Swagger** interactive
- ✅ **Docker** ready
- ✅ **Déploiement** cloud simplifié

## 🎓 Contexte éducatif

Ce projet a été développé dans le cadre d'un cours sur les **APIs REST modernes** avec Spring Boot. Il démontre :

- **Architecture** en couches (Controller → Service → Repository)
- **Intégration** d'Intelligence Artificielle
- **Sécurité** avec JWT et rôles
- **Documentation** automatisée
- **Tests** et validation
- **Déploiement** moderne avec Docker

> ⚠️ **Disclaimer** : Ce projet est purement éducatif et fictif. Aucune activité illégale n'est promue ou encouragée.
