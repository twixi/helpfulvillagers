/*     */ package mods.helpfulvillagers.ai;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Random;
/*     */ import mods.helpfulvillagers.crafting.CraftItem;
/*     */ import mods.helpfulvillagers.crafting.CraftTree;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.enums.EnumActivity;
/*     */ import mods.helpfulvillagers.util.AIHelper;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.tileentity.TileEntityFurnace;
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
/*     */ public abstract class EntityAIWorker
/*     */   extends EntityAIBase
/*     */ {
/*     */   protected AbstractVillager villager;
/*     */   protected BlockPos target;
/*     */   protected float speed;
/*     */   protected int previousTime;
/*     */   protected int currentTime;
/*     */   protected float harvestTime;
/*     */   protected Random gen;
/*     */   private boolean craftInit;
/*     */   protected boolean craftCheck;
/*     */   protected boolean readyToSmelt;
/*     */   protected boolean readyToCraft;
/*     */   private CraftTree craftTree;
/*     */   
/*     */   public EntityAIWorker(AbstractVillager villager) {
/*  44 */     this.villager = villager;
/*  45 */     this.target = null;
/*  46 */     this.speed = 0.5F;
/*  47 */     this.currentTime = 0;
/*  48 */     this.previousTime = 0;
/*  49 */     this.harvestTime = 0.0F;
/*  50 */     this.gen = new Random();
/*  51 */     this.craftInit = false;
/*  52 */     this.craftCheck = false;
/*  53 */     this.readyToSmelt = false;
/*  54 */     this.readyToCraft = false;
/*  55 */     this.craftTree = null;
/*  56 */     setMutexBits(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  64 */     switch (this.villager.currentActivity) {
/*     */       case GATHER:
/*  66 */         return true;
/*     */       case RETURN:
/*  68 */         return false;
/*     */       case CRAFT:
/*  70 */         return true;
/*     */       case STORE:
/*  72 */         return true;
/*     */       case IDLE:
/*  74 */         return idle();
/*     */     } 
/*  76 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean continueExecuting() {
/*  84 */     switch (this.villager.currentActivity) {
/*     */       case GATHER:
/*  86 */         return gather();
/*     */       case RETURN:
/*  88 */         return false;
/*     */       case CRAFT:
/*  90 */         return craft();
/*     */       case STORE:
/*  92 */         return store();
/*     */       case IDLE:
/*  94 */         return idle();
/*     */     } 
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean idle() {
/* 104 */     this.villager.currentActivity = EnumActivity.IDLE;
/*     */ 
/*     */     
/* 107 */     if (!this.villager.worldObj.isRemote && this.villager.homeVillage == null) {
/* 108 */       System.out.println("No Home Village");
/* 109 */       return false;
/*     */     } 
/*     */     
/* 112 */     this.villager.checkGuildHall();
/*     */ 
/*     */     
/* 115 */     if (this.villager.homeGuildHall == null)
/*     */     {
/* 117 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 121 */     if (this.villager.currentCraftItem != null && this.villager.currentCraftItem.getPriority() >= 1) {
/*     */       
/* 123 */       if ((this.readyToCraft || this.readyToSmelt) && !this.villager.nearHall()) {
/* 124 */         this.villager.currentActivity = EnumActivity.RETURN;
/* 125 */         return false;
/*     */       } 
/*     */       
/* 128 */       if ((this.readyToCraft || this.readyToSmelt) && this.villager.nearHall()) {
/* 129 */         this.villager.currentActivity = EnumActivity.CRAFT;
/* 130 */         return true;
/*     */       } 
/*     */       
/* 133 */       if (!this.craftCheck) {
/* 134 */         this.villager.currentActivity = EnumActivity.CRAFT;
/* 135 */         this.craftCheck = true;
/* 136 */         return true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 142 */     if (this.villager.inventory.isFull() || !this.villager.hasTool) {
/*     */       
/* 144 */       if (this.villager.nearHall()) {
/*     */         
/* 146 */         if (this.villager.currentCraftItem != null && !this.craftCheck) {
/* 147 */           this.villager.currentActivity = EnumActivity.CRAFT;
/* 148 */           this.craftCheck = true;
/* 149 */           return true;
/*     */         } 
/* 151 */         if (!this.villager.inventory.isEmpty() || !this.villager.hasTool) {
/* 152 */           this.villager.currentActivity = EnumActivity.STORE;
/* 153 */           this.craftCheck = false;
/* 154 */           return true;
/*     */         } 
/*     */         
/* 157 */         this.craftCheck = false;
/* 158 */         return false;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 163 */       this.villager.currentActivity = EnumActivity.RETURN;
/* 164 */       this.craftCheck = false;
/* 165 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 170 */     if (this.villager.worldObj.isDaytime()) {
/* 171 */       this.villager.currentActivity = EnumActivity.GATHER;
/* 172 */       this.craftCheck = false;
/* 173 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 177 */     if (!this.villager.nearHall()) {
/* 178 */       this.villager.currentActivity = EnumActivity.RETURN;
/* 179 */       this.craftCheck = false;
/* 180 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 184 */     if (this.villager.currentCraftItem != null && !this.craftCheck) {
/* 185 */       this.villager.currentActivity = EnumActivity.CRAFT;
/* 186 */       this.craftCheck = true;
/* 187 */       return true;
/*     */     } 
/* 189 */     if (!this.villager.inventory.isEmpty() || !this.villager.hasTool) {
/* 190 */       this.villager.currentActivity = EnumActivity.STORE;
/* 191 */       this.craftCheck = false;
/* 192 */       return true;
/*     */     } 
/*     */     
/* 195 */     this.craftCheck = false;
/* 196 */     return true;
/*     */   }
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
/*     */   protected boolean store() {
/* 209 */     if (this.villager.homeGuildHall == null) {
/* 210 */       return idle();
/*     */     }
/*     */     
/* 213 */     if (!this.villager.inventory.isEmpty() || !this.villager.hasTool) {
/* 214 */       TileEntityChest chest = this.villager.homeGuildHall.getAvailableChest();
/* 215 */       if (chest != null) {
/* 216 */         this.villager.moveTo(chest.getPos(), this.speed);
/*     */       } else {
/* 218 */         this.villager.changeGuildHall = true;
/*     */       } 
/*     */       
/* 221 */       if (chest != null && AIHelper.findDistance((int)this.villager.posX, chest.getPos().getX()) <= 2 && AIHelper.findDistance((int)this.villager.posY, chest.getPos().getY()) <= 2 && AIHelper.findDistance((int)this.villager.posZ, chest.getPos().getZ()) <= 2) {
/*     */         try {
/* 223 */           this.villager.inventory.dumpInventory(chest);
/* 224 */         } catch (NullPointerException e) {
/* 225 */           chest.update();
/*     */         } 
/*     */         
/* 228 */         if (!this.villager.hasTool) {
/* 229 */           Iterator<TileEntityChest> iterator = this.villager.homeGuildHall.guildChests.iterator();
/* 230 */           while (iterator.hasNext()) {
/* 231 */             chest = iterator.next();
/* 232 */             int index = AIHelper.chestContains(chest, this.villager);
/* 233 */             if (index >= 0) {
/* 234 */               this.villager.inventory.swapEquipment(chest, index, 0);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 241 */     if (!this.villager.hasTool && this.villager.queuedTool == null) {
/* 242 */       int lowestPrice = Integer.MAX_VALUE;
/* 243 */       ItemStack lowestItem = null;
/* 244 */       for (int i = 0; i < (this.villager.getValidTools()).length; i++) {
/* 245 */         ItemStack item = this.villager.getValidTools()[i];
/* 246 */         int price = this.villager.homeVillage.economy.getPrice(item.getDisplayName());
/* 247 */         if (price < lowestPrice || lowestItem == null) {
/* 248 */           lowestPrice = price;
/* 249 */           lowestItem = item;
/*     */         } 
/*     */       } 
/*     */       
/* 253 */       this.villager.addCraftItem(new CraftItem(lowestItem, this.villager));
/* 254 */       this.villager.queuedTool = lowestItem;
/* 255 */     } else if (this.villager.hasTool) {
/* 256 */       this.villager.queuedTool = null;
/*     */     } 
/*     */ 
/*     */     
/* 260 */     return idle();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean craft() {
/* 270 */     if (this.villager.currentCraftItem == null) {
/* 271 */       return idle();
/*     */     }
/*     */     
/* 274 */     if (!this.craftInit) {
/* 275 */       this.craftTree = new CraftTree(this.villager.currentCraftItem.getItem(), this.villager);
/* 276 */       this.craftInit = true;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 286 */     if (!this.readyToSmelt) {
/* 287 */       if (!this.villager.smeltablesNeeded.isEmpty()) {
/*     */         
/* 289 */         Iterator<ItemStack> iterator = this.villager.smeltablesNeeded.iterator();
/* 290 */         while (iterator.hasNext()) {
/* 291 */           ItemStack currItem = iterator.next();
/* 292 */           this.villager.inventory.storeAsCollected(currItem, true);
/* 293 */           if (currItem.stackSize <= 0) {
/* 294 */             iterator.remove(); continue;
/*     */           } 
/* 296 */           this.villager.lookForItem(currItem);
/* 297 */           if (this.villager.inventory.getTotalAmount(currItem) >= currItem.stackSize) {
/* 298 */             this.villager.inventory.storeAsCollected(currItem, true);
/* 299 */             if (currItem.stackSize <= 0) {
/* 300 */               iterator.remove();
/*     */             
/*     */             }
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 308 */       else if (!this.villager.inventory.smeltablesCollected.isEmpty()) {
/* 309 */         this.readyToSmelt = true;
/* 310 */         return idle();
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/* 316 */     else if (!this.villager.inventory.smeltablesCollected.isEmpty()) {
/* 317 */       if (this.villager.nearHall()) {
/* 318 */         TileEntityFurnace furnace = this.villager.homeGuildHall.getAvailableFurnace();
/* 319 */         if (furnace != null) {
/*     */           
/* 321 */           if (furnace.getStackInSlot(2) != null) {
/* 322 */             this.villager.inventory.addItem(furnace.getStackInSlot(2));
/* 323 */             furnace.setInventorySlotContents(2, null);
/*     */           } 
/*     */ 
/*     */           
/* 327 */           if (!TileEntityFurnace.isItemFuel(furnace.getStackInSlot(1))) {
/* 328 */             int burnTime = ((ItemStack)this.villager.inventory.smeltablesCollected.get(0)).stackSize * 200;
/* 329 */             AIHelper.addFuelToFurnace(this.villager.homeVillage, furnace, burnTime);
/*     */           } else {
/*     */             
/* 332 */             ItemStack item = this.villager.inventory.smeltablesCollected.remove(0);
/* 333 */             furnace.setInventorySlotContents(0, item);
/*     */           } 
/*     */         } else {
/*     */           
/* 337 */           this.villager.changeGuildHall = true;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 341 */       this.readyToSmelt = false;
/* 342 */       if (this.villager.materialsNeeded.isEmpty() && this.villager.inventory.materialsCollected.isEmpty()) {
/* 343 */         this.villager.resetCraftItem();
/* 344 */         this.craftInit = false;
/* 345 */         return idle();
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 352 */     if (!this.readyToCraft) {
/* 353 */       if (!this.villager.materialsNeeded.isEmpty()) {
/*     */         
/* 355 */         Iterator<ItemStack> iterator = this.villager.materialsNeeded.iterator();
/* 356 */         while (iterator.hasNext()) {
/* 357 */           ItemStack currItem = iterator.next();
/* 358 */           this.villager.inventory.storeAsCollected(currItem, false);
/* 359 */           if (currItem.stackSize <= 0) {
/* 360 */             iterator.remove(); continue;
/*     */           } 
/* 362 */           this.villager.lookForItem(currItem);
/* 363 */           if (this.villager.inventory.getTotalAmount(currItem) >= currItem.stackSize) {
/* 364 */             this.villager.inventory.storeAsCollected(currItem, false);
/* 365 */             if (currItem.stackSize <= 0) {
/* 366 */               iterator.remove();
/*     */             
/*     */             }
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 374 */       else if (!this.villager.inventory.materialsCollected.isEmpty()) {
/* 375 */         this.readyToCraft = true;
/* 376 */         return idle();
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/* 382 */     else if (this.villager.nearHall() && 
/* 383 */       this.villager.homeGuildHall.hasWorkbench()) {
/* 384 */       Iterator<CraftTree.Node> iterator = this.villager.craftChain.iterator();
/* 385 */       while (iterator.hasNext()) {
/* 386 */         CraftTree.Node currNode = iterator.next();
/* 387 */         if (currNode.isSmelted()) {
/*     */           continue;
/*     */         }
/* 390 */         for (ItemStack i : currNode.getInputs()) {
/* 391 */           if (!AIHelper.removeItemStack(i, this.villager.inventory.materialsCollected)) {
/* 392 */             System.out.println("MATERIALS NOT COLLECTED: " + i);
/*     */           }
/*     */         } 
/*     */         
/* 396 */         int amountProduced = (currNode.getItemStack()).stackSize + currNode.getLeftover();
/* 397 */         AIHelper.mergeItemStackArrays(new ItemStack(currNode.getItemStack().getItem(), amountProduced, currNode.getItemStack().getMetadata()), this.villager.inventory.materialsCollected);
/* 398 */         iterator.remove();
/*     */       } 
/*     */       
/* 401 */       this.villager.inventory.dumpCollected(false);
/* 402 */       this.villager.resetCraftItem();
/* 403 */       this.craftInit = false;
/* 404 */       this.readyToCraft = false;
/*     */       
/* 406 */       return store();
/*     */     } 
/*     */ 
/*     */     
/* 410 */     return idle();
/*     */   }
/*     */   
/*     */   protected abstract boolean gather();
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\ai\EntityAIWorker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */