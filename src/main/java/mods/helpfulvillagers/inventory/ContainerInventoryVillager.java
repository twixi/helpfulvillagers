/*     */ package mods.helpfulvillagers.inventory;
/*     */ 
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.ItemStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContainerInventoryVillager
/*     */   extends Container
/*     */ {
/*     */   private AbstractVillager villager;
/*     */   
/*     */   public ContainerInventoryVillager(IInventory inventoryPlayer, IInventory inventoryVillager, AbstractVillager villager) {
/*     */     try {
/*  21 */       this.villager = villager;
/*  22 */       int slotIndex = 0;
/*     */ 
/*     */       
/*  25 */       for (int j = 0; j < 3; j++) {
/*  26 */         for (int width = 0; width < 9; width++) {
/*  27 */           addSlotToContainer(new Slot(inventoryVillager, slotIndex, width * 18 + 8, j * 18 + 9));
/*  28 */           slotIndex++;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/*  33 */       for (int slot = 0; slot < 5; slot++) {
/*  34 */   addSlotToContainer(new Slot(inventoryVillager, slotIndex, slot * 18 + 43, 68));
/*  35 */         slotIndex++;
/*     */       } 
/*     */ 
/*     */       
/*  39 */       slotIndex = 0;
/*  40 */       for (int height = 0; height < 3; height++) {
/*  41 */         for (int width = 0; width < 9; width++) {
/*  42 */     addSlotToContainer(new Slot(inventoryPlayer, slotIndex + 9, width * 18 + 8, height * 18 + 93));
/*  43 */           slotIndex++;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/*  48 */       for (int i = 0; i < 9; i++) {
/*  49 */   addSlotToContainer(new Slot(inventoryPlayer, i, i * 18 + 8, 151));
/*     */       }
/*  51 */     } catch (ArrayIndexOutOfBoundsException e) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canInteractWith(EntityPlayer var1) {
/*  59 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
/*     */     try {
/*  66 */       Slot slot = this.inventorySlots.get(slotId);
/*  67 */       ItemStack transferStack = null;
/*     */       
/*  69 */       if (slot != null && slot.getHasStack()) {
/*  70 */         ItemStack slotStack = slot.getStack();
/*  71 */         transferStack = slotStack.copy();
/*     */         
/*  73 */         if (slotId < 32) {
/*  74 */           if (!mergeItemStack(slotStack, 32, this.inventorySlots.size(), true)) {
/*  75 */             return null;
/*     */           }
/*  77 */         } else if (mergeItemStack(slotStack, 0, 32, false)) {
/*  78 */           return null;
/*     */         } 
/*     */         
/*  81 */         if (slotStack.stackSize <= 0) {
/*  82 */           slot.putStack(null);
/*     */         } else {
/*  84 */           slot.onSlotChanged();
/*     */         } 
/*     */       } 
/*     */       
/*  88 */       return transferStack;
/*  89 */     } catch (ArrayIndexOutOfBoundsException e) {
/*  90 */       e.printStackTrace();
/*  91 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onContainerClosed(EntityPlayer par1EntityPlayer) {
/*  97 */     for (int i = 27; i < 32; i++) {
/*  98 */       if (!this.villager.inventory.isItemValidForSlot(i, getSlot(i).getStack())) {
/*  99 */         this.villager.inventory.dropFromInventory(i);
/* 100 */         getSlot(i).putStack(null);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\inventory\ContainerInventoryVillager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */