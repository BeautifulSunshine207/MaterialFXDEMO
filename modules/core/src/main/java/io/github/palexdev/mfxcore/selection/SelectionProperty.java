package io.github.palexdev.mfxcore.selection;

import javafx.beans.property.SimpleBooleanProperty;

/**
 * Extension of {@link SimpleBooleanProperty} meant to be used by classes implementing {@link Selectable}.
 * <p></p>
 * This property overrides the {@link #set(boolean)} method to correctly handle the selection when a group is/is not present,
 * see {@link #set(boolean)} for more info.
 * <p>
 * Note that for this purpose this property needs the reference to the {@link Selectable} on which it will operate, in order
 * to get the {@code Selectable}'s {@link SelectionGroup}.
 * <p></p>
 * Last note but not least, if for whatever reason you need to override the {@code set(...)} method beware that the
 * {@code newValue} parameter may not be right as it may be modified by the {@code Selectables}'s {@link SelectionGroup}
 * (if there's one), since in Java all parameters are passed by value you won't be able to see the right value.
 * There are two ways to avoid this issue:
 * <p> 1) If you need to execute some kind of side effect, and you don't need the old
 * selection state, you can move your code to the {@link #invalidated()} method instead
 * <p> 2) If the above solution cannot be implemented, {@link SelectionGroup} offers a method to check what the true state
 * of a {@code Selectable} should be, therefore use {@link SelectionGroup#check(Selectable, boolean)} to get the true value
 */
public class SelectionProperty extends SimpleBooleanProperty {
    //================================================================================
    // Properties
    //================================================================================
    private final Selectable selectable;

    //================================================================================
    // Constructors
    //================================================================================
    public SelectionProperty(Selectable selectable) {
        this.selectable = selectable;
    }

    public SelectionProperty(Selectable selectable, boolean initialValue) {
        super(initialValue);
        this.selectable = selectable;
    }

    //================================================================================
    // Overridden Methods
    //================================================================================

    /**
     * {@inheritDoc}
     * <p></p>
     * Overridden to correctly handle the selection state (true|false) when the {@link Selectable} is/is not in a
     * {@link SelectionGroup}.
     * <p>
     * When it is in a group, the {@code newValue} is given by {@link SelectionGroup#check(Selectable, boolean)}.
     * This is because there are cases in which the selection cannot be set to true/false at the user will,
     * the group's rules will prevail.
     * <p></p>
     * For example the group will not allow a {@link Selectable} to be deselected if {@link SelectionGroup#isAtLeastOneSelected()}
     * is true and it is the only one present in the selection list. Cases like this, but not limited to, must be handled
     * by the group.
     * <p></p>
     * Now there can be two cases:
     * <p> 1) The returned state is "selected" and the group' selection Set doesn't contain the {@code Selectable}
     * <p> 2) The returned state is "deselected" and the group' selection Set contains the {@code Selectable}
     * <p>
     * In either cases the {@link #invalidated()} is invoked!
     */
    @Override
    public void set(boolean newValue) {
        SelectionGroup group = selectable.getSelectionGroup();
        if (group != null) {
            newValue = group.check(selectable, newValue);
            if ((newValue && !group.getSelection().contains(selectable)) ||
                    !newValue && group.getSelection().contains(selectable)) {
                invalidated();
            }
        }
        super.set(newValue);
    }

    /**
     * {@inheritDoc}
     * <p></p>
     * Overridden to update the {@link SelectionGroup} assigned to the {@link Selectable} handled by this property.
     * <p>
     * If the {@code Selectable} is in a group, and the selection state has changed (or additional checks decide that
     * invalidation is needed, see {@link #set(boolean)}), then {@link SelectionGroup#handleSelection(Selectable, boolean)}.
     * <p>
     * Last but not least, note that the invalidation doesn't occur if the group is changing its state due to a "switch"
     * operation, see {@link SelectionGroup#isSwitching()}.
     */
    @Override
    protected void invalidated() {
        SelectionGroup group = selectable.getSelectionGroup();
        if (group != null && !group.isSwitching())
            group.handleSelection(selectable, get());
    }
}