/*     */ package mods.helpfulvillagers.ai;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.entity.EntityLumberjack;
/*     */ import mods.helpfulvillagers.util.AIHelper;
/*     */ import mods.helpfulvillagers.util.ResourceCluster;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
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
/*     */ public class EntityAILumberjack
/*     */   extends EntityAIWorker
/*     */ {
/*     */   private EntityLumberjack lumberjack;
/*     */   private int searchLimit;
/*     */   
/*     */   public EntityAILumberjack(EntityLumberjack lumberjack) {
/*  41 */     super((AbstractVillager)lumberjack);
/*  42 */     this.lumberjack = lumberjack;
/*  43 */     this.currentTime = 0;
/*  44 */     this.previousTime = 0;
/*  45 */     this.harvestTime = 0.0F;
/*  46 */     this.searchLimit = 20;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean gather() {
/*  56 */     if (this.lumberjack.homeGuildHall == null) {
/*  57 */       return idle();
/*     */     }
/*  59 */     if (this.lumberjack.insideHall()) {
/*  60 */       BlockPos exit = this.lumberjack.homeGuildHall.entranceCoords;
/*  61 */       if (exit == null) {
/*  62 */         exit = AIHelper.getRandInsideCoords((AbstractVillager)this.lumberjack);
/*     */       }
/*  64 */       this.lumberjack.moveTo(exit, this.speed);
/*     */     }
/*  66 */     else if (this.lumberjack.currentResource == null) {
/*  67 */       findTree();
/*     */     } else {
/*  69 */       int distX = AIHelper.findDistance((int)this.lumberjack.posX, this.lumberjack.currentResource.coords.getX());
/*  70 */       int distZ = AIHelper.findDistance((int)this.lumberjack.posZ, this.lumberjack.currentResource.coords.getZ());
/*  71 */       if (distX > 5 || distZ > 5) {
/*  72 */         moveToTree();
/*     */       } else {
/*  74 */         chopTree();
/*     */       } 
/*     */     } 
/*     */     
/*  78 */     return idle();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void findTree() {
/*  87 */     if (this.target == null) {
/*  88 */       this.target = AIHelper.getRandOutsideCoords((AbstractVillager)this.lumberjack, this.searchLimit);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  93 */     if (this.target != null) {
/*  94 */       this.lumberjack.moveTo(this.target, this.speed);
/*     */     }
/*     */ 
/*     */     
/*  98 */     if (this.lumberjack.searchBox != null && this.lumberjack.worldObj.isMaterialInBB(this.lumberjack.searchBox, Material.wood) && 
/*  99 */       !AIHelper.isInRangeOfAnyVillage(this.lumberjack.posX, this.lumberjack.posY, this.lumberjack.posZ)) {
/* 100 */       this.lumberjack.currentResource = getNewResource();
/* 101 */       if (this.lumberjack.currentResource != null) {
/*     */         
/* 103 */         this.searchLimit = 20;
/* 104 */         this.lumberjack.foundTree = true;
/* 105 */         this.lumberjack.getNavigator().clearPathEntity();
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 114 */     if (Math.abs(this.lumberjack.posX - this.target.getX()) <= 5.0D && Math.abs(this.lumberjack.posZ - this.target.getZ()) <= 5.0D) {
/* 115 */       this.target = null;
/* 116 */       this.searchLimit += 10;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void moveToTree() {
/* 124 */     this.target = this.lumberjack.currentResource.lowestCoords;
/* 125 */     this.lumberjack.moveTo(this.target, this.speed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ResourceCluster getNewResource() {
/* 134 */     ArrayList<BlockPos> boxCoords = this.lumberjack.getValidCoords();
/* 135 */     double closestDist = Double.MAX_VALUE;
/* 136 */     ResourceCluster closestValidCluster = null;
/*     */     
/* 138 */     for (int i = 0; i < boxCoords.size(); i++) {
/* 139 */       BlockPos currCoords = boxCoords.get(i);
/* 140 */       double dist = this.lumberjack.getDistance(currCoords.getX(), currCoords.getY(), currCoords.getZ());
/* 141 */       if (dist < closestDist) {
/* 142 */         ResourceCluster currentCluster = new ResourceCluster(this.lumberjack.worldObj, boxCoords.get(i));
/* 143 */         ArrayList<Block> sideBlocks = currentCluster.getAdjacent();
/* 144 */         for (int j = 0; j < sideBlocks.size(); j++) {
/* 145 */           Block currentBlock = sideBlocks.get(j);
/* 146 */           if (currentBlock.equals(Blocks.cobblestone) || currentBlock.equals(Blocks.planks) || currentBlock.equals(Blocks.farmland) || currentBlock
/* 147 */             .equals(Blocks.oak_fence) || currentBlock.equals(Blocks.oak_fence_gate) || currentBlock.equals(Blocks.oak_door) || currentBlock
/* 148 */             .equals(Blocks.iron_door) || currentBlock.equals(Blocks.bookshelf) || currentBlock.equals(Blocks.chest) || currentBlock
/* 149 */             .equals(Blocks.crafting_table) || currentBlock instanceof net.minecraft.block.BlockStairs) {
/* 150 */             currentCluster = null;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 155 */         if (currentCluster != null) {
/* 156 */           closestValidCluster = currentCluster;
/* 157 */           closestDist = dist;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 162 */     return closestValidCluster;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void chopTree() {
/* 171 */     this.lumberjack.moveTo(this.lumberjack.currentResource.lowestCoords, this.speed);
/* 172 */     boolean shouldSwing = false;
/*     */ 
/*     */     
/* 175 */     if (!this.lumberjack.currentResource.builtFlag) {
/* 176 */       this.lumberjack.currentResource.buildCluster();
/* 177 */       this.lumberjack.currentResource.builtFlag = true;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     if (this.lumberjack.getNavigator().noPath()) {
/* 184 */       this.lumberjack.getLookHelper().setLookPosition(this.lumberjack.currentResource.lowestCoords.getX(), this.lumberjack.currentResource.lowestCoords.getY(), this.lumberjack.currentResource.lowestCoords.getZ(), 10.0F, 10.0F);
/* 185 */       shouldSwing = true;
/*     */ 
/*     */       
/* 188 */       if (this.previousTime <= 0) {
/* 189 */         this.previousTime = this.lumberjack.ticksExisted;
/* 190 */         this.harvestTime = 60.0F / this.lumberjack.getCurrentItem().getItem().getDigSpeed(this.lumberjack.getCurrentItem(), this.lumberjack.currentResource.startBlock.getDefaultState());
/*     */       } 
/*     */     } else {
/* 193 */       shouldSwing = false;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 198 */     if (this.previousTime > 0) {
/* 199 */       this.currentTime = this.lumberjack.ticksExisted;
/* 200 */       if (!this.lumberjack.currentResource.blockCluster.isEmpty()) {
/* 201 */         if ((this.currentTime - this.previousTime) >= this.harvestTime) {
/* 202 */           this.previousTime = this.currentTime;
/* 203 */           this.harvestTime = 60.0F / this.lumberjack.getCurrentItem().getItem().getDigSpeed(this.lumberjack.getCurrentItem(), this.lumberjack.currentResource.startBlock.getDefaultState());
/* 204 */           BlockPos currentCoords = (BlockPos) this.lumberjack.currentResource.blockCluster.get(0);
/* 205 */           Block currentBlock = this.lumberjack.worldObj.getBlockState(currentCoords).getBlock();
/* 206 */           //int metadata = this.lumberjack.worldObj.getBlockState(currentCoords);
/* 207 */           if (Block.getIdFromBlock(currentBlock) == Block.getIdFromBlock(this.lumberjack.currentResource.startBlock)) {
/* 208 */             this.lumberjack.currentResource.blockCluster.remove(0);
/* 209 */             AIHelper.breakBlock(currentCoords, (AbstractVillager)this.lumberjack);
/*     */           } 
/*     */         } 
/*     */       } else {
/* 213 */         this.lumberjack.lastResource = this.lumberjack.currentResource;
/* 214 */         this.lumberjack.currentResource = null;
/* 215 */         this.target = null;
/* 216 */         this.previousTime = 0;
/* 217 */         this.currentTime = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 221 */     if (shouldSwing) {
/* 222 */       this.lumberjack.swingItem();
/*     */     } else {
/* 224 */       this.previousTime = 0;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\ai\EntityAILumberjack.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */