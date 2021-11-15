package nuparu.sevendaystomine.entity.human.dialogue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.util.dialogue.DialogueParser;
import nuparu.sevendaystomine.util.dialogue.Dialogues;

import java.util.HashMap;
import java.util.Map;

public class DialogueDataManager extends JsonReloadListener {

    private static final Gson GSON = (new GsonBuilder()).create();

    public static DialogueDataManager instance = new DialogueDataManager();

    private HashMap<ResourceLocation, Dialogues> dialogues;

    public DialogueDataManager() {
        super(GSON,"dialogues");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn,
                         IProfiler profilerIn) {
        HashMap<ResourceLocation, Dialogues> dialoguez = new HashMap<>();
        System.out.println("dialoguez");
        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation key = entry.getKey();
            System.out.println("dial " + key.toString());
            Dialogues dialog = DialogueParser.INSTANCE.getDialoguesFromResource(entry.getValue(),key);
            dialog.setKey(key);
            dialoguez.put(key, dialog);
        }
        dialogues = dialoguez;
    }

    public Dialogues get(ResourceLocation res) {
        return this.dialogues.get(res);
    }

    public HashMap<ResourceLocation, Dialogues> getBooks(){
        return (HashMap<ResourceLocation, Dialogues>) dialogues.clone();
    }



}
