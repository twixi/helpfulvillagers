/*     */ package mods.helpfulvillagers.entity;
/*     */ 
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import mods.helpfulvillagers.ai.EntityAILumberjack;
/*     */ import mods.helpfulvillagers.enums.EnumActivity;
/*     */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*     */ import mods.helpfulvillagers.network.SaplingPacket;
/*     */ import mods.helpfulvillagers.util.AIHelper;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.ai.EntityAIAvoidEntity;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
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
/*     */ public class EntityLumberjack
/*     */   extends AbstractVillager
/*     */ {
/*     */   public boolean foundTree;
/*     */   public boolean shouldPlant;
/*     */   private int previousTime;
/*     */   private int currentTime;
/*  62 */   private final int SAPLING_TIME = 200;
/*     */ 
/*     */   
/*  65 */   private final ItemStack[] lumberjackTools = new ItemStack[] { new ItemStack(Items.diamond_axe), new ItemStack(Items.golden_axe), new ItemStack(Items.iron_axe), new ItemStack(Items.stone_axe), new ItemStack(Items.wooden_axe) };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   public static final ItemStack[] lumberjackCraftables = new ItemStack[] { new ItemStack(Blocks.planks), new ItemStack(Blocks.crafting_table), new ItemStack((Block)Blocks.chest), new ItemStack(Blocks.ladder), new ItemStack(Blocks.oak_fence), new ItemStack((Block)Blocks.wooden_slab), new ItemStack(Blocks.bookshelf), new ItemStack(Blocks.noteblock), new ItemStack(Blocks.acacia_stairs), new ItemStack(Blocks.oak_stairs), new ItemStack(Blocks.dark_oak_stairs), new ItemStack(Blocks.spruce_stairs), new ItemStack(Blocks.birch_stairs), new ItemStack(Blocks.jungle_stairs), new ItemStack(Blocks.wooden_pressure_plate), new ItemStack(Blocks.wooden_button), new ItemStack(Blocks.trapdoor), new ItemStack(Blocks.oak_fence_gate), new ItemStack(Blocks.jukebox), new ItemStack((Block)Blocks.daylight_detector), new ItemStack((Block)Blocks.tripwire_hook), new ItemStack(Blocks.trapped_chest), new ItemStack(Items.stick), new ItemStack(Items.boat), new ItemStack(Items.sign), new ItemStack(Items.painting), new ItemStack(Items.oak_door), new ItemStack(Items.wooden_axe), new ItemStack(Items.wooden_hoe), new ItemStack(Items.wooden_pickaxe), new ItemStack(Items.wooden_shovel), new ItemStack(Items.wooden_sword), new ItemStack((Item)Items.bow), new ItemStack(Items.arrow), new ItemStack(Items.bowl), new ItemStack((Item)Items.fishing_rod), new ItemStack(Items.carrot_on_a_stick), new ItemStack(Items.item_frame), new ItemStack(Items.bed) };
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
/*     */   public EntityLumberjack(World world) {
/* 117 */     super(world);
/* 118 */     init();
/*     */   }
/*     */   
/*     */   public EntityLumberjack(AbstractVillager villager) {
/* 122 */     super(villager);
/* 123 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init() {
/* 130 */     this.profession = 1;
/* 131 */     this.profName = "Lumberjack";
/* 132 */     this.currentActivity = EnumActivity.IDLE;
/* 133 */     this.searchRadius = 10;
/* 134 */     this.foundTree = false;
/* 135 */     this.shouldPlant = false;
/* 136 */     this.previousTime = 0;
/* 137 */     this.currentTime = 0;
/* 138 */     this.knownRecipes.addAll(HelpfulVillagers.lumberjackRecipes);
/* 139 */     setTools(this.lumberjackTools);
/* 140 */     getNewGuildHall();
/* 141 */     addThisAI();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addThisAI() {
/* 148 */     ((PathNavigateGround)this.getNavigator()).setAvoidsWater(false);
/* 149 */     this.tasks.addTask(1, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)this, EntityZombie.class, 8.0F, 0.30000001192092896D, 0.3499999940395355D));
/* 150 */     this.tasks.addTask(2, (EntityAIBase)new EntityAILumberjack(this));
/* 151 */     this.tasks.addTask(3, (EntityAIBase)new EntityAIRestrictOpenDoor((EntityCreature)this));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sync() {
/* 158 */     if (!this.worldObj.isRemote) {
/* 159 */       HelpfulVillagers.network.sendToAll((IMessage)new SaplingPacket(getEntityId(), this.shouldPlant));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void fuonUpdate() {
/* 165 */     super.onUpdate();
/* 166 */     pickupSaplings();
/* 167 */     shouldPlantSapling();
/* 168 */     sync();
/* 169 */     if (this.shouldPlant) {
/* 170 */       plantSapling();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dayCheck() {
/* 176 */     super.dayCheck();
/* 177 */     if (this.foundTree) {
/* 178 */       this.foundTree = false;
/*     */     } else {
/* 180 */       this.lastResource = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void shouldPlantSapling() {
/* 188 */     if (this.homeVillage != null && !this.worldObj.isRemote) {
/* 189 */       this.shouldPlant = (!AIHelper.isInRangeOfAnyVillage(this.posX, this.posY, this.posZ) && !nearHall());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void plantSapling() {
/* 198 */     int index = this.inventory.containsItem(new ItemStack(Blocks.sapling));
/*     */     
/* 200 */     if (this.previousTime <= 0) {
/* 201 */       this.previousTime = this.ticksExisted;
/*     */     }
/* 203 */     this.currentTime = this.ticksExisted;
/*     */     
/* 205 */     if (this.currentTime - this.previousTime >= 200) {
/* 206 */       this.previousTime = 0;
/*     */       
/* 208 */       if (index >= 0) {
/* 209 */         int y = (int)this.posY;
/*     */         while (true) {
					BlockPos blkair = new BlockPos(this.posX, y, this.posZ);
/* 211 */           Block air = this.worldObj.getBlockState(blkair).getBlock();
					BlockPos blkdirt = new BlockPos(this.posX, y-1, this.posZ);
/* 212 */           Block dirt = this.worldObj.getBlockState(blkdirt).getBlock();
/* 213 */           if (air.equals(Blocks.air) && (dirt.equals(Blocks.grass) || dirt.equals(Blocks.dirt))) {
/* 214 */             ItemStack saplingItem = this.inventory.getStackInSlot(index);
/* 215 */             int metadata = saplingItem.getMetadata(); 
/* 216 */             this.worldObj.setBlockState(blkair, Block.getBlockFromItem(saplingItem.getItem()).getDefaultState(), 2);
/* 217 */             this.inventory.decrementSlot(index); return;
/*     */           } 
/* 219 */           if (!air.equals(Blocks.air)) {
/*     */             return;
/*     */           }
/* 222 */           y--;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void pickupSaplings() {
/* 233 */     if (this.inventory.isFull()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 238 */     List items = this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.searchBox);
/* 239 */     Iterator<EntityItem> iterator = items.iterator();
/*     */     
/* 241 */     while (iterator.hasNext()) {
/* 242 */       EntityItem currentItem = iterator.next();
/* 243 */       ItemStack currentStack = currentItem.getEntityItem();
/* 244 */       if (currentStack.getDisplayName() != null && currentStack.getDisplayName().contains("Sapling")) {
/* 245 */         this.inventory.addItem(currentItem.getEntityItem());
/* 246 */         currentItem.setDead();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public ArrayList getValidCoords() {
/* 252 */     updateBoxes();
/*     */     
/* 254 */     ArrayList<BlockPos> coords = new ArrayList();
/* 255 */     AxisAlignedBB searchBox = this.searchBox;
/*     */     
/* 257 */     for (int x = (int)searchBox.minX; x <= searchBox.maxX; x++) {
/* 258 */       for (int y = (int)searchBox.minY; y <= searchBox.maxY; y++) {
/* 259 */         for (int z = (int)searchBox.minZ; z <= searchBox.maxZ; z++) {
					BlockPos xyzpos = new BlockPos(x, y, z);
/* 260 */           Block block = this.worldObj.getBlockState(xyzpos).getBlock();
/* 261 */           if (block instanceof net.minecraft.block.BlockRotatedPillar && block.getMaterial() == Material.wood) {
/* 262 */             coords.add(new BlockPos(x, y, z));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 268 */     return coords;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidTool(ItemStack item) {
/* 273 */     return item.getItem() instanceof net.minecraft.item.ItemAxe;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCraft() {
/* 278 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\entity\EntityLumberjack.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */