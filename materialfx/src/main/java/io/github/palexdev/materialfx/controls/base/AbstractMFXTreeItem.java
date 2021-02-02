package io.github.palexdev.materialfx.controls.base;

import io.github.palexdev.materialfx.controls.MFXTreeView;
import io.github.palexdev.materialfx.utils.TreeItemStream;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.util.Callback;

import java.util.List;

/**
 * Base class for every item used in {@code MFXTreeView}.
 * <p>
 * To be precise the {@code MFXTreeView} class is just the container of the tree.
 * According to this implementation each item is a tree, the base concept is something like this:
 * <pre>
 * {@code
 * public class Node<T> {
 *     private T data;
 *     private Node<T> parent;
 *     private List<Node<T>> children;
 * }
 * }
 * </pre>
 * The root is defined as the element which parent is null.
 * <p></p>
 *
 * @param <T> The type of the data within TreeItem.
 * @see AbstractMFXTreeCell
 * @see MFXTreeView
 * @see ISelectionModel
 */
public abstract class AbstractMFXTreeItem<T> extends Control {
    //================================================================================
    // Properties
    //================================================================================
    protected final T data;
    protected final ObservableList<AbstractMFXTreeItem<T>> items = FXCollections.observableArrayList();
    protected final ObjectProperty<Callback<AbstractMFXTreeItem<T>, AbstractMFXTreeCell<T>>> cellFactory = new SimpleObjectProperty<>();
    private final ObjectProperty<MFXTreeView<T>> treeView = new SimpleObjectProperty<>(null);
    private final BooleanProperty startExpanded = new SimpleBooleanProperty(false);
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    protected AbstractMFXTreeItem<T> parent;

    //================================================================================
    // Constructors
    //================================================================================
    public AbstractMFXTreeItem(T data) {
        this.data = data;
    }

    //================================================================================
    // Abstract Methods
    //================================================================================
    public abstract ISelectionModel<T> getSelectionModel();

    protected abstract void defaultCellFactory();

    protected abstract void updateChildrenParent(List<? extends AbstractMFXTreeItem<T>> treeItems, final AbstractMFXTreeItem<T> newParent);

    //================================================================================
    // Methods
    //================================================================================

    /**
     * Checks if the parent item is null.
     *
     * @return true if parent is null otherwise returns false
     */
    public boolean isRoot() {
        return this.parent == null;
    }

    /**
     * Retrieves the tree's root.
     *
     * @return the root item
     */
    public AbstractMFXTreeItem<T> getRoot() {
        if (isRoot()) {
            return this;
        }

        AbstractMFXTreeItem<T> par = this;
        while (true) {
            par = par.getItemParent();
            if (par.isRoot()) {
                return par;
            }
        }
    }

    /**
     * Calculates the item's index in the tree structure.
     *
     * @return the item's index
     * @see TreeItemStream
     */
    public long getIndex() {
        if (isRoot()) {
            return 0;
        }

        return TreeItemStream.flattenTree(getRoot())
                .takeWhile(item -> !item.equals(this))
                .count();
    }

    /**
     * Calculates the number of items contained by this item (included).
     */
    public long getItemsCount() {
        return TreeItemStream.stream(this).count();
    }

    /**
     * Calculates the this item's level in the tree structure.
     */
    public int getLevel() {
        if (isRoot()) {
            return 0;
        }

        int index = 0;
        AbstractMFXTreeItem<T> par = this;
        while (true) {
            par = par.getItemParent();
            index++;
            if (par.isRoot()) {
                return index;
            }
        }
    }

    /**
     * Retrieves the next item at the same level in the tree.
     *
     * @return the item's next sibling. Null if is root or there is no other item next
     */
    public AbstractMFXTreeItem<T> getNextSibling() {
        if (isRoot()) {
            return null;
        }

        List<AbstractMFXTreeItem<T>> parentItems = getItemParent().getItems();
        int index = parentItems.indexOf(this);
        if (index == parentItems.size() - 1) {
            return null;
        }
        return parentItems.get(index + 1);
    }

    /**
     * Retrieves the previous item at the same level in the tree.
     *
     * @return the item's previous sibling. Null if is root or there is no other item before
     */
    public AbstractMFXTreeItem<T> getPreviousSibling() {
        if (isRoot()) {
            return null;
        }

        List<AbstractMFXTreeItem<T>> parentItems = getItemParent().getItems();
        int index = parentItems.indexOf(this);
        if (index == 0) {
            return null;
        }
        return parentItems.get(index - 1);
    }

    /**
     * @return if this item is leaf or not.
     */
    public boolean isLeaf() {
        return items.isEmpty();
    }

    /**
     * Retrieves the instance of the TreeView which contains the tree.
     * <p>
     * The reference is stored only in the root item so this method retrieves the root first
     * and then returns the tree view instance.
     *
     * @return the TreeView instance
     */
    public MFXTreeView<T> getTreeView() {
        if (isRoot()) {
            return treeView.get();
        } else {
            return getRoot().getTreeView();
        }
    }

    /**
     * Sets this item's TreeView reference to the given one.
     * <p>
     * <b>WARNING: THIS METHOD IS INTENDED FOR INTERNAL USE ONLY</b>
     *
     * @see MFXTreeView
     */
    public void setTreeView(MFXTreeView<T> treeView) {
        this.treeView.set(treeView);
    }

    public ObjectProperty<MFXTreeView<T>> treeViewProperty() {
        return treeView;
    }

    /**
     * @return the data associated with this item
     */
    public T getData() {
        return data;
    }

    /**
     * @return the list containing this item's children
     */
    public ObservableList<AbstractMFXTreeItem<T>> getItems() {
        return items;
    }

    /**
     * @return this item's parent item
     */
    public AbstractMFXTreeItem<T> getItemParent() {
        return this.parent;
    }

    /**
     * Sets this item's parent. This method should be called by subclasses only.
     * <p>
     * <b>WARNING: THIS METHOD IS INTENDED FOR INTERNAL USE ONLY</b>
     */
    public void setItemParent(AbstractMFXTreeItem<T> parent) {
        this.parent = parent;
    }

    /**
     * @return the state of {@link #startExpandedProperty()}
     */
    public boolean isStartExpanded() {
        return startExpanded.get();
    }

    /**
     * Sets the state of {@link #startExpandedProperty()}
     */
    public void setStartExpanded(boolean startExpanded) {
        this.startExpanded.set(startExpanded);
    }

    /**
     * Specifies whether the item should be expanded when created.
     */
    public BooleanProperty startExpandedProperty() {
        return startExpanded;
    }

    /**
     * @return this item's cell factory
     */
    public Callback<AbstractMFXTreeItem<T>, AbstractMFXTreeCell<T>> getCellFactory() {
        return cellFactory.get();
    }

    /**
     * Sets the cell factory used by this item.
     */
    public void setCellFactory(Callback<AbstractMFXTreeItem<T>, AbstractMFXTreeCell<T>> cellFactory) {
        this.cellFactory.set(cellFactory);
    }

    /**
     * Specifies the cell factory used by this item.
     */
    public ObjectProperty<Callback<AbstractMFXTreeItem<T>, AbstractMFXTreeCell<T>>> cellFactoryProperty() {
        return cellFactory;
    }

    /**
     * @return this item's selection state.
     */
    public boolean isSelected() {
        return selected.get();
    }

    /**
     * Sets this item selection state.
     */
    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    /**
     * Selection property.
     */
    public BooleanProperty selectedProperty() {
        return selected;
    }
}
