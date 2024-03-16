import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Game extends Thread {

    private Image background = new ImageIcon("src/image/backGround_Image.png").getImage();
    private Image user_pilot = new ImageIcon("src/images/main_pilot.jpg").getImage();
    private int pilot_x, pilot_y;
    private int pilot_width = user_pilot.getWidth(null);
    private int pilot_height = user_pilot.getHeight(null);

    private int pilot_speed = 10;
    private int pilot_hp = 30;
    private int bullet_amount = 10;
    //총알 개수
    private int boss_time;
    private int attack_cnt;
    private boolean up, down, back, forward, gunfire;
    private int score = 0;
    private boolean gameover = false;
    private int backx;

    private ArrayList<PilotAttack> bulletList = new ArrayList<PilotAttack>();
    private ArrayList<BasicEnemy> basicEnemyList = new ArrayList<BasicEnemy>();
    private ArrayList<CaptainEnemy> captainEnemyList = new ArrayList<CaptainEnemy>();
    private ArrayList<EnemyAttack> enemyAttackList = new ArrayList<EnemyAttack>();
    private ArrayList<CaptainAttack> captainAttackList = new ArrayList<CaptainAttack>();
    private ArrayList<Item> itemList = new ArrayList<>();
    private PilotAttack p_bullet;
    private BasicEnemy basicEnemy;
    private CaptainEnemy captainEnemy;
    private EnemyAttack e_bullet;
    private CaptainAttack c_bullet;
    private Item item;

    private int delay = 20;
    private int cnt;
    private long pretime;

    @Override
    public void run() {
        gameOverProcess();
        while (true) {
            while (!gameover) {
                pretime = System.currentTimeMillis();
                if (System.currentTimeMillis() - pretime < delay) {
                    try {
                        Thread.sleep(delay - System.currentTimeMillis() + pretime);
                        KeyProcess();
                        pilotAttackProcess();
                        enemySummonProcess();
                        enemyMoveProcess();
                        enemyAttackProcess();
                        captainAttackProcess();
                        itemDropProcess();
                        itemMoveProcess();
                        cnt++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void KeyProcess() {
        if (up && pilot_y - pilot_speed > 0) {
            pilot_y -= pilot_speed;
        }
        if (down && pilot_y + pilot_height + pilot_speed < Main.screen_height) {
            pilot_y += pilot_speed;
        }
        if (back && pilot_x - pilot_speed > 0) {
            pilot_x -= pilot_speed;
        }
        if (forward && pilot_x + pilot_width + pilot_speed < Main.screen_width) {
            pilot_x += pilot_speed;
        }
        if (gunfire && cnt % bullet_amount == 0) {
            p_bullet = new PilotAttack(pilot_x + pilot_width, pilot_y + pilot_height / 2);
            bulletList.add(p_bullet);
        }
    }

    private void pilotAttackProcess() {
        for (int i = 0; i < bulletList.size(); i++) {
            p_bullet = bulletList.get(i);
            p_bullet.fire();

            for (int j = 0; j < basicEnemyList.size(); j++) {
                basicEnemy = basicEnemyList.get(j);
                if (p_bullet.bullet_x > basicEnemy.enemy_x && p_bullet.bullet_x < basicEnemy.enemy_x + basicEnemy.enemy_width && p_bullet.bullet_y > basicEnemy.enemy_y && p_bullet.bullet_y < basicEnemy.enemy_y + basicEnemy.enemy_height) {
                    basicEnemy.enemy_hp -= PilotAttack.bullet_damage;
                    bulletList.remove(p_bullet);
                }
                if (basicEnemy.enemy_hp <= 0) {
                    basicEnemyList.remove(basicEnemy);
                    score += 100;
                }
            }
            if (!captainEnemyList.isEmpty()) {
                for (int k = 0; k < captainEnemyList.size(); k++) {
                    captainEnemy = captainEnemyList.get(k);
                    if (p_bullet.bullet_x > captainEnemy.enemy_x && p_bullet.bullet_x < captainEnemy.enemy_x + captainEnemy.enemy_width && p_bullet.bullet_y > captainEnemy.enemy_y && p_bullet.bullet_y < captainEnemy.enemy_y + captainEnemy.enemy_height) {
                        captainEnemy.captain_hp -= PilotAttack.bullet_damage;
                        bulletList.remove(p_bullet);
                    }
                    if (captainEnemy.captain_hp <= 0) {
                        captainEnemyList.remove(captainEnemy);
                        captainAttackList.clear();
                        item = new Item((int) (Math.random() * 300) + 400, 1);
                        itemList.add(item);
                        score += 5000;
                        background = new ImageIcon("src/images/backGround_Image2.png").getImage();
                        boss_time = 0;
                    }
                }
            }
        }
    }

    private void enemySummonProcess() {
        //1.5초마다 기본 적 스폰,
        if (cnt % 75 == 0) {
            for (int i = 0; i < (int) (Math.random() * 3) + 1; i++) {
                basicEnemy = new BasicEnemy(1150, (int) (Math.random() * 620));
                basicEnemyList.add(basicEnemy);
            }
        }
        //10초가 되면 적 보스 생성
        //나중에 summontime 수정 1분 뒤에 소환 3000으로 고정
        if (cnt == 200) {
            captainEnemy = new CaptainEnemy(1190, 160);
            captainEnemyList.add(captainEnemy);
        }
    }

    private void enemyMoveProcess() {
        for (int i = 0; i < basicEnemyList.size(); i++) {
            int enemy_speed = (int) (Math.random() * 2 + 2);
            basicEnemy = basicEnemyList.get(i);
            basicEnemy.enemy_move(enemy_speed);

            if (basicEnemy.enemy_x + basicEnemy.enemy_width < 0) {
                basicEnemyList.remove(basicEnemy);
            }
            if (pilot_x < basicEnemy.enemy_x && pilot_x + pilot_width > basicEnemy.enemy_x && pilot_y < basicEnemy.enemy_y && pilot_y + pilot_height > basicEnemy.enemy_y) {
                basicEnemyList.remove(basicEnemy);
                pilot_hp -= 10;
                if (pilot_hp <= 0) {
                    gameover = true;
                }
            }
        }
        //보스 움직이는 부분
        if (!captainEnemyList.isEmpty()) {
            for (int j = 0; j < captainEnemyList.size(); j++) {
                if (captainEnemy.enemy_x > 900) {
                    captainEnemy = captainEnemyList.get(j);
                    captainEnemy.move();
                }
                //
                if (captainEnemy.enemy_x == 900) {
                    boss_time = 0;
                }
            }
        }
    }

    private void enemyAttackProcess() {
        if (cnt % 50 == 0) {
            e_bullet = new EnemyAttack(basicEnemy.enemy_x - 10, basicEnemy.enemy_y + basicEnemy.enemy_height / 2);
            enemyAttackList.add(e_bullet);
        }
        for (int i = 0; i < enemyAttackList.size(); i++) {
            e_bullet = enemyAttackList.get(i);
            e_bullet.fire();
            if (e_bullet.bullet_x > pilot_x && e_bullet.bullet_x < pilot_x + pilot_width && e_bullet.bullet_y > pilot_y && e_bullet.bullet_y < pilot_y + pilot_height) {
                //한발 맞을 때마다 플레이어가 받는 데미지
                pilot_hp -= 10;
                enemyAttackList.remove(e_bullet);
                if (pilot_hp <= 0) {
                    gameover = true;
                }
            }
            if (e_bullet.bullet_x + e_bullet.bullet_width < 0) {
                //맵밖으로 적 총알이 나가면 리스트에서 제거
                enemyAttackList.remove(e_bullet);
            }
        }
    }

    private void captainAttackProcess() {
        attack_cnt = 0;
        boss_time = 0;
        if(!captainEnemyList.isEmpty()) {
            captainEnemy = captainEnemyList.get(0);
            //0.04초마다
            if(cnt % 100 == 0) {
                attack_cnt++;
                System.out.println(attack_cnt);
                System.out.println(captainAttackList.size());
                System.out.println();
            }
            //파일럿 위치에 폭격
            if(cnt % 150 == 0) {
                c_bullet = new CaptainAttack("critical_bomb", pilot_x + pilot_width/2, 30);
                captainAttackList.add(c_bullet);
            }
            //2초마다
            switch(attack_cnt % 2) {
                //이동 범위 제한 공격
                case 0:
                    //0.06초마다
                    if(cnt % 3 == 0) {
                        c_bullet = new CaptainAttack("attack1", captainEnemy.enemy_x + 10, captainEnemy.enemy_y + captainEnemy.enemy_height - 20);
                        captainAttackList.add(c_bullet);
                        c_bullet = new CaptainAttack("attack1", captainEnemy.enemy_x + 10, captainEnemy.enemy_y + 20);
                        captainAttackList.add(c_bullet);
                    }
                    break;
                //범위 내 랜덤 공격
                case 1:
                    if(cnt % 5 == 0) {
                        c_bullet = new CaptainAttack("attack2", captainEnemy.enemy_x, (int)(Math.random()*361)+180);
                        captainAttackList.add(c_bullet);
                        c_bullet = new CaptainAttack("attack2", captainEnemy.enemy_x, pilot_y);
                        captainAttackList.add(c_bullet);
                    }
                    break;
            }
            for(int i = 0; i < captainAttackList.size(); i++) {
                c_bullet = captainAttackList.get(i);
                c_bullet.fire();
                if(c_bullet.x < - 20 || c_bullet.y > 750) {
                    captainAttackList.remove(c_bullet);
                }
                if(c_bullet.x > pilot_x && c_bullet.x < pilot_x + pilot_width && c_bullet.y > pilot_y && c_bullet.y < pilot_y + pilot_height) {
                    captainAttackList.remove(c_bullet);
                    pilot_hp -= c_bullet.damage;
                    if(pilot_hp <= 0) {
                        gameover = true;
                    }
                }
            }

        }
    }

    private void itemDropProcess() {
        int droptime = cnt%200;
        if(droptime == 0 && cnt != 0) {
            item = new Item((int)(Math.random()*200)+400, 1);
            itemList.add(item);
        }
    }
    private void itemMoveProcess() {
        if(!itemList.isEmpty()) {
            for(int i = 0; i < itemList.size(); i++) {
                item = itemList.get(i);
                item.move();
                if(cnt % 20 == 0) {
                    item.leftright();

                }
                if(item.item_x > pilot_x && item.item_x < pilot_x + pilot_width && item.item_y > pilot_y && item.item_y < pilot_y + pilot_height) {
                    int luck = (int)(Math.random()*5);
                    if(luck == 0 || luck == 1) {
                        //파일럿 속도 증가
                        pilot_speed += 5;
                    } else if(luck == 2 || luck == 3) {
                        //총알 속도 증가
                        bullet_amount -= 3;
                    } else {
                        //파일럿 체력 + 1
                        pilot_hp += 10;
                    }
                    itemList.remove(item);
                }
            }
        }
    }

    public void gameOverProcess() {
        gameover = false;
        //정보들 재설정
        cnt = 0;
        score = 0;
        bulletList.clear();
        basicEnemyList.clear();
        enemyAttackList.clear();
        captainEnemyList.clear();
        captainAttackList.clear();
        background = new ImageIcon("src/images/backGround_Image.png").getImage();
        backx = 0;
        boss_time = 0;
        attack_cnt = 0;
        //파일럿 리스폰 위치 초기화
        pilot_hp = 30;
        pilot_x = 10;
        pilot_y = (Main.screen_height - pilot_height) / 2;
        pilot_speed = 10;
    }

    //gameDraw아래에 쓰일 객체들의 Draw를 한번에 모아서 screenDraw에 보낼 메서드
    public void gameDraw(Graphics g) {
        backGroundDraw(g);
        userDraw(g);
        enemyDraw(g);
        captainAttackDraw(g);
        numDraw(g);
        itemDraw(g);
        gameOverDraw(g);
    }

    //메인 객체 Draw 메서드 - 아마 다른 객체들도 이런 식으로 만들어서 gameDraw에 합치면될 듯
    public void userDraw(Graphics g) {
        g.drawImage(user_pilot, pilot_x, pilot_y, null);
        for(int i = 0; i < bulletList.size(); i++) {
            p_bullet = bulletList.get(i);
          //g.drawImage(PilotAttack.bullet, PilotAttack.bullet_x, PilotAttack.bullet_y, null); //STATIC 필요함
            g.drawImage(PilotAttack.bullet, p_bullet.bullet_x, p_bullet.bullet_y, null);
        }
    }
    public void backGroundDraw(Graphics g) {
        //이미지 크기 : 2000x720
        g.drawImage(background, backx, 0, null);
        //배경화면 속도 조절 부
        if(cnt%3 == 0) {
            backx--;
        }
        if(backx < (-1)*(background.getWidth(null) - Main.screen_width)) {
            g.drawImage(background, Main.screen_width - ((backx*(-1)) - 800), 0, null);
        }
        if(backx == (-1)*background.getWidth(null)) {
            backx = 0;
        }
    }


    public void enemyDraw(Graphics g) {
        for(int i = 0; i < basicEnemyList.size(); i++) {
            basicEnemy = basicEnemyList.get(i);
            g.drawImage(BasicEnemy.basic_enemy, basicEnemy.enemy_x, basicEnemy.enemy_y, null);
            //적 체력 표시 필요하면 활성화
//            g.setColor(Color.GREEN);
//            g.fillRect(basicEnemy.enemy_x+1, basicEnemy.enemy_y - 10, basicEnemy.enemy_hp+20, 5);
        }
        if(!captainEnemyList.isEmpty()) {
            for(int j = 0; j < captainEnemyList.size(); j++) {
                captainEnemy = captainEnemyList.get(j);
                g.drawImage(CaptainEnemy.image, captainEnemy.enemy_x, captainEnemy.enemy_y, null);
                //보스 체력 바 표시
                g.setColor(Color.GRAY);
                g.fillRect(200, 50, 800, 5);
                g.setColor(Color.red);
                g.fillRect(200, 50, captainEnemy.captain_hp, 5);
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("first boss", 200, 80);

            }
        }
        for(int i = 0; i < enemyAttackList.size(); i++) {
            e_bullet = enemyAttackList.get(i);
            g.drawImage(EnemyAttack.e_bullet, e_bullet.bullet_x, e_bullet.bullet_y, null);
        }
    }

    public void captainAttackDraw(Graphics g) {
        if(!captainAttackList.isEmpty()) {
            for(int i = 0; i < captainAttackList.size(); i++) {
                c_bullet = captainAttackList.get(i);
                g.drawImage(c_bullet.image, c_bullet.x, c_bullet.y, null);
            }
        }
    }

    public void itemDraw(Graphics g) {
        if(!itemList.isEmpty()) {
            for(int i = 0; i < itemList.size(); i++) {
                item = itemList.get(i);
                g.drawImage(Item.image, item.item_x, item.item_y, null);
            }
        }
    }

    public void numDraw(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score : " + score, 20, 50);
        g.setColor(Color.black);
        g.drawString("life : " + pilot_hp/10, 20, 80);

    }

    public void gameOverDraw(Graphics g) {
        if(gameover) {
            //죽었을 때 화면 정지 후 띄울 배경
            g.setColor(new Color(0x95474747, true));
            g.fillRect(250, 185, 700, 350);
            //메세지 안내문
            g.setColor(new Color(213, 47, 47));
            g.setFont(new Font("Arial", Font.BOLD, 56));
            g.drawString("You've got shot down", 300, 285);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 32));
            g.drawString("press [P] to restart", 450, 410);
        }
    }
    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setBack(boolean back) {
        this.back = back;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
    }

    public void setGunfire(boolean gunfire) {
        this.gunfire = gunfire;
    }
}