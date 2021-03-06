package joshie.enchiridion.library.handlers;

import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.library.LibraryRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.GuiOpenEvent;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class BotaniaBookHandler {
    public static ItemStack alfheim;
    public static ItemStack lexicon;

    public static boolean IS_ALFHEIM = false;

    public BotaniaBookHandler() {
        lexicon = new ItemStack(GameRegistry.findItem("Botania", "lexicon"), 1, 0);
        alfheim = lexicon.copy();
        alfheim.setTagCompound(new NBTTagCompound());
        alfheim.stackTagCompound.setBoolean("knowledge.minecraft", true);
        alfheim.stackTagCompound.setBoolean("knowledge.alfheim", true);
        updateIsAlfheim();
    }

    public static void updateIsAlfheim() {
        //Check for whether we should continue
        ItemStack lexicon = new ItemStack(GameRegistry.findItem("Botania", "lexicon"), 1, 0);
        for (ItemStack book : LibraryRegistry.INSTANCE.getBooks()) {
            if (book.isItemEqual(lexicon)) {
                if (lexicon.hasTagCompound()) {
                    IS_ALFHEIM = (lexicon.stackTagCompound.hasKey("knowledge.alfheim"));
                }

                break;
            }
        }
    }

    /** If the gui is the lexicon gui, it will init itself as my gui instead **/
    @SubscribeEvent
    public void openGui(GuiOpenEvent event) {
        if (event.gui != null) {
            if (event.gui.getClass().toString().equals("vazkii.botania.client.gui.lexicon.GuiLexicon")) {
                event.gui = new BotaniaLexiconGuiOverride();
                if (!IS_ALFHEIM) { // If the player doesn't have the alfheim book, check if they are opening one
                    ItemStack held = ClientHelper.getPlayer().getCurrentEquippedItem();
                    if (held != null && held.hasTagCompound()) {
                        if (held.stackTagCompound.hasKey("knowledge.alfheim")) {
                            //If they are opening an alfheim book, update the book in the library to an alfheim copy
                            LibraryRegistry.INSTANCE.overwrite(alfheim.copy());
                            IS_ALFHEIM = true;
                        }
                    }
                }
            }
        }
    }

    /** Overrides the botania init gui, to set the item to a botania book when opening it initially, to prevent a crash **/
    public static class BotaniaLexiconGuiOverride extends GuiLexicon {
        @Override
        public void initGui() {
            ItemStack previous = ClientHelper.getPlayer().getCurrentEquippedItem();
            if (previous != null) previous = previous.copy();
            ItemStack lexicon = BotaniaBookHandler.IS_ALFHEIM ? BotaniaBookHandler.alfheim.copy() : BotaniaBookHandler.lexicon.copy();
            ClientHelper.getPlayer().setCurrentItemOrArmor(0, lexicon);
            super.initGui();
            ClientHelper.getPlayer().setCurrentItemOrArmor(0, previous);
        }
    }
}
