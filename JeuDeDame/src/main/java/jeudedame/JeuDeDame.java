package jeudedame;

import java.util.Scanner;

public class JeuDeDame {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Jeu partie = new Jeu();

        afficherBienvenue();

        boolean jeuEnCours = true;
        while (jeuEnCours) {
            afficherEtat(partie);

            String entree = scanner.nextLine().trim();

            // 1) Commandes
            if (estCommande(entree)) {
                if (entree.equals("quitter")) {
                    System.out.println("Au revoir !");
                    jeuEnCours = false;
                    continue;
                }

                Jeu nouvellePartie = gererCommande(partie, entree);
                if (nouvellePartie != null) {
                    partie = nouvellePartie;
                }
                continue;
            }

            // 2) Coup (coordonnées)
            jouerUnCoup(partie, entree);
        }

        scanner.close();
    }

    private static void afficherBienvenue() {
        System.out.println("=== BIENVENUE AU JEU DE DAMES ===");
        System.out.println("Règles : Tapez les coordonnées 'x1 y1 x2 y2'.");
        System.out.println("Exemple : '0 6 1 5' déplace le pion de la colonne 0, ligne 6 vers colonne 1, ligne 5.");
        System.out.println("Commandes spéciales : 'sauver [nom]', 'charger [nom]', 'quitter'.");
    }

    private static void afficherEtat(Jeu partie) {
        System.out.println("\n-----------------------------");
        partie.getPlateau().afficher();
        System.out.println("C'est au tour des " + partie.getJoueurCourant());
        System.out.print("Votre coup > ");
    }

    private static boolean estCommande(String entree) {
        return entree.equals("quitter")
                || entree.startsWith("sauver")
                || entree.startsWith("charger");
    }

    /**
     * @return une nouvelle partie si "charger" réussit, sinon null
     */
    private static Jeu gererCommande(Jeu partie, String entree) {
        if (entree.startsWith("sauver")) {
            String nom = extraireArgument(entree);
            if (nom == null) {
                System.out.println("Erreur : utilisez 'sauver NOM'.");
                return null;
            }
            partie.sauvegarder(nom);
            return null;
        }

        if (entree.startsWith("charger")) {
            String nom = extraireArgument(entree);
            if (nom == null) {
                System.out.println("Erreur : utilisez 'charger NOM'.");
                return null;
            }
            return Jeu.charger(nom); // peut être null si échec
        }

        return null;
    }

    private static String extraireArgument(String entree) {
        String[] parts = entree.split("\\s+");
        return (parts.length >= 2) ? parts[1] : null;
    }

    private static void jouerUnCoup(Jeu partie, String entree) {
        try {
            int[] c = parseCoords(entree);
            int x1 = c[0], y1 = c[1], x2 = c[2], y2 = c[3];

            Piece pieceSelectionnee = partie.getPlateau().getPiece(x1, y1);

            if (pieceSelectionnee == null) {
                System.out.println("ERREUR : Il n'y a pas de pièce ici !");
                return;
            }

            if (!partie.estMonTour(pieceSelectionnee)) {
                System.out.println("ERREUR : Ce n'est pas votre pièce ! C'est aux " + partie.getJoueurCourant());
                return;
            }

            partie.getPlateau().bougerPiece(x1, y1, x2, y2);
            partie.changerTour();

        } catch (Exception e) {
            System.out.println("Erreur de saisie. Format attendu : 0 6 1 5");
        }
    }

    private static int[] parseCoords(String entree) {
        String[] coords = entree.sp
