# Salutation
Dans ce TP, vous réalisez des exercices sur l'injection de dépendance, les tests logciel, le développement piloté par les tests, et l'architecture REST.
L'exercice est réalisé à l'aide d'une application qui salue les utilisateurs avec leurs noms.

## Modalité
* Ces travaux pratiques sont notés.
* Ils sont à réaliser en binôme.
* Le compte rendu à remplir se trouve [ici](Rapport.md).

## Une application pour dire bonjour
Vous concevez une application web qui salue les utilisateurs connectés dans leurs propres langues.

Un code souche est fourni pour vous guider dans vos démarches.
Le code est organisé dans les paquetages suivants :
* `domain` : contient le noyau de l'application.
  Le code dans ce paquetage représente la logique métier.
  Ce code doit être indépendant des technologies utilisées pour communiquer avec le monde extérieur tel que les interfaces utilisateur et les bases de données.
* `persistence` : contient le code nécessaire pour sauvegarder les données de l'application.
  Par simplicité, le code fourni sauvegarde les données d'utilisateurs dans un fichier.
* `controller` : contient l'interface de programmation de l'application (API)[^1].
  Cette API est matérialisée avec des contrôleurs REST qui donnent accès aux fonctionnalités métier.

L'application contient aussi une vue simple représentant l'interface utilisateur.
Il s'agit d'une page HTML statique qui communique avec les contrôleurs REST de l'application.
La page est présente dans le fichier [index.html](`src/main/resources/index.html`).

Pour vérifier la bonne fonctionnement de l'application, les suites des tests suivantes sont fournies :
* Tests unitaires présents dans les paquetages `domain`.
  Ils vérifient le bon fonctionnement du code métier.
* Tests d'intégration présents dans les paquetages `controller`.
  Ils sont responsables de la vérification du bon fonctionnement des contrôleurs REST.

Une fois l'application est lancée, vous pouvez la visualiser sur l'adresse http://localhost:8080/.

## Tests unitaires
Les tests unitaires sont des tests qui vérifient le bon fonctionnement d'une unité de code d'une manière isolée de son entourage.

Les unités du code testées en Java sont généralement des classes.
Pour pouvoir tester une classe en isolation, il est nécessaire de remplacer ses dépendances par des objets factices.
Ces objets factices sont appelés des bouchons, ou _mocks_.

Pour la création des bouchons, nous pouvons soit utiliser des bibliothèques dédiées aux tests (type Mockito), soit créer les bouchons nous-même.
Dans le code fourni, nous adoptons la deuxième approche.
La classe `InMemoryUserRepository` remplace la persistence des données utilisateurs par une réalisation en mémoire.
La classe `Base64PasswordEncoder` remplace le chiffrement des mots de passe par une réalisation simple en base-64.

Les tests unitaires ne doivent pas dépendre d'un framework particulier à part la batterie de test, Jupiter dans notre cas.

> **Exercice 1**
>
> Observez la suite des tests [`SimpleLoginServiceTest`](/src/test/java/fr/polytech/isi3/hello/domain/login/SimpleLoginServiceTest.java).
>
> Quelle unité de code teste-t-elle ?
>
> Certains tests dans cette suite échouent, signalant que le processus d'authentification ne fonctionne pas correctement.
> En effet, l'authentification échoue dans les cas où elle devrait réussir.
>
> Identifiez l'erreur signalée par les tests dans le code de l'unité testée et corrigez-la.
> Faites en sorte que tous les tests dans la suite `SimpleLoginServiceTest` passent avec succès.

## Injection de dépendance, sans dépendance
L'injection de dépendance implique que le code client ne s'occupe pas de la création des instances de ses dépendances.
Il lui suffit alors de les déclarer et le cadre applicatif se charge de les injecter.

Spring fournit plusieurs moyens pour déclarer les dépendances dont la classe a besoin.
La première méthode est d'annoter les champs qui référencent ces dépendances avec `@Autowired`.
Cette méthode n'est pas sans inconvenient, car elle rend le code dépendant à un framework comme Spring.
Cela rend les tests unitaires plus difficiles à réaliser puisque ça nécessite un contexte Spring pour les exécuter, alors qu'ils devraient être indépendants du framework.
Bien que tolérée dans les tests, cette méthode est considérée comme anti-patron ailleurs dans le code.

