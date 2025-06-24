import java.awt.*;

public class Bullet {
    private double x, y;
    private final double dx, dy;
    private final int SPEED = 6;
    private final int SIZE = 15;

    private final Rectangle hitbox = new Rectangle();

    public Bullet(int startX, int startY, int targetX, int targetY) {
        x = startX;
        y = startY;
        double angle = Math.atan2(targetY - y, targetX - x);
        dx = Math.cos(angle) * SPEED;
        dy = Math.sin(angle) * SPEED;
    }

    public void update() {
        x += dx;
        y += dy;
    }

    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillOval((int)x, (int)y, SIZE, SIZE);
    }

    public Rectangle getHitbox() {
        hitbox.setBounds((int)x, (int)y, SIZE, SIZE);
        return hitbox;
    }

    public boolean isOffScreen(int width, int height) {
        return x < 0 || x > width || y < 0 || y > height;
    }
}
