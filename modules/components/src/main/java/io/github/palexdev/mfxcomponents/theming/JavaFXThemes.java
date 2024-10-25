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

package io.github.palexdev.mfxcomponents.theming;

import java.io.InputStream;
import java.net.URL;

import io.github.palexdev.mfxcomponents.theming.base.Theme;
import io.github.palexdev.mfxresources.MFXResources;

/**
 * Enumeration of the standard JavaFX themes. This is useful when used with {@link UserAgentBuilder}.
 * Implements {@link Theme}.
 */
public enum JavaFXThemes implements Theme {
    CASPIAN("javafx/caspian/caspian.css"),
    CASPIAN_NO_TRANSPARENCY("javafx/caspian/caspian-no-transparency.css"),
    CASPIAN_TWO_LEVEL_FOCUS("javafx/caspian/two-level-focus.css"),
    MODENA("javafx/modena/modena.css"),
    MODENA_NO_TRANSPARENCY("javafx/modena/modena-no-transparency.css"),
    MODENA_TOUCH("javafx/modena/touch.css"),
    MODENA_TWO_LEVEL_FOCUS("javafx/modena/two-level-focus.css"),
    FXVK("javafx/caspian/fxvk.css");

    private final String path;

    JavaFXThemes(String path) {
        this.path = path;
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public URL asURL(String path) {
        return MFXResources.loadURL(path);
    }

    @Override
    public InputStream assets() {
        String path = path().substring(0, path().lastIndexOf("/") + 1) + "assets.zip";
        return MFXResources.loadStream(path);
    }
}
