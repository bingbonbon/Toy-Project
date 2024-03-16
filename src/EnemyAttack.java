import javax.swing.*;
import java.awt.*;

public class EnemyAttack {
    static Image e_bullet = new ImageIcon("src/images/bullet.jpg").getImage();
    int bullet_x, bullet_y;
    int bullet_width = e_bullet.getWidth(null);
    int bullet_height = e_bullet.getHeight(null);

    private int bullet_speed = 5;

    public EnemyAttack(int bullet_x, int bullet_y) {
        this.bullet_x = bullet_x;
        this.bullet_y = bullet_y;
    }
    public void fire() {
        this.bullet_x -= bullet_speed;
    }


}
