import javax.swing.*;
import java.awt.*;

public class Item {
    static Image image = new ImageIcon("src/images/Item.jpg").getImage();
    int item_x, item_y;
    int item_width = image.getWidth(null);
    int item_height = image.getHeight(null);
    public Item(int item_x, int item_y) {
        this.item_x = item_x;
        this.item_y = item_y;
    }

    public void move() {
        this.item_y += 5;
    }
    public void leftright() {
        int rl = (int)(Math.random()*2);
        if(rl == 0) {
            this.item_x -= 30;
        } else {
            this.item_x += 30;
        }
    }


}
