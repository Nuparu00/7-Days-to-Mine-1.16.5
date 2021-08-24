package nuparu.sevendaystomine.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.gui.NotificationModUpdateScreen;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.client.ClientModLoader;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.versions.forge.ForgeVersion;

public class ModdedNotificationModUpdateScreen extends NotificationModUpdateScreen{
	
    private static final ResourceLocation VERSION_CHECK_ICONS = new ResourceLocation(ForgeVersion.MOD_ID, "textures/gui/version_check_icons.png");

    private VersionChecker.Status showNotification = null;
    private boolean hasCheckedForUpdates = false;
    private final Button modButton;
    
	public ModdedNotificationModUpdateScreen(Button modButton) {
		super(modButton);
		this.modButton = modButton;
	}
	
    @Override
    public void init()
    {
        if (!hasCheckedForUpdates)
        {
            if (modButton != null)
            {
                showNotification = ClientModLoader.checkForUpdates();
            }
            hasCheckedForUpdates = true;
        }
    }
	
    @SuppressWarnings("deprecation")
    @Override
    public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks)
    {
        if (showNotification == null || !showNotification.shouldDraw() || !FMLConfig.runVersionCheck())
        {
            return;
        }

        Minecraft.getInstance().getTextureManager().bind(VERSION_CHECK_ICONS);
        RenderSystem.color4f(1, 1, 1, 1);

        int x = modButton.x;
        int y = modButton.y;
        int w = modButton.getWidth();
        int h = modButton.getHeight();

        blit(mStack, x + w - (h / 2-6), y + (h / 2 - 4), showNotification.getSheetOffset() * 8, (showNotification.isAnimated() && ((System.currentTimeMillis() / 800 & 1) == 1)) ? 8 : 0, 8, 8, 64, 16);
    }

}
