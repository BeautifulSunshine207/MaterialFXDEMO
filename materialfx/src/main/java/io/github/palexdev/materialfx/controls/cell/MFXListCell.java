package io.github.palexdev.materialfx.controls.cell;

import io.github.palexdev.materialfx.MFXResourcesLoader;
import io.github.palexdev.materialfx.effects.RippleGenerator;
import io.github.palexdev.materialfx.utils.NodeUtils;
import javafx.css.*;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import java.util.List;

/**
 * This is the implementation of a ListCell restyled to comply with modern standards.
 * <p>
 * Extends {@code ListCell}, redefines the style class to "mfx-list-cell" for usage in CSS,
 * each cell has a {@code RippleGenerator} to generate ripple effects on click.
 */
public class MFXListCell<T> extends ListCell<T> {
    //================================================================================
    // Properties
    //================================================================================
    private static final StyleablePropertyFactory<MFXListCell<?>> FACTORY = new StyleablePropertyFactory<>(ListCell.getClassCssMetaData());
    private final String STYLE_CLASS = "mfx-list-cell";
    private final String STYLESHEET = MFXResourcesLoader.load("css/mfx-listcell.css").toString();
    private final RippleGenerator rippleGenerator;
    /**
     * Specifies the background color of the cell when it is selected.
     */
    private final StyleableObjectProperty<Paint> selectedColor = new SimpleStyleableObjectProperty<>(
            StyleableProperties.SELECTED_COLOR,
            this,
            "selectedColor",
            Color.rgb(180, 180, 255)
    );
    /**
     * Specifies the background color of the cell when the mouse is hover.
     */
    private final StyleableObjectProperty<Paint> hoverColor = new SimpleStyleableObjectProperty<>(
            StyleableProperties.HOVER_COLOR,
            this,
            "hoverColor",
            Color.rgb(50, 150, 255, 0.2)
    );

    //================================================================================
    // Constructors
    //================================================================================
    public MFXListCell() {
        rippleGenerator = new RippleGenerator(this);
        rippleGenerator.setRippleColor(Color.rgb(50, 150, 255));
        rippleGenerator.setInDuration(Duration.millis(400));

        initialize();
    }

    //================================================================================
    // Styleable Properties
    //================================================================================

    public static List<CssMetaData<? extends Styleable, ?>> getControlCssMetaDataList() {
        return StyleableProperties.cssMetaDataList;
    }

    //================================================================================
    // Methods
    //================================================================================
    private void initialize() {
        getStyleClass().add(STYLE_CLASS);
        setPadding(new Insets(8, 12, 8, 12));
        addListeners();
    }

    /**
     * Adds a listener to {@code listViewProperty} to bind the ripple radius to the
     * listView width.
     * <p>
     * Adds listeners to {@code selectedProperty} and {@code hoverProperty} to set the background color
     * according to {@link #selectedColor} and {@link #hoverColor}. When not selected the default color
     * is white.
     * <p>
     * Adds a listener to the {@link #selectedColor} property in case of changes and the cell is selected.
     */
    private void addListeners() {
        listViewProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                rippleGenerator.rippleRadiusProperty().bind(newValue.widthProperty().divide(2.0));
            }
        });

        selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                NodeUtils.updateBackground(MFXListCell.this, getSelectedColor());
            } else {
                NodeUtils.updateBackground(MFXListCell.this, Color.WHITE);
            }
        });

        hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (isSelected()) {
                return;
            }

            if (newValue) {
                if (getIndex() == 0) {
                    setBackground(new Background(new BackgroundFill(getHoverColor(), CornerRadii.EMPTY, Insets.EMPTY)));
                } else {
                    NodeUtils.updateBackground(MFXListCell.this, getHoverColor());
                }
            } else {
                NodeUtils.updateBackground(MFXListCell.this, Color.WHITE);
            }
        });

        selectedColor.addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.equals(oldValue) && isSelected()) {
                NodeUtils.updateBackground(MFXListCell.this, newValue);
            }
        });
    }

    public Paint getSelectedColor() {
        return selectedColor.get();
    }

    public void setSelectedColor(Paint selectedColor) {
        this.selectedColor.set(selectedColor);
    }

    public StyleableObjectProperty<Paint> selectedColorProperty() {
        return selectedColor;
    }

    public Paint getHoverColor() {
        return hoverColor.get();
    }

    public void setHoverColor(Paint hoverColor) {
        this.hoverColor.set(hoverColor);
    }

    public StyleableObjectProperty<Paint> hoverColorProperty() {
        return hoverColor;
    }

    //================================================================================
    // Override Methods
    //================================================================================
    @Override
    public String getUserAgentStylesheet() {
        return STYLESHEET;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return MFXListCell.getControlCssMetaDataList();
    }

    /**
     * Overridden method to add the {@code RippleGenerator} and
     * allow {@code Node}s.
     */
    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
            setText(null);
        } else {
            if (item instanceof Node) {
                Node nodeItem = (Node) item;
                setGraphic(nodeItem);

            } else {
                setText(item.toString());
            }

            if (!getChildren().contains(rippleGenerator)) {
                getChildren().add(0, rippleGenerator);
            }

            setOnMousePressed(mouseEvent -> {
                rippleGenerator.setGeneratorCenterX(mouseEvent.getX());
                rippleGenerator.setGeneratorCenterY(mouseEvent.getY());
                rippleGenerator.createRipple();
            });
        }
    }

    //================================================================================
    // CssMetaData
    //================================================================================
    private static class StyleableProperties {
        private static final List<CssMetaData<? extends Styleable, ?>> cssMetaDataList;

        private static final CssMetaData<MFXListCell<?>, Paint> SELECTED_COLOR =
                FACTORY.createPaintCssMetaData(
                        "-mfx-selected-color",
                        MFXListCell::selectedColorProperty,
                        Color.rgb(180, 180, 255)
                );

        private static final CssMetaData<MFXListCell<?>, Paint> HOVER_COLOR =
                FACTORY.createPaintCssMetaData(
                        "-mfx-hover-color",
                        MFXListCell::hoverColorProperty,
                        Color.rgb(50, 150, 255, 0.2)
                );

        static {
            cssMetaDataList = List.of(SELECTED_COLOR, HOVER_COLOR);
        }

    }
}
