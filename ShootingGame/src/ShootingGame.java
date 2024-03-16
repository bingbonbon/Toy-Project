import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class ShootingGame extends JFrame {

    private Image bufferImage;
    private Graphics screenGraphics;
    private Image mainScreen = new ImageIcon("src\\images\\Main_Image.jpg").getImage();
    private Image loadingScreen = new ImageIcon("src\\images\\Controller_Image.jpg").getImage();
    private Image battleScreen = new ImageIcon("src\\images\\backGround_Image.png").getImage();

    private boolean ismainScreen, isloadingScreen, isbattleScreen;

    public Game game = new Game();

    public ShootingGame() {
        setTitle("ShootingGame");
        setUndecorated(false);
        setSize(Main.screen_width, Main.screen_height);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        init();

    }

    private void init() {
        ismainScreen = true;
        isloadingScreen = false;
        isbattleScreen = false;
        addKeyListener(new KeyListener());
    }

    public void paint(Graphics g) {
        bufferImage = createImage(Main.screen_width, Main.screen_height);
        screenGraphics = bufferImage.getGraphics();
        screenDraw(screenGraphics);
        g.drawImage(bufferImage, 0, 0, null);
    }

    public void screenDraw(Graphics g) {
        if (ismainScreen) {
            g.drawImage(mainScreen, 0, 0, null);
        }
        if (isloadingScreen) {
            g.drawImage(loadingScreen, 0, 0, null);
        }

        if (isbattleScreen) {

            game.gameDraw(g);
        }
        this.repaint();
    }

    private void loading() {
        ismainScreen = false;
        isloadingScreen = true;

        Timer loadingTimer = new Timer();
        TimerTask loadingTimerTask = new TimerTask() {
            @Override
            public void run() {
                isloadingScreen = false;
                isbattleScreen = true;
                game.start();
            }
        };
        loadingTimer.schedule(loadingTimerTask, 3000);
    }

    class KeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    game.setUp(true);
                    break;
                case KeyEvent.VK_S:
                    game.setDown(true);
                    break;
                case KeyEvent.VK_A:
                    game.setBack(true);
                    break;
                case KeyEvent.VK_D:
                    game.setForward(true);
                    break;
                case KeyEvent.VK_SPACE:
                    game.setGunfire(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
                    break;
                case KeyEvent.VK_P:
                    game.gameOverProcess();
                case KeyEvent.VK_ENTER:
                    if (ismainScreen) {
                        loading();
                    }
                    break;
            }
        }

        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    game.setUp(false);
                    break;
                case KeyEvent.VK_S:
                    game.setDown(false);
                    break;
                case KeyEvent.VK_A:
                    game.setBack(false);
                    break;
                case KeyEvent.VK_D:
                    game.setForward(false);
                    break;
                case KeyEvent.VK_SPACE:
                    game.setGunfire(false);
                    break;
            }
        }
    }
}