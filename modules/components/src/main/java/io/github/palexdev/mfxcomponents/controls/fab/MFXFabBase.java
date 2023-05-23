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

package io.github.palexdev.mfxcomponents.controls.fab;

import io.github.palexdev.mfxcomponents.behaviors.MFXFabBehavior;
import io.github.palexdev.mfxcomponents.controls.base.MFXButtonBase;
import io.github.palexdev.mfxcomponents.controls.base.MFXSkinBase;
import io.github.palexdev.mfxcomponents.skins.MFXFabSkin;
import io.github.palexdev.mfxcomponents.theming.enums.PseudoClasses;
import io.github.palexdev.mfxcore.base.properties.styleable.StyleableBooleanProperty;
import io.github.palexdev.mfxcore.observables.When;
import io.github.palexdev.mfxcore.utils.fx.StyleUtils;
import io.github.palexdev.mfxresources.base.properties.IconProperty;
import io.github.palexdev.mfxresources.fonts.IconProvider;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleablePropertyFactory;
import javafx.scene.control.Skin;
import javafx.scene.text.Font;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Extension of {@link MFXButtonBase} and base class to implement the Floating Action Buttons shown
 * in the MD3 guidelines.
 * <p></p>
 * This base class has one implementation that is styled: {@link MFXFab}.
 * M3 guidelines also show the Extended variant. Since they also show that a standard FAB can transition to an Extended
 * one through an animation, and vice-versa, I decided to merge the Extended variant in the standard one, add a property to extend it,
 * {@link #extendedProperty()}, and implement FAB specific behavior for animations.
 * <p>
 * The default behavior for all {@link MFXFabBase} components is {@link MFXFabBehavior}.
 * <p>
 * This is meant to be used by users that want an untouched base FAB, this component just like {@link MFXButtonBase} is
 * not styled by the themes by default.
 * <p>
 * It's selector in CSS is: '.mfx-button.fab-base'.
 * <p></p>
 * Since FABs are meant to be used with icons, these enforce the usage of {@link MFXFontIcon}s
 * rather than generic nodes.
 *
 * @see MFXFabSkin
 * @see MFXFabBehavior
 */
public class MFXFabBase extends MFXButtonBase<MFXFabBehavior> {
    //================================================================================
    // Properties
    //================================================================================
    private final IconProperty icon = new IconProperty(new MFXFontIcon());

    //================================================================================
    // Constructors
    //================================================================================
    public MFXFabBase() {
        initialize();
    }

    public MFXFabBase(String text) {
        super(text);
        initialize();
    }

    public MFXFabBase(MFXFontIcon icon) {
        setIcon(icon);
        initialize();
    }

    public MFXFabBase(String text, MFXFontIcon icon) {
        super(text);
        setIcon(icon);
        initialize();
    }

    //================================================================================
    // Methods
    //================================================================================
    private void initialize() {
        graphicProperty().bind(icon);

        // This is needed since the default value is 'false'
        // This makes the FAB have the correct sizes at init when "collapsed"
        extend();
    }

    /**
     * This is responsible for triggering the animation that changes the FAB to an Extended one or vice-versa when the
     * {@link #extendedProperty()} changes.
     * <p></p>
     * Implementation details. The default state is standard (non-extended). For this reason this needs to be executed as
     * soon as the component is created so that it has the correct sizes. When called upon initialization the skin will be
     * most certainly null, meaning that the animation will need to be postponed.
     */
    protected void extend() {
        boolean extended = isExtended();
        PseudoClasses.EXTENDED.setOn(this, extended);

        // This is necessary, otherwise padding and other properties may still result outdated!
        applyCss();

        Skin<?> skin = getSkin();
        if (skin == null) {
            // This is needed because if this property is set before the Skin has been
            // created it's not possible for the control to correctly compute its
            // expanded/collapsed size. So, first of all we must wait until the Skin is created,
            // and then we must also make sure that the control is in the right 'layout state'.
            // What I mean is that even if the Skin is created there's no guarantee that the
            // sizes will be correct, remember JavaFX is hot garbage. For this reason we force
            // to apply the CSS and compute the layout, this should ensure the correctness of the
            // measurements.
            When.onChanged(skinProperty())
                .condition((o, n) -> n != null)
                .then((o, n) -> {
                    applyCss();
                    layout();
                    getBehavior().extend(false);
                })
                .oneShot()
                .listen();
            return;
        }
        getBehavior().extend(true);
    }

    //================================================================================
    // Overridden Methods
    //================================================================================

    @Override
    public void onLayoutStrategyChanged() {
        super.onLayoutStrategyChanged();
        if (getSkin() == null) return;
        getBehavior().extend(false);
    }

    @Override
    protected void onInitSizesChanged() {
        // Reset the prefWidth if not extended and init sizes changed
        if (!isExtended()) setPrefWidth(USE_COMPUTED_SIZE);
    }

    @Override
    public Supplier<MFXFabBehavior> defaultBehaviorProvider() {
        return () -> new MFXFabBehavior(this);
    }

    @Override
    public List<String> defaultStyleClasses() {
        return List.of("mfx-button", "fab-base");
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    @Override
    protected MFXSkinBase<?, ?> buildSkin() {
        return new MFXFabSkin(this);
    }

    //================================================================================
    // Styleable Properties
    //================================================================================
    private final StyleableBooleanProperty extended = new StyleableBooleanProperty(
        StyleableProperties.EXTENDED,
        this,
        "extended",
        false
    ) {
        @Override
        protected void invalidated() {
            extend();
        }
    };

    public boolean isExtended() {
        return extended.get();
    }

    /**
     * Specifies whether the FAB also shows its text or not.
     * <p>
     * By default, the change of this property will trigger the animated transition defined in {@link MFXFabBehavior},
     * can be avoided by changing the behavior, overriding the behavior method, or simply by overriding {@link #extend()}
     * <p></p>
     * Also note that {@link MFXFabBehavior#extend(boolean)} is responsible for activating the ":extended" pseudo class on the
     * FAB which may change the component's appearance if specified by the current active theme.
     * <p></p>
     * Can be set in CSS via the property: '-mfx-extended'.
     */
    public StyleableBooleanProperty extendedProperty() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended.set(extended);
    }

    //================================================================================
    // CssMetaData
    //================================================================================
    private static class StyleableProperties {
        private static final StyleablePropertyFactory<MFXFabBase> FACTORY = new StyleablePropertyFactory<>(MFXButtonBase.getClassCssMetaData());
        private static final List<CssMetaData<? extends Styleable, ?>> cssMetaDataList;

        private static final CssMetaData<MFXFabBase, Boolean> EXTENDED =
            FACTORY.createBooleanCssMetaData(
                "-mfx-extended",
                MFXFabBase::extendedProperty,
                false
            );

        static {
            cssMetaDataList = StyleUtils.cssMetaDataList(
                MFXButtonBase.getClassCssMetaData(),
                EXTENDED
            );
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.cssMetaDataList;
    }

    //================================================================================
    // Getters/Setters
    //================================================================================
    public MFXFontIcon getIcon() {
        return iconProperty().get();
    }

    /**
     * Specifies the FAB's icon.
     */
    public IconProperty iconProperty() {
        return icon;
    }

    public void setIcon(MFXFontIcon icon) {
        iconProperty().set(icon);
    }

    /**
     * Delegate of {@link IconProperty#setDescription(String)}.
     */
    public IconProperty setIconDescription(String description) {
        return icon.setDescription(description);
    }

    /**
     * Delegate of {@link IconProperty#setProvider(IconProvider)}.
     *
     * @see MFXFontIcon#setIconsProvider(IconProvider)
     */
    public IconProperty setIconProvider(IconProvider provider) {
        return icon.setProvider(provider);
    }

    /**
     * Delegate of {@link IconProperty#setProvider(Font, Function)}.
     *
     * @see MFXFontIcon#setIconsProvider(Font, Function)
     */
    public IconProperty setIconProvider(Font font, Function<String, Character> converter) {
        return icon.setProvider(font, converter);
    }

    /**
     * Delegate of {@link IconProperty#setProvider(IconProvider, String)}.
     */
    public IconProperty setIconProvider(IconProvider provider, String description) {
        return icon.setProvider(provider, description);
    }
}
