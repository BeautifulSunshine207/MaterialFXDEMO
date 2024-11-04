/*
 * Copyright (C) 2023 Parisi Alessandro - alessandro.parisi406@gmail.com
 * This file is part of MaterialFX (https://github.com/palexdev/MaterialFX)
 *
 * MaterialFX is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * MaterialFX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with MaterialFX. If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.palexdev.mfxcomponents.theming.enums;

import java.util.HashMap;
import java.util.Map;

import javafx.css.PseudoClass;
import javafx.scene.Node;

/**
 * This enumerator keeps references to all the {@link PseudoClass}es needed by MaterialFX components.
 */
public enum PseudoClasses {
    DISABLED(PseudoClass.getPseudoClass("disabled")),
    ERROR(PseudoClass.getPseudoClass("error")),
    EXTENDED(PseudoClass.getPseudoClass("extended")),
    FIRST(PseudoClass.getPseudoClass("first")),
    FOCUSED(PseudoClass.getPseudoClass("focused")),
    FOCUS_VISIBLE(PseudoClass.getPseudoClass("focus-visible")),
    FOCUS_WITHIN(PseudoClass.getPseudoClass("focus-within")),
    HOVER(PseudoClass.getPseudoClass("hover")),
    INDETERMINATE(PseudoClass.getPseudoClass("indeterminate")),
    LAST(PseudoClass.getPseudoClass("last")),
    PRESSED(PseudoClass.getPseudoClass("pressed")),
    SELECTABLE(PseudoClass.getPseudoClass("selectable")),
    SELECTED(PseudoClass.getPseudoClass("selected")),
    WITH_ICON_LEFT(PseudoClass.getPseudoClass("with-icon-left")),
    WITH_ICON_RIGHT(PseudoClass.getPseudoClass("with-icon-right")),
    ;

    private static final Map<String, PseudoClass> CUSTOM_CLASSES_CACHE = new HashMap<>();

    private final PseudoClass pseudoClass;

    PseudoClasses(PseudoClass pseudoClass) {
        this.pseudoClass = pseudoClass;
    }

    /**
     * Activates or deactivates the PseudoClass on the given node.
     */
    public void setOn(Node node, boolean state) {
        node.pseudoClassStateChanged(pseudoClass, state);
    }

    public static void setOn(Node node, String pseudoClass, boolean state) {
        PseudoClass pClass = CUSTOM_CLASSES_CACHE.computeIfAbsent(pseudoClass, PseudoClass::getPseudoClass);
        node.pseudoClassStateChanged(pClass, state);
    }

    /**
     * Checks whether the PseudoClass is active or not on the given node by searching in the
     * {@link Node#getPseudoClassStates()} Set.
     */
    public boolean isActiveOn(Node node) {
        return node.getPseudoClassStates().contains(pseudoClass);
    }

    public static boolean isActiveOn(Node node, PseudoClass pseudoClass) {
        return node.getPseudoClassStates().contains(pseudoClass);
    }

    public PseudoClass getPseudoClass() {
        return pseudoClass;
    }
}
