import javax.swing.*;
import java.awt.*;

public class PilotAttack {
    static Image bullet = new ImageIcon("src\\images\\bullet.jpg").getImage();
    int bullet_x;
    int bullet_y;
    static int bullet_width = bullet.getWidth(null);
    static int bullet_height = bullet.getHeight(null);
    int bullet_speed = 8;
    static int bullet_damage = 100;

    //생성자
    public PilotAttack(int bullet_x, int bullet_y) {
        this.bullet_x = bullet_x;
        this.bullet_y = bullet_y;
    }
    public void fire() {
        this.bullet_x += bullet_speed;
    }

}