Un autre moyen est de passer les dépendances par le constructeur.
C'est en effet la méthode à privilégier pour le code applicatif.

> **Exercice 2**
>
> Observez la classe [`MeController`](/src/main/java/fr/polytech/isi3/hello/controller/MeController.java).
>
> De quoi dépend cette classe ?
> Comment cette dépendance est-elle injectée ?
> Comparez cette méthode avec celle utilisée pour injecter les dépendances de la classe [`GreetingController`](/src/main/java/fr/polytech/isi3/hello/controller/GreetingController.java).
>
> Modifiez la classe `MeController` pour qu'elle utilise la méthode de l'injection de dépendance par constructeur.

## Journaux pérsonalisés
L'injection de dépendance est gérée au sein de l'application par le cadre applicatif.
Le framework Spring est utilisé à cette fin dans le code fourni.

L'injection de dépendance est employé pour injecter les journaux d'événements (ou _logs_) : des instances de type `Logger`.

On souhaite avoir des journaux nommés, qui portent le nom de la classe qui les utilise.
Néanmoins, à l'état actuel, tous les journaux portent le même nom.
Il parait que le motif de création singleton est utilisé pour créer les journaux, qui fait qu'une seule instance est créée au sein de l'application.

> **Exercice 3**
>
> Observer la classe [`LoggerFactory`](/src/main/java/fr/polytech/isi3/hello/domain/utils/logging/LoggerFactory.java).
>
> À quoi servent les annotations `@Configuration`, `@Bean` et `@Scope` ?
> Qu'est-ce que représente la classe `InjectionPoint` ?
>
>
> Modifiez la portée de l'instance de `Logger` créée par Spring pour qu'elle ne soit plus un singleton.
> Indice : Regardez les constants que contient l'interface `ConfigurableBeanFactory`.
>
> Modifiez le code pour injecter des journaux d'événement de type `FileLogger` lorsque la variable d'environnement `profile` est définie à `prod`.
> L'application doit continuer à marcher correctement même quand une telle variable n'est pas définie.

## Salutation dédiée
Le contrôleur [`GreetingController`](/src/main/java/fr/polytech/isi3/hello/controller/GreetingController.java) expose une ressource REST sur le chemin "/api/greeting".
La méthode GET est responsable de saluer les utilisateurs connectés en utilisant sa langue du choix.

Le test de ce contrôleur, défini dans [`GreetingControllerTest`](/src/test/java/fr/polytech/isi3/hello/controller/GreetingControllerTest.java), échoue.
En regardant le message d'erreur du test en échec, on constate que le contrôleur salue l'utilisateur connecté toujours avec la même phrase : "Hi", plus nom d'utilisateur.
Le comportement attendu néanmoins est de faire varier la salutation en fonction de la langue de l'utilisateur.

Une analyse plus approfondie du code montre que le contrôleur n'est pas branché au bon service, responsable pour l'obtention de la salutation adaptée.

> **Exercice 4**
>
> Identifiez le service qui génère la bonne salutation à partir d'un utilisateur donné.
> Ce service est-il testé ? Par quelle suite de test ?
>
> Modifiez la classe `GreetingController` pour injecter le bon service.
> Modifiez le code de cette classe pour utiliser ce service.
> Veillez à ce que les tests, définis dans [`GreetingControllerTest`](/src/test/java/fr/polytech/isi3/hello/controller/GreetingControllerTest.java), passent tous avec succès.

## Les services Web REST
Les services Web et un moyen d'offrir une API accessible en ligne, par exemple en dessus du protocole HTTP.
Les services Web peuvent utiliser des différents protocoles d'échange comme SOAP, REST ou GraphQL.

L'architecture REST implique que les services Web tirent bénéfices de la puissance du protocole HTTP pour représenter les requêtes et les réponses.
Par exemple, le verbe HTTP (`GET`, `POST`, `PUT`, `DELETE`, etc.) est utilisé pour refléter la nature de l'opération.
Le code de status de réponse, quant à lui, est utilisé pour refléter la nature de réponse (erreur serveur, erreur client, rédirection et succès).

