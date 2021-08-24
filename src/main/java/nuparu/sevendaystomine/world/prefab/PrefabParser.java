package nuparu.sevendaystomine.world.prefab;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.prefab.json.JsonPrefab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

public class PrefabParser {

    public static final PrefabParser INSTANCE = new PrefabParser();
    public Prefab getPrefabFromFile(String fileName) throws FileNotFoundException {
        File file = new File("./resources/prefabs/" + fileName + ".prfb");
        file.getParentFile().mkdirs();
        String json;
        try {
            json = Utils.readFile(file.getAbsolutePath(), Charset.defaultCharset());
            Gson gson = new GsonBuilder().create();
            JsonPrefab jPrefab = gson.fromJson(json, JsonPrefab.class);
            return jPrefab.toPrefab();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
