import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Player player;
    private Enemy enemy;

    public GamePanel() {
        setPreferredSize(new Dimension(1280, 720));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        player = new Player(100, 100);
        enemy = new Enemy(600, 400);

        timer = new Timer(15, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        player.draw(g);
        enemy.draw(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Життя: " + player.getHealth(), 20, 30);

        if (player.getHealth() <= 0) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("Гру завершено", getWidth() / 2 - 150, getHeight() / 2);
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (player.getHealth() > 0) {
            player.update();
            enemy.update(player.getX(), player.getY(), player.getWidth(), player.getHeight());


            if (player.getHitbox().intersects(enemy.getHitbox())) {
                player.takeDamage(enemy .getX(), enemy.getY());
                for (Bullet b : enemy.getBullets()) {
                    if (player.getHitbox().intersects(b.getHitbox())) {
                        player.takeDamage((int)b.getHitbox().getCenterX(), (int)b.getHitbox().getCenterY());
                        enemy.getBullets().remove(b);
                        break;
                    }
                }

            }
        }
        repaint();
    }

    @Override public void keyPressed(KeyEvent e) { player.keyPressed(e); }
    @Override public void keyReleased(KeyEvent e) { player.keyReleased(e); }
    @Override public void keyTyped(KeyEvent e) {}
}
