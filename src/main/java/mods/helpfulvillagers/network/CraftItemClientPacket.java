/*     */ package mods.helpfulvillagers.network;
/*     */ 
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.ByteBufUtils;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.ArrayList;
/*     */ import mods.helpfulvillagers.crafting.CraftItem;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.item.ItemStack;
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
/*     */ public class CraftItemClientPacket
/*     */   implements IMessage
/*     */ {
/*     */   private int id;
/*     */   private CraftItem currentCraftItem;
/*  32 */   private ArrayList<ItemStack> materialsCollected = new ArrayList<ItemStack>();
/*     */   
/*     */   private int collectedSize;
/*  35 */   private ArrayList<ItemStack> materialsNeeded = new ArrayList<ItemStack>();
/*     */   
/*     */   private int neededSize;
/*     */ 
/*     */   
/*     */   public CraftItemClientPacket(int id, CraftItem craftItem, ArrayList<ItemStack> materialsCollected, ArrayList<ItemStack> materialsNeeded) {
/*  41 */     this.id = id;
/*  42 */     this.currentCraftItem = craftItem;
/*     */     
/*  44 */     this.materialsCollected.addAll(materialsCollected);
/*  45 */     this.collectedSize = materialsCollected.size();
/*     */     
/*  47 */     this.materialsNeeded.addAll(materialsNeeded);
/*  48 */     this.neededSize = materialsNeeded.size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void toBytes(ByteBuf buffer) {
/*  54 */     buffer.writeInt(this.id);
/*     */     
/*  56 */     if (this.currentCraftItem != null) {
/*  57 */       buffer.writeBoolean(true);
/*  58 */       ByteBufUtils.writeItemStack(buffer, this.currentCraftItem.getItem());
/*  59 */       ByteBufUtils.writeUTF8String(buffer, this.currentCraftItem.getName());
/*  60 */       buffer.writeInt(this.currentCraftItem.getPriority());
/*     */     } else {
/*  62 */       buffer.writeBoolean(false);
/*     */     } 
/*     */     
/*  65 */     buffer.writeInt(this.collectedSize);
/*  66 */     for (ItemStack i : this.materialsCollected) {
/*  67 */       ByteBufUtils.writeItemStack(buffer, i);
/*     */     }
/*     */     
/*  70 */     buffer.writeInt(this.neededSize);
/*  71 */     for (ItemStack i : this.materialsNeeded) {
/*  72 */       ByteBufUtils.writeItemStack(buffer, i);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fromBytes(ByteBuf buffer) {
/*  83 */     this.id = buffer.readInt();
/*     */     
/*  85 */     boolean read = buffer.readBoolean();
/*     */     
/*  87 */     if (read) {
/*  88 */       ItemStack item = ByteBufUtils.readItemStack(buffer);
/*  89 */       String name = ByteBufUtils.readUTF8String(buffer);
/*  90 */       int priority = buffer.readInt();
/*  91 */       this.currentCraftItem = new CraftItem(item, name, priority);
/*     */     } else {
/*  93 */       this.currentCraftItem = null;
/*     */     } 
/*     */     
/*  96 */     this.collectedSize = buffer.readInt(); int i;
/*  97 */     for (i = 0; i < this.collectedSize; i++) {
/*  98 */       this.materialsCollected.add(ByteBufUtils.readItemStack(buffer));
/*     */     }
/*     */     
/* 101 */     this.neededSize = buffer.readInt();
/* 102 */     for (i = 0; i < this.neededSize; i++)
/* 103 */       this.materialsNeeded.add(ByteBufUtils.readItemStack(buffer)); 
/*     */   }
/*     */   
/*     */   public CraftItemClientPacket() {}
/*     */   
/*     */   public static class Handler implements IMessageHandler<CraftItemClientPacket, IMessage> {
/*     */     public IMessage onMessage(CraftItemClientPacket message, MessageContext ctx) {
/*     */       try {
/* 111 */         if (ctx.side == Side.CLIENT) {
/* 112 */           Minecraft mc = Minecraft.getMinecraft();
/* 113 */           WorldClient world = mc.theWorld;
/* 114 */           AbstractVillager entity = (AbstractVillager)world.getEntityByID(message.id);
/* 115 */           if (entity == null) {
/* 116 */             return null;
/*     */           }
/*     */           
/* 119 */           entity.currentCraftItem = message.currentCraftItem;
/*     */           
/* 121 */           entity.inventory.materialsCollected.clear();
/* 122 */           entity.inventory.materialsCollected.addAll(message.materialsCollected);
/*     */           
/* 124 */           entity.materialsNeeded.clear();
/* 125 */           entity.materialsNeeded.addAll(message.materialsNeeded);
/*     */         }
/*     */       
/* 128 */       } catch (NullPointerException e) {}
/*     */ 
/*     */       
/* 131 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\CraftItemClientPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */