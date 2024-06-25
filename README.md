# Graphadon - Dynamic Graphs - Lab

Pour réaliser le TP de Graphes Dynamiques de Mercredi, 2 éléments sont nécessaires sur vos machines.


1. Télécharger le code source du TP sur le dépôt Git  suivant : https://github.com/graphstream/graphadon
   1. cliquer sur le bouton vert `Code` puis `Download ZIP`
   2. Dézipper le fichier téléchargé

2. Installer un environnement de développement java (et idéalement Git et Maven). Le plus simple est d’installer un IDE qui content tout cela (IndelliJ, Eclipse, etc.) ou un éditeur de code « solide »  avec un plugin java (VS Code, Atom, etc.)


Voici la procédure pour Eclipse :

- Télécharger Eclipse IDE for Java Developers sur le site officiel : https://www.eclipse.org/downloads/packages/release/2024-06/r/eclipse-ide-java-developers
- Dézipper le fichier téléchargé
- Lancer Eclipse en exécutant le fichier `eclipse` dans le dossier dézippé
- Selectionner un (nouveau) workspace (dossier de travail) pour stocker vos projets
- importer le projet Graphadon dans Eclipse
  1. File -> Import -> Existing Maven Projects
  2. Selectionner le dossier `Demos` dans l'archive dézippé précédemment
  3. Cliquer sur `Finish`

Vous pouvez maintenant lancer les différents exemples de graphes dynamiques en exécutant les classes Java correspondantes. par exemele `src/org/graphstream/tutorial/tutorial1/Tutorial1.java`.


Pour les plus ~~fous~~ courageux, vous pouvez aussi utiliser un éditeur de texte et compiler et exécuter les classes Java à la main. Pour cela, il vous faudra installer Java, Maven et Git sur votre machine. Voici la procédure pour Ubuntu :

- Installer Java : `sudo apt install default-jdk`
- Installer Maven : `sudo apt install maven`
- Installer Git : `sudo apt install git`
- Cloner le dépôt Git : `git clone https://github.com/graphstream/graphadon.git`
- Compiler le projet : `cd graphadon/Demos && mvn compile`
- Exécuter un exemple : `mvn exec:java -Dexec.mainClass="org.graphstream.tutorial.tutorial1.Tutorial1"`




