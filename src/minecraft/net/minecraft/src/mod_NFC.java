package net.minecraft.src;

import java.lang.reflect.Field;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.forge.ForgeHooksClient;
import net.minecraft.src.forge.IHighlightHandler;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.MinecraftForgeClient;
import nfc.*;

public class mod_NFC extends BaseMod {

	
	@Override
	public String Version() {
		return "v0.1";
	}
	
	public mod_NFC() {
		ModLoader.RegisterTileEntity(TileEntityBlock.class, "nfc.ore");
		ModLoader.RegisterTileEntity(TileEntitySlab.class, "nfc.slab");
		ModLoader.RegisterTileEntity(TileEntityBrickOven.class, "nfc.brickoven");
		MinecraftForgeClient.registerHighlightHandler(new IHighlightHandler() {

			@Override
			public boolean onBlockHighlight(RenderGlobal renderglobal, EntityPlayer entityplayer, MovingObjectPosition movingobjectposition, int i, ItemStack itemstack, float f) {
				if(true) return false;
				if(itemstack != null && itemstack.itemID == mod_NFC.slab.blockID && mod_NFC.slab.isStairs(itemstack.getItemDamage())) {
					renderglobal.drawBlockBreaking(entityplayer, movingobjectposition, 0, entityplayer.inventory.getCurrentItem(), f);
			        renderglobal.drawSelectionBox(entityplayer, movingobjectposition, 0, entityplayer.inventory.getCurrentItem(), f);
					if(i == 0 && movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
			        {
			            int j = renderglobal.worldObj.getBlockId(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
			            if(j > 0) {
			            	Block block = Block.blocksList[j];
			            	block.setBlockBoundsBasedOnState(renderglobal.worldObj, movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
			            	if(block.minY < 0.5D && block.maxY > 0.5D)
				            {
					            GL11.glEnable(3042 /*GL_BLEND*/);
					            GL11.glLineWidth(5.0F);
					            GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
					            GL11.glDepthMask(false);
					            float f1 = 0.03F;
				                double d = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)f;
				                double d1 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)f;
				                double d2 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)f;
				                drawOutlinedBoundingBox(AxisAlignedBB.getBoundingBoxFromPool(movingobjectposition.blockX + block.minY, movingobjectposition.blockY + 0.5D, movingobjectposition.blockZ + block.minZ, movingobjectposition.blockX + block.maxX, movingobjectposition.blockY + 0.5D, movingobjectposition.blockZ + block.maxZ).expand(f1, f1, f1).getOffsetBoundingBox(-d, -d1, -d2));
					            GL11.glDepthMask(true);
					            GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
					            GL11.glDisable(3042 /*GL_BLEND*/);
				            }
			            }
			        }
					return true;
				}
				return false;
			}

			private void drawOutlinedBoundingBox(AxisAlignedBB axisalignedbb) {
				Tessellator tessellator = Tessellator.instance;
		        tessellator.startDrawing(3);
		        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		        tessellator.draw();
			}
			
		});
		
		slab_stair = ModLoader.getUniqueBlockModelID(this, true);
		render_ID  = ModLoader.getUniqueBlockModelID(this, true);
		new ItemMagicStick(itemID - 1);

		slab1 = new BlockMultiCustomRender(blockID + 2, Material.rock, STONE_SLAB, STONE_PLATED, BRICK_SLAB);
		slab2 = new BlockMultiCustomRender(blockID + 3, Material.wood, PLANKS_SLAB, PLANKS_STAIRS);
		slab3 = new BlockMultiCustomRender(blockID + 4, Material.rock, COBBLE_SLAB, SANDSTONE_SLAB);
		slab4 = new BlockMultiCustomRender(blockID + 5, Material.rock, SANDSTONE_STAIRS, BRICK_STAIRS);
		slab5 = new BlockMultiCustomRender(blockID + 6, Material.rock, STONE_BRICK_STAIRS, COBBLE_STAIRS);
		glass = new BlockMultiGlass(blockID + 7, Material.glass, GLASS, WINDOW_LARGE, WINDOW_DOUBLE, WINDOW);
		
		//Override recipes for new slabs/stairs and such
		this.overrideAllRecipes(new ItemStack(Block.stairSingle, 1, 0), STONE_SLAB.getItemStack());
		this.overrideAllRecipes(new ItemStack(Block.stairSingle, 1, 1), SANDSTONE_SLAB.getItemStack());
		this.overrideAllRecipes(new ItemStack(Block.stairSingle, 1, 2), PLANKS_SLAB.getItemStack());
		this.overrideAllRecipes(new ItemStack(Block.stairSingle, 1, 3), COBBLE_SLAB.getItemStack());
		this.overrideAllRecipes(new ItemStack(Block.stairCompactCobblestone), COBBLE_STAIRS.getItemStack());
		this.overrideAllRecipes(new ItemStack(Block.stairCompactPlanks), PLANKS_STAIRS.getItemStack());
		
		ModLoader.AddRecipe(BRICK_SLAB.getItemStack(), new Object[]{
				"XXX",
				Character.valueOf('X'), Block.brick
		});
		
		ModLoader.AddRecipe(STONE_BRICK_STAIRS.getItemStack(), new Object[]{
				"X  ",
				"XX ",
				"XXX",
				Character.valueOf('X'), STONE_BRICK.getItemStack()
		});
		
		ModLoader.AddRecipe(BRICK_STAIRS.getItemStack(), new Object[]{
				"X  ",
				"XX ",
				"XXX",
				Character.valueOf('X'), Block.brick
		});
		
		ModLoader.AddRecipe(STONE_PLATED.getItemStack(), new Object[]{
				"XX",
				"XX",
				Character.valueOf('X'), Block.stone
		});
		
		ModLoader.AddShapelessRecipe(new ItemStack(BrickOvenIdle), new Object[] {Block.dirt});
	}
	
