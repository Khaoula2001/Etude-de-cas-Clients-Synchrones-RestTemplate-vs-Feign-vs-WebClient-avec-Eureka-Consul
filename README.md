# Étude de Cas : Clients Synchrones avec Eureka et Consul

Ce projet est une étude comparative de trois clients HTTP synchrones (**RestTemplate**, **Feign Client**, **WebClient**) dans une architecture microservices utilisant **Spring Boot**. Il explore également l'utilisation et la migration de la découverte de services de **Eureka** vers **Consul**.

## 🏗️ Architecture

Le système se compose de :
*   **Service Voiture** (`service-voiture`) : Microservice exposant une API REST pour récupérer des informations sur des voitures. Simule une latence pour les tests de performance.
*   **Service Client** (`service-client`) : Microservice consommant l'API Voiture via trois implémentations différentes.
*   **Discovery Service** :
    *   Initialement : **Netflix Eureka**.
    *   Actuellement configuré : **HashiCorp Consul**.

### Diagramme de flux
`Service Client` --(HTTP)--> `Service Voiture`
*(Résolution d'adresse via Consul)*

## 🚀 Démarrage

### Prérequis
*   Java 17+
*   Maven
*   Docker & Docker Compose (Recommandé)

### Option 1 : Via Docker Compose (Recommandé)
Lancez l'environnement complet (Consul + Services) :
```bash
docker-compose up --build -d
```
Les services seront accessibles à :
*   Consul UI : [http://localhost:8500](http://localhost:8500)
*   Service Voiture : [http://localhost:8081](http://localhost:8081)
*   Service Client : [http://localhost:8082](http://localhost:8082)

### Option 2 : Démarrage Manuel
1.  **Démarrer Consul** :
    ```bash
    consul agent -dev -ui -node=server-1 -client=0.0.0.0
    # OU via Docker
    docker run -d -p 8500:8500 hashicorp/consul
    ```
2.  **Service Voiture** :
    ```bash
    cd service-voiture
    mvn spring-boot:run
    ```
3.  **Service Client** :
    ```bash
    cd service-client
    mvn spring-boot:run
    ```

## 🧪 Tests des Clients
Le `service-client` expose trois endpoints pour tester chaque implémentation :

| Client | URL | Description |
| :--- | :--- | :--- |
| **RestTemplate** | `GET /api/clients/{id}/car/rest` | Client synchrone classique de Spring. |
| **Feign** | `GET /api/clients/{id}/car/feign` | Client déclaratif via Interface. |
| **WebClient** | `GET /api/clients/{id}/car/webclient` | Client réactif (utilisé ici en mode bloquant). |

**Exemple de test :**
```bash
curl http://localhost:8082/api/clients/1/car/rest
curl http://localhost:8082/api/clients/1/car/feign
curl http://localhost:8082/api/clients/1/car/webclient
```

## 📊 Analyse et Performance (Partie D-H)

### Objectifs
Comparer les clients sur la base de :
1.  **Latence** (Temps de réponse).
2.  **Débit** (Requêtes / seconde).
3.  **Facilité de développement**.

### Protocole de Test Recommandé
Utilisez JMeter pour simuler une charge (10, 50, 100 utilisateurs) sur chaque endpoint.

*Exemple de tableau de résultats à remplir :*

| Client | Latence (10 users) | Latence (100 users) | Débit (Req/s) | CPU Usage |
| :--- | :--- | :--- | :--- | :--- |
| RestTemplate | ... ms | ... ms | ... /s | ... % |
| Feign | ... ms | ... ms | ... /s | ... % |
| WebClient | ... ms | ... ms | ... /s | ... % |

### Observations Initiales (Code)
*   **Feign** offre la meilleure lisibilité et facilité d'implémentation (interface déclarative).
*   **WebClient** est le plus moderne et flexible, mais son utilisation synchrone (`.block()`) annule ses avantages réactifs.
*   **RestTemplate** est robuste mais verbeux et en mode maintenance.

## 🛠️ Migration Eureka -> Consul
Le projet a été configuré pour démontrer la facilité de changement de Discovery Service avec Spring Cloud.
*   Le code utilise `@LoadBalanced` et `@FeignClient(name="SERVICE-VOITURE")`.
*   Le changement s'est fait uniquement par modification des dépendances (`pom.xml`) et de la configuration (`application.yml`), sans changer le code Java.