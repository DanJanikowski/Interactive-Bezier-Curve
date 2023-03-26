import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

    public static GraphicsContext g;

    public static final long TIMESTEP = 30;
    public static int w = 1800, h = 900;
    public Space m;
    public Vector selected = null;

    @Override
    public void start(Stage stage) throws Exception{
        BorderPane bp = new BorderPane();
        bp.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(bp);
        Canvas canvas = new Canvas(w, h);
        bp.setCenter(canvas);
        g = canvas.getGraphicsContext2D();
        m = new Space();

        scene.setRoot(bp);
        stage.setScene(scene);

        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Vector mouse = new Vector(event.getSceneX(), event.getSceneY());
                for (Vector v: m.v) {
                    if (v.clone().subtract(mouse).magnitude() <= 5) {
                        selected = v;
                        break;
                    }
                }
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (selected != null)
                    selected.set(new Vector(event.getSceneX(), event.getSceneY()));
            }
        });
        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (selected == null)
                    m.addPoint(event.getSceneX(), event.getSceneY());
                if (selected != null)
                    selected = null;
            }
        });
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                KeyCode key = event.getCode();
                switch (key) {
                    case R:
                        m.reset();
                        break;
                    case O:
                        break;
                    case P:
                        break;
                    case X:
                        System.exit(0);
                        break;
                }
            }
        });

        stage.show();

        ScheduledExecutorService e = Executors.newSingleThreadScheduledExecutor();
        e.scheduleAtFixedRate(this::tick, 0, TIMESTEP, TimeUnit.MILLISECONDS);
    }

    public void tick() {
        CountDownLatch drawLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            g.clearRect(0, 0, w, h);
            m.draw(g);
            drawLatch.countDown();
        });
        try {
            drawLatch.await();
        } catch (InterruptedException ignore) {}
    }

    @Override
    public void stop() throws Exception{
        System.exit(0);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