	private final Field recipeOutput = Utils.getField(ShapedRecipes.class, "recipeOutput");
	
	public void overrideAllRecipes(ItemStack vanilla, ItemStack newitems) {
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
	
	@Override
	public boolean RenderWorldBlock(RenderBlocks renderblocks, IBlockAccess iblockaccess, int x, int y, int z, Block block, int renderType)
    {
		((BlockMultiTexture) block).renderBlockWorld(renderblocks, iblockaccess.getBlockMetadata(x, y, z), x, y, z);
        return true;
    }
	
	@Override
	public void RenderInvBlock(RenderBlocks renderblocks, Block block, int metadata, int renderType)
    {
		((BlockMultiTexture) block).renderBlockInv(renderblocks, metadata);;
    }
	
	//wood
	//stone
	public static final PropsItemToolMaterial ALUMINUM = new PropsItemToolMaterial("Aluminum", 1, 35, 5.0F, 3, 144);
	public static final PropsItemToolMaterial BISMUTH = new PropsItemToolMaterial("Bismuth", 1, 65, 3.5F, 3, 145);
	public static final PropsItemToolMaterial COPPER = new PropsItemToolMaterial("Copper", 1, 50, 4.0F, 3, 146);
	public static final PropsItemToolMaterial LEAD = new PropsItemToolMaterial("Lead", 1, 115, 2.5F, 3, false, 147);
	public static final PropsItemToolMaterial TIN = new PropsItemToolMaterial("Tin", 1, 40, 4.5F, 3, 148);
	public static final PropsItemToolMaterial ZINC = new PropsItemToolMaterial("Zinc", 1, 80, 3.0F, 3, 149);
	//gold
	public static final PropsItemToolMaterial BORON = new PropsItemToolMaterial("Boron", 2, 50, 10.0F, 4, 150);
	public static final PropsItemToolMaterial BRASS = new PropsItemToolMaterial("Brass", 2, 125, 5.0F, 4, 151);
	public static final PropsItemToolMaterial BRONZE = new PropsItemToolMaterial("Bronze", 2, 125, 5.0F, 4, 152);
	public static final PropsItemToolMaterial NICKEL = new PropsItemToolMaterial("Nickel", 2, 85, 7.0F, 4, 153);
	public static final PropsItemToolMaterial PLATINUM = new PropsItemToolMaterial("Platinum", 2, 215, 3.5F, 4, 154);
	public static final PropsItemToolMaterial SILVER = new PropsItemToolMaterial("Silver", 2, 260, 3.0F, 4, 156);
	public static final PropsItemToolMaterial CHROME = new PropsItemToolMaterial("Chrome", 3, 200, 8.0F, 6, 157);
	public static final PropsItemToolMaterial COBALT = new PropsItemToolMaterial("Cobalt", 3, 700, 4.0F, 6, 158);
	//iron
	public static final PropsItemToolMaterial SILICON = new PropsItemToolMaterial("Silicon", 3, 150, 10.0F, 6, 159);
	public static final PropsItemToolMaterial STEEL = new PropsItemToolMaterial("Steel", 4, 700, 8.0F, 10, 160);
	public static final PropsItemToolMaterial TITANIUM = new PropsItemToolMaterial("Titanium", 4, 350, 14.0F, 10, 161);
	public static final PropsItemToolMaterial TUNGSTEN = new PropsItemToolMaterial("Tungsten", 4, 1100, 6.0F, 10, 162);
	public static final PropsItemToolMaterial RUBY = new PropsItemToolMaterial("Ruby", 4, 1000, 10.0F, 20, 163);
	public static final PropsItemToolMaterial SAPHIRE = new PropsItemToolMaterial("Saphire", 4, 1000, 10.0F, 20, 164);
	public static final PropsItemToolMaterial EMERALD = new PropsItemToolMaterial("Emerald", 4, 1000, 10.0F, 20, 165);
	//diamond
	public static final PropsItemToolMaterial OSMIUM = new PropsItemToolMaterial("Osmium", 5, 5000, 5.0F, 20, 166);
	
	public static final PropsItemFood COOKED_EGG = new PropsItemFood("Cooked Egg", 4, 167);
	public static final PropsItemFood CHEESE = new PropsItemFood("Cheese", 5, 168);

	public static int render_ID;
	public int slab_stair;
	public static BlockSlab slab;
	public static final String resources = "/nfc/resources/";
	public static final int blockID = 150;
	public static final int itemID = 454 - 256;
	static int id = itemID;
	static int ingotID = 0;
	
	public static ItemMulti item = new ItemMulti(itemID, ALUMINUM, BISMUTH, COPPER, LEAD, TIN, ZINC, BORON, BRASS, BRONZE, NICKEL, PLATINUM, SILVER, CHROME, COBALT, SILICON, STEEL, TITANIUM, RUBY, SAPHIRE, EMERALD, OSMIUM, COOKED_EGG, CHEESE);
	
	public static final PropsBlockOre ORE_COPPER = new PropsBlockOre(COPPER, 3.0F, 0);
	public static final PropsBlockOre ORE_TIN = new PropsBlockOre(TIN, 3.0F, 1);
	public static final PropsBlockOre ORE_ZINC = new PropsBlockOre(ZINC, 3.0F, 2);
	public static final PropsBlockOre ORE_ALUMINUM = new PropsBlockOre(ALUMINUM, 3.0F, 3);
	public static final PropsBlockOre ORE_LEAD = new PropsBlockOre(LEAD, 3.0F, 4);
	public static final PropsBlockOre ORE_BISMUTH = new PropsBlockOre(BISMUTH, 3.0F, 5);
	public static final PropsBlockOre ORE_BORON = new PropsBlockOre(BORON, 3.5F, 6);
	public static final PropsBlockOre ORE_SILVER = new PropsBlockOre(SILVER, 3.5F, 7);
	public static final PropsBlockOre ORE_CHROME = new PropsBlockOre(CHROME, 4.0F, 8);
	public static final PropsBlockOre ORE_NICKEL = new PropsBlockOre(NICKEL, 3.5F, 9);
	public static final PropsBlockOre ORE_PLATINUM = new PropsBlockOre(PLATINUM, 3.5F, 10);
	public static final PropsBlockOre ORE_TUNGSTEN = new PropsBlockOre(TUNGSTEN, 6.0F, 11);
	public static final PropsBlockOre ORE_SILICON = new PropsBlockOre(SILICON, 4.0F, 12);
	public static final PropsBlockOre ORE_COBALT = new PropsBlockOre(COBALT, 4.0F, 13);
	public static final PropsBlockOre ORE_MAGNETITE = new PropsBlockOre("Magnetite", 4.0F, 14);
	public static final PropsBlockOre ORE_TITANIUM = new PropsBlockOre(TITANIUM, 6.0F, 15);
	public static final PropsBlockOre ORE_ANTHRACITE = new PropsBlockOre("Anthracite", 4.0F/*, item.anthracite.id*/, 16);
	public static final PropsBlockOre ORE_RUBY = new PropsBlockOre(RUBY, 8.0F, RUBY.item_id, RUBY.item_metadata, 17);
	public static final PropsBlockOre ORE_SAPHIRE = new PropsBlockOre(SAPHIRE, 8.0F, SAPHIRE.item_id, SAPHIRE.item_metadata, 18);
	public static final PropsBlockOre ORE_EMERALD = new PropsBlockOre(EMERALD, 8.0F, EMERALD.item_id, EMERALD.item_metadata, 19);
	public static final PropsBlockOre ORE_URANINITE = new PropsBlockOre("Uraninite", 8.0F, 20);
	public static final PropsBlockOre ORE_OSMIUM = new PropsBlockOre(OSMIUM, 10.0F, 21);
	public static final PropsBlock STONE_BLOCK = new PropsBlock("Stone Block", 1.0F, 10.0F, 22);
	public static final PropsBlock STONE_BLOCK_OFFSET_XY = new PropsBlock("Stone Block Offset:XY", 1.0F, 10.0F, 23);
	public static final PropsBlock STONE_BLOCK_OFFSET_X = new PropsBlock("Stone Block Offset:X", 1.0F, 10.0F, 24);
	public static final PropsBlock STONE_BLOCK_OFFSET_Y = new PropsBlock("Stone Block Offset:Y", 1.0F, 10.0F, 25);
	public static final PropsBlock STONE_BRICK = new PropsBlock("Stone Brick", 1.0F, 10.0F, 26);
	public static final PropsBlock STONE_BRICK_SMALL = new PropsBlock("Small Stone Brick", 1.0F, 10.0F, 27);
	
	private static final BlockMulti stone1 = new BlockMulti(blockID, Material.rock, ORE_COPPER, ORE_TIN, ORE_ZINC, ORE_ALUMINUM, ORE_LEAD, ORE_BISMUTH, ORE_BORON, ORE_SILVER, ORE_CHROME, ORE_NICKEL, ORE_PLATINUM, ORE_TUNGSTEN, ORE_SILICON, ORE_COBALT, ORE_MAGNETITE, ORE_TITANIUM);
	private static final BlockMulti stone2 = new BlockMulti(blockID + 1, Material.rock, ORE_ANTHRACITE, ORE_RUBY, ORE_SAPHIRE, ORE_EMERALD, ORE_OSMIUM, STONE_BLOCK, STONE_BLOCK_OFFSET_XY,  STONE_BLOCK_OFFSET_X, STONE_BLOCK_OFFSET_Y, STONE_BRICK, STONE_BRICK_SMALL);
	
	public static final PropsBlockDummyCustom STONE_PLATED = new PropsBlockDummyCustom(new PropsBlockTexture("Plated Stone", "/terrain.png", 1.0F, 10.0F, 6));
	public static final PropsBlockSlab STONE_SLAB = new PropsBlockSlab("Stone", Block.stairDouble);
	public static final PropsBlockStairs COBBLE_STAIRS = new PropsBlockStairs("Stone", Block.cobblestone);
	public static final PropsBlockSlab PLANKS_SLAB = new PropsBlockSlab("Wood", Block.planks);
	public static final PropsBlockStairs PLANKS_STAIRS = new PropsBlockStairs("Wood", Block.planks);
	public static final PropsBlockSlab COBBLE_SLAB = new PropsBlockSlab("Cobble", Block.cobblestone);
	public static final PropsBlockSlab SANDSTONE_SLAB = new PropsBlockSlab("Sandstone", Block.sandStone);
	public static final PropsBlockStairs SANDSTONE_STAIRS = new PropsBlockStairs("Sandstone", Block.sandStone);
	public static final PropsBlockStairs BRICK_STAIRS = new PropsBlockStairs("Brick", Block.brick);
	public static final PropsBlockStairs STONE_BRICK_STAIRS = new PropsBlockStairs("Stone", Block.blocksList[STONE_BRICK.block_id], STONE_BRICK.block_metadata);
	public static final PropsBlockSlab BRICK_SLAB = new PropsBlockSlab("Brick", Block.brick);
	public static final PropsBlockTexture GLASS = new PropsBlockTexture("Glass", Block.glass);
	public static final PropsBlock WINDOW_LARGE = new PropsBlock("Large Window", 0.3F, 1.5F, 28);
	public static final PropsBlock WINDOW_DOUBLE = new PropsBlock("Double Window", 0.3F, 1.5F, 29);
	public static final PropsBlock WINDOW = new PropsBlock("Window", 0.3F, 1.5F, 30);
	
	private final BlockMultiCustomRender slab1;
	private final BlockMultiCustomRender slab2;
	private final BlockMultiCustomRender slab3;
	private final BlockMultiCustomRender slab4;
	private final BlockMultiCustomRender slab5;
	private final BlockMultiTexture glass;
	
	public static final Block BrickOvenIdle = (new BlockBrickOven(230, false, 32)).setHardness(5F).setStepSound(Block.soundStoneFootstep).setBlockName("Brick Oven").disableNeighborNotifyOnMetadataChange();
	public static final Block BrickOvenActive = (new BlockBrickOven(231, true, 34)).setHardness(5F).setStepSound(Block.soundStoneFootstep).setBlockName("Brick Oven").setLightValue(0.875F).disableNeighborNotifyOnMetadataChange();
	
	public static class PropsItemFood extends PropsItem {
		
		public final int HEAL_AMOUNT;

		public PropsItemFood(String name, int healAmount, int textureIndex) {
			super(name, textureIndex);
			this.HEAL_AMOUNT = healAmount;
		}
		
		public void onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
	    {
	        itemstack.stackSize--;
	        entityplayer.heal(HEAL_AMOUNT);
	    }
	}
	
	public static class PropsBlockOre extends PropsBlock {
		private final int TIER;
		
		public PropsBlockOre(PropsItemToolMaterial material, float hardness, int textureIndex) {
			this(material.NAME, material.TIER - 1, hardness, 0, 0, textureIndex);
		}

		public PropsBlockOre(PropsItemToolMaterial material, float hardness, int idDropped, int damageDropped, int textureIndex) {
			this(material.NAME, material.TIER - 1, hardness, idDropped, damageDropped, textureIndex);
		}
		
		public PropsBlockOre(String name, float hardness, int textureIndex) {
			this(name, 0, hardness, 0, 0, textureIndex);
		}
		
		public PropsBlockOre(String name, int tier, float hardness, int idDropped, int damageDropped, int textureIndex) {
			super(name, hardness, 500.0F, idDropped, damageDropped, textureIndex);
			this.TIER = tier;
			this.addLocalisation(new StringBuilder().append(name).append(' ').append("Ore").toString());
		}
		
		public void setHarvestLevel() {
			MinecraftForge.setBlockHarvestLevel(Block.blocksList[this.block_id], this.block_metadata, "pickaxe", this.TIER);
		}
		
		@Override
		public String getNamePrefix() {
			return new StringBuilder().append(super.getNamePrefix()).append("ore.").toString();
		}
	}
}