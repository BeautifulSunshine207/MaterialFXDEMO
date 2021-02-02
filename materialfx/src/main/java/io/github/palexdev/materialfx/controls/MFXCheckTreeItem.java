package io.github.palexdev.materialfx.controls;

import io.github.palexdev.materialfx.MFXResourcesLoader;
import io.github.palexdev.materialfx.controls.base.AbstractMFXTreeCell;
import io.github.palexdev.materialfx.controls.base.AbstractMFXTreeItem;
import io.github.palexdev.materialfx.controls.base.ICheckModel;
import io.github.palexdev.materialfx.controls.cell.MFXCheckTreeCell;
import io.github.palexdev.materialfx.skins.MFXCheckTreeItemSkin;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.control.Skin;
import javafx.util.Callback;

import java.lang.ref.WeakReference;

/**
 * Simple implementation of a tree item with a checkbox.
 * <p>
 * The default associated {@link Skin} is {@link MFXCheckTreeItemSkin<T>}.
 *
 * @param <T> The type of the data within TreeItem.
 * @see MFXCheckTreeView
 * @see ICheckModel
 */
public class MFXCheckTreeItem<T> extends MFXTreeItem<T> {
    //================================================================================
    // Properties
    //================================================================================
    private final String STYLE_CLASS = "mfx-check-tree-item";
    private final String STYLESHEET = MFXResourcesLoader.load("css/mfx-treeitem.css").toString();

    private final BooleanProperty checked = new SimpleBooleanProperty(false);
    private final BooleanProperty indeterminate = new SimpleBooleanProperty(false);

    //================================================================================
    // Constructors
    //================================================================================
    public MFXCheckTreeItem(T data) {
        super(data);
        initialize();
    }

    public MFXCheckTreeItem(T data, Callback<AbstractMFXTreeItem<T>, AbstractMFXTreeCell<T>> cellFactory) {
        super(data, cellFactory);
        initialize();
    }

    //================================================================================
    // Methods
    //================================================================================

    /**
     * Sets the style class to "mfx-tree-view".
     * <p>
     * Adds a listener to {@link #treeViewProperty()} allowing item check before the Scene is shown
     * by calling the CheckModel {@link CheckModel#scanTree(MFXCheckTreeItem)} )} method.
     */
    private void initialize() {
        getStyleClass().add(STYLE_CLASS);

        treeViewProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && isRoot()) {
                CheckModel<T> checkModel = (CheckModel<T>) getSelectionModel();
                checkModel.scanTree((MFXCheckTreeItem<T>) getRoot());
            }
        });
    }

    public boolean isChecked() {
        return checked.get();
    }

    public void setChecked(boolean checked) {
        this.checked.set(checked);
    }

    public BooleanProperty checkedProperty() {
        return checked;
    }

    public boolean isIndeterminate() {
        return indeterminate.get();
    }

    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate.set(indeterminate);
    }

    public BooleanProperty indeterminateProperty() {
        return indeterminate;
    }

    //================================================================================
    // Override Methods
    //================================================================================

    /**
     * Overridden to return the ICheckModel instance of the MFXCheckTreeView.
     */
    @Override
    public ICheckModel<T> getSelectionModel() {
        return (ICheckModel<T>) super.getSelectionModel();
    }

    /**
     * Overridden to use {@link MFXCheckTreeCell}s.
     */
    @Override
    protected void defaultCellFactory() {
        super.cellFactory.set(cell -> new MFXCheckTreeCell<>(this));
    }

    /**
     * Overridden to use {@link MFXCheckTreeItemSkin}.
     */
    @Override
    protected Skin<?> createDefaultSkin() {
        return new MFXCheckTreeItemSkin<>(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return STYLESHEET;
    }

    //================================================================================
    // Events Class
    //================================================================================

    /**
     * Events class for the items.
     * <p>
     * Defines a new EventTypes:
     * <p>
     * - CHECK_EVENT: when an item is checked/unchecked, the item and all the parents up to the root should adjust their state accordingly. <p></p>
     * <p>
     * Note on constructor: when we fire an event we pass the item reference to distinguish between the item on which the item is fired and the parents.
     * <p>
     * Of course these events are for internal use only so they should not be used by users.
     */
    public static final class CheckTreeItemEvent<T> extends Event {
        public static final EventType<CheckTreeItemEvent<?>> CHECK_EVENT = new EventType<>(ANY, "CHECK_EVENT");
        private final WeakReference<AbstractMFXTreeItem<T>> itemRef;

        public CheckTreeItemEvent(EventType<? extends Event> eventType, AbstractMFXTreeItem<T> item) {
            super(eventType);
            this.itemRef = new WeakReference<>(item);
        }

        public AbstractMFXTreeItem<T> getItemRef() {
            return itemRef.get();
        }
    }
}
