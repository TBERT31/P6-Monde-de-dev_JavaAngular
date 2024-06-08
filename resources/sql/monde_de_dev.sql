-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : ven. 07 juin 2024 à 14:55
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `monde_de_dev`
--

-- --------------------------------------------------------

--
-- Structure de la table `articles`
--

CREATE TABLE `articles` (
  `id` bigint(20) NOT NULL,
  `title` varchar(50) DEFAULT NULL,
  `content` text DEFAULT NULL,
  `author_id` bigint(20) DEFAULT NULL,
  `topic_id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT current_timestamp(),
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `articles`
--

INSERT INTO `articles` (`id`, `title`, `content`, `author_id`, `topic_id`, `created_at`, `updated_at`) VALUES
(1, 'prompt engineering', 'L\'ingénierie de prompt est le processus de structuration d\'un prompt, de sorte à ce qu\'il soit efficacement interprété et compris par un modèle d\'intelligence artificielle générative. Un prompt est une description textuelle de la tâche qu’une IA doit effectuer.', 8, 8, '2024-06-07 11:35:26', '2024-06-07 11:35:30'),
(2, 'Les 7 modèles d\'intelligence artificielle les plus', 'Introduction\r\nLes modèles d\'IA simulent un comportement intelligent, exécutant des tâches qui requièrent l\'intelligence humaine. Ces modèles comprennent des algorithmes d\'apprentissage automatique, des réseaux neuronaux et des architectures d\'apprentissage profond. Ils utilisent des algorithmes avancés et des données pour apprendre, raisonner et faire des prédictions.\r\n\r\nLes modèles d\'IA les plus courants modifient déjà la façon dont les entreprises travaillent. Ils sont appliqués dans les domaines de la santé, de la finance, du traitement du langage et de la reconnaissance d\'images. Le fait de les comprendre peut aider votre entreprise ou vous-même à devenir plus efficace et plus compétitif. Pour en savoir plus sur les différents modèles d\'IA et sur la manière dont ils affectent plusieurs domaines, nous vous invitons à poursuivre votre lecture. À la fin, vous aurez également des réponses à vos questions relatives à l\'IA.\r\n\r\n', 8, 8, '2024-06-07 11:35:32', '2024-06-07 11:35:36'),
(3, 'Rust', 'Rust est un langage de programmation compilé multi-paradigme qui met l\'accent sur la performance, la sûreté des types et la concurrence.', 7, 7, '2024-06-07 11:36:28', '2024-06-07 11:36:28'),
(4, 'Solidity', 'Solidity est un langage de programmation orienté objet dédié à l\'écriture de contrats intelligents. Il est utilisé pour implémenter des smartcontrat sur diverses blockchains, notamment Ethereum.', 7, 7, '2024-06-07 11:36:28', '2024-06-07 11:36:28'),
(5, 'Sinatra', 'Sinatra est une bibliothèque d\'applications Web gratuite et à code source ouvert, ainsi qu\'un langage spécifique à un domaine écrit en Ruby. C\'est une alternative aux autres frameworks d\'applications Web Ruby tels que Ruby on Rails, Merb, Nitro et Camping. Il est dépendant de l\'interface du serveur Web Rack.', 6, 6, '2024-06-07 11:37:35', '2024-06-07 11:37:35'),
(6, 'Ruby on Rails', 'Ruby on Rails, également appelé RoR ou Rails, est un framework web libre écrit en Ruby. Il suit le motif de conception modèle-vue-contrôleur. Il propose une structure qui permet de développer rapidement et intuitivement.', 6, 6, '2024-06-07 11:37:35', '2024-06-07 11:37:35'),
(7, 'Laravel', 'Laravel est un framework web open-source écrit en PHP respectant le principe modèle-vue-contrôleur et entièrement développé en programmation orientée objet. Laravel est distribué sous licence MIT, avec ses sources hébergées sur GitHub.', 5, 5, '2024-06-07 11:38:27', '2024-06-07 11:38:27'),
(8, 'Symfony', 'Symfony est un ensemble de composants PHP ainsi qu\'un framework MVC libre écrit en PHP. Il fournit des fonctionnalités modulables et adaptables qui permettent de faciliter et d’accélérer le développement d\'un site web.', 5, 5, '2024-06-07 11:38:27', '2024-06-07 11:38:27'),
(9, 'Quelles est la différence entre C++, C# et Objecti', 'Pour comprendre les différences entre C++, C# et Objective-C, il faut commencer par le langage de programmation original C.\r\n\r\nLe langage de programmation C a été développé au début des années 1970, a été implémenté dans la plupart des premiers mainframes et micro-ordinateurs des années 1980, et est devenu depuis une présence de codage omniprésente, influençant de nombreux langages de programmation utilisés aujourd\'hui. \r\n\r\nAlors que certains types de langages de programmation fonctionnent en fournissant aux ordinateurs une liste de tâches à exécuter, les langages de programmation orientée objet permettent aux programmeurs de créer des objets virtuels dans leur code - chacun ayant des attributs et des capacités uniques - qui peuvent ensuite interagir les uns avec les autres pour effectuer des actions. Une façon simple de se représenter la programmation orientée objet est de penser à un ensemble virtuel de blocs de construction. Chaque bloc est un objet défini par une forme, une taille, etc., ainsi que par des types de comportement.\r\n\r\nL\'avantage de ce modèle d\'objet est qu\'il réduit la complexité en imitant la construction du monde réel et en donnant aux programmeurs une structure claire sur laquelle travailler. Les objets peuvent être isolés et maintenus séparément du reste de leur code (ce qui facilite la localisation et la réparation des bogues) et, une fois créés, ils peuvent être facilement réutilisés dans de futurs programmes.\r\n\r\nLe C reste l\'un des langages de programmation les plus utilisés de tous les temps. Il est encore utilisé pour programmer les systèmes d\'exploitation et le matériel des systèmes embarqués. De nombreux projets open source sont également écrits en C. \r\n\r\nC++ vs C# vs Objective-C\r\nQu’est ce que le C++ ?\r\nLe C++ est également très utilisé. Lancé en 1979, le C++ a été spécifiquement créé pour ajouter des objets et des méthodes d\'instance (comportement d\'objet) au langage C original. L\'idée de base était que la programmation orientée objet s', 4, 4, '2024-06-07 11:39:43', '2024-06-07 11:39:43'),
(10, 'Différence clé entre C# et C++', 'C++ est un langage de programmation de bas niveau qui ajoute des fonctionnalités orientées objet à son langage de base C tandis que C# est un langage de haut niveau.\r\nC++ compile en code machine tandis que C# « compile » en CLR (Common Language Runtime), qui est interprété par JIT dans ASP.NET.\r\nC++ est un langage orienté objet tandis que C# est considéré comme un langage de programmation orienté composants.\r\nEn C++, vous devez gérer la mémoire manuellement alors que C# s\'exécute sur une machine virtuelle, qui effectue automatiquement la gestion de la mémoire.\r\nEn C++, le développement doit suivre des architecture et doit être portable alors que le développement C# doit être un langage de programmation simple, moderne, polyvalent et orienté objet.', 4, 4, '2024-06-07 11:39:43', '2024-06-07 11:39:43'),
(11, 'Python', 'Django est un framework web open source en Python. Il a pour but de rendre le développement d\'applications web simple et basé sur la réutilisation de code. Développé en 2003 pour le journal local de Lawrence, Django a été publié sous licence BSD à partir de juillet 2005. ', 3, 3, '2024-06-07 11:41:10', '2024-06-07 11:41:10'),
(12, 'Flask', 'Flask est un micro framework open-source de développement web en Python. Il est classé comme microframework car il est très léger. Flask a pour objectif de garder un noyau simple mais extensible. ', 3, 3, '2024-06-07 11:41:10', '2024-06-07 11:41:10'),
(13, 'Java JEE', 'Java SE Jakarta EE Java ME JavaFX Java Card Jakarta EE, est une spécification pour la plate-forme Java d\'Oracle, destinée aux applications d\'entreprise.', 2, 2, '2024-06-07 11:42:07', '2024-06-07 11:42:07'),
(14, 'Spring Boot', 'Traduit de l\'anglais-Spring Boot est un framework Java open source utilisé pour programmer des applications Spring autonomes de qualité production avec un minimum d\'effort.', 2, 2, '2024-06-07 11:42:07', '2024-06-07 11:42:07'),
(15, 'Angular', 'Angular est un framework pour clients, open source, basé sur TypeScript et codirigé par l\'équipe du projet « Angular » chez Google ainsi que par une communauté de particuliers et de sociétés. Angular est une réécriture complète d\'AngularJS, cadriciel construit par la même équipe.', 1, 1, '2024-06-07 11:42:49', '2024-06-07 11:42:49'),
(16, 'React', 'React est une bibliothèque JavaScript libre. Elle est maintenue par Meta ainsi que par une communauté de développeurs individuels et d\'entreprises depuis 2013.', 1, 1, '2024-06-07 11:42:49', '2024-06-07 11:42:49');

-- --------------------------------------------------------

--
-- Structure de la table `comments`
--

CREATE TABLE `comments` (
  `id` bigint(20) NOT NULL,
  `message` varchar(2000) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `article_id` bigint(20) DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp(),
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `comments`
--

INSERT INTO `comments` (`id`, `message`, `user_id`, `article_id`, `created_at`, `updated_at`) VALUES
(1, 'mon message', 1, 1, '2024-06-07 14:36:20', '2024-06-07 14:36:20'),
(2, 'mon message', 1, 1, '2024-06-07 14:40:22', '2024-06-07 14:40:22');

-- --------------------------------------------------------

--
-- Structure de la table `subscriptions`
--

CREATE TABLE `subscriptions` (
  `user_id` bigint(20) NOT NULL,
  `topic_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `topics`
--

CREATE TABLE `topics` (
  `id` bigint(20) NOT NULL,
  `title` varchar(50) DEFAULT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp(),
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `topics`
--

INSERT INTO `topics` (`id`, `title`, `description`, `created_at`, `updated_at`) VALUES
(1, 'JavaScript', 'JavaScript est un langage de programmation de scripts principalement employé dans les pages web interactives et à ce titre est une partie essentielle des applications web. Avec les langages HTML et CSS, JavaScript est au cœur des langages utilisés par les développeurs web.', '2024-06-07 11:31:52', '2024-06-07 14:19:41'),
(2, 'Java', 'Java est un langage de programmation de haut niveau orienté objet créé par James Gosling et Patrick Naughton, employés de Sun Microsystems, avec le soutien de Bill Joy, présenté officiellement le 23 mai 1995 au SunWorld. La société Sun est rachetée en 2009 par la société Oracle qui détient et maintient désormais Java.', '2024-06-07 11:31:58', '2024-06-07 11:32:00'),
(3, 'Python', 'Python est un langage de programmation interprété, multiparadigme et multiplateformes. Il favorise la programmation impérative structurée, fonctionnelle et orientée objet.', '2024-06-07 11:32:02', '2024-06-07 11:32:06'),
(4, 'C#', 'C# est un langage de programmation orientée objet, commercialisé par Microsoft depuis 2002 et destiné à développer sur la plateforme Microsoft .NET, au même titre que d’autres langages liés à cette plateforme.', '2024-06-07 11:32:08', '2024-06-07 11:32:12'),
(5, 'PHP', 'PHP: Hypertext Preprocessor, plus connu sous son sigle PHP, est un langage de programmation libre, principalement utilisé pour produire des pages Web dynamiques via un serveur web, mais pouvant également fonctionner comme n\'importe quel langage interprété de façon locale. PHP est un langage impératif orienté objet.', '2024-06-07 11:32:14', '2024-06-07 11:32:16'),
(6, 'Ruby', 'Ruby est un langage de programmation libre. Il est interprété, orienté objet et multi-paradigme. Le langage a été standardisé au Japon en 2011 (JIS X 3017:2011), et en 2012 par l\'Organisation internationale de normalisation (ISO 30170:2012).', '2024-06-07 11:32:18', '2024-06-07 11:32:20'),
(7, 'Web3', 'Le Web3 ou Web 3.0 est un terme utilisé pour désigner l\'idée d\'un web décentralisé exploitant la technologie des chaînes de blocs, se voulant ainsi le successeur du Web 2.0, terme utilisé pour désigner le web « social ».', '2024-06-07 11:32:22', '2024-06-07 11:32:25'),
(8, 'Data Science', 'La Data Science ou science des données est un vaste champ multi-disciplinaire visant à donner du sens aux données brutes. Data Science : définition, champs d\'applications et limites actuelles, découvrez tout ce que vous devez savoir sur ce domaine complexe, devenu un enjeu prioritaire dans les entreprises de toutes les industries.', '2024-06-07 11:32:27', '2024-06-07 11:32:29');

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(120) DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp(),
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `password`, `created_at`, `updated_at`) VALUES
(1, 'john.doe01', 'john.doe@test.com', 'Test!1234', '2024-06-07 11:30:34', '2024-06-07 11:30:34'),
(2, 'jane.doe02', 'jane.doe@test.com', 'Test!1234', '2024-06-07 11:30:45', '2024-06-07 11:30:45'),
(3, 'john.smith03', 'john.smith@test.com', 'Test!1234', '2024-06-07 11:30:53', '2024-06-07 11:30:57'),
(4, 'jane.smith04', 'jane.smith@test.com', 'Test!1234', '2024-06-07 11:31:00', '2024-06-07 11:31:02'),
(5, 'johnny.depp05', 'johnny.depp@test.com', 'Test!1234', '2024-06-07 11:31:04', '2024-06-07 11:31:07'),
(6, 'janny.depp06', 'janny.depp@test.com', 'Test!1234', '2024-06-07 11:31:09', '2024-06-07 11:31:13'),
(7, 'john.wick07', 'john.wick@test.com', 'Test!1234', '2024-06-07 11:31:15', '2024-06-07 11:31:18'),
(8, 'jane.wick08', 'jane.wick@test.com', 'Test!1234', '2024-06-07 11:31:20', '2024-06-07 11:31:23');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `articles`
--
ALTER TABLE `articles`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_articles_topic_id` (`topic_id`),
  ADD KEY `fk_articles_author_id` (`author_id`);

--
-- Index pour la table `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_comments_user_id` (`user_id`),
  ADD KEY `fk_comments_article_id` (`article_id`);

--
-- Index pour la table `subscriptions`
--
ALTER TABLE `subscriptions`
  ADD PRIMARY KEY (`user_id`,`topic_id`),
  ADD KEY `fk_subscriptions_user_id` (`user_id`),
  ADD KEY `fk_subscriptions_topic_id` (`topic_id`);

--
-- Index pour la table `topics`
--
ALTER TABLE `topics`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_avh1b2ec82audum2lyjx2p1ws` (`email`),
  ADD UNIQUE KEY `UK_dc4eq7plr20fdhq528twsak1b` (`username`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `articles`
--
ALTER TABLE `articles`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT pour la table `comments`
--
ALTER TABLE `comments`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `topics`
--
ALTER TABLE `topics`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `articles`
--
ALTER TABLE `articles`
  ADD CONSTRAINT `fk_articles_author_id` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_articles_topic_id` FOREIGN KEY (`topic_id`) REFERENCES `topics` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `comments`
--
ALTER TABLE `comments`
  ADD CONSTRAINT `fk_comments_article_id` FOREIGN KEY (`article_id`) REFERENCES `articles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_comments_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `subscriptions`
--
ALTER TABLE `subscriptions`
  ADD CONSTRAINT `fk_subscriptions_topic_id` FOREIGN KEY (`topic_id`) REFERENCES `topics` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_subscriptions_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
