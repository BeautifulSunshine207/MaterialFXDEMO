package io.github.palexdev.materialfx.font;

/**
 * Enumerator class for MaterialFX font resources.
 */
public enum FontResources {
    ANGLE_DOWN("mfx-angle-down", '\uE91b'),
    ANGLE_LEFT("mfx-angle-left", '\uE91c'),
    ANGLE_RIGHT("mfx-angle-right", '\uE91d'),
    ANGLE_UP("mfx-angle-up", '\uE91e'),
    CALENDAR_BLACK("mfx-calendar-black", '\uE904'),
    CALENDAR_SEMI_BLACK("mfx-calendar-semi-black", '\uE905'),
    CALENDAR_WHITE("mfx-calendar-white", '\uE906'),
    CASPIAN_MARK("mfx-caspian-mark", '\uE90b'),
    CHEVRON_DOWN("mfx-chevron-down", '\uE902'),
    CHEVRON_LEFT("mfx-chevron-left", '\uE903'),
    CHEVRON_RIGHT("mfx-chevron-right", '\uE907'),
    CHEVRON_UP("mfx-chevron-right", '\uE908'),
    CIRCLE("mfx-circle", '\uE909'),
    EXCLAMATION_CIRCLE("mfx-exclamation-circle", '\uE917'),
    EXCLAMATION_TRIANGLE("mfx-exclamation-triangle", '\uE918'),
    EXPAND("mfx-expand", '\uE919'),
    GOOGLE("mfx-google", '\uE90a'),
    INFO_CIRCLE("mfx-info-circle", '\uE91a'),
    MINUS("mfx-minus", '\uE901'),
    MINUS_CIRCLE("mfx-minus-circle", '\uE90c'),
    MODENA_MARK("mfx-modena-mark", '\uE90d'),
    VARIANT3_MARK("mfx-variant3-mark", '\uE90e'),
    VARIANT4_MARK("mfx-variant4-mark", '\uE90f'),
    VARIANT5_MARK("mfx-variant5-mark", '\uE910'),
    VARIANT6_MARK("mfx-variant6-mark", '\uE911'),
    VARIANT7_MARK("mfx-variant7-mark", '\uE912'),
    VARIANT8_MARK("mfx-variant8-mark", '\uE913'),
    VARIANT9_MARK("mfx-variant9-mark", '\uE914'),
    X("mfx-x", '\uE916'),
    X_CIRCLE("mfx-x-circle", '\uE915'),
    X_CIRCLE_LIGHT("mfx-x-circle-light", '\uE900'),
    ;

    private final String description;
    private final char code;
    FontResources(String description, char code) {
        this.description = description;
        this.code = code;
    }

    public static FontResources findByDescription(String description) {
        for (FontResources font : values()) {
            if (font.getDescription().equals(description)) {
                return font;
            }
        }
        throw new IllegalArgumentException("Icon description '" + description + "' is invalid!");
    }

    public String getDescription() {
        return description;
    }

    public char getCode() {
        return code;
    }

}
