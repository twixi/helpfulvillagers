/*     */ package mods.helpfulvillagers.ai;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import mods.helpfulvillagers.crafting.CraftItem;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.entity.EntityArcher;
/*     */ import mods.helpfulvillagers.enums.EnumActivity;
/*     */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*     */ import mods.helpfulvillagers.util.AIHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.ai.EntityAITarget;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.init.Items;
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
/*     */ public class EntityAIGuardVillageArcher
/*     */   extends EntityAITarget
/*     */ {
/*     */   private EntityArcher archer;
/*     */   private EntityLivingBase villageAgressorTarget;
/*     */   private float speed;
/*     */   private int previousTime;
/*     */   private int currentTime;
/*     */   
/*     */   public EntityAIGuardVillageArcher(EntityArcher archer) {
/*  40 */     super((EntityCreature)archer, false, false);
/*  41 */     this.archer = archer;
/*  42 */     this.speed = 0.75F;
/*  43 */     this.previousTime = -1;
/*  44 */     this.currentTime = 0;
/*  45 */     setMutexBits(2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  54 */     if (this.archer.currentActivity == EnumActivity.RETURN || this.archer.currentActivity == EnumActivity.FOLLOW) {
/*  55 */       return false;
/*     */     }
/*     */     
/*  58 */     if (this.archer.getHealth() < this.archer.getHealth() / 2.0F) {
/*  59 */       this.archer.currentActivity = EnumActivity.STORE;
/*  60 */       return true;
/*     */     } 
/*     */     
/*  63 */     if (this.archer.getAITarget() != null && this.archer.getAITarget().isEntityAlive() && this.archer.getAITarget() instanceof net.minecraft.entity.monster.IMob) {
/*  64 */       this.villageAgressorTarget = this.archer.getAITarget();
/*  65 */       return true;
/*     */     } 
/*     */     
/*  68 */     if (!this.archer.worldObj.isRemote && this.archer.homeVillage != null) {
/*  69 */       this.villageAgressorTarget = this.archer.homeVillage.findNearestVillageAggressor((EntityLivingBase)this.archer);
/*  70 */       if (this.villageAgressorTarget != null) {
/*  71 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  75 */     if (!this.archer.hasTool || this.archer.inventory.containsItem(new ItemStack(Items.arrow)) < 0) {
/*  76 */       this.archer.currentActivity = EnumActivity.STORE;
/*  77 */       return true;
/*     */     } 
/*     */     
/*  80 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  85 */     this.archer.setAttackTarget(this.villageAgressorTarget);
/*  86 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean continueExecuting() {
/*  93 */     if (this.archer.currentActivity == EnumActivity.RETURN || this.archer.currentActivity == EnumActivity.FOLLOW) {
/*  94 */       return false;
/*     */     }
/*     */     
/*  97 */     if (this.archer.currentActivity == EnumActivity.STORE) {
/*  98 */       if (this.archer.getHealth() < this.archer.getHealth() / 2.0F || !this.archer.hasTool) {
/*  99 */         return true;
/*     */       }
/* 101 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 105 */     if (this.villageAgressorTarget != null && this.villageAgressorTarget.isEntityAlive()) {
/* 106 */       return true;
/*     */     }
/* 108 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTask() {
/* 116 */     if (this.archer.currentActivity == EnumActivity.STORE) {
/* 117 */       resupply();
/*     */     } else {
/* 119 */       attack();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resupply() {
/* 127 */     if (this.archer.homeGuildHall == null) {
/*     */       
/* 129 */       this.archer.currentActivity = EnumActivity.IDLE; return;
/*     */     } 
/* 131 */     if (!this.archer.nearHall()) {
/*     */       
/* 133 */       this.archer.currentActivity = EnumActivity.RETURN;
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 138 */     this.villageAgressorTarget = this.archer.homeVillage.findNearestVillageAggressor((EntityLivingBase)this.archer);
/* 139 */     if (this.archer.getHealth() >= this.archer.getHealth() / 2.0F && this.villageAgressorTarget != null) {
/* 140 */       this.archer.currentActivity = EnumActivity.IDLE;
/*     */     }
/*     */ 
/*     */     
/* 144 */     int arrowIndex = this.archer.inventory.containsItem(new ItemStack(Items.arrow));
/*     */ 
/*     */ 
/*     */     
/* 148 */     if (!this.archer.inventory.isEmpty() || !this.archer.hasTool || (arrowIndex < 0 && !HelpfulVillagers.infiniteArrows)) {
/* 149 */       TileEntityChest chest = this.archer.homeGuildHall.getAvailableChest();
				BlockPos chestPos = new BlockPos(chest.getPos());
/* 150 */       if (chest != null) {
/* 151 */         this.archer.moveTo(chestPos, this.speed);
/* 152 */         this.archer.changeGuildHall = false;
/*     */       } else {
/* 154 */         this.archer.changeGuildHall = true;
/*     */       } 
/*     */ 
/*     */       
/* 158 */       if (chest != null && AIHelper.findDistance((int)this.archer.posX, chestPos.getX()) <= 2 && AIHelper.findDistance((int)this.archer.posY, chestPos.getY()) <= 2 && AIHelper.findDistance((int)this.archer.posZ, chestPos.getZ()) <= 2) {
/*     */         
/* 160 */         ArrayList<ItemStack> arrows = new ArrayList();
/* 161 */         if (!HelpfulVillagers.infiniteArrows) {
/* 162 */           for (int i = 0; i < this.archer.inventory.getSizeInventory(); i++) {
/* 163 */             if (this.archer.inventory.getStackInSlot(i) != null && this.archer.inventory.getStackInSlot(i).equals(new ItemStack(Items.arrow))) {
/* 164 */               arrows.add(this.archer.inventory.getStackInSlot(i));
/* 165 */               this.archer.inventory.setMainContents(i, null);
/*     */             } 
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         try {
/* 172 */           this.archer.inventory.dumpInventory(chest);
/* 173 */         } catch (NullPointerException e) {
/* 174 */           chest.update();
/*     */         } 
/*     */ 
/*     */         
/* 178 */         if (!HelpfulVillagers.infiniteArrows) {
/* 179 */           for (int i = 0; i < arrows.size(); i++) {
/* 180 */             this.archer.inventory.addItem(arrows.get(i));
/*     */           }
/* 182 */           arrows.clear();
/*     */         } 
/*     */ 
/*     */         
/* 186 */         if (!this.archer.isFullyArmored()) {
/* 187 */           Iterator<TileEntityChest> iterator = this.archer.homeGuildHall.guildChests.iterator();
/* 188 */           while (iterator.hasNext() && !this.archer.isFullyArmored()) {
/* 189 */             chest = iterator.next();
/* 190 */             for (int i = 0; i < chest.getSizeInventory(); i++) {
/* 191 */               ItemStack chestItem = chest.getStackInSlot(i);
/* 192 */               if (chestItem != null && chestItem.getItem() instanceof ItemArmor) {
/* 193 */                 ItemArmor armor = (ItemArmor)chestItem.getItem();
/* 194 */                 switch (armor.armorType) {
/*     */                   case 0:
/* 196 */                     if (this.archer.inventory.getStackInSlot(28) == null) {
/* 197 */                       this.archer.inventory.swapEquipment(chest, i, 1);
/*     */                     }
/*     */                     break;
/*     */                   case 1:
/* 201 */                     if (this.archer.inventory.getStackInSlot(29) == null) {
/* 202 */                       this.archer.inventory.swapEquipment(chest, i, 2);
/*     */                     }
/*     */                     break;
/*     */                   case 2:
/* 206 */                     if (this.archer.inventory.getStackInSlot(30) == null) {
/* 207 */                       this.archer.inventory.swapEquipment(chest, i, 3);
/*     */                     }
/*     */                     break;
/*     */                   case 3:
/* 211 */                     if (this.archer.inventory.getStackInSlot(31) == null) {
/* 212 */                       this.archer.inventory.swapEquipment(chest, i, 4);
/*     */                     }
/*     */                     break;
/*     */                 } 
/*     */               } 
/* 217 */               if (this.archer.isFullyArmored()) {
/*     */                 break;
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 225 */         if (!this.archer.hasTool) {
/* 226 */           Iterator<TileEntityChest> iterator = this.archer.homeGuildHall.guildChests.iterator();
/* 227 */           while (iterator.hasNext()) {
/* 228 */             chest = iterator.next();
/* 229 */             int index = AIHelper.chestContains(chest, (AbstractVillager)this.archer);
/* 230 */             if (index >= 0) {
/* 231 */               this.archer.inventory.swapEquipment(chest, index, 0);
/*     */             }
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 237 */         if (!this.archer.hasTool && this.archer.queuedTool == null) {
/* 238 */           int lowestPrice = Integer.MAX_VALUE;
/* 239 */           ItemStack lowestItem = null;
/* 240 */           for (int i = 0; i < (this.archer.getValidTools()).length; i++) {
/* 241 */             ItemStack item = this.archer.getValidTools()[i];
/* 242 */             int price = this.archer.homeVillage.economy.getPrice(item.getDisplayName());
/* 243 */             if (price < lowestPrice || lowestItem == null) {
/* 244 */               lowestPrice = price;
/* 245 */               lowestItem = item;
/*     */             } 
/*     */           } 
/*     */           
/* 249 */           this.archer.addCraftItem(new CraftItem(lowestItem, (AbstractVillager)this.archer));
/* 250 */           this.archer.queuedTool = lowestItem;
/*     */         }
/* 252 */         else if (this.archer.hasTool) {
/* 253 */           this.archer.queuedTool = null;
/*     */         } 
/*     */ 
/*     */         
/* 257 */         if (arrowIndex < 0 && !HelpfulVillagers.infiniteArrows) {
/* 258 */           Iterator<TileEntityChest> iterator = this.archer.homeGuildHall.guildChests.iterator();
/* 259 */           while (iterator.hasNext()) {
/* 260 */             chest = iterator.next();
/* 261 */             if (AIHelper.chestContains(chest, new ItemStack(Items.arrow)) >= 0) {
/* 262 */               int index = AIHelper.chestContains(chest, new ItemStack(Items.arrow));
/* 263 */               this.archer.inventory.swapEquipment(chest, index, 0);
/* 264 */               this.archer.inventory.addItem(chest.getStackInSlot(index));
/* 265 */               chest.setInventorySlotContents(index, null);
/* 266 */               this.archer.currentActivity = EnumActivity.IDLE;
/*     */               break;
/*     */             } 
/*     */           } 
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
/* 281 */     if (this.archer.getAITarget() != null && this.archer.getAITarget().isEntityAlive() && this.archer.getAITarget() instanceof net.minecraft.entity.monster.IMob) {
/* 282 */       if (this.villageAgressorTarget != this.archer.getAITarget()) {
/* 283 */         this.villageAgressorTarget = this.archer.getAITarget();
/*     */       
/*     */       }
/*     */     }
/* 287 */     else if (this.archer.homeVillage != null && this.archer.homeVillage.lastAggressor != null && 
/* 288 */       this.villageAgressorTarget != this.archer.homeVillage.lastAggressor && this.archer.homeVillage.lastAggressor.isEntityAlive() && this.archer.homeVillage.lastAggressor instanceof net.minecraft.entity.monster.IMob) {
/* 289 */       this.villageAgressorTarget = this.archer.homeVillage.lastAggressor;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 294 */     this.archer.moveTo((Entity)this.villageAgressorTarget, this.speed);
/*     */ 
/*     */     
/* 297 */     if ((this.archer.hasTool && this.archer.inventory.containsItem(new ItemStack(Items.arrow)) >= 0) || HelpfulVillagers.infiniteArrows) {
/* 298 */       if (this.archer.canEntityBeSeen((Entity)this.villageAgressorTarget)) {
/* 299 */         this.archer.getNavigator().clearPathEntity();
/* 300 */         this.archer.getLookHelper().setLookPositionWithEntity((Entity)this.villageAgressorTarget, 30.0F, 30.0F);
/* 301 */         if (this.previousTime < 0)
/* 302 */         { this.previousTime = this.archer.ticksExisted; }
/* 303 */         else { this.archer.getClass(); if (this.currentTime - this.previousTime >= 20) {
/* 304 */             if (!this.archer.worldObj.isRemote) {
/* 305 */               EntityArrow arrow = new EntityArrow(this.archer.worldObj, (EntityLivingBase)this.archer, this.villageAgressorTarget, 1.6F, 2.0F);
/* 306 */               if (!HelpfulVillagers.infiniteArrows) {
/* 307 */                 arrow.canBePickedUp = 1;
/*     */               }
/* 309 */               this.archer.worldObj.spawnEntityInWorld((Entity)arrow);
/*     */             } 
/*     */             
/* 312 */             this.archer.worldObj.playSoundAtEntity((Entity)this.archer, "random.bow", 1.0F, 1.0F / (this.archer.getRNG().nextFloat() * 0.4F + 0.8F));
/* 313 */             this.archer.damageItem();
/*     */             
/* 315 */             if (HelpfulVillagers.infiniteArrows) {
/* 316 */               this.archer.inventory.decrementSlot(this.archer.inventory.containsItem(new ItemStack(Items.arrow)));
/*     */             }
/*     */             
/* 319 */             this.previousTime = -1;
/*     */           } else {
/* 321 */             this.currentTime = this.archer.ticksExisted;
/*     */           }
/*     */            }
/*     */       
/*     */       } 
/* 326 */     } else if (this.archer.getDistanceSqToEntity((Entity)this.villageAgressorTarget) <= 5.0D) {
/* 327 */       this.archer.getNavigator().clearPathEntity();
/* 328 */       this.archer.swingItem();
/* 329 */       if (this.villageAgressorTarget instanceof net.minecraft.entity.monster.EntityCreeper) {
/* 330 */         boolean attackSuccess = this.villageAgressorTarget.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this.archer), 20.0F);
/* 331 */         if (attackSuccess) {
/* 332 */           this.archer.damageItem();
/* 333 */           this.archer.damageItem();
/* 334 */           this.archer.damageItem();
/*     */         } 
/*     */       } else {
/* 337 */         boolean attackSuccess = this.villageAgressorTarget.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this.archer), this.archer.getAttackDamage());
/* 338 */         if (attackSuccess)
/* 339 */           this.archer.damageItem(); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\ai\EntityAIGuardVillageArcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */