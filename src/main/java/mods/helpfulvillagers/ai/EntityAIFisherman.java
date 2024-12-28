/*     */ package mods.helpfulvillagers.ai;
/*     */ 
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.entity.EntityFishHookCustom;
/*     */ import mods.helpfulvillagers.entity.EntityFisherman;
/*     */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*     */ import mods.helpfulvillagers.network.FishHookPacket;
/*     */ import mods.helpfulvillagers.util.AIHelper;
/*     */ import mods.helpfulvillagers.util.ResourceCluster;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.passive.EntitySquid;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityAIFisherman
/*     */   extends EntityAIWorker
/*     */ {
/*     */   private EntityFisherman fisherman;
/*     */   private int searchLimit;
/*     */   private Random rand;
/*     */   
/*     */   public EntityAIFisherman(EntityFisherman fisherman) {
/*  34 */     super((AbstractVillager)fisherman);
/*  35 */     this.fisherman = fisherman;
/*  36 */     this.target = null;
/*  37 */     this.currentTime = 0;
/*  38 */     this.previousTime = 0;
/*  39 */     this.harvestTime = 20.0F;
/*  40 */     this.searchLimit = 20;
/*  41 */     this.rand = new Random();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean gather() {
/*  47 */     if (this.fisherman.homeGuildHall == null) {
/*  48 */       return idle();
/*     */     }
/*  50 */     if (this.fisherman.insideHall()) {
/*  51 */       BlockPos exit = this.fisherman.homeGuildHall.entranceCoords;
/*  52 */       if (exit == null) {
/*  53 */         exit = AIHelper.getRandInsideCoords((AbstractVillager)this.fisherman);
/*     */       }
/*  55 */       this.fisherman.moveTo(exit, this.speed);
/*     */     }
/*  57 */     else if (this.fisherman.currentResource == null) {
/*  58 */       findWater();
/*     */     } else {
/*  60 */       int distX = AIHelper.findDistance((int)this.fisherman.posX, this.fisherman.currentResource.coords.getX());
/*  61 */       int distY = AIHelper.findDistance((int)this.fisherman.posY, this.fisherman.currentResource.coords.getY());
/*  62 */       int distZ = AIHelper.findDistance((int)this.fisherman.posZ, this.fisherman.currentResource.coords.getZ());
/*  63 */       this.target = this.fisherman.currentResource.coords;
/*  64 */       if (distX > 3 || distY > 3 || distZ > 3) {
/*  65 */         this.fisherman.moveTo(this.target, this.speed);
/*     */       } else {
/*  67 */         fish();
/*     */       } 
/*     */     } 
/*     */     
/*  71 */     return idle();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void findWater() {
/*  79 */     if (this.target == null) {
/*  80 */       this.target = AIHelper.getRandOutsideCoords((AbstractVillager)this.fisherman, this.searchLimit);
/*     */     }
/*     */ 
/*     */     
/*  84 */     if (this.target != null) {
/*  85 */       this.fisherman.moveTo(this.target, this.speed);
/*     */     }
/*     */     
/*  88 */     if (!AIHelper.isInRangeOfAnyVillage(this.fisherman.posX, this.fisherman.posY, this.fisherman.posZ)) {
/*  89 */       this.fisherman.updateBoxes();
/*  90 */       if (this.fisherman.searchBox != null && this.fisherman.worldObj.isMaterialInBB(this.fisherman.searchBox, Material.water)) {
/*  91 */         this.fisherman.currentResource = getNewResource();
/*  92 */         if (this.fisherman.currentResource != null) {
/*  93 */           this.fisherman.currentResource.buildCluster(5);
/*  94 */           this.fisherman.getNavigator().clearPathEntity();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 100 */     if (Math.abs(this.fisherman.posX - this.target.getX()) <= 5.0D && Math.abs(this.fisherman.posZ - this.target.getZ()) <= 5.0D) {
/* 101 */       this.target = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ResourceCluster getNewResource() {
/* 111 */     ArrayList<BlockPos> boxCoords = this.fisherman.getValidCoords();
/* 112 */     double closestDist = Double.MAX_VALUE;
/* 113 */     ResourceCluster closestValidCluster = null;
/*     */     
/* 115 */     for (int i = 0; i < boxCoords.size(); i++) {
/* 116 */       BlockPos currCoords = boxCoords.get(i);
/* 117 */       double dist = this.fisherman.getDistance(currCoords.getX(), currCoords.getY(), currCoords.getZ());
/* 118 */       if (dist < closestDist) {
/* 119 */         ResourceCluster currentCluster = new ResourceCluster(this.fisherman.worldObj, boxCoords.get(i));
/*     */         
/* 121 */         if (currentCluster != null) {
/* 122 */           closestValidCluster = currentCluster;
/* 123 */           closestDist = dist;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 128 */     return closestValidCluster;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fish() {
/* 135 */     if (this.fisherman.fishEntity == null && this.fisherman.currentResource.blockCluster.size() > 0) {
/* 136 */       if (this.previousTime <= 0) {
/* 137 */         this.previousTime = this.fisherman.ticksExisted;
/*     */       }
/*     */       
/* 140 */       if (this.previousTime > 0) {
/* 141 */         this.currentTime = this.fisherman.ticksExisted;
/* 142 */         if ((this.currentTime - this.previousTime) >= this.harvestTime) {
/* 143 */           int index = this.rand.nextInt(this.fisherman.currentResource.blockCluster.size());
/* 144 */           BlockPos coords = (BlockPos) this.fisherman.currentResource.blockCluster.get(index);
/* 145 */           this.fisherman.fishEntity = new EntityFishHookCustom(this.fisherman.worldObj, coords.getX(), coords.getY(), coords.getZ(), this.fisherman);
/* 146 */           this.fisherman.swingItem();
/* 147 */           this.fisherman.worldObj.spawnEntityInWorld((Entity)this.fisherman.fishEntity);
/* 148 */           HelpfulVillagers.network.sendToAll((IMessage)new FishHookPacket(this.fisherman.getEntityId(), false, coords.getX(), coords.getY(), coords.getZ()));
/* 149 */           this.previousTime = 0;
/* 150 */           this.currentTime = 0;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 155 */     List<EntitySquid> squids = this.fisherman.worldObj.getEntitiesWithinAABB(EntitySquid.class, this.fisherman.searchBox);
/* 156 */     for (EntitySquid squid : squids) {
/* 157 */       if (!this.fisherman.harvestedSquids.contains(squid)) {
/* 158 */         this.fisherman.inventory.addItem(new ItemStack(Items.dye, 1, 0));
/* 159 */         this.fisherman.harvestedSquids.add(squid);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\ai\EntityAIFisherman.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */