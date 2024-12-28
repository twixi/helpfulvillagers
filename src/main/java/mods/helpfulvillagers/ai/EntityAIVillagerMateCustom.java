/*     */ package mods.helpfulvillagers.ai;
/*     */ 
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*     */ import java.util.Random;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.enums.EnumMessage;
/*     */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*     */ import mods.helpfulvillagers.network.PlayerMessagePacket;
/*     */ import mods.helpfulvillagers.village.HelpfulVillage;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityAgeable;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.passive.EntityVillager;
/*     */ import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityAIVillagerMateCustom
/*     */   extends EntityAIBase
/*     */ {
/*     */   private AbstractVillager villagerObj;
/*     */   private AbstractVillager mate;
/*     */   private World worldObj;
/*     */   private int matingTimeout;
/*  30 */   private Random gen = new Random();
/*     */   private HelpfulVillage villageObj;
/*     */   
/*     */   public EntityAIVillagerMateCustom(AbstractVillager villager) {
/*  34 */     this.villagerObj = villager;
/*  35 */     this.worldObj = villager.worldObj;
/*  36 */     setMutexBits(3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  46 */     if (!this.villagerObj.shouldReproduce()) {
/*  47 */       return false;
/*     */     }
/*     */     
/*  50 */     if (this.villagerObj.getGrowingAge() != 0) {
/*  51 */       return false;
/*     */     }
/*  53 */     this.villageObj = this.villagerObj.homeVillage;
/*     */     
/*  55 */     if (this.villageObj == null || this.worldObj.isRemote)
/*  56 */       return false; 
/*  57 */     if (!checkSufficientHallssPresentForNewVillager()) {
/*  58 */       return false;
/*     */     }
/*  60 */     Entity entity = this.worldObj.findNearestEntityWithinAABB(AbstractVillager.class, this.villagerObj.getEntityBoundingBox().expand(8.0D, 3.0D, 8.0D), (Entity)this.villagerObj);
/*  61 */     AbstractVillager entityV = (AbstractVillager)entity;
/*  62 */     if (entity == null || !entity.isEntityAlive() || entityV.isChild()) {
/*  63 */       return false;
/*     */     }
/*  65 */     this.mate = (AbstractVillager)entity;
/*  66 */     return (this.mate.getGrowingAge() == 0 && (this.mate.leader == null || this.mate.leader == this.villagerObj));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  77 */     this.matingTimeout = this.gen.nextInt(50) + 300;
/*  78 */     this.villagerObj.setMating(true);
/*  79 */     this.mate.setMating(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetTask() {
/*  87 */     this.villagerObj.setMating(false);
/*  88 */     this.mate.setMating(false);
/*  89 */     this.villageObj = null;
/*  90 */     this.mate = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean continueExecuting() {
/*  98 */     return (this.villagerObj.shouldReproduce() && this.matingTimeout >= 0 && checkSufficientHallssPresentForNewVillager() && this.villagerObj.getGrowingAge() == 0 && this.mate != null && this.mate.isEntityAlive());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTask() {
/* 106 */     this.matingTimeout--;
/* 107 */     this.villagerObj.getLookHelper().setLookPositionWithEntity((Entity)this.mate, 10.0F, 30.0F);
/*     */     
/* 109 */     if (this.villagerObj.getDistanceSqToEntity((Entity)this.mate) > 2.25D) {
/* 110 */       this.villagerObj.getNavigator().tryMoveToEntityLiving((Entity)this.mate, 0.25D);
/* 111 */     } else if (this.matingTimeout == 0 && this.mate.isMating()) {
/* 112 */       giveBirth();
/*     */     } 
/*     */     
/* 115 */     if (this.villagerObj.getRNG().nextInt(35) == 0) {
/* 116 */       this.worldObj.setEntityState((Entity)this.villagerObj, (byte)12);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkSufficientHallssPresentForNewVillager() {
/*     */     int i;
/* 126 */     if (this.villageObj.guildHallList != null && this.villageObj.guildHallList.size() <= 0) {
/* 127 */       i = 3;
/*     */     } else {
/* 129 */       i = this.villageObj.guildHallList.size() * 2 + 1;
/*     */     } 
/*     */     
/* 132 */     return (this.villageObj.getPopulation() < i);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void giveBirth() {
/*     */     int prob;
/*     */     String message;
/* 140 */     this.mate.setGrowingAge(6000);
/* 141 */     this.villagerObj.setGrowingAge(6000);
/*     */     
/* 143 */     if (!this.villagerObj.shouldReproduce()) {
/*     */       return;
/*     */     }
/*     */     
/* 147 */     int children = 1;
/*     */     
/* 149 */     if (bedCheck()) {
/* 150 */       prob = this.gen.nextInt(100);
/*     */     } else {
/* 152 */       prob = this.gen.nextInt(1000);
/*     */     } 
/*     */     
/* 155 */     if (prob <= 20) {
/* 156 */       children++;
/* 157 */       if (prob <= 10) {
/* 158 */         children++;
/*     */       }
/*     */     } 
/*     */     
/* 162 */     for (int i = 0; i < children; i++) {
/* 163 */       EntityVillager entityvillager = this.villagerObj.createChild((EntityAgeable)this.mate);
/* 164 */       entityvillager.setGrowingAge(-24000);
/* 165 */       entityvillager.setLocationAndAngles(this.villagerObj.posX, this.villagerObj.posY, this.villagerObj.posZ, 0.0F, 0.0F);
/* 166 */       this.worldObj.spawnEntityInWorld((Entity)entityvillager);
/* 167 */       this.worldObj.setEntityState((Entity)entityvillager, (byte)12);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 172 */     switch (children) {
/*     */       case 1:
/* 174 */         message = "A villager was born";
/*     */         break;
/*     */       case 2:
/* 177 */         message = "Twin villagers were born";
/*     */         break;
/*     */       case 3:
/* 180 */         message = "Triplet villagers were born";
/*     */         break;
/*     */       default:
/* 183 */         message = "A villager was born";
/*     */         break;
/*     */     } 
/* 186 */     HelpfulVillagers.network.sendToAll((IMessage)new PlayerMessagePacket(message, EnumMessage.BIRTH, this.villagerObj.getEntityId()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean bedCheck() {
/* 194 */     AxisAlignedBB box = this.villagerObj.getEntityBoundingBox().expand(3.0D, 3.0D, 3.0D);
/* 195 */     for (int i = (int)box.minX; i < box.maxX; i++) {
/* 196 */       for (int j = (int)box.minY; j < box.maxY; j++) {
/* 197 */         for (int k = (int)box.minZ; k < box.maxZ; k++) {
					BlockPos newPos0 = new BlockPos(i, j, k);
/* 198 */           Block block = this.villagerObj.worldObj.getBlockState(newPos0).getBlock();
/* 199 */           if (block instanceof net.minecraft.block.BlockBed) {
/* 200 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 205 */     return false;
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\ai\EntityAIVillagerMateCustom.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */