/*     */ package mods.helpfulvillagers.ai;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import mods.helpfulvillagers.crafting.CraftItem;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.entity.EntitySoldier;
/*     */ import mods.helpfulvillagers.enums.EnumActivity;
/*     */ import mods.helpfulvillagers.util.AIHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.ai.EntityAITarget;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.util.BlockPos;
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
/*     */ public class EntityAIGuardVillageSoldier
/*     */   extends EntityAITarget
/*     */ {
/*     */   private EntitySoldier soldier;
/*     */   private EntityLivingBase villageAgressorTarget;
/*     */   private float speed;
/*     */   
/*     */   public EntityAIGuardVillageSoldier(EntitySoldier soldier) {
/*  35 */     super((EntityCreature)soldier, false, false);
/*  36 */     this.soldier = soldier;
/*  37 */     this.speed = 0.75F;
/*  38 */     setMutexBits(2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  47 */     if (this.soldier.currentActivity == EnumActivity.RETURN || this.soldier.currentActivity == EnumActivity.FOLLOW) {
/*  48 */       return false;
/*     */     }
/*     */     
/*  51 */     if (this.soldier.getHealth() < this.soldier.getHealth() / 2.0F) {
/*  52 */       this.soldier.currentActivity = EnumActivity.STORE;
/*  53 */       return true;
/*     */     } 
/*     */     
/*  56 */     if (this.soldier.getAITarget() != null && this.soldier.getAITarget().isEntityAlive() && this.soldier.getAITarget() instanceof net.minecraft.entity.monster.IMob) {
/*  57 */       this.villageAgressorTarget = this.soldier.getAITarget();
/*  58 */       return true;
/*     */     } 
/*     */     
/*  61 */     if (!this.soldier.worldObj.isRemote && this.soldier.homeVillage != null) {
/*  62 */       this.villageAgressorTarget = this.soldier.homeVillage.findNearestVillageAggressor((EntityLivingBase)this.soldier);
/*  63 */       if (this.villageAgressorTarget != null) {
/*  64 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  68 */     if (!this.soldier.hasTool) {
/*  69 */       this.soldier.currentActivity = EnumActivity.STORE;
/*  70 */       return true;
/*     */     } 
/*     */     
/*  73 */     return false;
/*     */   }
/*     */   
/*     */   public void startExecuting() {
/*  77 */     this.soldier.setAttackTarget(this.villageAgressorTarget);
/*  78 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean continueExecuting() {
/*  85 */     if (this.soldier.currentActivity == EnumActivity.RETURN || this.soldier.currentActivity == EnumActivity.FOLLOW) {
/*  86 */       return false;
/*     */     }
/*     */     
/*  89 */     if (this.soldier.currentActivity == EnumActivity.STORE) {
/*  90 */       if (this.soldier.getHealth() < this.soldier.getHealth() / 2.0F || !this.soldier.hasTool) {
/*  91 */         return true;
/*     */       }
/*  93 */       return false;
/*     */     } 
/*     */ 
/*     */     
/*  97 */     if (this.villageAgressorTarget != null && this.villageAgressorTarget.isEntityAlive()) {
/*  98 */       return true;
/*     */     }
/* 100 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTask() {
/* 108 */     if (this.soldier.currentActivity == EnumActivity.STORE) {
/* 109 */       resupply();
/*     */     } else {
/* 111 */       attack();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resupply() {
/* 119 */     if (this.soldier.homeGuildHall == null) {
/*     */       
/* 121 */       this.soldier.currentActivity = EnumActivity.IDLE; return;
/*     */     } 
/* 123 */     if (!this.soldier.nearHall()) {
/*     */       
/* 125 */       this.soldier.currentActivity = EnumActivity.RETURN;
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 130 */     this.villageAgressorTarget = this.soldier.homeVillage.findNearestVillageAggressor((EntityLivingBase)this.soldier);
/* 131 */     if (this.soldier.getHealth() >= this.soldier.getHealth() / 2.0F && this.villageAgressorTarget != null) {
/* 132 */       this.soldier.currentActivity = EnumActivity.IDLE;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 137 */     if (!this.soldier.inventory.isEmpty() || !this.soldier.hasTool) {
/* 138 */       TileEntityChest chest = this.soldier.homeGuildHall.getAvailableChest();
				BlockPos chestPos = new BlockPos(chest.getPos());
/* 139 */       if (chest != null) {
/* 140 */         this.soldier.moveTo(chestPos, this.speed);
/*     */       } else {
/* 142 */         this.soldier.changeGuildHall = true;
/*     */       } 
/*     */ 
/*     */       
/* 146 */       if (chest != null && AIHelper.findDistance((int)this.soldier.posX, chestPos.getX()) <= 2 && AIHelper.findDistance((int)this.soldier.posY, chestPos.getY()) <= 2 && AIHelper.findDistance((int)this.soldier.posZ, chestPos.getZ()) <= 2) {
/*     */ 
/*     */         
/*     */         try {
/* 150 */           this.soldier.inventory.dumpInventory(chest);
/* 151 */         } catch (NullPointerException e) {
/* 152 */           chest.update();
/*     */         } 
/*     */ 
/*     */         
/* 156 */         if (!this.soldier.isFullyArmored()) {
/* 157 */           Iterator<TileEntityChest> iterator = this.soldier.homeGuildHall.guildChests.iterator();
/* 158 */           while (iterator.hasNext() && !this.soldier.isFullyArmored()) {
/* 159 */             chest = iterator.next();
/* 160 */             for (int i = 0; i < chest.getSizeInventory(); i++) {
/* 161 */               ItemStack chestItem = chest.getStackInSlot(i);
/* 162 */               if (chestItem != null && chestItem.getItem() instanceof ItemArmor) {
/* 163 */                 ItemArmor armor = (ItemArmor)chestItem.getItem();
/* 164 */                 switch (armor.armorType) {
/*     */                   case 0:
/* 166 */                     if (this.soldier.inventory.getStackInSlot(28) == null) {
/* 167 */                       this.soldier.inventory.swapEquipment(chest, i, 1);
/*     */                     }
/*     */                     break;
/*     */                   case 1:
/* 171 */                     if (this.soldier.inventory.getStackInSlot(29) == null) {
/* 172 */                       this.soldier.inventory.swapEquipment(chest, i, 2);
/*     */                     }
/*     */                     break;
/*     */                   case 2:
/* 176 */                     if (this.soldier.inventory.getStackInSlot(30) == null) {
/* 177 */                       this.soldier.inventory.swapEquipment(chest, i, 3);
/*     */                     }
/*     */                     break;
/*     */                   case 3:
/* 181 */                     if (this.soldier.inventory.getStackInSlot(31) == null) {
/* 182 */                       this.soldier.inventory.swapEquipment(chest, i, 4);
/*     */                     }
/*     */                     break;
/*     */                 } 
/*     */               } 
/* 187 */               if (this.soldier.isFullyArmored()) {
/*     */                 break;
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 195 */         if (!this.soldier.hasTool) {
/* 196 */           Iterator<TileEntityChest> iterator = this.soldier.homeGuildHall.guildChests.iterator();
/* 197 */           while (iterator.hasNext()) {
/* 198 */             chest = iterator.next();
/* 199 */             int index = AIHelper.chestContains(chest, (AbstractVillager)this.soldier);
/* 200 */             if (index >= 0) {
/* 201 */               this.soldier.inventory.swapEquipment(chest, index, 0);
/*     */             }
/*     */           } 
/*     */         } 
/*     */         
/* 206 */         if (!this.soldier.hasTool && this.soldier.queuedTool == null) {
/* 207 */           int lowestPrice = Integer.MAX_VALUE;
/* 208 */           ItemStack lowestItem = null;
/* 209 */           for (int i = 0; i < (this.soldier.getValidTools()).length; i++) {
/* 210 */             ItemStack item = this.soldier.getValidTools()[i];
/* 211 */             int price = this.soldier.homeVillage.economy.getPrice(item.getDisplayName());
/* 212 */             if (price < lowestPrice || lowestItem == null) {
/* 213 */               lowestPrice = price;
/* 214 */               lowestItem = item;
/*     */             } 
/*     */           } 
/* 217 */           this.soldier.addCraftItem(new CraftItem(lowestItem, (AbstractVillager)this.soldier));
/* 218 */           this.soldier.queuedTool = lowestItem;
/* 219 */         } else if (this.soldier.hasTool) {
/* 220 */           this.soldier.queuedTool = null;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void attack() {
/* 232 */     if (this.soldier.getAITarget() != null && this.soldier.getAITarget().isEntityAlive() && this.soldier.getAITarget() instanceof net.minecraft.entity.monster.IMob) {
/* 233 */       if (this.villageAgressorTarget != this.soldier.getAITarget()) {
/* 234 */         this.villageAgressorTarget = this.soldier.getAITarget();
/*     */       
/*     */       }
/*     */     }
/* 238 */     else if (this.soldier.homeVillage != null && this.soldier.homeVillage.lastAggressor != null && 
/* 239 */       this.villageAgressorTarget != this.soldier.homeVillage.lastAggressor && this.soldier.homeVillage.lastAggressor.isEntityAlive() && this.soldier.homeVillage.lastAggressor instanceof net.minecraft.entity.monster.IMob) {
/* 240 */       this.villageAgressorTarget = this.soldier.homeVillage.lastAggressor;
/*     */     } 
/*     */ 
/*     */     
/* 244 */     this.soldier.moveTo((Entity)this.villageAgressorTarget, this.speed);
/*     */ 
/*     */     
/* 247 */     if (this.soldier.getDistanceSqToEntity((Entity)this.villageAgressorTarget) <= 5.0D) {
/* 248 */       this.soldier.getNavigator().clearPathEntity();
/* 249 */       this.soldier.swingItem();
/*     */       
/* 251 */       if (this.villageAgressorTarget instanceof net.minecraft.entity.monster.EntityCreeper) {
/* 252 */         boolean attackSuccess = this.villageAgressorTarget.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this.soldier), 20.0F);
/* 253 */         if (attackSuccess) {
/* 254 */           this.soldier.damageItem();
/* 255 */           this.soldier.damageItem();
/* 256 */           this.soldier.damageItem();
/*     */         } 
/*     */       } else {
/* 259 */         boolean attackSuccess = this.villageAgressorTarget.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this.soldier), 20.0F);
/* 260 */         if (attackSuccess)
/* 261 */           this.soldier.damageItem(); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\ai\EntityAIGuardVillageSoldier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */