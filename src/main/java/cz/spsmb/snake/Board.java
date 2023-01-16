package cz.spsmb.snake;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Board extends AnimationTimer {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 120_000_000;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private javafx.scene.image.Image ball;
    private javafx.scene.image.Image apple;
    private javafx.scene.image.Image head;

    private Canvas canvas;

    public Board() {

        initBoard();
    }

    private long lastUpdate = 0;

    @Override
    public void handle(long now) {
        if (now - lastUpdate >= DELAY) {
            lastUpdate = now;
            doDrawing();
            if (inGame) {
                checkApple();
                checkCollision();
                move();
            }
        }
    }

    private void initBoard() {

        this.canvas = new Canvas(B_WIDTH, B_HEIGHT);
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        loadImages();
        initGame();

    }

    private void loadImages() {

        javafx.scene.image.Image iid = new javafx.scene.image.Image("dot.png");
        ball = iid;

        javafx.scene.image.Image iia = new javafx.scene.image.Image("apple.png");
        apple = iia;

        javafx.scene.image.Image iih = new javafx.scene.image.Image("head.png");
        head = iih;
    }

    private void initGame() {

        canvas.setOnKeyPressed(e -> {
            KeyCode key = e.getCode();

            if (key == KeyCode.LEFT && !rightDirection) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyCode.RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyCode.UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyCode.DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

        });

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        locateApple();

        this.start();
    }

    private void doDrawing() {

        GraphicsContext gc = canvas.getGraphicsContext2D();

        if (inGame) {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

            gc.drawImage(apple, apple_x, apple_y);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    gc.drawImage(head, x[z], y[z]);
                } else {
                    gc.drawImage(ball, x[z], y[z]);
                }
            }
        } else {
            gameOver(gc);
        }
    }

    private void gameOver(GraphicsContext gc) {
        String msg = "Game Over";
        Text text = new Text(msg);
        double textWidth = text.getLayoutBounds().getWidth();
        javafx.scene.text.Font small = new javafx.scene.text.Font("Helvetica", 14);

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFont(small);
        gc.setStroke(Color.WHITE);
        gc.strokeText(msg, B_WIDTH / 2 - textWidth / 2, B_HEIGHT / 2);

        this.stop();
    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            dots++;
            locateApple();
        }
    }

    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            //this.stop();
        }
    }

    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }


    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }
}