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

package app;

import io.github.palexdev.mfxcomponents.controls.progress.MFXProgressIndicator;
import io.github.palexdev.mfxcomponents.controls.progress.ProgressDisplayMode;
import io.github.palexdev.mfxcomponents.skins.MFXLinearProgressIndicatorSkin;
import io.github.palexdev.mfxcomponents.theming.JavaFXThemes;
import io.github.palexdev.mfxcomponents.theming.MaterialThemes;
import io.github.palexdev.mfxcomponents.theming.UserAgentBuilder;
import io.github.palexdev.mfxcore.observables.When;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.scenicview.ScenicView;

import static io.github.palexdev.mfxcore.utils.EnumUtils.next;

public class Sandbox extends Application {

    @Override
    public void start(Stage stage) {
        UserAgentBuilder.builder()
            .themes(JavaFXThemes.MODENA)
            .themes(MaterialThemes.INDIGO_LIGHT)
            .build()
            .setGlobal();

        MFXProgressIndicator indicator = new MFXProgressIndicator();
        StackPane pane = new StackPane(indicator);

        When.onInvalidated(indicator.skinProperty())
            .then(s -> {
                if (s instanceof MFXLinearProgressIndicatorSkin) return;
                indicator.setDisplayMode(next(ProgressDisplayMode.class, indicator.getDisplayMode()));
            })
            .listen();

        Scene scene = new Scene(pane, 600, 600);
        stage.setScene(scene);
        stage.show();

        ScenicView.show(scene);
    }
}
