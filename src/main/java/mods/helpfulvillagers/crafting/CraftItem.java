/*     */ package mods.helpfulvillagers.crafting;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
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
/*     */ public class CraftItem
/*     */ {
/*     */   private ItemStack item;
/*     */   private String name;
/*     */   private int priority;
/*     */   private boolean metadataSensitive;
/*     */   
/*     */   private CraftItem() {}
/*     */   
/*     */   public CraftItem(ItemStack item, String name, int priority) {
/*  31 */     this.item = item;
/*  32 */     this.name = name;
/*  33 */     this.priority = priority;
/*  34 */     this.metadataSensitive = false;
/*     */   }
/*     */   
/*     */   public CraftItem(ItemStack item, EntityPlayer player) {
/*  38 */     this(item, player.getName(), 1);
/*     */   }
/*     */   
/*     */   public CraftItem(ItemStack item, AbstractVillager villager) {
/*  42 */     this(item, villager.getCommandSenderEntity().getName() + " the " + villager.profName, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getItem() {
/*  49 */     return this.item;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  56 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPriority() {
/*  63 */     return this.priority;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSensitive() {
/*  70 */     return this.metadataSensitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSensitivity(boolean b) {
/*  78 */     this.metadataSensitive = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getTooltip() {
/*  86 */     List<String> list = new ArrayList<String>();
/*  87 */     list.add(this.getName() + " x" + this.item.stackSize);
/*  88 */     if (this.priority >= 1) {
/*  89 */       list.add("Requested by Player:");
/*     */     } else {
/*  91 */       list.add("Requested by Villager:");
/*     */     } 
/*  93 */     list.add(this.name);
/*  94 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTBase writeToNBT(NBTTagCompound compound) {
/* 103 */     this.item.writeToNBT(compound);
/* 104 */     compound.setString("Name", this.name);
/* 105 */     compound.setInteger("Priority", this.priority);
/* 106 */     compound.setBoolean("Metadata", this.metadataSensitive);
/* 107 */     return (NBTBase)compound;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound) {
/* 116 */     this.item = ItemStack.loadItemStackFromNBT(compound);
/*     */     
/* 118 */     this.name = compound.getString("Name");
/*     */     
/* 120 */     this.priority = compound.getInteger("Priority");
/*     */     
/* 122 */     this.metadataSensitive = compound.getBoolean("Metadata");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CraftItem loadCraftItemFromNBT(NBTTagCompound compound) {
/* 131 */     CraftItem item = new CraftItem();
/* 132 */     item.readFromNBT(compound);
/* 133 */     return (item.getItem() != null) ? item : null;
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\crafting\CraftItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */