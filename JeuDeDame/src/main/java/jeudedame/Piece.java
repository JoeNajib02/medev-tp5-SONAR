package jeudedame;

import java.io.Serializable;

public class Piece implements Serializable {

    private Point2D pos;
    private String couleur;
    private Boolean isKing;

    // Taille du plateau (même valeur que dans Plateau)
    private static final int TAILLE = 10;

    public enum Direction {
        HAUT_GAUCHE,
        HAUT_DROIT,
        BAS_GAUCHE,
        BAS_DROIT
    }

    public Piece(Point2D pos, String couleur) {
        this.pos = pos;
        this.couleur = couleur;
        this.isKing = false;
    }

    // --- Getters / Setters ---
    public Point2D getPos() {
        return pos;
    }

    public String getCouleur() {
        return couleur;
    }

    public Boolean getIsKing() {
        return isKing;
    }

    public void setPos(Point2D pos) {
        this.pos = pos;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public void setIsKing(Boolean isKing) {
        this.isKing = isKing;
    }

    public void passerDame() {
        this.isKing = true;
    }

    // -----------------------------------------------------------
    // DEPLACER : ne pas déplacer si la case d'arrivée est occupée
    // -----------------------------------------------------------
    public boolean deplacer(Plateau plateau, Direction direction, int distance) {
        verifierPlateauEtDirection(plateau, direction);

        int x1 = pos.getX();
        int y1 = pos.getY();

        int d = calculerDistanceDeplacement(distance);
        int[] delta = calculerDelta(direction, d);

        int x2 = x1 + delta[0];
        int y2 = y1 + delta[1];

        if (!estDansPlateau(x2, y2)) {
            return false;
        }

        if (plateau.getPiece(x2, y2) != null) {
            return false;
        }

        plateau.bougerPiece(x1, y1, x2, y2);
        return true;
    }

    private int calculerDistanceDeplacement(int distance) {
        if (!Boolean.TRUE.equals(isKing)) {
            return 1; // pion : toujours 1 case
        }
        if (distance <= 0) {
            throw new IllegalArgumentException("La distance doit être positive pour une dame.");
        }
        return distance;
    }

    // -----------------------------------------------------------
    // MANGER : vérifier s'il y a une pièce à manger puis déplacer
    // -----------------------------------------------------------
    public boolean manger(Plateau plateau, Direction direction, int distance) {
        verifierPlateauEtDirection(plateau, direction);
        verifierDistanceManger(distance);

        int x1 = pos.getX();
        int y1 = pos.getY();

        int[] delta = calculerDelta(direction, distance);
        int x2 = x1 + delta[0];
        int y2 = y1 + delta[1];

        if (!estDansPlateau(x2, y2)) {
            return false;
        }

        if (plateau.getPiece(x2, y2) != null) {
            return false;
        }

        if (!Boolean.TRUE.equals(isKing)) {
            return mangerPion(plateau, x1, y1, x2, y2);
        }

        return mangerDame(plateau, x1, y1, x2, y2);
    }

    private void verifierPlateauEtDirection(Plateau plateau, Direction direction) {
        if (plateau == null || direction == null) {
            throw new IllegalArgumentException("Plateau ou direction null.");
        }
    }

    private void verifierDistanceManger(int distance) {
        if (!Boolean.TRUE.equals(isKing)) {
            if (distance != 2) {
                throw new IllegalArgumentException("Un pion doit sauter exactement 2 cases pour manger.");
            }
            return;
        }

        if (distance < 2) {
            throw new IllegalArgumentException("Une dame doit sauter au moins 2 cases pour manger.");
        }
    }

    private boolean mangerPion(Plateau plateau, int x1, int y1, int x2, int y2) {
        int mx = (x1 + x2) / 2;
        int my = (y1 + y2) / 2;

        Piece victime = plateau.getPiece(mx, my);
        if (victime == null) {
            return false;
        }

        if (victime.getCouleur().equals(this.couleur)) {
            return false;
        }

        // ⚠️ Plateau ne permet pas de supprimer la victime (mettre null).
        // On déplace au moins l'attaquant :
        plateau.bougerPiece(x1, y1, x2, y2);
        return true;
    }

    private boolean mangerDame(Plateau plateau, int x1, int y1, int x2, int y2) {
        int stepX = Integer.compare(x2, x1); // -1 ou +1
        int stepY = Integer.compare(y2, y1); // -1 ou +1

        int cx = x1 + stepX;
        int cy = y1 + stepY;

        Piece victime = null;

        while (cx != x2 && cy != y2) {
            Piece p = plateau.getPiece(cx, cy);
            if (p != null) {
                if (victime != null) {
                    return false; // plus d'une pièce sur le chemin
                }
                if (p.getCouleur().equals(this.couleur)) {
                    return false; // pièce alliée sur le chemin
                }
                victime = p;
            }
            cx += stepX;
            cy += stepY;
        }

        if (victime == null) {
            return false;
        }

        // ⚠️ Plateau ne permet pas de supprimer la victime.
        plateau.bougerPiece(x1, y1, x2, y2);
        return true;
    }

    private int[] calculerDelta(Direction direction, int distance) {
        return switch (direction) {
            case HAUT_GAUCHE -> new int[]{-distance, -distance};
            case HAUT_DROIT  -> new int[]{ distance, -distance};
            case BAS_GAUCHE  -> new int[]{-distance,  distance};
            case BAS_DROIT   -> new int[]{ distance,  distance};
        };
    }

    private boolean estDansPlateau(int x, int y) {
        return x >= 0 && x < TAILLE && y >= 0 && y < TAILLE;
    }
}
