import javax.swing.*;
import java.awt.*;

public class BasicEnemy {
    public static String image_path = "src/images/basic_enemy.jpg";
    static Image basic_enemy = new ImageIcon(image_path).getImage();
    int enemy_x, enemy_y;
    int enemy_width = basic_enemy.getWidth(null);
    int enemy_height = basic_enemy.getHeight(null);
    int enemy_speed;
    int enemy_hp = 10;
    public BasicEnemy(int enemy_x, int enemy_y) {
        this.enemy_x = enemy_x;
        this.enemy_y = enemy_y;
    }

    public void enemy_move(int enemy_speed) {
        this.enemy_x -= enemy_speed;
    }
}
