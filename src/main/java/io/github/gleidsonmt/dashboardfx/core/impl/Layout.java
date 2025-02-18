package io.github.gleidsonmt.dashboardfx.core.impl;

import io.github.gleidsonmt.dashboardfx.core.Context;
import io.github.gleidsonmt.dashboardfx.core.controls.GNIconButton;
import io.github.gleidsonmt.dashboardfx.core.controls.icon.Icons;
import io.github.gleidsonmt.dashboardfx.core.interfaces.ActionView;
import io.github.gleidsonmt.dashboardfx.core.view.layout.Bar;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Version 0.0.1
 * Create on  02/04/2023
 */
public class Layout {
    private final HBox root = new HBox();
    private final BorderPane content = new BorderPane();
    private final Context context;

    private GNIconButton hamburger = new GNIconButton(Icons.HAMBURGER);

    private Node side;

    public Layout(Context context) {
        this.context = context;
        root.getStyleClass().add("layout");
        root.getChildren().add(content);
        HBox.setHgrow(content, Priority.ALWAYS);

        hamburger.getStyleClass().addAll("hamburger");
        
        root.widthProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.doubleValue() < 660) {
                if (root.getChildren().contains(side)) {
                    root.getChildren().remove(0);
                    bar().addInLeft(0, hamburger);
                }
            } else {
                if (!root.getChildren().contains(side)) {
                    if (side != null) {
                        root.getChildren().add(0, side);
                        bar().removeInLeft(hamburger);
                    }
                }
            }

        });

        hamburger.setOnMouseClicked(event -> {
            context.wrapper().drawer()
                    .content((StackPane) side)
                    .show();
        });
        
    }

    public void setContent(Node node) {
        content.setCenter(node);
    }

    public void setRight(Node node) {
        content.setRight(node);
    }

    public void setBar(Node bar) {
        content.setTop(bar);
    }

    public void setNav(Node node) {
        side = node;
        root.getChildren().add(0, node);
    }

    public void setNav(URL location) {
        // lembrar de colcoar exceções polir o codigo

        FXMLLoader loader = new FXMLLoader();
        try {

            loader.setLocation(location);
            loader.load();

            if(loader.getController() != null) {

                ActionView ac = loader.getController();
                ac.onInit(context);
                ac.onEnter();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //content.setLeft(loader.getRoot());
        root.getChildren().add(0, loader.getRoot());
        Region region = loader.getRoot();
        region.setMinWidth(250D);
        side = region;
    }

    public Node getContent() {
        return root;
    }

    public Bar bar() {
        return (Bar) content.getTop();
    }
}
