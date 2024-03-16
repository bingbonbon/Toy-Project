import javax.swing.*;
import java.awt.*;

public class CaptainEnemy {
    public static String image_path = "src/images/captain_Enemy.jpg";
    static Image image = new ImageIcon(image_path).getImage();
    int enemy_x, enemy_y;
    int enemy_width = image.getWidth(null);
    int enemy_height = image.getHeight(null);
    int captain_hp = 800;

    public CaptainEnemy(int enemy_x, int enemy_y) {
        this.enemy_x = enemy_x;
        this.enemy_y = enemy_y;
    }

    public void move() {
        this.enemy_x -= 10;
    }
}
