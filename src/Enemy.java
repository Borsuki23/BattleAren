import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Enemy {
    private int x, y, speed = 2;
    private final int WIDTH = 32, HEIGHT = 32;
    private final Rectangle hitbox = new Rectangle();

    private BufferedImage[][] animations;
    private int currentDirection = 2;
    private int currentFrame = 0, frameDelay = 0;

    private ArrayList<Bullet> bullets = new ArrayList<>();
    private long lastShotTime = 0;
    private final int shootCooldown = 1500;

    public Enemy(int x, int y) {
        this.x = x; this.y = y;
        animations = new BufferedImage[4][4];

        try {
            BufferedImage sheet = ImageIO.read(new File("assets/enemy.png"));
            for (int dir = 0; dir < 4; dir++) {
                for (int i = 0; i < 4; i++) {
                    animations[dir][i] = sheet.getSubimage(i * 32, dir * 32, 32, 32);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Enemy sprite load error");
        }


    }

    public void update(int px, int py, int playerWidth, int playerHeight) {
        // рух до гравця
        if (px < x) { x -= speed; currentDirection = 1; }
        if (px > x) { x += speed; currentDirection = 0; }
        if (py < y) { y -= speed; currentDirection = 3; }
        if (py > y) { y += speed; currentDirection = 2; }

        // кадри
        frameDelay++;
        if (frameDelay % 10 == 0)
            currentFrame = (currentFrame + 1) % animations[currentDirection].length;

        // стріляє
        long now = System.currentTimeMillis();
        if (now - lastShotTime >= shootCooldown) {
            bullets.add(new Bullet(x + WIDTH / 2, y + HEIGHT / 2, px + playerWidth / 2, py + playerHeight / 2));
            lastShotTime = now;
        }

        for (Bullet b : bullets) b.update();
        bullets.removeIf(b -> b.isOffScreen(1280, 720));
    }

    public void draw(Graphics g) {
        g.drawImage(animations[currentDirection][currentFrame], x, y, WIDTH, HEIGHT, null);
        for (Bullet b : bullets) b.draw(g);
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public Rectangle getHitbox() {
        hitbox.setBounds(x, y, WIDTH, HEIGHT);
        return hitbox;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
