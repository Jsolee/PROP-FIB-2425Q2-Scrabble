package Main;

public class Posicio {
    private int x;
    private int y;

    public Posicio(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
