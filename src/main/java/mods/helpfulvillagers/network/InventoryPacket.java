/*     */ package mods.helpfulvillagers.network;
/*     */ 
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.ByteBufUtils;
/*     */ import io.netty.buffer.ByteBuf;
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
/*     */ public class InventoryPacket
/*     */   implements IMessage
/*     */ {
/*     */   private int id;
/*  26 */   private ItemStack[] main = new ItemStack[27];
/*     */   private boolean sendMain;
/*  28 */   private ItemStack[] equipment = new ItemStack[5];
/*     */   
/*     */   private boolean sendEquip;
/*     */ 
/*     */   
/*     */   public InventoryPacket(int id, ItemStack[] main, ItemStack[] equipment) {
/*  34 */     this.id = id;
/*  35 */     if (main != null) {
/*  36 */       System.arraycopy(main, 0, this.main, 0, main.length);
/*  37 */       this.sendMain = true;
/*     */     } else {
/*  39 */       this.sendMain = false;
/*     */     } 
/*     */     
/*  42 */     if (equipment != null) {
/*  43 */       System.arraycopy(equipment, 0, this.equipment, 0, equipment.length);
/*  44 */       this.sendEquip = true;
/*     */     } else {
/*  46 */       this.sendEquip = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void toBytes(ByteBuf buffer) {
/*  52 */     buffer.writeInt(this.id);
/*  53 */     buffer.writeBoolean(this.sendMain);
/*  54 */     buffer.writeBoolean(this.sendEquip);
/*     */     
/*  56 */     if (this.sendMain) {
/*  57 */       for (int i = 0; i < this.main.length; i++) {
/*  58 */         ByteBufUtils.writeItemStack(buffer, this.main[i]);
/*     */       }
/*     */     }
/*     */     
/*  62 */     if (this.sendEquip) {
/*  63 */       for (int i = 0; i < this.equipment.length; i++) {
/*  64 */         ByteBufUtils.writeItemStack(buffer, this.equipment[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void fromBytes(ByteBuf buffer) {
/*  71 */     this.id = buffer.readInt();
/*  72 */     this.sendMain = buffer.readBoolean();
/*  73 */     this.sendEquip = buffer.readBoolean();
/*     */     
/*  75 */     if (this.sendMain) {
/*  76 */       for (int i = 0; i < this.main.length; i++) {
/*  77 */         this.main[i] = ByteBufUtils.readItemStack(buffer);
/*     */       }
/*     */     }
/*     */     
/*  81 */     if (this.sendEquip)
/*  82 */       for (int i = 0; i < this.equipment.length; i++)
/*  83 */         this.equipment[i] = ByteBufUtils.readItemStack(buffer);  
/*     */   }
/*     */   
/*     */   public InventoryPacket() {}
/*     */   
/*     */   public static class Handler
/*     */     implements IMessageHandler<InventoryPacket, IMessage> {
/*     */     public IMessage onMessage(InventoryPacket message, MessageContext ctx) {
/*     */       try {
/*  92 */         if (ctx.side == Side.CLIENT) {
/*  93 */           Minecraft mc = Minecraft.getMinecraft();
/*  94 */           WorldClient world = mc.theWorld;
/*  95 */           AbstractVillager entity = (AbstractVillager)world.getEntityByID(message.id);
/*  96 */           if (entity == null) {
/*  97 */             return null;
/*     */           }
/*     */           
/* 100 */           if (message.sendMain) {
/* 101 */             for (int i = 0; i < message.main.length; i++) {
/* 102 */               entity.inventory.setMainContents(i, message.main[i]);
/*     */             }
/*     */           }
/*     */           
/* 106 */           if (message.sendEquip) {
/* 107 */             for (int i = 0; i < message.equipment.length; i++) {
/* 108 */               entity.inventory.setEquipmentContents(i, message.equipment[i]);
/*     */             }
/*     */           }
/*     */         } 
/* 112 */       } catch (NullPointerException e) {}
/*     */ 
/*     */       
/* 115 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\InventoryPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */