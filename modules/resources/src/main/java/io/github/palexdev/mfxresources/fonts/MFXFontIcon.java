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

package io.github.palexdev.mfxresources.fonts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import io.github.palexdev.mfxresources.builders.IconWrapperBuilder;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * This class is used to display font icons given three main requirements:
 * <p> - The icon font to use
 * <p> - The function which will convert icon names to Unicode characters
 * <p> - The icon description/name
 * <p></p>
 * The new API allows {@code MFXFontIcon} to work with any icon font resource as long as the above requirements are met.
 * <p>
 * Users can switch between icon packs at any time with the provided method {@link #setIconsProvider(IconProvider)}.
 * To use a third party icon pack, first register the provider in {@link IconsProviders} either with
 * {@link IconsProviders#registerProvider(String, IconProvider)} or {@link IconsProviders#registerProvider(String, Font, Function)}
 * <p>
 * It is also possible to convert an icon description to its Unicode character and vice versa with {@link #descToCode(String)},
 * {@link #symbolToCode()}.
 * <p>
 * Now integrates with {@link MFXIconWrapper} in many ways with fluent API.
 */
public class MFXFontIcon extends Text implements Cloneable {
	//================================================================================
	// Properties
	//================================================================================
	private final String STYLE_CLASS = "mfx-font-icon";
	private final ObjectProperty<Function<String, Character>> descriptionConverter = new SimpleObjectProperty<>();
	private boolean changingFont = false;

	//================================================================================
	// Constructors
	//================================================================================
	public MFXFontIcon() {
		this((String) null);
	}

	public MFXFontIcon(IconDescriptor icon) {
		this(icon.getDescription());
	}

	public MFXFontIcon(IconDescriptor icon, Color color) {
		this(icon.getDescription(), color);
	}

	public MFXFontIcon(IconDescriptor icon, double size) {
		this(icon.getDescription(), size);
	}

	public MFXFontIcon(IconDescriptor icon, double size, Color color) {
		this(icon.getDescription(), size, color);
	}

	public MFXFontIcon(String description) {
		this(description, 16.0);
	}

	public MFXFontIcon(String description, Color color) {
		this(description, 16.0, color);
	}

	public MFXFontIcon(String description, double size) {
		this(description, size, Color.web("#454545"));
	}

	public MFXFontIcon(String description, double size, Color color) {
		initialize();
		setFont(Font.font(getFont().getFamily(), size));
		setSize(size);
		setDescription(description);
		setColor(color);
	}

	//================================================================================
	// Methods
	//================================================================================
	private void initialize() {
		getStyleClass().add(STYLE_CLASS);
		fontProperty().addListener(i -> update(getDescription()));
		fillProperty().bind(colorProperty());
	}

	/**
	 * This is responsible for updating the icon's state. It's called whenever the description or font properties change.
	 * <p>
	 * With the latest version, {@code MFXFontIcon} is capable of automatically determine the icons' provider according
	 * to the set description. The provider must have been registered beforehand, as it is determined by
	 * {@link IconsProviders#getProvider(String)}. In case no provider is found, an {@link IllegalArgumentException} is
	 * thrown.
	 * <p>
	 * If the description is valid (not null, not blank, provider found), the text is updated by converting the description
	 * to a Unicode character, see {@link #descToCode(String)}.
	 */
	protected void update(String description) {
		if (changingFont) return;
		if (description == null || description.isBlank()) {
			setText("");
			return;
		}

		IconProvider provider = IconsProviders.getProvider(description);
		if (provider == null) {
			setText("");
			throw new IllegalArgumentException("No icons provider for: " + description);
		}
		if (!Objects.equals(getDescriptionConverter(), provider.getConverter())) {
			changingFont = true;
			setIconsProvider(provider);
			changingFont = false;
		}
		setText(descToCode(description));
	}

	/**
	 * Switches icons pack to the given {@link IconProvider}.
	 * <p></p>
	 * Note that this will also clear the {@link #descriptionProperty()} to avoid any exception or invalid state during
	 * the transition.
	 */
	public MFXFontIcon setIconsProvider(IconProvider provider) {
		setDescriptionConverter(provider.getConverter());
		Font font = provider.loadFont();
		setFont(font);
		setFontSize(getSize());
		return this;
	}

	/**
	 * Converts the given icon description/name to a unicode character by using the current {@link #descriptionConverterProperty()}.
	 *
	 * @return the unicode character for the given description as a String
	 */
	public String descToCode(String desc) {
		return String.valueOf(getDescriptionConverter().apply(desc));
	}

	/**
	 * Converts back the current set icon from {@link #getText()} to a unicode character.
	 *
	 * @return the current unicode character as a String. "\0" (empty character) if the text is empty (no icon set)
	 */
	public String symbolToCode() {
		String text = getText();
		if (text.isEmpty()) return "\0";
		return ("\\u" + Integer.toHexString(getText().charAt(0) | 0x10000).substring(1).toUpperCase());
	}

	/**
	 * Wraps this font icon in a {@link MFXIconWrapper} and returns it.
	 */
	public MFXIconWrapper wrap() {
		return new MFXIconWrapper(this);
	}

	/**
	 * Wraps this font icon in a {@link MFXIconWrapper} and returns an instance of {@link IconWrapperBuilder} to customize
	 * the wrapper.
	 */
	public IconWrapperBuilder wrapperBuilder() {
		return new IconWrapperBuilder(wrap());
	}

	/**
	 * Responsible for changing the current font' size.
	 */
	private void setFontSize(double size) {
		String family = getFont().getFamily();
		setFont(Font.font(family, size));
	}

	//================================================================================
	// Styleable Properties
	//================================================================================
	private final StyleableObjectProperty<Color> color = new SimpleStyleableObjectProperty<>(
			StyleableProperties.COLOR,
			this,
			"color",
			Color.web("#454545")
	) {
		@Override
		public StyleOrigin getStyleOrigin() {
			return StyleOrigin.USER_AGENT;
		}
	};

	private final StyleableStringProperty description = new SimpleStyleableStringProperty(
			StyleableProperties.DESCRIPTION,
		this,
		"description"
	) {
		@Override
		protected void invalidated() {
			update(get());
		}

		@Override
		public StyleOrigin getStyleOrigin() {
			return StyleOrigin.USER_AGENT;
		}
	};

	private final StyleableDoubleProperty size = new SimpleStyleableDoubleProperty(
			StyleableProperties.SIZE,
			this,
			"size",
			16.0
	) {
		@Override
		protected void invalidated() {
			double size = get();
			setFontSize(size);
		}

		@Override
		public StyleOrigin getStyleOrigin() {
			return StyleOrigin.USER_AGENT;
		}
	};

	public Color getColor() {
		return color.get();
	}

	/**
	 * Specifies the color of the icon.
	 * <p></p>
	 * Settable in CSS via the property: '-mfx-color'.
	 */
	public StyleableObjectProperty<Color> colorProperty() {
		return color;
	}

	public MFXFontIcon setColor(Color color) {
		this.color.set(color);
		return this;
	}

	public String getDescription() {
		return description.get();
	}

	/**
	 * Specifies the icon's description/name inside the icon font pack.
	 * <p></p>
	 * Settable in CSS via the property: '-mfx-description'.
	 */
	public StyleableStringProperty descriptionProperty() {
		return description;
	}

	public MFXFontIcon setDescription(String code) {
		this.description.set(code);
		return this;
	}

	public MFXFontIcon setDescription(IconDescriptor description) {
		this.description.set(description.getDescription());
		return this;
	}

	public double getSize() {
		return size.get();
	}

	/**
	 * Specifies the size of the icon, in other words the size of the current icon font pack.
	 * <p>
	 * On change this will automatically call {@link #setFontSize(double)}.
	 * <p></p>
	 * Settable in CSS via the property: '-mfx-size'.
	 */
	public StyleableDoubleProperty sizeProperty() {
		return size;
	}

	public MFXFontIcon setSize(double size) {
		this.size.set(size);
		return this;
	}

	//================================================================================
	// CssMetaData
	//================================================================================
	private static class StyleableProperties {
		private static final StyleablePropertyFactory<MFXFontIcon> FACTORY = new StyleablePropertyFactory<>(Text.getClassCssMetaData());
		private static final List<CssMetaData<? extends Styleable, ?>> cssMetaDataList;

		private static final CssMetaData<MFXFontIcon, Color> COLOR =
				FACTORY.createColorCssMetaData(
						"-mfx-color",
						MFXFontIcon::colorProperty,
						Color.web("#454545")
				);

		private static final CssMetaData<MFXFontIcon, String> DESCRIPTION =
				FACTORY.createStringCssMetaData(
						"-mfx-description",
						MFXFontIcon::descriptionProperty
				);

		private static final CssMetaData<MFXFontIcon, Number> SIZE =
				FACTORY.createSizeCssMetaData(
						"-mfx-size",
						MFXFontIcon::sizeProperty,
						16.0
				);

		static {
			List<CssMetaData<? extends Styleable, ?>> txtMetadata = new ArrayList<>(Text.getClassCssMetaData());
			Collections.addAll(txtMetadata, COLOR, DESCRIPTION, SIZE);
			cssMetaDataList = List.copyOf(txtMetadata);
		}
	}

	public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
		return StyleableProperties.cssMetaDataList;
	}

	//================================================================================
	// Overridden Methods
	//================================================================================
	@Override
	public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
		return getClassCssMetaData();
	}

	/**
	 * Creates a new {@code MFXFontIcon} instance with the same properties from this.
	 */
	@SuppressWarnings({"MethodDoesntCallSuperMethod", "CloneDoesntDeclareCloneNotSupportedException"})
	@Override
	protected MFXFontIcon clone() {
		MFXFontIcon clone = new MFXFontIcon();
		clone.setDescriptionConverter(getDescriptionConverter());
		clone.setDescription(getDescription());
		clone.setSize(getSize());
		clone.setColor(getColor());
		return clone;
	}

	@Override
	public String toString() {
		return "MFXFontIcon{" +
				"description=" + getDescription() +
				", code=" + symbolToCode() +
				", color=" + getColor() +
				", size=" + getSize() +
				'}';
	}

	//================================================================================
	// Getters/Setters
	//================================================================================
	public Function<String, Character> getDescriptionConverter() {
		return descriptionConverter.get();
	}

	/**
	 * Specifies the function used by {@code MFXFontIcon} to convert the {@link #descriptionProperty()} to a unicode
	 * character representing the icon in the font resource.
	 */
	public ObjectProperty<Function<String, Character>> descriptionConverterProperty() {
		return descriptionConverter;
	}

	public void setDescriptionConverter(Function<String, Character> descriptionConverter) {
		this.descriptionConverter.set(descriptionConverter);
	}
}
