package assignment5;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;


public class WorldView extends Application {

    private double sceneWidth = 1000;
    private double sceneHeight = 1000;

    double gridWidth = sceneWidth / Params.world_width;
    double gridHeight = sceneHeight / Params.world_height;

    CritterNode[][] critterArray = new CritterNode[Params.world_width][Params.world_height];

    @Override
    public void start(Stage primaryStage) {


        Group root = new Group();

        // adds critters to critterArray
        for(Critter current : Critter.TestCritter.getPopulation()) {
        	int x = current.xCoord();
        	int y = current.yCoord();
			critterArray[x][y] = new CritterNode(current, x, y, gridWidth, gridHeight);
        }


        Scene scene = new Scene( root, sceneWidth, sceneHeight);

        primaryStage.setScene( scene);
        primaryStage.show();

    }

    public static class CritterNode extends StackPane {

        public CritterNode(Critter current, double x, double y, double width, double height) {
        	
            // create rectangle
            Rectangle rectangle = new Rectangle(width, height);
            rectangle.setStroke(Color.BLACK);
            rectangle.setFill(Color.LIGHTBLUE);

            Shape shape = current.viewShape();
            shape.setFill(current.viewFillColor());
            shape.setStroke(current.viewOutlineColor());
            
            // create label
            //Label label = new Label( name);

            // set position
            setTranslateX( x*width);
            setTranslateY( y*height);

            getChildren().addAll(shape);

        }

    }

}