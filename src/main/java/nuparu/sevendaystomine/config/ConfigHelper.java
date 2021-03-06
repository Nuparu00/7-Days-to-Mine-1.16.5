package nuparu.sevendaystomine.config;

import java.io.File;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ConfigHelper {

    private static final ForgeConfigSpec.Builder commonBuilder = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder clientBuilder = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder serverBuilder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec commonConfig;
    public static final ForgeConfigSpec clientConfig;
    public static final ForgeConfigSpec serverConfig;

    static {
        CommonConfig.init(commonBuilder);
        commonConfig = commonBuilder.build();


        ClientConfig.init(clientBuilder);
        clientConfig = clientBuilder.build();


        ServerConfig.init(serverBuilder);
        serverConfig = serverBuilder.build();
    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);

    }
}