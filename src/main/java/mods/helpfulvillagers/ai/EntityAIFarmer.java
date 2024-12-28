/*     */ package mods.helpfulvillagers.ai;
/*     */ 
/*     */ import java.util.ArrayList;
import java.util.List;

/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.entity.EntityFarmer;
/*     */ import mods.helpfulvillagers.util.AIHelper;
/*     */ import mods.helpfulvillagers.util.ResourceCluster;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.BlockReed;
/*     */ import net.minecraft.block.BlockStem;
/*     */ import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraftforge.common.EnumPlantType;
/*     */ import net.minecraftforge.common.IPlantable;
		  import net.minecraft.block.BlockCrops;
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
/*     */ public class EntityAIFarmer
/*     */   extends EntityAIWorker
/*     */ {
/*     */   private EntityFarmer farmer;
/*  39 */   private ArrayList<BlockPos> farmCoords = new ArrayList<BlockPos>();
/*     */   
/*     */   public EntityAIFarmer(EntityFarmer farmer) {
/*  42 */     super((AbstractVillager)farmer);
/*  43 */     this.farmer = farmer;
/*  44 */     this.target = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean gather() {
/*  53 */     if (this.farmer.homeGuildHall == null) {
/*  54 */       return idle();
/*     */     }
/*  56 */     if (this.farmer.insideHall()) {
/*  57 */       BlockPos exit = this.farmer.homeGuildHall.entranceCoords;
/*  58 */       if (exit == null) {
/*  59 */         exit = AIHelper.getRandInsideCoords((AbstractVillager)this.farmer);
/*     */       }
/*  61 */       this.farmer.moveTo(exit, this.speed);
/*     */     }
/*  63 */     else if (this.farmer.currentResource == null) {
/*  64 */       findFarm();
/*     */     } else {
/*  66 */       this.target = this.farmCoords.get(0);
/*  67 */       int distX = AIHelper.findDistance((int)this.farmer.posX, this.target.getX());
/*  68 */       int distY = AIHelper.findDistance((int)this.farmer.posY, this.target.getY());
/*  69 */       int distZ = AIHelper.findDistance((int)this.farmer.posZ, this.target.getZ());
/*  70 */       if (distX > 3 || distY > 3 || distZ > 3) {
/*  71 */         this.farmer.moveTo(this.target, this.speed);
/*     */       } else {
/*  73 */         harvestCrops();
/*     */       } 
/*     */     } 
/*     */     
/*  77 */     return idle();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void findFarm() {
/*  87 */     if (this.target == null) {
/*  88 */       this.target = AIHelper.getRandInsideCoords((AbstractVillager)this.farmer);
/*     */     }
/*     */ 
/*     */     
/*  92 */     if (this.target != null) {
/*  93 */       this.farmer.moveTo(this.target, this.speed);
/*     */     }
/*  95 */     this.farmer.updateBoxes();
/*  96 */     if (this.farmer.searchBox != null && this.farmer.worldObj.isMaterialInBB(this.farmer.searchBox, Material.ground)) {
/*  97 */       this.farmer.currentResource = getNewResource();
/*  98 */       if (this.farmer.currentResource != null) {
/*  99 */         this.farmCoords.addAll(this.farmer.currentResource.blockCluster);
/* 100 */         this.farmer.getNavigator().clearPathEntity();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 105 */     if (Math.abs(this.farmer.posX - this.target.getX()) <= 5.0D && Math.abs(this.farmer.posZ - this.target.getZ()) <= 5.0D) {
/* 106 */       this.target = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ResourceCluster getNewResource() {
/* 117 */     ArrayList<BlockPos> boxCoords = this.farmer.getValidCoords();
/* 118 */     double closestDist = Double.MAX_VALUE;
/* 119 */     ResourceCluster closestValidCluster = null;
/*     */     
/* 121 */     for (int i = 0; i < boxCoords.size(); i++) {
/* 122 */       BlockPos currCoords = boxCoords.get(i);
/* 123 */       ResourceCluster currentCluster = new ResourceCluster(this.farmer.worldObj, currCoords);
/* 124 */       currentCluster.buildCluster();
/* 125 */       boolean visited = false;
/*     */ 
/*     */       
/* 128 */       if (this.farmer.visitedFarms != null) {
/* 129 */         for (int j = 0; j < this.farmer.visitedFarms.size(); j++) {
/* 130 */           ResourceCluster farm = this.farmer.visitedFarms.get(j);
/* 131 */           if (farm.matchesCluster(currentCluster)) {
/* 132 */             visited = true;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 139 */       if (!visited) {
/* 140 */         double dist = this.farmer.getDistance(currCoords.getX(), currCoords.getY(), currCoords.getZ());
/* 141 */         if (dist < closestDist) {
/* 142 */           closestDist = dist;
/* 143 */           closestValidCluster = currentCluster;
/*     */         } 
/*     */       } 
/*     */     } 
/* 147 */     return closestValidCluster;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void harvestCrops() {
/* 155 */     boolean shouldSwing = false;
/* 156 */     BlockPos aboveCoords = new BlockPos(this.target.getX(), this.target.getY() + 1, this.target.getZ());
/* 157 */     Block currentCrop = this.farmer.worldObj.getBlockState(aboveCoords).getBlock();
/* 158 */     if (!canHarvest(aboveCoords)) {
/* 159 */       this.farmCoords.remove(0);
/* 160 */       if (this.farmCoords.isEmpty()) {
/* 161 */         this.farmer.visitedFarms.add(this.farmer.currentResource);
/* 162 */         this.farmer.currentResource = null;
/* 163 */         this.target = null;
/* 164 */         this.previousTime = 0;
/* 165 */         this.currentTime = 0;
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 170 */     if (this.farmer.getNavigator().noPath()) {
/* 171 */       this.farmer.getLookHelper().setLookPosition(this.farmer.currentResource.lowestCoords.getX(), this.farmer.currentResource.lowestCoords.getY(), this.farmer.currentResource.lowestCoords.getZ(), 10.0F, 10.0F);
/* 172 */       shouldSwing = true;
/*     */ 
/*     */       
/* 175 */       if (this.previousTime <= 0) {
/* 176 */         this.previousTime = this.farmer.ticksExisted;
/* 177 */         this.harvestTime = this.farmer.getHarvestTime();
/*     */       } 
/*     */     } else {
/* 180 */       shouldSwing = false;
/*     */     } 
/*     */     
/* 183 */     if (this.previousTime > 0) {
/* 184 */       this.currentTime = this.farmer.ticksExisted;
/* 185 */       if (!this.farmCoords.isEmpty()) {
/* 186 */         if ((this.currentTime - this.previousTime) >= this.harvestTime) {
/* 187 */           this.previousTime = this.currentTime;
/* 188 */           this.harvestTime = this.farmer.getHarvestTime();
/* 189 */           if (canHarvest(aboveCoords)) {
/* 190 */             if (!(currentCrop instanceof BlockStem)) {
/* 191 */               List<ItemStack> cropDrops = currentCrop.getDrops(this.farmer.worldObj, aboveCoords, this.farmer.worldObj.getBlockState(aboveCoords), 0);
/* 192 */               ItemStack foundSeed = null;
/* 193 */               for (ItemStack i : cropDrops) {
/* 194 */                 if (i.getItem() instanceof IPlantable) {
/* 195 */                   foundSeed = i;
/*     */                   break;
/*     */                 } 
/*     */               } 
/* 199 */               if (foundSeed != null) {
/* 200 */                 this.farmer.seedToPlant = (IPlantable)foundSeed.getItem();
/*     */               }
/* 202 */               AIHelper.breakBlock(aboveCoords, (AbstractVillager)this.farmer);
/* 203 */               plantCrop(aboveCoords);
/*     */             } else {
/* 205 */               BlockStem stem = (BlockStem)currentCrop;
						IBlockState state = this.farmer.worldObj.getBlockState(aboveCoords);
/* 206 */               EnumFacing dir = state.getValue(stem.FACING);
/* 207 */               switch (dir) {
/*     */                 case SOUTH:
/* 209 */                   aboveCoords.add(-1, 0, 0);
/*     */                   break;
/*     */                 case NORTH:
/* 212 */                   aboveCoords.add(1, 0, 0);
/*     */                   break;
/*     */                 case EAST:
/* 215 */                   aboveCoords.add(0, 0, -1);
/*     */                   break;
/*     */                 case WEST:
/* 218 */                   aboveCoords.add(0, 0, 1);
/*     */                   break;
/*     */               } 
/* 221 */               AIHelper.breakBlock(aboveCoords, (AbstractVillager)this.farmer);
/*     */             } 
/*     */           }
/* 224 */           this.farmCoords.remove(0);
/*     */           
/* 226 */           if (this.farmCoords.isEmpty()) {
/* 227 */             this.farmer.visitedFarms.add(this.farmer.currentResource);
/* 228 */             this.farmer.currentResource = null;
/* 229 */             this.target = null;
/* 230 */             this.previousTime = 0;
/* 231 */             this.currentTime = 0;
/*     */           } 
/*     */         } 
/*     */       } else {
/* 235 */         this.farmer.visitedFarms.add(this.farmer.currentResource);
/* 236 */         this.farmer.currentResource = null;
/* 237 */         this.target = null;
/* 238 */         this.previousTime = 0;
/* 239 */         this.currentTime = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 243 */     if (shouldSwing) {
/* 244 */       this.farmer.swingItem();
/*     */     } else {
/* 246 */       this.previousTime = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean canHarvest(BlockPos coords) {
/* 256 */     Block block = this.farmer.worldObj.getBlockState(coords).getBlock();
/* 257 */     if (block instanceof BlockCrops)
/* 258 */       return (this.farmer.worldObj.getBlockState(coords).getValue(((BlockCrops) block).AGE) >= 7); 
/* 259 */     if (block instanceof BlockReed)
/* 260 */       return true; 
/* 261 */     if (block instanceof BlockNetherWart) {
/* 262 */       BlockNetherWart wart = (BlockNetherWart)block;
/* 263 */       return (this.farmer.worldObj.getBlockState(coords).getValue(((BlockNetherWart) block).AGE) >= 3);
/* 264 */     }  if (block instanceof BlockStem) {
/* 265 */       BlockStem stem = (BlockStem)block;
/* 266 */       return (this.farmer.worldObj.getBlockState(coords).getValue(((BlockStem) block).AGE) >= 7);
/*     */     } 
/* 268 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void plantCrop(BlockPos coords) {
/* 277 */     if (!this.farmer.worldObj.isAirBlock(coords)) {
/*     */       return;
/*     */     }
/*     */     
/* 281 */     if (this.farmer.seedToPlant != null) {
/*     */       
/* 283 */       EnumPlantType plantType = this.farmer.seedToPlant.getPlantType((IBlockAccess)this.farmer.worldObj, coords);
/* 284 */       if (plantType == EnumPlantType.Nether && this.farmer.worldObj.getBlockState(coords.add(0, -1, 0)).getBlock() != Blocks.soul_sand)
/*     */         return; 
/* 286 */       if (plantType == EnumPlantType.Crop && this.farmer.worldObj.getBlockState(coords.add(0, -1, 0)).getBlock() != Blocks.farmland) {
/*     */         return;
/*     */       }
/*     */       
/* 290 */       Item plantItem = (Item)this.farmer.seedToPlant;
/* 291 */       Block newPlant = (Block) this.farmer.seedToPlant.getPlant((IBlockAccess)this.farmer.worldObj, coords);
/* 292 */       if (this.farmer.lastSeedIndex < 0 || this.farmer.inventory.getStackInSlot(this.farmer.lastSeedIndex) == null || !this.farmer.inventory.getStackInSlot(this.farmer.lastSeedIndex).getItem().equals(plantItem)) {
/* 293 */         this.farmer.lastSeedIndex = this.farmer.inventory.containsItem(new ItemStack(plantItem));
/*     */       }
/* 295 */       if (this.farmer.lastSeedIndex >= 0) {
/* 296 */         this.farmer.inventory.decrementSlot(this.farmer.lastSeedIndex);
/* 297 */         this.farmer.worldObj.setBlockState(coords, (IBlockState) newPlant);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\ai\EntityAIFarmer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */