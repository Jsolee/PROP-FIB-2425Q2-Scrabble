package Main;

public class Posicio implements Comparable<Posicio>{
    private int x;
    private int y;

    public Posicio(int x, int y) {
        if (x < 1 || y < 1 || x > 15 || y > 15) {
            throw new IllegalArgumentException("Les coordenades estan fora del taulell");
        }
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        if (x < 1 || x > 15) {
            throw new IllegalArgumentException("La coordenada X esta fora del taulell");
        }
        this.x = x;
    }
    public void setY(int y) {
        if (y < 1 || y > 15) {
            throw new IllegalArgumentException("La coordenada Y esta fora del taulell");
        }
        this.y = y;
    }

    @Override
    public int compareTo(Posicio other) {
        if (this.x != other.x) {
            return Integer.compare(this.x, other.x);
        }
        return Integer.compare(this.y, other.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Posicio)) return false;
        Posicio posicio = (Posicio) o;
        return x == posicio.x && y == posicio.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    @Override
    public String toString() {
        return "Posicio{" + "x=" + x + ", y=" + y + '}';
    }

}