Selon l'architecture proposée dans le code fourni, la vue communique avec le contrôleur au travers des appels aux API REST.
Étant fondés sur le protocol HTTP, ces appels sont de nature asynchrone.
Le code Javascript dans le fichier [`index.html`](src/main/resources/static/index.html) utilise la fonction `fetch()` pour réaliser ces appels.

> **Exercice 5**
>
> Identifiez quelques appels aux API REST dans le fichier [`index.html`](src/main/resources/static/index.html).
>
> Est-ce que toutes les requêtes à la ressource "/api/user/" peuvent être satisfaites par le contrôleur [`UserController`](src/main/java/fr/polytech/isi3/hello/controller/UserController.java) ?
>
> À l'exemple des méthodes existantes dans la classe `UserController`, créez les méthodes qui seront résponsables de :
>  * la modification d'un utilisateur existant (verbe HTTP `PUT`), et
>  * la suppression d'un utilisateur existant (verbe HTTP `DELETE`).
>
> Faites-vous aider par les tests dans [`UserControllerTest`](src/test/java/fr/polytech/isi3/hello/controller/UserControllerTest.java).
> Tous les tests, sauf `testCreateDuplicate`, doivent passer avec succès.

## Gestion d'erreur dans l'API
Comme mentionné précédemment, les services Web REST utilisent le code de status de réponse HTTP pour encoder la nature retour.
Dans le cas d'erreur, ce même code est utilisé pour indiquer le type d'erreur.
Par exemple, le code 404 signifie que la ressource demandée n'existe pas, alors que le code 401 signifie un manque d'authentification.

Par défaut, toute exception non traitée dans le code d'un contrôleur REST est traduite par une réponse avec le code de status 500.
Cela est ce qui est souhaitable, car ce code de réponse signifie une erreur interne du serveur, alors que l'erreur est peut-être le résultat d'une mauvaise requête du client.

Spring fournit plusieurs manières pour gérer les exceptions dans les contrôleurs d'un API REST.
Cela permettre de faire correspondre certains types d'exceptions à des codes HTTP plus parlants.

> **Exercice 6**
> Examinez la classe [`ExceptionHandlerAdvice`](src/main/java/fr/polytech/isi3/hello/controller/ExceptionHandlerAdvice.java) et ces méthodes.
>
> À quoi sert les annotations `@ExceptionHandler`, `@ResponseStatus` et `@ControllerAdvice` ?
>
> À l'exemple de deux méthodes dans cette classe, créez une nouvelle méthode résponsable de gérer les exceptions de type `DuplicateKeyException`.
> Quel code de status HTTP doit être retourné lors qu'une telle exception est levée ?
> Faites-vous aidez par les tests dans [`UserControllerTest`](src/test/java/fr/polytech/isi3/hello/controller/UserControllerTest.java).
> Suite à votre correction, tous les tests doivent passer avec succès.

## Stockage des mots de passe
Comme tous les logiciels respectueux, notre application ne stocke pas les mots de passe en claire.
Elle les chiffre grâce au service `PasswordEncrypter`.
Cette interface peut avoir plusieurs réalisations, avec différents algorithmes.
À présent, une réalisation est fournie avec l'algorithme MD5.
La classe abstraite `BasePasswordEncrypter` factorise les fonctionnalités communes du chiffrage de mot de passe.

> **Exercice 7**
>
> Observez la classe [`MD5PasswordEncrypter`](src/main/java/fr/polytech/isi3/hello/domain/utils/cryptography/MD5PasswordEncrypter.java).
> À quoi sert l'annotation `@Service` ?
>
> Créez la classe `Sha512PasswordEncrypter` qui réalise `PasswordEncrypter` avec l'algorithme SHA512.
> Faite en sorte que cette réalisation-là soit utilisée pour le chiffrage des mots de passe au lieu de celle de MD5.
> Attention : tous les anciens mots de passe stockés seront invalides quand on change l'algorithme.

[^1]: *Application Programming Interface* en anglais.

