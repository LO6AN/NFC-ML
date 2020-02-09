package nfc;

import java.lang.reflect.*;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class Utils {

	public static final Minecraft mc = ModLoader.getMinecraftInstance();
	private static final Field modifiersField = getField(Field.class, "modifiers");
	private static final Field recipeOutput = Utils.getField(ShapedRecipes.class, "recipeOutput");

	// Used for easy reflection with obfuscated or regular fields
	public static final Field getField(Class<?> target, String... names) {
		for (Field field : target.getDeclaredFields()) {
			for (String name : names) {
				if (field.getName() == name) {
					field.setAccessible(true);
					return field;
				}
			}
		}
		return null;
	}
	
	public static void replaceBlock(Block newBlock, String ...fields) {
        try {
    		Field blockField = getField(Block.class, fields);
			modifiersField.setInt(blockField, blockField.getModifiers() & ~Modifier.FINAL);
			blockField.set(null, newBlock);
			Block.blocksList[newBlock.blockID] = newBlock;
        } 
        catch (Exception e) { e.printStackTrace(); }
	}
	
	public static int clearBlockID(Block block) {
		return clearBlockID(block.blockID);
	}
	
	public static int clearBlockID(int id) {
		Block.blocksList[id] = null;
		return id;
	}
	
	public static void overrideShapedRecipes(ItemStack vanilla, ItemStack newitems) {
		@SuppressWarnings("unchecked")
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for(IRecipe recipe : recipes) {
			if(recipe instanceof ShapedRecipes) {
				if(recipe.getRecipeOutput().isItemEqual(vanilla)) {
					try {
						recipeOutput.set(recipe, newitems);
					} 
					catch (Exception e) { e.printStackTrace(); }
				}
			}
		}
	}
}
