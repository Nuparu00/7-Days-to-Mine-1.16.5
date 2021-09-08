package nuparu.sevendaystomine.item.guide;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.util.book.BookData;
import nuparu.sevendaystomine.util.book.BookDataParser;

public class BookDataManager extends JsonReloadListener {

	private static final Gson GSON = (new GsonBuilder()).create();
	
	public static BookDataManager instance = new BookDataManager();
	
	private HashMap<ResourceLocation, BookData> books;

	public BookDataManager() {
		super(GSON,"book");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn,
			IProfiler profilerIn) {
		System.out.println("BOOK DATA RELOAD");
		HashMap<ResourceLocation, BookData> bookz = new HashMap<>();
		for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
			ResourceLocation key = entry.getKey();
			System.out.println("BOOK DATA " + key.toString());
			bookz.put(key, BookDataParser.INSTANCE.getBookDataFromResource(entry.getValue()));
		}
		books = bookz;
	}
	
	public BookData get(ResourceLocation res) {
		return this.books.get(res);
	}
	
	public HashMap<ResourceLocation, BookData> getBooks(){
		return (HashMap<ResourceLocation, BookData>) books.clone();
	}

}
