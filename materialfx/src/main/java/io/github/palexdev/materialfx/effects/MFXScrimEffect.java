package io.github.palexdev.materialfx.effects;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Window;

/**
 * From Google's material design guidelines:
 * <p>
 * Scrims are temporary treatments that can be applied to Material surfaces for the purpose of making content on a surface less prominent.
 * They help direct user attention to other parts of the screen, away from the surface receiving a scrim.
 */
public class MFXScrimEffect {
    //================================================================================
    // Properties
    //================================================================================
    private final Rectangle scrim;

    //================================================================================
    // Constructor
    //================================================================================
    public MFXScrimEffect() {
        scrim = new Rectangle();
    }

    //================================================================================
    // Methods
    //================================================================================

    /**
     * Adds a scrim effect to the specified pane with specified opacity.
     *
     * @param pane    The pane to which add the effect
     * @param opacity The effect opacity/strength
     */
    public void scrim(Pane pane, double opacity) {
        scrim.setWidth(pane.getWidth());
        scrim.setHeight(pane.getHeight());
        scrim.setFill(Color.rgb(0, 0, 0, opacity));
        scrim.setBlendMode(BlendMode.SRC_ATOP);

        pane.getChildren().add(0, scrim);
    }

    /**
     * Adds a scrim effect to the specified pane with specified opacity.
     * It also simulates the modal behavior of {@code Stage}s, leaving only the specified
     * {@code Node} interactable.
     *
     * @param parent  The pane to which add the effect
     * @param child   The node to leave interactable
     * @param opacity The effect opacity/strength
     */
    public void modalScrim(Pane parent, Node child, double opacity) {
        scrim.setWidth(parent.getWidth());
        scrim.setHeight(parent.getHeight());
        scrim.setFill(Color.rgb(0, 0, 0, opacity));
        scrim.setBlendMode(BlendMode.SRC_ATOP);

        /*
         * Workaround, especially for SceneBuilder
         * This method adds the scrim effect to the given pane's children list
         * before the given node to leave interactable so if that node is let's say in position 2
         * and there are others controls after index 2 they will be interactable.
         * To fix that and avoid some hassle for developers this piece of code
         * finds the node to leave interactable and if it is not in the last position of the list
         * removes and re-adds it, then adds the scrim effect in the second-last position which of course is
         * (list.size() - 1)
         */
        ObservableList<Node> children = parent.getChildren();
        children.stream()
                .filter(node -> node.equals(child))
                .findFirst()
                .ifPresent(node -> {
                    if (children.indexOf(node) != children.size() - 1) {
                        parent.getChildren().remove(node);
                        parent.getChildren().add(node);
                    }
                });

        parent.getChildren().add(children.size() - 1, scrim);
    }

    /**
     * Adds a scrim effect to the specified {@code Window}'s root pane with the specified opacity.
     *
     * @param window  The desired window
     * @param opacity The desired opacity
     */
    public void scrimWindow(Window window, double opacity) {
        Pane root = (Pane) window.getScene().getRoot();

        scrim.setWidth(root.getWidth());
        scrim.setHeight(root.getHeight());
        scrim.setFill(Color.rgb(0, 0, 0, opacity));
        scrim.setBlendMode(BlendMode.SRC_ATOP);

        root.getChildren().add(scrim);
    }

    /**
     * Removes the scrim effect from the specified pane.
     *
     * @param pane The pane to which remove the effect.
     */
    public void removeEffect(Pane pane) {
        pane.getChildren().remove(scrim);
    }

    /**
     * Removes the scrim effect from the specified window.
     *
     * @param window The window to which remove the effect.
     */
    public void removeEffect(Window window) {
        Pane root = (Pane) window.getScene().getRoot();
        removeEffect(root);
    }

    public Node getScrimNode() {
        return scrim;
    }
}
