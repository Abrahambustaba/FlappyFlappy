import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FlappyBird extends JFrame implements ActionListener, KeyListener, MouseListener {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int BIRD_SIZE = 30;
    private final int PIPE_WIDTH = 80;
    private final int PIPE_HEIGHT = 300;
    private final int PIPE_GAP = 200;
    private final int PIPE_SPEED = 8;
    private final int GRAVITY = 1;
    private final int JUMP_STRENGTH = -12;

    private JPanel gamePanel;
    private Timer gameTimer;
    private int birdY;
    private int birdVelocity;
    private int pipeX;
    private int pipeGapY;
    private int score;

    public FlappyBird() {
        setTitle("Flappy Bird");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBird(g);
                drawPipe(g);
                drawScore(g);
            }
        };
        gamePanel.setBackground(Color.CYAN);
        gamePanel.addMouseListener(this);
        add(gamePanel);

        addKeyListener(this);
        setFocusable(true);

        startGame();
        setVisible(true);
    }

    private void startGame() {
        birdY = HEIGHT / 2;
        birdVelocity = 0;
        pipeX = WIDTH;
        pipeGapY = HEIGHT / 2;
        score = 0;

        gameTimer = new Timer(20, this);
        gameTimer.start();
    }

    private void drawBird(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(WIDTH / 4, birdY, BIRD_SIZE, BIRD_SIZE);
    }

    private void drawPipe(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(pipeX, 0, PIPE_WIDTH, pipeGapY);
        g.fillRect(pipeX, pipeGapY + PIPE_GAP, PIPE_WIDTH, HEIGHT - pipeGapY - PIPE_GAP);
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 20, 30);
    }

    private void moveBird() {
        birdVelocity += GRAVITY;
        birdY += birdVelocity;
        if (birdY < 0) {
            birdY = 0;
            birdVelocity = 0;
        }
        if (birdY + BIRD_SIZE > HEIGHT) {
            birdY = HEIGHT - BIRD_SIZE;
            birdVelocity = 0;
        }
    }

    private void movePipe() {
        pipeX -= PIPE_SPEED;
        if (pipeX + PIPE_WIDTH < 0) {
            pipeX = WIDTH;
            pipeGapY = (int) (Math.random() * (HEIGHT - PIPE_GAP));
            score++;
        }
    }

    private void checkCollision() {
        if (birdY <= 0 || birdY >= HEIGHT - BIRD_SIZE ||
                (WIDTH / 4 + BIRD_SIZE >= pipeX && WIDTH / 4 <= pipeX + PIPE_WIDTH &&
                        (birdY <= pipeGapY || birdY + BIRD_SIZE >= pipeGapY + PIPE_GAP))) {
            gameOver();
        }
    }

    private void gameOver() {
        gameTimer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Your score: " + score);
        startGame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveBird();
        movePipe();
        checkCollision();
        gamePanel.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
            birdVelocity = JUMP_STRENGTH;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        birdVelocity = JUMP_STRENGTH;
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FlappyBird();
            }
        });
    }
}
