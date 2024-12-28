/*     */ package mods.helpfulvillagers.ai;
/*     */ 
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.entity.EntityArcher;
/*     */ import mods.helpfulvillagers.enums.EnumActivity;
/*     */ import mods.helpfulvillagers.util.AIHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.DamageSource;
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
/*     */ public class EntityAIFollowLeader
/*     */   extends EntityAIBase
/*     */ {
/*     */   private AbstractVillager villager;
/*     */   private EntityLivingBase leader;
/*     */   private EntityLivingBase threatTarget;
/*     */   private int count;
/*     */   private float speed;
/*     */   private int previousTime;
/*     */   private int currentTime;
/*     */   
/*     */   public EntityAIFollowLeader(AbstractVillager abstractEntity) {
/*  43 */     this.villager = abstractEntity;
/*  44 */     this.speed = 0.8F;
/*  45 */     setMutexBits(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  53 */     this.leader = this.villager.getLeader();
/*  54 */     return (this.leader != null);
/*     */   }
/*     */   
/*     */   public void startExecuting() {
/*  58 */     this.count = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  65 */     if (AIHelper.findDistance((int)this.villager.posX, (int)this.leader.posX) <= 1 && AIHelper.findDistance((int)this.villager.posY, (int)this.leader.posY) <= 1 && AIHelper.findDistance((int)this.villager.posZ, (int)this.leader.posZ) <= 1) {
/*  66 */       this.villager.getNavigator().clearPathEntity();
/*  67 */     } else if (--this.count <= 0) {
/*  68 */       this.count = 10;
/*  69 */       this.speed = 0.8F;
/*  70 */       this.villager.moveTo((Entity)this.leader, this.speed);
/*     */     } 
/*     */ 
/*     */     
/*  74 */     if (this.villager instanceof mods.helpfulvillagers.entity.EntitySoldier || (this.villager instanceof EntityArcher && this.villager.inventory.containsItem(new ItemStack(Items.arrow)) < 0)) {
/*  75 */       protectLeaderMelee();
/*  76 */     } else if (this.villager instanceof EntityArcher) {
/*  77 */       protectLeaderRanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean continueExecuting() {
/*  85 */     this.leader = this.villager.getLeader();
/*  86 */     if (this.leader == null || (this.leader != null && !this.leader.isEntityAlive())) {
/*  87 */       return false;
/*     */     }
/*  89 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetTask() {
/*  97 */     this.speed = 0.8F;
/*  98 */     this.villager.getNavigator().tryMoveToXYZ(this.villager.posX, this.villager.posY, this.villager.posZ, 0.30000001192092896D);
/*  99 */     this.villager.setLeader(null);
/* 100 */     this.villager.currentActivity = EnumActivity.IDLE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void protectLeaderMelee() {
/* 107 */     if (this.villager.getAITarget() != null && this.villager.getAITarget().isEntityAlive() && this.villager.getAITarget() instanceof net.minecraft.entity.monster.IMob) {
/* 108 */       this.threatTarget = this.villager.getAITarget();
/* 109 */     } else if (this.leader.getAITarget() != null && this.leader.getAITarget().isEntityAlive() && this.leader.getAITarget() instanceof net.minecraft.entity.monster.IMob) {
/* 110 */       this.threatTarget = this.leader.getAITarget();
/*     */     } else {
/* 112 */       this.threatTarget = null;
/*     */     } 
/*     */     
/* 115 */     if (this.threatTarget != null) {
/* 116 */       boolean canMove = this.villager.getNavigator().tryMoveToEntityLiving((Entity)this.threatTarget, this.speed);
/* 117 */       if (!canMove) {
/* 118 */         this.villager.getNavigator().tryMoveToEntityLiving((Entity)this.leader, this.speed);
/*     */       }
/*     */       
/* 121 */       if (this.villager.getDistanceSqToEntity((Entity)this.threatTarget) <= 5.0D) {
/* 122 */         this.villager.getNavigator().clearPathEntity();
/* 123 */         this.villager.swingItem();
/* 124 */         boolean attackSuccess = this.threatTarget.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this.villager), this.villager.getAttackDamage());
/* 125 */         if (attackSuccess) {
/* 126 */           this.villager.damageItem();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void protectLeaderRanged() {
/* 136 */     if (this.villager.getAITarget() != null && this.villager.getAITarget().isEntityAlive() && this.villager.getAITarget() instanceof net.minecraft.entity.monster.IMob) {
/* 137 */       this.threatTarget = this.villager.getAITarget();
/* 138 */     } else if (this.leader.getAITarget() != null && this.leader.getAITarget().isEntityAlive() && this.leader.getAITarget() instanceof net.minecraft.entity.monster.IMob) {
/* 139 */       this.threatTarget = this.leader.getAITarget();
/*     */     } else {
/* 141 */       this.threatTarget = null;
/*     */     } 
/*     */     
/* 144 */     EntityArcher archer = (EntityArcher)this.villager;
/* 145 */     if (this.threatTarget != null) {
/*     */       
/* 147 */       boolean canMove = this.villager.getNavigator().tryMoveToEntityLiving((Entity)this.threatTarget, this.speed);
/* 148 */       if (!canMove) {
/* 149 */         this.villager.getNavigator().tryMoveToEntityLiving((Entity)this.leader, this.speed);
/*     */       }
/*     */       
/* 152 */       if (archer.canEntityBeSeen((Entity)this.threatTarget)) {
/* 153 */         archer.getNavigator().clearPathEntity();
/* 154 */         archer.getLookHelper().setLookPositionWithEntity((Entity)this.threatTarget, 30.0F, 30.0F);
/* 155 */         if (this.previousTime < 0)
/* 156 */         { this.previousTime = archer.ticksExisted; }
/* 157 */         else { archer.getClass(); if (this.currentTime - this.previousTime >= 20) {
/* 158 */             if (!archer.worldObj.isRemote) {
/* 159 */               EntityArrow arrow = new EntityArrow(archer.worldObj, (EntityLivingBase)archer, this.threatTarget, 1.6F, 2.0F);
/* 160 */               arrow.canBePickedUp = 1;
/* 161 */               archer.worldObj.spawnEntityInWorld((Entity)arrow);
/*     */             } 
/*     */             
/* 164 */             archer.worldObj.playSoundAtEntity((Entity)archer, "random.bow", 1.0F, 1.0F / (archer.getRNG().nextFloat() * 0.4F + 0.8F));
/* 165 */             archer.damageItem();
/* 166 */             archer.inventory.decrementSlot(archer.inventory.containsItem(new ItemStack(Items.arrow)));
/*     */             
/* 168 */             this.previousTime = -1;
/*     */           } else {
/* 170 */             this.currentTime = archer.ticksExisted;
/*     */           }  }
/*     */       
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\ai\EntityAIFollowLeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */