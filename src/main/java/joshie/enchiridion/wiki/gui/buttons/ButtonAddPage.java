package joshie.enchiridion.wiki.gui.buttons;

import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.gui.popups.PageEditAddition;

public class ButtonAddPage extends ButtonBase {
    public ButtonAddPage(int id, int x, int y, int width, int height, float scale) {
        super(id, x, y, width, height, "add", 2F);
    }

    @Override
    public void onClicked() {
        ((PageEditAddition) WikiHelper.getInstance(PageEditAddition.class).setVisibility(true)).setToDefault();
    }
}
