/*
 *     Copyright (C) 2021 Parisi Alessandro
 *     This file is part of MaterialFX (https://github.com/palexdev/MaterialFX).
 *
 *     MaterialFX is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     MaterialFX is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with MaterialFX.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.palexdev.materialfx.controls.base;

import io.github.palexdev.materialfx.selection.base.IListSelectionModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.util.Callback;

public interface IListView<T, C extends AbstractMFXFlowlessListCell<T>, S extends IListSelectionModel<T>> {
    ObservableList<T> getItems();
    ReadOnlyObjectProperty<ObservableList<T>> itemsProperty();
    void setItems(ObservableList<T> items);

    Callback<T, C> getCellFactory();
    ObjectProperty<Callback<T, C>> cellFactoryProperty();
    void setCellFactory(Callback<T, C> cellFactory);

    S getSelectionModel();
    ObjectProperty<S> selectionModelProperty();
    void setSelectionModel(S selectionModel);
}