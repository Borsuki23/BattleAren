import java.awt.*;
import java.awt.event.KeyEvent;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    private int x, y, speed = 5;
    private boolean left, right, up, down;

    private BufferedImage[][] animations;
    private int currentDirection = 2;
    private int currentFrame = 0, frameDelay = 0;

    private final int WIDTH = 64, HEIGHT = 64;

    private int health = 3;
    private long lastDamageTime = 0;
    private final int damageCooldown = 1000;
    private final Rectangle hitbox = new Rectangle();

    public int getWidth() { return WIDTH; }
    public int getHeight() { return HEIGHT; }


    public Player(int x, int y) {
        this.x = x; this.y = y;
        animations = new BufferedImage[4][3];

        try {
            BufferedImage spriteSheet = ImageIO.read(new File("assets/player.png"));
            int[] rowMap = {3,2,0,1};
            for (int dir=0; dir<4; dir++) {
                for (int i=0; i<3; i++) {
                    animations[dir][i] = spriteSheet.getSubimage(i*16, rowMap[dir]*18, 16, 18);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Не вдалося завантажити спрайт");
        }
    }

    public void update() {
        boolean moved = false;
        if (left) {x-=speed; currentDirection=1; moved=true;}
        if (right){x+=speed; currentDirection=0; moved=true;}
        if (up)   {y-=speed; currentDirection=3; moved=true;}
        if (down) {y+=speed; currentDirection=2; moved=true;}

        x = Math.max(0, Math.min(x, 1280-WIDTH));
        y = Math.max(0, Math.min(y, 720-HEIGHT));

        if (moved) {
            frameDelay++;
            if (frameDelay%10==0) currentFrame=(currentFrame+1)%animations[currentDirection].length;
        } else {
            currentFrame = 1;
        }
    }

    public void draw(Graphics g) {
        if (animations!=null)
            g.drawImage(animations[currentDirection][currentFrame], x, y, WIDTH, HEIGHT, null);
        else {
            g.setColor(Color.CYAN);
            g.fillRect(x, y, WIDTH, HEIGHT);
        }
    }

    // Cooldown-для урону
    public void takeDamage(int enemyX, int enemyY) {
        long now = System.currentTimeMillis();
        if (now - lastDamageTime >= damageCooldown) {
            health--;
            lastDamageTime = now;

            // Відштовхування
            int dx = x - enemyX;
            int dy = y - enemyY;

            double length = Math.sqrt(dx * dx + dy * dy);
            if (length != 0) {
                dx = (int)((dx / length) * 100);
                dy = (int)((dy / length) * 100);
                x += dx;
                y += dy;
            }
        }
    }



    public int getHealth() { return health; }

    public Rectangle getHitbox() {
        hitbox.setBounds(x,y,WIDTH,HEIGHT);
        return hitbox;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT->left=true;
            case KeyEvent.VK_RIGHT->right=true;
            case KeyEvent.VK_UP->up=true;
            case KeyEvent.VK_DOWN->down=true;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT->left=false;
            case KeyEvent.VK_RIGHT->right=false;
            case KeyEvent.VK_UP->up=false;
            case KeyEvent.VK_DOWN->down=false;
        }
    }
}
