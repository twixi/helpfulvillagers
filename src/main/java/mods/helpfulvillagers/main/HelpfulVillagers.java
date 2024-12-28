/*     */ package mods.helpfulvillagers.main;
/*     */ 
/*     */ import net.minecraftforge.fml.common.Mod;
/*     */ import net.minecraftforge.fml.common.Mod.EventHandler;
/*     */ import net.minecraftforge.fml.common.Mod.Instance;
/*     */ import net.minecraftforge.fml.common.SidedProxy;
/*     */ import net.minecraftforge.fml.common.event.FMLInitializationEvent;
/*     */ import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
/*     */ import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
/*     */ import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
/*     */ import net.minecraftforge.fml.common.network.NetworkRegistry;
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
/*     */ import net.minecraftforge.fml.common.registry.EntityRegistry;
/*     */ import net.minecraftforge.fml.common.registry.GameRegistry;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import mods.helpfulvillagers.block.BlockActiveConstructionFence;
/*     */ import mods.helpfulvillagers.block.BlockConstructionFence;
/*     */ import mods.helpfulvillagers.command.VillagerMessagesCommand;
/*     */ import mods.helpfulvillagers.crafting.VillagerRecipe;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.entity.EntityArcher;
/*     */ import mods.helpfulvillagers.entity.EntityBuilder;
/*     */ import mods.helpfulvillagers.entity.EntityFarmer;
/*     */ import mods.helpfulvillagers.entity.EntityFisherman;
/*     */ import mods.helpfulvillagers.entity.EntityLumberjack;
/*     */ import mods.helpfulvillagers.entity.EntityMerchant;
/*     */ import mods.helpfulvillagers.entity.EntityMiner;
/*     */ import mods.helpfulvillagers.entity.EntityRancher;
/*     */ import mods.helpfulvillagers.entity.EntityRegularVillager;
/*     */ import mods.helpfulvillagers.entity.EntitySoldier;
/*     */ import mods.helpfulvillagers.network.AddRecipePacket;
/*     */ import mods.helpfulvillagers.network.ConstructionJobPacket;
/*     */ import mods.helpfulvillagers.network.CraftItemClientPacket;
/*     */ import mods.helpfulvillagers.network.CraftItemServerPacket;
/*     */ import mods.helpfulvillagers.network.CraftQueueClientPacket;
/*     */ import mods.helpfulvillagers.network.CraftQueueServerPacket;
/*     */ import mods.helpfulvillagers.network.CustomRecipesPacket;
/*     */ import mods.helpfulvillagers.network.FishHookPacket;
/*     */ import mods.helpfulvillagers.network.GUICommandPacket;
/*     */ import mods.helpfulvillagers.network.InventoryPacket;
/*     */ import mods.helpfulvillagers.network.ItemFrameEventPacket;
/*     */ import mods.helpfulvillagers.network.ItemPriceClientPacket;
/*     */ import mods.helpfulvillagers.network.ItemPriceServerPacket;
/*     */ import mods.helpfulvillagers.network.LeaderPacket;
/*     */ import mods.helpfulvillagers.network.MessageOptionsPacket;
/*     */ import mods.helpfulvillagers.network.NicknamePacket;
/*     */ import mods.helpfulvillagers.network.PlayerAccountClientPacket;
/*     */ import mods.helpfulvillagers.network.PlayerAccountServerPacket;
/*     */ import mods.helpfulvillagers.network.PlayerCraftMatrixResetPacket;
/*     */ import mods.helpfulvillagers.network.PlayerInventoryPacket;
/*     */ import mods.helpfulvillagers.network.PlayerItemStackPacket;
/*     */ import mods.helpfulvillagers.network.PlayerMessagePacket;
/*     */ import mods.helpfulvillagers.network.ProfessionChangePacket;
/*     */ import mods.helpfulvillagers.network.ResetRecipesPacket;
/*     */ import mods.helpfulvillagers.network.SaplingPacket;
/*     */ import mods.helpfulvillagers.network.SwingPacket;
/*     */ import mods.helpfulvillagers.network.UnlockedHallsPacket;
/*     */ import mods.helpfulvillagers.network.VillageSyncPacket;
/*     */ import mods.helpfulvillagers.tileentity.TileEntityContructionFence;
/*     */ import mods.helpfulvillagers.village.HelpfulVillage;
/*     */ import mods.helpfulvillagers.village.HelpfulVillageCollection;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.command.ICommand;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.projectile.EntityFishHook;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.crafting.CraftingManager;
/*     */ import net.minecraft.item.crafting.FurnaceRecipes;
/*     */ import net.minecraft.item.crafting.IRecipe;
/*     */ import net.minecraftforge.common.config.Configuration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Mod(modid = "helpfulvillagers", name = "Helpful Villagers", version = "1.4.0b5")
/*     */ public class HelpfulVillagers
/*     */ {
/*     */   public static final String MODID = "helpfulvillagers";
/*     */   public static final String MODNAME = "Helpful Villagers";
/*     */   public static final String VERSION = "1.4.0b5";
/*     */   public static SimpleNetworkWrapper network;
/*     */   public static HelpfulVillageCollection villageCollection;
/* 119 */   public static ArrayList<HelpfulVillage> villages = new ArrayList<HelpfulVillage>();
/* 120 */   public static ArrayList<EntityItemFrame> checkedFrames = new ArrayList<EntityItemFrame>();
/* 121 */   public static HashMap<EntityPlayer, AbstractVillager> player_guard = new HashMap<EntityPlayer, AbstractVillager>();
/* 122 */   public static HashMap<Integer, AbstractVillager> villager_id = new HashMap<Integer, AbstractVillager>();
/*     */   
/*     */   public static Configuration config;
/* 125 */   public static int deathMessageOption = 1;
/* 126 */   public static int birthMessageOption = 1;
/* 127 */   public static int constructionMessageOption = 1;
/*     */   
/*     */   public static boolean infiniteArrows = false;
/*     */   
/* 131 */   public static ArrayList<VillagerRecipe> allCrafting = new ArrayList<VillagerRecipe>();
/* 132 */   public static ArrayList<VillagerRecipe> allSmelting = new ArrayList<VillagerRecipe>();
/* 133 */   public static ArrayList<VillagerRecipe> lumberjackRecipes = new ArrayList<VillagerRecipe>();
/* 134 */   public static ArrayList<VillagerRecipe> farmerRecipes = new ArrayList<VillagerRecipe>();
/* 135 */   public static ArrayList<VillagerRecipe> minerRecipes = new ArrayList<VillagerRecipe>();
/* 136 */   public static ArrayList<VillagerRecipe> fishermanRecipes = new ArrayList<VillagerRecipe>();
/* 137 */   public static ArrayList<VillagerRecipe> rancherRecipes = new ArrayList<VillagerRecipe>();
/*     */   
/* 139 */   public static List<IRecipe> vanillaRecipes = new ArrayList<IRecipe>();
/*     */ 
/*     */ 
/*     */   
/*     */   @Instance("HelpfulVillagers")
/*     */   public static HelpfulVillagers instance;
/*     */ 
/*     */ 
/*     */   
/*     */   @SidedProxy(clientSide = "mods.helpfulvillagers.main.ClientProxy", serverSide = "mods.helpfulvillagers.main.CommonProxy")
/*     */   public static CommonProxy proxy;
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void preInit(FMLPreInitializationEvent event) {
/* 155 */     config = new Configuration(event.getSuggestedConfigurationFile());
/* 156 */     config.load();
/* 157 */     deathMessageOption = config.getInt("deathMessage", "general", 1, 0, 2, "0 - Off, 1 - On, 2 - Verbose");
/* 158 */     birthMessageOption = config.getInt("birthMessage", "general", 1, 0, 2, "0 - Off, 1 - On, 2 - Verbose");
/* 159 */     constructionMessageOption = config.getInt("constructionMessage", "general", 1, 0, 2, "0 - Off, 1 - On, 2 - Verbose");
/* 160 */     infiniteArrows = config.getBoolean("infiniteArrows", "archer", false, "Set to true to allow Archers to shoot without using arrows");
/* 161 */     config.save();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void init(FMLInitializationEvent event) {
/* 172 */     vanillaRecipes.addAll(CraftingManager.getInstance().getRecipeList());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 177 */     proxy.registerRenderers();
/*     */ 
/*     */     
/* 180 */     proxy.Init();
/*     */ 
/*     */     
/* 183 */     instance = this;
/* 184 */     NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
/*     */ 
/*     */     
/* 187 */     network = NetworkRegistry.INSTANCE.newSimpleChannel("HV");
/* 188 */     network.registerMessage(SaplingPacket.Handler.class, SaplingPacket.class, 0, Side.CLIENT);
/* 189 */     network.registerMessage(SwingPacket.Handler.class, SwingPacket.class, 1, Side.CLIENT);
/* 190 */     network.registerMessage(ProfessionChangePacket.Handler.class, ProfessionChangePacket.class, 2, Side.SERVER);
/* 191 */     network.registerMessage(LeaderPacket.Handler.class, LeaderPacket.class, 3, Side.CLIENT);
/* 192 */     network.registerMessage(GUICommandPacket.Handler.class, GUICommandPacket.class, 4, Side.SERVER);
/* 193 */     network.registerMessage(UnlockedHallsPacket.Handler.class, UnlockedHallsPacket.class, 5, Side.CLIENT);
/* 194 */     network.registerMessage(InventoryPacket.Handler.class, InventoryPacket.class, 6, Side.CLIENT);
/* 195 */     network.registerMessage(NicknamePacket.Handler.class, NicknamePacket.class, 7, Side.SERVER);
/* 196 */     network.registerMessage(PlayerMessagePacket.Handler.class, PlayerMessagePacket.class, 8, Side.CLIENT);
/* 197 */     network.registerMessage(MessageOptionsPacket.Handler.class, MessageOptionsPacket.class, 9, Side.CLIENT);
/* 198 */     network.registerMessage(ItemFrameEventPacket.Handler.class, ItemFrameEventPacket.class, 10, Side.SERVER);
/* 199 */     network.registerMessage(CraftItemServerPacket.Handler.class, CraftItemServerPacket.class, 11, Side.SERVER);
/* 200 */     network.registerMessage(CraftItemClientPacket.Handler.class, CraftItemClientPacket.class, 12, Side.CLIENT);
/* 201 */     network.registerMessage(CraftQueueServerPacket.Handler.class, CraftQueueServerPacket.class, 13, Side.SERVER);
/* 202 */     network.registerMessage(CraftQueueClientPacket.Handler.class, CraftQueueClientPacket.class, 14, Side.CLIENT);
/* 203 */     network.registerMessage(CustomRecipesPacket.Handler.class, CustomRecipesPacket.class, 15, Side.CLIENT);
/* 204 */     network.registerMessage(AddRecipePacket.Handler.class, AddRecipePacket.class, 16, Side.SERVER);
/* 205 */     network.registerMessage(ResetRecipesPacket.Handler.class, ResetRecipesPacket.class, 17, Side.SERVER);
/* 206 */     network.registerMessage(ItemPriceClientPacket.Handler.class, ItemPriceClientPacket.class, 18, Side.CLIENT);
/* 207 */     network.registerMessage(ItemPriceServerPacket.Handler.class, ItemPriceServerPacket.class, 19, Side.SERVER);
/* 208 */     network.registerMessage(VillageSyncPacket.Handler.class, VillageSyncPacket.class, 20, Side.CLIENT);
/* 209 */     network.registerMessage(PlayerInventoryPacket.Handler.class, PlayerInventoryPacket.class, 21, Side.SERVER);
/* 210 */     network.registerMessage(PlayerItemStackPacket.Handler.class, PlayerItemStackPacket.class, 22, Side.SERVER);
/* 211 */     network.registerMessage(PlayerCraftMatrixResetPacket.Handler.class, PlayerCraftMatrixResetPacket.class, 23, Side.SERVER);
/* 212 */     network.registerMessage(PlayerAccountClientPacket.Handler.class, PlayerAccountClientPacket.class, 24, Side.CLIENT);
/* 213 */     network.registerMessage(PlayerAccountServerPacket.Handler.class, PlayerAccountServerPacket.class, 25, Side.SERVER);
/* 214 */     network.registerMessage(FishHookPacket.Handler.class, FishHookPacket.class, 26, Side.CLIENT);
/* 215 */     network.registerMessage(ConstructionJobPacket.Handler.class, ConstructionJobPacket.class, 27, Side.SERVER);
/*     */ 
/*     */     
/* 218 */     EntityRegistry.registerModEntity(EntityRegularVillager.class, "Villager", 0, this, 50, 2, true);
/* 219 */     EntityRegistry.registerModEntity(EntityLumberjack.class, "Lumberjack", 1, this, 50, 2, true);
/* 220 */     EntityRegistry.registerModEntity(EntityMiner.class, "Miner", 2, this, 50, 2, true);
/* 221 */     EntityRegistry.registerModEntity(EntityFarmer.class, "Farmer", 3, this, 50, 2, true);
/* 222 */     EntityRegistry.registerModEntity(EntitySoldier.class, "Soldier", 4, this, 50, 2, true);
/* 223 */     EntityRegistry.registerModEntity(EntityArcher.class, "Archer", 5, this, 50, 2, true);
/* 224 */     EntityRegistry.registerModEntity(EntityMerchant.class, "Merchant", 6, this, 50, 2, true);
/* 225 */     EntityRegistry.registerModEntity(EntityFisherman.class, "Fisherman", 7, this, 50, 2, true);
/* 226 */     EntityRegistry.registerModEntity(EntityRancher.class, "Rancher", 8, this, 50, 2, true);
/* 227 */     EntityRegistry.registerModEntity(EntityBuilder.class, "Builder", 9, this, 50, 2, true);
/*     */ 
/*     */     
/* 230 */     EntityRegistry.registerModEntity(EntityFishHook.class, "Fish Hook", 100, this, 50, 2, true);
/*     */ 
/*     */     
/* 233 */     GameRegistry.registerBlock((Block)new BlockConstructionFence("construction_fence", Material.wood), "construction_fence");
/* 234 */     GameRegistry.registerBlock((Block)new BlockActiveConstructionFence("active_construction_fence", Material.wood), "active_construction_fence");
/*     */ 
/*     */     
/* 237 */     GameRegistry.registerTileEntity(TileEntityContructionFence.class, "construction_fence");
/*     */ 
/*     */     
/* 240 */     GameRegistry.addShapelessRecipe(new ItemStack(GameRegistry.findBlock("helpfulvillagers", "construction_fence")), new Object[] { new ItemStack(Blocks.oak_fence), new ItemStack(Items.dye, 1, 0), new ItemStack(Items.dye, 1, 11) });
/*     */     
/* 242 */     initVillagerRecipes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void serverStart(FMLServerStartingEvent event) {
/* 251 */     event.registerServerCommand((ICommand)new VillagerMessagesCommand());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void serverStop(FMLServerStoppingEvent event) {
/* 260 */     villages.clear();
/* 261 */     villageCollection = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initVillagerRecipes() {
/* 270 */     List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
/*     */     
/* 272 */     for (int i = 0; i < recipes.size(); i++) {
/* 273 */       ItemStack outputItem = ((IRecipe)recipes.get(i)).getRecipeOutput();
/* 274 */       if (outputItem != null) {
/*     */         VillagerRecipe recipe = null;
/*     */         try {
/* 277 */           recipe = new VillagerRecipe(recipes.get(i), false);
/* 278 */         } catch (NullPointerException e) {
/*     */         
/* 280 */         } catch (Exception e) {
/* 281 */           System.out.println(e.getMessage());
/*     */         } 
/*     */ 
/*     */         
/* 285 */         allCrafting.add(recipe);
/*     */         
/*     */         int j;
/* 288 */         for (j = 0; j < EntityLumberjack.lumberjackCraftables.length; j++) {
/* 289 */           ItemStack currItem = EntityLumberjack.lumberjackCraftables[j];
/* 290 */           if (currItem.getItem().equals(outputItem.getItem()) && recipe.getOutput() != null && !lumberjackRecipes.contains(recipe)) {
/* 291 */             lumberjackRecipes.add(recipe);
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */         
/* 297 */         for (j = 0; j < EntityFarmer.farmerCraftables.length; j++) {
/* 298 */           ItemStack currItem = EntityFarmer.farmerCraftables[j];
/* 299 */           if (currItem.getItem().equals(outputItem.getItem()) && recipe.getOutput() != null && !farmerRecipes.contains(recipe)) {
/* 300 */             farmerRecipes.add(recipe);
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */         
/* 306 */         for (j = 0; j < EntityMiner.minerCraftables.length; j++) {
/* 307 */           ItemStack currItem = EntityMiner.minerCraftables[j];
/* 308 */           if (currItem.getItem().equals(outputItem.getItem()) && recipe.getOutput() != null && !minerRecipes.contains(recipe)) {
/* 309 */             minerRecipes.add(recipe);
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */         
/* 315 */         for (j = 0; j < EntityRancher.rancherCraftables.length; j++) {
/* 316 */           ItemStack currItem = EntityRancher.rancherCraftables[j];
/* 317 */           if (currItem.getItem().equals(outputItem.getItem()) && recipe.getOutput() != null && !rancherRecipes.contains(recipe)) {
/* 318 */             rancherRecipes.add(recipe);
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 326 */     Map map = FurnaceRecipes.instance().getSmeltingList();
/* 327 */     Set<Map.Entry> entrySet = map.entrySet();
/* 328 */     for (Map.Entry entry : entrySet) {
/* 329 */       ItemStack outputItem = (ItemStack)entry.getValue();
/*     */       
/* 331 */       VillagerRecipe recipe = new VillagerRecipe((ItemStack)entry.getKey(), outputItem, true);
/* 332 */       allSmelting.add(recipe);
/*     */       
/*     */       int j;
/* 335 */       for (j = 0; j < EntityFarmer.farmerSmeltables.length; j++) {
/* 336 */         ItemStack currItem = EntityFarmer.farmerSmeltables[j];
/* 337 */         if (currItem.getItem().equals(outputItem.getItem()) && recipe.getOutput() != null && !farmerRecipes.contains(recipe)) {
/* 338 */           recipe = new VillagerRecipe((ItemStack)entry.getKey(), outputItem, true);
/* 339 */           farmerRecipes.add(recipe);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       
/* 345 */       for (j = 0; j < EntityMiner.minerSmeltables.length; j++) {
/* 346 */         ItemStack currItem = EntityMiner.minerSmeltables[j];
/* 347 */         if (currItem.getItem().equals(outputItem.getItem()) && recipe.getOutput() != null && !minerRecipes.contains(recipe)) {
/* 348 */           recipe = new VillagerRecipe((ItemStack)entry.getKey(), outputItem, true);
/* 349 */           minerRecipes.add(recipe);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       
/* 355 */       for (j = 0; j < EntityFisherman.fishermanSmeltables.length; j++) {
/* 356 */         ItemStack currItem = EntityFisherman.fishermanSmeltables[j];
/* 357 */         if (currItem.getItem().equals(outputItem.getItem()) && recipe.getOutput() != null && !fishermanRecipes.contains(recipe)) {
/* 358 */           recipe = new VillagerRecipe((ItemStack)entry.getKey(), outputItem, true);
/* 359 */           fishermanRecipes.add(recipe);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       
/* 365 */       for (j = 0; j < EntityRancher.rancherSmeltables.length; j++) {
/* 366 */         ItemStack currItem = EntityRancher.rancherSmeltables[j];
/* 367 */         if (currItem.getItem().equals(outputItem.getItem()) && recipe.getOutput() != null && !rancherRecipes.contains(recipe)) {
/* 368 */           recipe = new VillagerRecipe((ItemStack)entry.getKey(), outputItem, true);
/* 369 */           rancherRecipes.add(recipe);
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 377 */     Collections.sort(lumberjackRecipes);
/* 378 */     Collections.sort(farmerRecipes);
/* 379 */     Collections.sort(minerRecipes);
/* 380 */     Collections.sort(fishermanRecipes);
/* 381 */     Collections.sort(rancherRecipes);
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\main\HelpfulVillagers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */