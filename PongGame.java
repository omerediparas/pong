package FXGLpackage;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PongGame extends GameApplication {

    // final variables like width etc
    private final int APP_WIDTH = 900;
    private final int APP_HEIGHT = 700;
    private final int BALL_WIDTH = 25;
    private final int BALL_HEIGHT = 25;
    private final int BAT_WIDTH = 25;
    private final int BAT_HEIGHT = 100;

    private static final int PADDLE_SPEED = 5;
    private static final int BALL_SPEED = 5;
    double speed = 10;

    //entities
    private Entity bat1;
    private Entity bat2;
    private Entity ball;
    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(APP_WIDTH);
        gameSettings.setHeight(APP_HEIGHT);
        gameSettings.setTitle("Omer's Pong Game");
    }

    @Override
    protected void initGame() {
        bat1 = spawnBat(0,APP_HEIGHT / 2 - BAT_HEIGHT / 2);
        // how to change color???
        bat2 = spawnBat(APP_WIDTH - BAT_WIDTH , APP_HEIGHT / 2 - BAT_HEIGHT / 2);
        ball = spawnBall(APP_HEIGHT/2 - BALL_HEIGHT / 2,APP_WIDTH / 2 - BALL_WIDTH / 2);
    }

    @Override
    protected void initInput() {
        FXGL.getInput().addAction(new UserAction("up1") {
            @Override
            protected void onAction() {
                bat1.translateY(-PADDLE_SPEED);
            }
        },KeyCode.W);

        FXGL.getInput().addAction(new UserAction("down1") {
            @Override
            protected void onAction() {
                bat1.translateY(PADDLE_SPEED);
            }
        },KeyCode.S);

        FXGL.getInput().addAction(new UserAction("up2") {
            @Override
            protected void onAction() {
                bat2.translateY(-PADDLE_SPEED);
            }
        },KeyCode.UP);

        FXGL.getInput().addAction(new UserAction("down2") {
            @Override
            protected void onAction() {
                bat2.translateY(PADDLE_SPEED);
            }
        },KeyCode.DOWN);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score1",0);
        vars.put("score2",0);
    }

    @Override
    protected void initUI() {
        Text score1 = new Text();
        score1.setTranslateX(50);
        score1.setTranslateY(20);

        FXGL.getGameScene().addUINode(score1);

        Text score2 = new Text();
        score2.setTranslateX(APP_WIDTH - 50);
        score2.setTranslateY(APP_HEIGHT - 20);

        FXGL.getGameScene().addUINode(score2);

        score1.textProperty().bind(getWorldProperties().intProperty("score1").asString());
        score2.textProperty().bind(getWorldProperties().intProperty("score2").asString());

    }

    @Override
    protected void onUpdate(double tpf) {
        Point2D velocity = ball.getObject("velocity");
        ball.translate(velocity);

        if (ball.getX() == bat1.getRightX()
                && ball.getY() < bat1.getBottomY()
                && ball.getBottomY() > bat1.getY()) {
            ball.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
        }

        if (ball.getRightX() == bat2.getX()
                && ball.getY() < bat2.getBottomY()
                && ball.getBottomY() > bat2.getY()) {
            ball.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
        }

        if (ball.getX() <= 0) {
            getWorldProperties().increment("score2", +1);
            resetBall();
        }

        if (ball.getRightX() >= getAppWidth()) {
            getWorldProperties().increment("score1", +1);
            resetBall();
        }

        if (ball.getY() <= 0) {
            ball.setY(0);
            ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
        }

        if (ball.getBottomY() >= getAppHeight()) {
            ball.setY(getAppHeight() - BALL_WIDTH);
            ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
        }
    }
    protected Entity spawnBat( double x, double y){
        return entityBuilder()
                .at(x,y)
                .viewWithBBox(new Rectangle(BAT_WIDTH , BAT_HEIGHT , Color.RED))
                .buildAndAttach();

    }
    protected Entity spawnBall(double x, double y){
        return entityBuilder()
                .at(x,y)
                .viewWithBBox(new Rectangle(BALL_HEIGHT, BALL_WIDTH , Color.BLUE))
                .with("velocity" , new Point2D(speed,speed))
                .buildAndAttach();
    }
    private void resetBall() {
        ball.setPosition(getAppWidth() / 2 - BALL_WIDTH / 2, getAppHeight() / 2 - BALL_HEIGHT/ 2);
        ball.setProperty("velocity", new Point2D(BALL_SPEED, -BALL_SPEED));
    }

    public static void main(String[] args) {
        launch(args);
    }

      
}
