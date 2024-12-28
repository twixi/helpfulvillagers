/*     */ package mods.helpfulvillagers.inventory;
/*     */ 
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*     */ import mods.helpfulvillagers.network.InventoryPacket;
/*     */ import mods.helpfulvillagers.util.AIHelper;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.IChatComponent;
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
/*     */ public class InventoryVillager
/*     */   implements IInventory, Serializable
/*     */ {
/*     */   private ItemStack[] mainInventory;
/*     */   private ItemStack[] equipmentInventory;
/*  46 */   public ArrayList<ItemStack> materialsCollected = new ArrayList<ItemStack>();
/*  47 */   public ArrayList<ItemStack> smeltablesCollected = new ArrayList<ItemStack>();
/*     */   
/*     */   String inventoryTitle;
/*     */   
/*     */   public AbstractVillager owner;
/*     */   
/*     */   public InventoryVillager(AbstractVillager abstractEntity) {
/*  54 */     this.mainInventory = new ItemStack[getSizeInventory()];
/*  55 */     this.equipmentInventory = new ItemStack[getSizeEquipment()];
/*  56 */     this.owner = abstractEntity;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSizeInventory() {
/*  61 */     return 27;
/*     */   }
/*     */   
/*     */   public int getSizeEquipment() {
/*  65 */     return 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getStackInSlot(int index) {
/*  70 */     if (index < 0) {
/*  71 */       return null;
/*     */     }
/*     */     
/*  74 */     ItemStack[] aitemstack = this.mainInventory;
/*     */     
/*  76 */     if (index >= aitemstack.length) {
/*  77 */       index -= aitemstack.length;
/*  78 */       aitemstack = this.equipmentInventory;
/*     */     } 
/*     */     
/*  81 */     if (index < aitemstack.length) {
/*  82 */       return aitemstack[index];
/*     */     }
/*  84 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack decrStackSize(int index, int amount) {
/*  90 */     ItemStack[] aitemstack = this.mainInventory;
/*     */     
/*  92 */     if (index >= this.mainInventory.length) {
/*  93 */       aitemstack = this.equipmentInventory;
/*  94 */       index -= this.mainInventory.length;
/*     */     } 
/*     */     
/*  97 */     if (index < aitemstack.length) {
/*     */ 
/*     */       
/* 100 */       if ((aitemstack[index]).stackSize <= amount) {
/* 101 */         ItemStack itemStack = aitemstack[index];
/* 102 */         aitemstack[index] = null;
/* 103 */         return itemStack;
/*     */       } 
/*     */       
/* 106 */       ItemStack itemstack = aitemstack[index].splitStack(amount);
/*     */       
/* 108 */       if ((aitemstack[index]).stackSize == 0) {
/* 109 */         aitemstack[index] = null;
/*     */       }
/*     */       
/* 112 */       return itemstack;
/*     */     } 
/*     */ 
/*     */     
/* 116 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInventorySlotContents(int index, ItemStack itemStack) {
/* 122 */     if (index < 0) {
/*     */       return;
/*     */     }
/*     */     
/* 126 */     if (index >= this.mainInventory.length) {
/* 127 */       index -= this.mainInventory.length;
/* 128 */       if (index >= this.equipmentInventory.length) {
/*     */         return;
/*     */       }
/* 131 */       this.equipmentInventory[index] = itemStack;
/*     */     } else {
/*     */       
/* 134 */       this.mainInventory[index] = itemStack;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMainContents(int index, ItemStack itemStack) {
/* 144 */     this.mainInventory[index] = itemStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEquipmentContents(int index, ItemStack itemStack) {
/* 153 */     this.equipmentInventory[index] = itemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getStackInSlotOnClosing(int i) {
/* 158 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getInventoryName() {
/* 163 */     return this.inventoryTitle;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInventoryStackLimit() {
/* 168 */     return 64;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
/* 173 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack) {
/* 178 */     if (par2ItemStack == null) {
/* 179 */       return true;
/*     */     }
/*     */     
/* 182 */     if (par1 == 27)
/* 183 */       return this.owner.isValidTool(par2ItemStack); 
/* 184 */     if (par1 == 28) {
/* 185 */       if (par2ItemStack.getItem() instanceof ItemArmor) {
/* 186 */         ItemArmor armor = (ItemArmor)par2ItemStack.getItem();
/* 187 */         return (armor.armorType == 0);
/*     */       } 
/* 189 */       return false;
/*     */     } 
/* 191 */     if (par1 == 29) {
/* 192 */       if (par2ItemStack.getItem() instanceof ItemArmor) {
/* 193 */         ItemArmor armor = (ItemArmor)par2ItemStack.getItem();
/* 194 */         return (armor.armorType == 1);
/*     */       } 
/* 196 */       return false;
/*     */     } 
/* 198 */     if (par1 == 30) {
/* 199 */       if (par2ItemStack.getItem() instanceof ItemArmor) {
/* 200 */         ItemArmor armor = (ItemArmor)par2ItemStack.getItem();
/* 201 */         return (armor.armorType == 2);
/*     */       } 
/* 203 */       return false;
/*     */     } 
/* 205 */     if (par1 == 31) {
/* 206 */       if (par2ItemStack.getItem() instanceof ItemArmor) {
/* 207 */         ItemArmor armor = (ItemArmor)par2ItemStack.getItem();
/* 208 */         return (armor.armorType == 3);
/*     */       } 
/* 210 */       return false;
/*     */     } 
/*     */     
/* 213 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 221 */     for (ItemStack i : this.mainInventory) {
/* 222 */       if (i != null) {
/* 223 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 227 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFull() {
/* 234 */     for (ItemStack i : this.mainInventory) {
/* 235 */       if (i == null) {
/* 236 */         return false;
/*     */       }
/*     */     } 
/* 239 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int containsItem(ItemStack item) {
/* 247 */     for (int i = 0; i < this.mainInventory.length; i++) {
/* 248 */       if (this.mainInventory[i] != null && this.mainInventory[i].getItem() == item.getItem()) {
/* 249 */         return i;
/*     */       }
/*     */     } 
/* 252 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int containsItem() {
/* 259 */     for (int i = 0; i < this.mainInventory.length; i++) {
/* 260 */       if (this.mainInventory[i] != null && this.owner.isValidTool(this.mainInventory[i])) {
/* 261 */         return i;
/*     */       }
/*     */     } 
/* 264 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int containsItemWithMetadata(ItemStack item) {
/* 273 */     for (int i = 0; i < this.mainInventory.length; i++) {
/* 274 */       if (this.mainInventory[i] != null && this.mainInventory[i].getItem() == item.getItem() && this.mainInventory[i].getMetadata() == item.getMetadata()) {
/* 275 */         return i;
/*     */       }
/*     */     } 
/* 278 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTotalAmount(ItemStack item) {
/* 287 */     int count = 0;
/* 288 */     for (int i = 0; i < this.mainInventory.length; i++) {
/* 289 */       if (this.mainInventory[i] != null && this.mainInventory[i].getItem().equals(item.getItem())) {
/* 290 */         count += (this.mainInventory[i]).stackSize;
/*     */       }
/*     */     } 
/*     */     
/* 294 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int findSolidBlock(ArrayList exlude) {
/* 303 */     for (int i = 0; i < this.mainInventory.length; i++) {
/* 304 */       if (this.mainInventory[i] != null) {
/* 305 */         Block block = Block.getBlockFromItem(this.mainInventory[i].getItem());
/* 306 */         if (!exlude.contains(block) && block.isNormalCube()) {
/* 307 */           return i;
/*     */         }
/*     */       } 
/*     */     } 
/* 311 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void swapItems(int index1, int index2) {
/* 320 */     if (index1 >= 0 && index1 < this.mainInventory.length && index2 >= 0 && index2 < this.mainInventory.length) {
/* 321 */       ItemStack item1 = this.mainInventory[index1];
/* 322 */       ItemStack item2 = this.mainInventory[index2];
/*     */       
/* 324 */       this.mainInventory[index1] = item2;
/* 325 */       this.mainInventory[index2] = item1;
/* 326 */       syncInventory();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void swapItems(TileEntityChest chest, int chestIndex, int invIndex) {
/* 337 */     System.out.println("Chest Swap");
/* 338 */     if (chestIndex >= 0 && chestIndex < chest.getSizeInventory() && invIndex >= 0 && invIndex < this.mainInventory.length) {
/* 339 */       ItemStack item1 = chest.getStackInSlot(chestIndex);
/* 340 */       ItemStack item2 = this.mainInventory[invIndex];
/*     */       
/* 342 */       chest.setInventorySlotContents(chestIndex, item2);
/* 343 */       this.mainInventory[invIndex] = item1;
/* 344 */       syncInventory();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void swapEquipment(int index1, int index2) {
/* 354 */     if (index1 >= 0 && index1 < this.mainInventory.length && index2 >= 0 && index2 < this.equipmentInventory.length) {
/* 355 */       ItemStack item1 = this.mainInventory[index1];
/* 356 */       ItemStack item2 = this.equipmentInventory[index2];
/*     */       
/* 358 */       this.mainInventory[index1] = item2;
/* 359 */       this.equipmentInventory[index2] = item1;
/* 360 */       syncInventory();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void swapEquipment(TileEntityChest chest, int chestIndex, int invIndex) {
/* 371 */     System.out.println("Chest Swap");
/* 372 */     if (chestIndex >= 0 && chestIndex < chest.getSizeInventory() && invIndex >= 0 && invIndex < this.equipmentInventory.length) {
/* 373 */       ItemStack item1 = chest.getStackInSlot(chestIndex);
/* 374 */       ItemStack item2 = this.equipmentInventory[invIndex];
/*     */       
/* 376 */       chest.setInventorySlotContents(chestIndex, item2);
/* 377 */       this.equipmentInventory[invIndex] = item1;
/* 378 */       syncInventory();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addItem(ItemStack item) {
/*     */     int i;
/* 389 */     for (i = 0; i < this.equipmentInventory.length; i++) {
/* 390 */       if (this.equipmentInventory[i] == null && isItemValidForSlot(this.mainInventory.length + i, item)) {
/* 391 */         this.equipmentInventory[i] = item;
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 396 */     for (i = 0; i < this.mainInventory.length; i++) {
/* 397 */       if (this.mainInventory[i] != null && this.mainInventory[i].getDisplayName().equals(item.getDisplayName())) {
/* 398 */         int temp = item.stackSize;
/* 399 */         while ((this.mainInventory[i]).stackSize < this.mainInventory[i].getMaxStackSize()) {
/* 400 */           (this.mainInventory[i]).stackSize++;
/* 401 */           temp--;
/* 402 */           if (temp <= 0) {
/* 403 */             item = null;
/*     */             return;
/*     */           } 
/*     */         } 
/* 407 */         item.stackSize = temp;
/*     */       } 
/*     */     } 
/*     */     
/* 411 */     for (i = 0; i < this.mainInventory.length; i++) {
/* 412 */       if (this.mainInventory[i] == null) {
/* 413 */         this.mainInventory[i] = item.copy();
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 418 */     if (this.owner.worldObj.isRemote) {
/* 419 */       dropItem(item);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void dropItem(ItemStack item) {
/* 429 */     if (item != null) {
/* 430 */       EntityItem worldItem = new EntityItem(this.owner.worldObj, this.owner.posX, this.owner.posY, this.owner.posZ, item);
/* 431 */       this.owner.worldObj.spawnEntityInWorld((Entity)worldItem);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dropFromInventory(int i) {
/* 440 */     if (!this.owner.worldObj.isRemote) {
/* 441 */       ItemStack stack = getStackInSlot(i);
/* 442 */       if (stack != null) {
/* 443 */         dropItem(stack);
/* 444 */       setInventorySlotContents(i, null);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dumpInventory(TileEntityChest chest) {
/* 454 */     syncInventory();
/* 455 */		chest.update();
/*     */     
/* 457 */     for (int i = 0; i < this.mainInventory.length; i++) {
/* 458 */       if (this.mainInventory[i] != null)
/* 459 */         for (int j = 0; j < chest.getSizeInventory(); j++) {
/* 460 */           ItemStack chestItem = chest.getStackInSlot(j);
/* 461 */           if (chestItem == null) {
/* 462 */             chest.setInventorySlotContents(j, this.mainInventory[i]);
/* 463 */             this.owner.homeVillage.economy.increaseItemSupply(this.owner, this.mainInventory[i]);
/* 464 */             this.mainInventory[i] = null; break;
/*     */           } 
/* 466 */           if(chestItem.stackSize < chestItem.getMaxStackSize())
/*     */           {
/* 468 */             if (this.mainInventory[i].getDisplayName().equals(chestItem.getDisplayName())) {
/* 469 */               int chestStackSize = chestItem.stackSize;
/* 470 */               int invStackSize = (this.mainInventory[i]).stackSize;
/* 471 */               chestStackSize += invStackSize;
/* 472 */               if (chestStackSize > chestItem.getMaxStackSize()) {
/* 473 */                 invStackSize = chestStackSize - chestItem.getMaxStackSize();
/* 474 */                 chestStackSize = chestItem.getMaxStackSize();
/*     */               } else {
/* 476 */                 invStackSize = 0;
/*     */               } 
/*     */               
/* 479 */               (this.mainInventory[i]).stackSize = invStackSize;
/* 480 */               if ((this.mainInventory[i]).stackSize <= 0) {
/* 481 */                 this.mainInventory[i] = null;
/*     */               }
/*     */               
/* 484 */               chestItem.stackSize = chestStackSize;
/* 485 */               chest.setInventorySlotContents(j, chestItem);
/* 486 */               this.owner.homeVillage.economy.increaseItemSupply(this.owner, chestItem);
/*     */             } 
/*     */           }
/*     */         }  
/*     */     } 
/* 491 **/		closeChest();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dumpInventory() {
/* 498 */     for (int i = 0; i < this.mainInventory.length + this.equipmentInventory.length; i++) {
/* 499 */       dropFromInventory(i);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void storeAsCollected(ItemStack item, boolean smelt) {
/* 509 */     if (smelt) {
/* 510 */       for (int i = 0; i < this.mainInventory.length; i++) {
/* 511 */         ItemStack invItem = this.mainInventory[i];
/* 512 */         Block block = Block.getBlockFromItem(item.getItem());
/* 513 */         Block invBlock = Block.getBlockFromItem((invItem != null) ? invItem.getItem() : null);
/* 514 */         if (invItem != null && invItem.stackSize > 0 && ((this.owner.currentCraftItem.isSensitive() && invItem.getDisplayName().equals(item.getDisplayName())) || (!this.owner.currentCraftItem.isSensitive() && (invItem.getItem().equals(item.getItem()) || (block instanceof net.minecraft.block.BlockLog && invBlock instanceof net.minecraft.block.BlockLog))))) {
/* 515 */           if (invItem.stackSize >= item.stackSize) {
/* 516 */             int j = item.stackSize;
/* 517 */             invItem.stackSize -= j;
/* 518 */             AIHelper.mergeItemStackArrays(new ItemStack(item.getItem(), j), this.smeltablesCollected);
/* 519 */             if (invItem.stackSize <= 0) {
/* 520 */               invItem = null;
/*     */             }
/* 522 */             this.mainInventory[i] = invItem;
/* 523 */             item.stackSize = 0;
/*     */             return;
/*     */           } 
/* 526 */           int itemSize = invItem.stackSize;
/* 527 */           item.stackSize -= invItem.stackSize;
/* 528 */           AIHelper.mergeItemStackArrays(new ItemStack(invItem.getItem(), itemSize), this.smeltablesCollected);
/* 529 */           this.mainInventory[i] = null;
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 534 */       for (int i = 0; i < this.mainInventory.length; i++) {
/* 535 */         ItemStack invItem = this.mainInventory[i];
/* 536 */         Block block = Block.getBlockFromItem(item.getItem());
/* 537 */         Block invBlock = Block.getBlockFromItem((invItem != null) ? invItem.getItem() : null);
/* 538 */         if (invItem != null && invItem.stackSize > 0 && ((this.owner.currentCraftItem.isSensitive() && invItem.getDisplayName().equals(item.getDisplayName())) || (!this.owner.currentCraftItem.isSensitive() && (invItem.getItem().equals(item.getItem()) || (block instanceof net.minecraft.block.BlockLog && invBlock instanceof net.minecraft.block.BlockLog))))) {
/* 539 */           if (invItem.stackSize >= item.stackSize) {
/* 540 */             int j = item.stackSize;
/* 541 */             invItem.stackSize -= j;
/* 542 */             AIHelper.mergeItemStackArrays(new ItemStack(item.getItem(), j), this.materialsCollected);
/* 543 */             if (invItem.stackSize <= 0) {
/* 544 */               invItem = null;
/*     */             }
/* 546 */             this.mainInventory[i] = invItem;
/* 547 */             item.stackSize = 0;
/*     */             return;
/*     */           } 
/* 550 */           int itemSize = invItem.stackSize;
/* 551 */           item.stackSize -= invItem.stackSize;
/* 552 */           AIHelper.mergeItemStackArrays(new ItemStack(invItem.getItem(), itemSize), this.materialsCollected);
/* 553 */           this.mainInventory[i] = null;
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
/*     */   public void dumpCollected(boolean smelt) {
/* 565 */     ArrayList<ItemStack> tempList = new ArrayList<ItemStack>();
/* 566 */     int slot = 0;
/*     */     
/* 568 */     if (smelt) {
/* 569 */       for (ItemStack i : this.smeltablesCollected) {
/* 570 */         if (this.mainInventory[slot] != null) {
/* 571 */           tempList.add(this.mainInventory[slot].copy());
/*     */         }
/* 573 */         this.mainInventory[slot] = i;
/* 574 */         slot++;
/*     */       } 
/*     */     } else {
/* 577 */       for (ItemStack i : this.materialsCollected) {
/* 578 */         if (!i.getDisplayName().equals(this.owner.currentCraftItem.getItem().getDisplayName()) || !this.owner.storeCraftedItem()) {
/* 579 */           if (this.mainInventory[slot] != null) {
/* 580 */             tempList.add(this.mainInventory[slot].copy());
/*     */           }
/* 582 */           this.mainInventory[slot] = i;
/* 583 */           slot++;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 588 */     for (ItemStack i : tempList) {
/* 589 */       addItem(i);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void decrementSlot(int index) {
/* 599 */     (this.mainInventory[index]).stackSize--;
/* 600 */     if ((this.mainInventory[index]).stackSize <= 0) {
/* 601 */       this.mainInventory[index] = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getCurrentItem() {
/* 609 */     return this.equipmentInventory[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCurrentItem(ItemStack itemStack) {
/* 617 */     this.equipmentInventory[0] = itemStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printInventory() {
/* 624 */     if (this.owner.worldObj.isRemote) {
/* 625 */       System.out.println("Client: ");
/*     */     } else {
/* 627 */       System.out.println("Server: ");
/*     */     } 
/*     */     int i;
/* 630 */     for (i = 0; i < this.mainInventory.length; i++) {
/* 631 */       if (this.mainInventory[i] != null) {
/* 632 */         System.out.println("Item in slot " + i + ": " + this.mainInventory[i].getDisplayName() + " Amount: " + (this.mainInventory[i]).stackSize);
/*     */       } else {
/*     */         
/* 635 */         System.out.println("Item in slot " + i + ": nothing");
/*     */       } 
/*     */     } 
/*     */     
/* 639 */     for (i = 0; i < this.equipmentInventory.length; i++) {
/* 640 */       if (this.equipmentInventory[i] != null) {
/* 641 */         System.out.println("Armor in slot " + i + ": " + this.equipmentInventory[i].getDisplayName());
/*     */       } else {
/* 643 */         System.out.println("Armor in slot " + i + ": nothing");
/*     */       } 
/*     */     } 
/* 646 */     System.out.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void syncInventory() {
/* 653 */     if (!this.owner.worldObj.isRemote) {
/* 654 */       HelpfulVillagers.network.sendToAll((IMessage)new InventoryPacket(this.owner.getEntityId(), this.mainInventory, this.equipmentInventory));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void syncEquipment() {
/* 662 */     if (!this.owner.worldObj.isRemote) {
/* 663 */       HelpfulVillagers.network.sendToAll((IMessage)new InventoryPacket(this.owner.getEntityId(), null, this.equipmentInventory));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTTagList writeToNBT(NBTTagList par1NBTTagList) {
/*     */     int i;
/* 674 */     for (i = 0; i < this.mainInventory.length; i++) {
/* 675 */       if (this.mainInventory[i] != null) {
/* 676 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 677 */         nbttagcompound.setByte("Slot", (byte)i);
/* 678 */         this.mainInventory[i].writeToNBT(nbttagcompound);
/* 679 */         par1NBTTagList.appendTag((NBTBase)nbttagcompound);
/*     */       } 
/*     */     } 
/*     */     
/* 683 */     for (i = 0; i < this.equipmentInventory.length; i++) {
/* 684 */       if (this.equipmentInventory[i] != null) {
/* 685 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 686 */         nbttagcompound.setByte("Slot", (byte)(i + this.mainInventory.length));
/* 687 */         this.equipmentInventory[i].writeToNBT(nbttagcompound);
/* 688 */         par1NBTTagList.appendTag((NBTBase)nbttagcompound);
/*     */       } 
/*     */     } 
/*     */     
/* 692 */     for (ItemStack itemStack : this.materialsCollected) {
/* 693 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 694 */       nbttagcompound.setByte("Slot", (byte)-1);
/* 695 */       itemStack.writeToNBT(nbttagcompound);
/* 696 */       par1NBTTagList.appendTag((NBTBase)nbttagcompound);
/*     */     } 
/*     */     
/* 699 */     for (ItemStack itemStack : this.smeltablesCollected) {
/* 700 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 701 */       nbttagcompound.setByte("Slot", (byte)-1);
/* 702 */       itemStack.writeToNBT(nbttagcompound);
/* 703 */       par1NBTTagList.appendTag((NBTBase)nbttagcompound);
/*     */     } 
/*     */     
/* 706 */     return par1NBTTagList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagList par1NBTTagList) {
/* 716 */     for (int i = 0; i < par1NBTTagList.tagCount(); i++) {
/* 717 */       if (par1NBTTagList.tagCount() > 0) {
/* 718 */         NBTTagCompound nbttagcompound = par1NBTTagList.getCompoundTagAt(i);
/* 719 */         byte slot = nbttagcompound.getByte("Slot");
/* 720 */         ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);
/* 721 */         if (slot >= 0) {
/* 722 */   setInventorySlotContents(slot, itemstack);
/*     */         } else {
/* 724 */           addItem(itemstack);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCustomInventoryName() {
/* 732 */     return false;
/*     */   }
/*     */   
/*     */   public void openChest() {}
/*     */   
/*     */   public void closeChest() {}
/*     */   
/*     */   public void markDirty() {}
/*     */
@Override
public String getName()
{
    return this.inventoryTitle;
}
@Override
public boolean hasCustomName()
{
    return false;
}
@Override
public ItemStack removeStackFromSlot(int index) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void openInventory(EntityPlayer player) {
	// TODO Auto-generated method stub
	
}
@Override
public void closeInventory(EntityPlayer player) {
	// TODO Auto-generated method stub
	
}
@Override
public int getField(int id) {
	return 0;
}
@Override
public void setField(int id, int value) {
}
@Override
public int getFieldCount() {
	return 0;
}
@Override
public void clear() {
	for (int i = 0; i < this.mainInventory.length; ++i)
    {
        this.mainInventory[i] = null;
    }
	for (int i = 0; i < this.equipmentInventory.length; ++i)
    {
        this.equipmentInventory[i] = null;
    }
}
@Override
public IChatComponent getDisplayName() {
	return null;
} }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\inventory\InventoryVillager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */