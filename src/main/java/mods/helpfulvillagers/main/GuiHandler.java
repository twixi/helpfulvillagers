/*     */ package mods.helpfulvillagers.main;
/*     */ 
/*     */ import net.minecraftforge.fml.common.network.IGuiHandler;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.gui.GuiBarter;
/*     */ import mods.helpfulvillagers.gui.GuiConstruction;
/*     */ import mods.helpfulvillagers.gui.GuiCraftStats;
/*     */ import mods.helpfulvillagers.gui.GuiCraftingMenu;
/*     */ import mods.helpfulvillagers.gui.GuiNickname;
/*     */ import mods.helpfulvillagers.gui.GuiProfessionDialog;
/*     */ import mods.helpfulvillagers.gui.GuiTeachRecipe;
/*     */ import mods.helpfulvillagers.gui.GuiVillagerDialog;
/*     */ import mods.helpfulvillagers.gui.GuiVillagerInventory;
/*     */ import mods.helpfulvillagers.inventory.ContainerInventoryVillager;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.world.World;
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
/*     */ public class GuiHandler
/*     */   implements IGuiHandler
/*     */ {
/*     */   public Object getServerGuiElement(int guiId, EntityPlayer player, World world, int posX, int posY, int posZ) {
/*  43 */     AbstractVillager villager = (AbstractVillager)world.getEntityByID(posX);
/*  44 */     if (villager != null) {
/*  45 */       switch (guiId) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 2:
/*  54 */           return new ContainerInventoryVillager((IInventory)player.inventory, (IInventory)villager.inventory, villager);
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
/*     */         case 6:
/*  66 */           return new GuiTeachRecipe.VillagerContainerWorkbench(player);
/*     */       } 
/*     */ 
/*     */     
/*     */     }
/*  71 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getClientGuiElement(int guiId, EntityPlayer player, World world, int posX, int posY, int posZ) {
/*  77 */     AbstractVillager villager = (AbstractVillager)world.getEntityByID(posX);
/*     */     
/*  79 */     if (villager != null) {
/*  80 */       switch (guiId) {
/*     */         case 0:
/*  82 */           return new GuiVillagerDialog(player, villager);
/*     */         case 1:
/*  84 */           return new GuiProfessionDialog(player, villager);
/*     */         case 2:
/*  86 */           return new GuiVillagerInventory(villager, (IInventory)player.inventory, (IInventory)villager.inventory);
/*     */         case 3:
/*  88 */           return new GuiNickname(player, villager);
/*     */         case 4:
/*  90 */           return new GuiCraftingMenu(player, villager);
/*     */         case 5:
/*  92 */           return new GuiCraftStats(player, villager);
/*     */         case 6:
/*  94 */           return new GuiTeachRecipe(player, villager);
/*     */         case 7:
/*  96 */           return new GuiBarter(player, villager);
/*     */         case 8:
/*  98 */           return new GuiConstruction(player, villager);
/*     */       } 
/*     */     
/*     */     }
/* 102 */     return null;
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\main\GuiHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */