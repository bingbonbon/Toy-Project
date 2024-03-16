import javax.swing.*;
import java.awt.*;

public class CaptainAttack {
    Image image;
    String type;
    int x, y;
    int width, height;
    int damage;
    public CaptainAttack(String type, int x, int y) {
        switch(type) {
            case "attack1":
                this.image = new ImageIcon("src/images/c_attack1.png").getImage();
                this.x = x;
                this.y = y;
                this.width = 7;
                this.height = 7;
                this.damage = 10;
                this.type = "attack1";
                break;
            case "attack2":
                this.image = new ImageIcon("src/images/c_attack2.png").getImage();
                this.x = x;
                this.y = y;
                this.width = 18;
                this.height = 18;
                this.damage = 10;
                this.type = "attack2";
                break;
            case "critical_bomb":
                this.image = new ImageIcon("src/images/critical_bomb.png").getImage();
                this.x = x;
                this.y = y;
                this.width = 16;
                this.height = 30;
                this.damage = 100;
                this.type = "critical_bomb";
                break;
        }
    }
    public void fire() {
        switch(type) {
            case "attack1":
                this.x -= 10;
                break;
            case "attack2":
                this.x -= 15;
                break;
            case "critical_bomb":
                this.y += 15;
                break;
        }
    }
}
