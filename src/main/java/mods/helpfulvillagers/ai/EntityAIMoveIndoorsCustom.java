/*     */ package mods.helpfulvillagers.ai;
/*     */ 
/*     */ import java.util.Random;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.enums.EnumActivity;
/*     */ import mods.helpfulvillagers.util.AIHelper;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockDoor;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.RandomPositionGenerator;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.village.VillageDoorInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityAIMoveIndoorsCustom
/*     */   extends EntityAIBase
/*     */ {
/*     */   private AbstractVillager entityObj;
/*     */   private VillageDoorInfo doorInfo;
/*  31 */   private int insidePosX = -1;
/*  32 */   private int insidePosZ = -1;
/*     */   protected BlockDoor targetDoor;
/*     */   private BlockPos destination;
/*     */   private Random gen;
/*     */   private float speed;
/*     */   
/*     */   public EntityAIMoveIndoorsCustom(AbstractVillager abstractEntity) {
/*  39 */     this.entityObj = abstractEntity;
/*  40 */     setMutexBits(1);
/*  41 */     this.gen = new Random();
/*  42 */     this.speed = 0.5F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  51 */     if (this.entityObj.homeGuildHall != null && this.entityObj.currentActivity == EnumActivity.RETURN) {
/*  52 */       this.destination = new BlockPos(this.entityObj.homeGuildHall.doorCoords.getX(), this.entityObj.homeGuildHall.doorCoords.getY(), this.entityObj.homeGuildHall.doorCoords.getZ());
/*  53 */       return !this.entityObj.nearHall();
/*     */     } 
/*     */     
/*  56 */     if (!(this.entityObj instanceof mods.helpfulvillagers.entity.EntitySoldier) && !(this.entityObj instanceof mods.helpfulvillagers.entity.EntityArcher) && (!this.entityObj.worldObj.isDaytime() || this.entityObj.worldObj.isRaining()) && !this.entityObj.worldObj.provider.getHasNoSky()) {
/*  57 */       if (this.entityObj.getRNG().nextInt(50) != 0)
/*  58 */         return false; 
/*  59 */       if (this.insidePosX != -1 && this.entityObj.getDistanceSq(this.insidePosX, this.entityObj.posY, this.insidePosZ) < 4.0D) {
/*  60 */         return false;
/*     */       }
/*  62 */       if (this.entityObj.homeVillage == null) {
/*  63 */         return false;
/*     */       }
/*  65 */       this.doorInfo = this.entityObj.homeVillage.findNearestDoorUnrestricted(MathHelper.floor_double(this.entityObj.posX), MathHelper.floor_double(this.entityObj.posY), MathHelper.floor_double(this.entityObj.posZ));
/*  66 */       return (this.doorInfo != null);
/*     */     } 
/*     */ 
/*     */     
/*  70 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean continueExecuting() {
/*  79 */     if (!this.entityObj.homeVillage.isInsideVillage(this.entityObj.posX, this.entityObj.posY, this.entityObj.posZ)) {
/*  80 */       this.speed = 0.75F;
/*     */     } else {
/*  82 */       this.speed = 0.5F;
/*     */     } 
/*     */     
/*  85 */     if (this.entityObj.homeGuildHall != null && this.entityObj.currentActivity == EnumActivity.RETURN) {
/*  86 */       if (this.entityObj.shouldReturn()) {
/*  87 */         this.entityObj.currentActivity = EnumActivity.IDLE;
/*  88 */         return false;
/*     */       } 
/*  90 */       BlockPos currentPosition = new BlockPos((int)this.entityObj.posX, (int)this.entityObj.posY, (int)this.entityObj.posZ);
/*     */ 
/*     */ 
/*     */       
/*  94 */       this.entityObj.moveTo(this.destination, this.speed);
/*     */       
/*  96 */       if (this.entityObj.nearHall()) {
/*  97 */         int distX = AIHelper.findDistance((int)this.entityObj.posX, this.destination.getX());
/*  98 */         int distZ = AIHelper.findDistance((int)this.entityObj.posZ, this.destination.getZ());
/*     */         
/* 100 */         if (distX < 1 || distZ < 1 || this.entityObj.getNavigator().noPath()) {
/* 101 */           this.entityObj.currentActivity = EnumActivity.IDLE;
/* 102 */           return false;
/*     */         } 
/* 104 */         return true;
/*     */       } 
/*     */       
/* 107 */       return true;
/*     */     } 
/*     */     
/* 110 */     return !this.entityObj.getNavigator().noPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/* 120 */     this.insidePosX = -1;
/*     */     
/* 122 */     if (this.entityObj.homeGuildHall != null && this.entityObj.currentActivity == EnumActivity.RETURN) {
/*     */       
/* 124 */       if ((new BlockPos((int)this.entityObj.posX + 1, (int)this.entityObj.posY, (int)this.entityObj.posZ)).equals(this.entityObj.homeGuildHall.doorCoords)) {
/* 125 */         this.targetDoor = findUsableDoor((int)this.entityObj.posX + 1, (int)this.entityObj.posY, (int)this.entityObj.posZ);
/*     */       }
/* 127 */       else if ((new BlockPos((int)this.entityObj.posX - 1, (int)this.entityObj.posY, (int)this.entityObj.posZ)).equals(this.entityObj.homeGuildHall.doorCoords)) {
/* 128 */         this.targetDoor = findUsableDoor((int)this.entityObj.posX - 1, (int)this.entityObj.posY, (int)this.entityObj.posZ);
/*     */       }
/* 130 */       else if ((new BlockPos((int)this.entityObj.posX, (int)this.entityObj.posY, (int)this.entityObj.posZ + 1)).equals(this.entityObj.homeGuildHall.doorCoords)) {
/* 131 */         this.targetDoor = findUsableDoor((int)this.entityObj.posX, (int)this.entityObj.posY, (int)this.entityObj.posZ + 1);
/*     */       }
/* 133 */       else if ((new BlockPos((int)this.entityObj.posX, (int)this.entityObj.posY, (int)this.entityObj.posZ - 1)).equals(this.entityObj.homeGuildHall.doorCoords)) {
/* 134 */         this.targetDoor = findUsableDoor((int)this.entityObj.posX, (int)this.entityObj.posY, (int)this.entityObj.posZ - 1);
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/* 141 */     else if (this.entityObj.getDistanceSq(this.doorInfo.getInsideBlockPos()) > 256.0D) {
				Vec3 helper3 = new Vec3(this.doorInfo.getInsideBlockPos().add(0.5, 0, 0.5));
/* 142 */       Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards((EntityCreature)this.entityObj, 14, 3, helper3);
/* 143 */       if (vec3 != null) {
/* 144 */         this.entityObj.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, this.speed);
/*     */       }
/*     */     } else {
/*     */       
/* 148 */       this.entityObj.getNavigator().tryMoveToXYZ(this.doorInfo.getInsideBlockPos().add(0.5, 0, 0.5).getX(), this.doorInfo.getInsideBlockPos().add(0.5, 0, 0.5).getY(), this.doorInfo.getInsideBlockPos().add(0.5, 0, 0.5).getZ(), this.speed);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 156 */     if (this.entityObj.homeGuildHall == null) {
/*     */       
/* 158 */       if (!this.entityObj.worldObj.isRemote && this.entityObj.homeVillage != null) {
/* 159 */         this.doorInfo = this.entityObj.homeVillage.findNearestDoorUnrestricted(MathHelper.floor_double(this.entityObj.posX), MathHelper.floor_double(this.entityObj.posY), MathHelper.floor_double(this.entityObj.posZ));
/* 160 */         if (this.doorInfo != null) {
/* 161 */           this.insidePosX = this.doorInfo.getInsideBlockPos().getX();
/* 162 */           this.insidePosZ = this.doorInfo.getInsideBlockPos().getZ();
/* 163 */           this.doorInfo = null;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 167 */       this.entityObj.getNavigator().tryMoveToXYZ((this.entityObj.homeVillage.getActualCenter()).getX(), (this.entityObj.homeVillage.getActualCenter()).getY(), (this.entityObj.homeVillage.getActualCenter()).getZ(), this.speed);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BlockDoor findUsableDoor(int par1, int par2, int par3) {
				BlockPos posl = new BlockPos(par1, par2, par3);
/* 179 */     Block l = this.entityObj.worldObj.getBlockState(posl).getBlock();
/* 180 */     return (l != Blocks.oak_door) ? null : (BlockDoor)l;
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\ai\EntityAIMoveIndoorsCustom.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */