/*     */ package mods.helpfulvillagers.entity;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import mods.helpfulvillagers.ai.EntityAIFarmer;
/*     */ import mods.helpfulvillagers.enums.EnumActivity;
/*     */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*     */ import mods.helpfulvillagers.util.ResourceCluster;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.ai.EntityAIAvoidEntity;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.common.IPlantable;
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
/*     */ public class EntityFarmer
/*     */   extends AbstractVillager
/*     */ {
/*  37 */   private final ItemStack[] farmerTools = new ItemStack[] { new ItemStack(Items.diamond_hoe), new ItemStack(Items.golden_hoe), new ItemStack(Items.iron_hoe), new ItemStack(Items.stone_hoe), new ItemStack(Items.wooden_hoe) };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public static final ItemStack[] farmerCraftables = new ItemStack[] { new ItemStack(Blocks.lit_pumpkin), new ItemStack(Blocks.hay_block), new ItemStack(Blocks.melon_block), new ItemStack(Items.cake), new ItemStack(Items.bread), new ItemStack(Items.cookie), new ItemStack(Items.bread), new ItemStack(Items.mushroom_stew), new ItemStack(Items.bread), new ItemStack(Items.pumpkin_seeds), new ItemStack(Items.melon_seeds), new ItemStack(Items.sugar), new ItemStack(Items.pumpkin_pie), new ItemStack(Items.paper), new ItemStack(Items.book) };
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
/*  65 */   public static final ItemStack[] farmerSmeltables = new ItemStack[] { new ItemStack(Items.baked_potato) };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   public ArrayList<ResourceCluster> visitedFarms = new ArrayList<ResourceCluster>();
/*     */ 
/*     */   
/*     */   public IPlantable seedToPlant;
/*     */   
/*     */   public int lastSeedIndex;
/*     */ 
/*     */   
/*     */   public EntityFarmer(World world) {
/*  79 */     super(world);
/*  80 */     init();
/*     */   }
/*     */   
/*     */   public EntityFarmer(AbstractVillager villager) {
/*  84 */     super(villager);
/*  85 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init() {
/*  92 */     this.profession = 3;
/*  93 */     this.profName = "Farmer";
/*  94 */     this.currentActivity = EnumActivity.IDLE;
/*  95 */     this.searchRadius = 10;
/*  96 */     this.seedToPlant = null;
/*  97 */     this.lastSeedIndex = 0;
/*  98 */     this.knownRecipes.addAll(HelpfulVillagers.farmerRecipes);
/*  99 */     setTools(this.farmerTools);
/* 100 */     getNewGuildHall();
/* 101 */     addThisAI();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addThisAI() {
/* 108 */     ((PathNavigateGround)this.getNavigator()).setAvoidsWater(false);
/*     */     
/* 110 */     this.tasks.addTask(1, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)this, EntityZombie.class, 8.0F, 0.30000001192092896D, 0.3499999940395355D));
/* 111 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIFarmer(this));
/* 112 */     this.tasks.addTask(3, (EntityAIBase)new EntityAIRestrictOpenDoor((EntityCreature)this));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getHarvestTime() {
/* 119 */     if (getCurrentItem() == null)
/* 120 */       return 60; 
/* 121 */     if (!(getCurrentItem().getItem() instanceof net.minecraft.item.ItemHoe)) {
/* 122 */       return 60;
/*     */     }
/* 124 */     ItemStack hoe = getCurrentItem();
/* 125 */     if (hoe.getDisplayName().equals((new ItemStack(Items.wooden_hoe)).getDisplayName()))
/* 126 */       return 50; 
/* 127 */     if (hoe.getDisplayName().equals((new ItemStack(Items.stone_hoe)).getDisplayName()))
/* 128 */       return 40; 
/* 129 */     if (hoe.getDisplayName().equals((new ItemStack(Items.iron_hoe)).getDisplayName()))
/* 130 */       return 30; 
/* 131 */     if (hoe.getDisplayName().equals((new ItemStack(Items.golden_hoe)).getDisplayName()))
/* 132 */       return 20; 
/* 133 */     if (hoe.getDisplayName().equals((new ItemStack(Items.diamond_hoe)).getDisplayName())) {
/* 134 */       return 10;
/*     */     }
/* 136 */     return 50;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 143 */     super.onUpdate();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList getValidCoords() {
/* 149 */     updateBoxes();
/*     */     
/* 151 */     ArrayList<BlockPos> coords = new ArrayList();
/* 152 */     AxisAlignedBB searchBox = this.searchBox;
/*     */     
/* 154 */     for (int x = (int)searchBox.minX; x <= searchBox.maxX; x++) {
/* 155 */       for (int y = (int)searchBox.minY; y <= searchBox.maxY; y++) {
/* 156 */         for (int z = (int)searchBox.minZ; z <= searchBox.maxZ; z++) {
					BlockPos blk = new BlockPos(x, y, z);
/* 157 */           Block block = this.worldObj.getBlockState(blk).getBlock();
/* 158 */           if (block == Blocks.farmland || block == Blocks.soul_sand || block == Blocks.reeds) {
/* 159 */             coords.add(new BlockPos(x, y, z));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 165 */     return coords;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dayCheck() {
/* 170 */     super.dayCheck();
/* 171 */     this.visitedFarms.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidTool(ItemStack item) {
/* 176 */     return item.getItem() instanceof net.minecraft.item.ItemHoe;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCraft() {
/* 181 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\entity\EntityFarmer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */