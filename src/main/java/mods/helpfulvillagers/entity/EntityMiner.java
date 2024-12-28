/*     */ package mods.helpfulvillagers.entity;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import mods.helpfulvillagers.ai.EntityAIMiner;
/*     */ import mods.helpfulvillagers.crafting.VillagerRecipe;
/*     */ import mods.helpfulvillagers.enums.EnumActivity;
/*     */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.ai.EntityAIAvoidEntity;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.oredict.OreDictionary;
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
/*     */ public class EntityMiner
/*     */   extends AbstractVillager
/*     */ {
/*  41 */   private final ItemStack[] minerTools = new ItemStack[] { new ItemStack(Items.diamond_pickaxe), new ItemStack(Items.golden_pickaxe), new ItemStack(Items.iron_pickaxe), new ItemStack(Items.stone_pickaxe), new ItemStack(Items.wooden_pickaxe) };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   public static final ItemStack[] minerCraftables = new ItemStack[] { new ItemStack(Blocks.furnace), new ItemStack((Block)Blocks.stone_slab), new ItemStack(Blocks.stone), new ItemStack(Blocks.stonebrick), new ItemStack(Blocks.stone_brick_stairs), new ItemStack(Blocks.stone_stairs), new ItemStack(Blocks.stone_button), new ItemStack(Blocks.stone_pressure_plate), new ItemStack(Blocks.light_weighted_pressure_plate), new ItemStack(Blocks.heavy_weighted_pressure_plate), new ItemStack(Blocks.lever), new ItemStack(Blocks.redstone_lamp), new ItemStack(Blocks.redstone_torch), new ItemStack(Blocks.rail), new ItemStack(Blocks.golden_rail), new ItemStack(Blocks.detector_rail), new ItemStack(Blocks.activator_rail), new ItemStack(Blocks.coal_block), new ItemStack(Blocks.iron_block), new ItemStack(Blocks.gold_block), new ItemStack(Blocks.redstone_block), new ItemStack(Blocks.lapis_block), new ItemStack(Blocks.emerald_block), new ItemStack(Blocks.diamond_block), new ItemStack(Blocks.cobblestone_wall), new ItemStack((Block)Blocks.piston), new ItemStack((Block)Blocks.sticky_piston), new ItemStack(Blocks.dispenser), new ItemStack(Blocks.dropper), new ItemStack((Block)Blocks.hopper), new ItemStack(Blocks.iron_bars), new ItemStack(Blocks.enchanting_table), new ItemStack(Blocks.anvil), new ItemStack(Blocks.torch), new ItemStack(Items.iron_door), new ItemStack((Item)Items.iron_helmet), new ItemStack((Item)Items.iron_chestplate), new ItemStack((Item)Items.iron_leggings), new ItemStack((Item)Items.iron_boots), new ItemStack(Items.iron_axe), new ItemStack(Items.iron_hoe), new ItemStack(Items.iron_pickaxe), new ItemStack(Items.iron_shovel), new ItemStack(Items.iron_sword), new ItemStack((Item)Items.golden_helmet), new ItemStack((Item)Items.golden_chestplate), new ItemStack((Item)Items.golden_leggings), new ItemStack((Item)Items.golden_boots), new ItemStack(Items.golden_axe), new ItemStack(Items.golden_hoe), new ItemStack(Items.golden_pickaxe), new ItemStack(Items.golden_shovel), new ItemStack(Items.golden_sword), new ItemStack((Item)Items.diamond_helmet), new ItemStack((Item)Items.diamond_chestplate), new ItemStack((Item)Items.diamond_leggings), new ItemStack((Item)Items.diamond_boots), new ItemStack(Items.diamond_axe), new ItemStack(Items.diamond_hoe), new ItemStack(Items.diamond_pickaxe), new ItemStack(Items.diamond_shovel), new ItemStack(Items.diamond_sword), new ItemStack(Items.stone_axe), new ItemStack(Items.stone_hoe), new ItemStack(Items.stone_pickaxe), new ItemStack(Items.stone_shovel), new ItemStack(Items.stone_sword), new ItemStack(Items.flint_and_steel), new ItemStack((Item)Items.shears), new ItemStack(Items.bucket), new ItemStack(Items.clock), new ItemStack(Items.compass), new ItemStack(Items.repeater), new ItemStack(Items.comparator), new ItemStack(Items.minecart), new ItemStack(Items.furnace_minecart), new ItemStack(Items.chest_minecart), new ItemStack(Items.hopper_minecart), new ItemStack(Items.tnt_minecart), new ItemStack(Items.cauldron), new ItemStack(Items.brewing_stand) };
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
/* 135 */   public static final ItemStack[] minerSmeltables = new ItemStack[] { new ItemStack(Blocks.stone), new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot) };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 142 */   private final Block[] excludeBlocksArray = new Block[] { Blocks.air, (Block)Blocks.sand, Blocks.gravel, Blocks.gold_ore, Blocks.iron_ore, Blocks.coal_ore, Blocks.lapis_ore, Blocks.diamond_ore, Blocks.redstone_ore, Blocks.emerald_ore };
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
/* 155 */   public ArrayList excludeBlocks = new ArrayList();
/* 156 */   public ArrayList shaftCoords = new ArrayList();
/*     */   public BlockPos topCoords;
/*     */   public int topDir;
/*     */   public int shaftIndex;
/* 160 */   public ArrayList digCoords = new ArrayList();
/*     */   public boolean dugSection;
/* 162 */   public ArrayList tunnelCoords = new ArrayList();
/* 163 */   public ArrayList returnPath = new ArrayList();
/*     */   public boolean beingFollowed;
/*     */   public boolean swingingPickaxe;
/*     */   private int suffocationCount;
/*     */   
/*     */   public EntityMiner(World world) {
/* 169 */     super(world);
/* 170 */     init();
/*     */   }
/*     */   
/*     */   public EntityMiner(AbstractVillager villager) {
/* 174 */     super(villager);
/* 175 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init() {
/* 182 */     this.topCoords = null;
/* 183 */     this.topDir = 0;
/* 184 */     this.shaftIndex = 0;
/* 185 */     this.dugSection = false;
/* 186 */     this.beingFollowed = false;
/* 187 */     this.swingingPickaxe = false;
/* 188 */     this.suffocationCount = 0;
/* 189 */     this.profession = 2;
/* 190 */     this.profName = "Miner";
/* 191 */     this.currentActivity = EnumActivity.IDLE;
/* 192 */     this.searchRadius = 5;
/* 193 */     this.knownRecipes.addAll(HelpfulVillagers.minerRecipes);
/* 194 */     addHorseArmorRecipes();
/* 195 */     setTools(this.minerTools);
/* 196 */     getNewGuildHall();
/* 197 */     addExcludeBlocks();
/* 198 */     addThisAI();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addHorseArmorRecipes() {
/* 205 */     if (!this.worldObj.isRemote) {
/* 206 */       ArrayList<ItemStack> inputs = new ArrayList<ItemStack>();
/* 207 */       inputs.add(new ItemStack(Blocks.wool, 1));
/* 208 */       inputs.add(new ItemStack(Items.iron_ingot, 6));
/* 209 */       this.knownRecipes.add(new VillagerRecipe(inputs, new ItemStack(Items.iron_horse_armor), false));
/*     */       
/* 211 */       inputs.set(1, new ItemStack(Items.gold_ingot, 6));
/* 212 */       this.knownRecipes.add(new VillagerRecipe(inputs, new ItemStack(Items.golden_horse_armor), false));
/* 213 */       inputs.set(1, new ItemStack(Items.diamond, 6));
/* 214 */       this.knownRecipes.add(new VillagerRecipe(inputs, new ItemStack(Items.diamond_horse_armor), false));
/* 215 */       Collections.sort(this.knownRecipes);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addExcludeBlocks() {
/* 223 */     for (int i = 0; i < this.excludeBlocksArray.length; i++) {
/* 224 */       this.excludeBlocks.add(this.excludeBlocksArray[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addThisAI() {
/* 232 */ ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
/*     */     
/* 234 */     this.tasks.addTask(1, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)this, EntityZombie.class, 8.0F, 0.30000001192092896D, 0.3499999940395355D));
/* 235 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIMiner(this));
/* 236 */     this.tasks.addTask(3, (EntityAIBase)new EntityAIRestrictOpenDoor((EntityCreature)this));
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_onUpdate() {
/* 241 */     super.onUpdate();
/* 242 */     if (getHealth() >= getMaxHealth()) {
/* 243 */       this.suffocationCount = 0;
/*     */     }
/*     */     
/* 246 */     if (this.posY <= 30.0D) {
/* 247 */       this.searchRadius = 8;
/*     */     } else {
/* 249 */       this.searchRadius = 5;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList getValidCoords() {
/* 255 */     updateBoxes();
/*     */     
/* 257 */     ArrayList<BlockPos> coords = new ArrayList();
/* 258 */     AxisAlignedBB searchBox = this.searchBox;
/*     */     
/* 260 */     for (int x = (int)searchBox.minX; x <= searchBox.maxX; x++) {
/* 261 */       for (int z = (int)searchBox.minZ; z <= searchBox.maxZ; z++) {
				  BlockPos pos5 = new BlockPos(x, this.posY, z);
/* 262 */         Block block = this.worldObj.getBlockState(pos5).getBlock();
/* 263 */         int[] oreDictIDs = OreDictionary.getOreIDs(new ItemStack(block));
/* 264 */         for (int j = 0; j < oreDictIDs.length; j++) {
/* 265 */           String name = OreDictionary.getOreName(oreDictIDs[j]);
/* 266 */           if (name.contains("ore")) {
/* 267 */             coords.add(new BlockPos(x, (int)this.posY, z));
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 274 */     return coords;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int nearestShaftCoord() {
/* 281 */     for (int i = 0; i < this.shaftCoords.size(); i++) {
/* 282 */       BlockPos currentCoords = (BlockPos) this.shaftCoords.get(i);
/* 283 */       if (currentCoords.getY() == (int)this.posY) {
/* 284 */         return i;
/*     */       }
/*     */     } 
/* 287 */     if (this.topCoords != null && (getCoords()).getY() >= this.topCoords.getY()) {
/* 288 */       return 0;
/*     */     }
/* 290 */     return this.shaftCoords.size() - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean areCoordsInMine(BlockPos coords) {
/* 298 */     for (int i = 0; i < 4; i++) {
/* 299 */       BlockPos check = (BlockPos) this.shaftCoords.get(i);
/* 300 */       if (coords.getX() == check.getX() && coords.getZ() == check.getZ()) {
/* 301 */         return true;
/*     */       }
/*     */     } 
/* 304 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInMine() {
/* 311 */     return areCoordsInMine(getCoords());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource src, float f) {
/* 319 */     if (src.equals(DamageSource.inWall) && this.shaftCoords.size() > 0) {
/* 320 */       this.suffocationCount++;
/* 321 */       if (this.suffocationCount > 2) {
/*     */         BlockPos dest;
/* 323 */         if (nearestShaftCoord() - 1 >= 0) {
/* 324 */           dest = (BlockPos) this.shaftCoords.get(nearestShaftCoord() - 1);
/*     */         } else {
/* 326 */           dest = (BlockPos) this.shaftCoords.get(nearestShaftCoord());
/*     */         } 
/*     */         
/* 329 */         if (getDistance(dest.getX(), dest.getY(), dest.getZ()) <= 5.0D) {
/* 330 */           setLocationAndAngles(dest.getX(), dest.getY(), dest.getZ(), this.rotationYaw, this.rotationPitch);
/* 331 */           this.suffocationCount = 0;
/*     */         } 
/*     */       } 
/*     */     } 
/* 335 */     return super.attackEntityFrom(src, f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound NBTTagCompound) {
/* 344 */     super.writeEntityToNBT(NBTTagCompound);
/* 345 */     NBTTagList tagList = new NBTTagList();
/* 346 */     if (this.topCoords != null) {
/* 347 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 348 */       BlockPos topCoords = this.topCoords;
/* 349 */       int[] coords = { topCoords.getX(), topCoords.getY(), topCoords.getZ() };
/* 350 */       nbttagcompound.setIntArray("Coords", coords);
/* 351 */       tagList.appendTag((NBTBase)nbttagcompound);
/*     */       
/* 353 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 354 */       nbttagcompound1.setInteger("Direction", this.topDir);
/* 355 */       tagList.appendTag((NBTBase)nbttagcompound1);
/* 356 */       NBTTagCompound.setTag("Mineshaft", (NBTBase)tagList);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound NBTTagCompound) {
/* 366 */     super.readEntityFromNBT(NBTTagCompound);
/* 367 */     if (NBTTagCompound.hasKey("Mineshaft")) {
/* 368 */       NBTTagList nbttaglist = NBTTagCompound.getTagList("Mineshaft", NBTTagCompound.getId());
/* 369 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(0);
/* 370 */       int[] coords = nbttagcompound.getIntArray("Coords");
/* 371 */       this.topCoords = new BlockPos(coords[0], coords[1], coords[2]);
/*     */       
/* 373 */       NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(1);
/* 374 */       this.topDir = nbttagcompound.getInteger("Direction");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidTool(ItemStack item) {
/* 380 */     return item.getItem() instanceof net.minecraft.item.ItemPickaxe;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCraft() {
/* 385 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\entity\EntityMiner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */