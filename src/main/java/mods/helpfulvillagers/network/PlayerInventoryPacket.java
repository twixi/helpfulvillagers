/*     */ package mods.helpfulvillagers.network;
/*     */ 
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.ByteBufUtils;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerInventoryPacket
/*     */   implements IMessage
/*     */ {
/*     */   private int id;
/*  23 */   private ItemStack[] main = new ItemStack[36];
/*     */   private boolean sendMain;
/*  25 */   private ItemStack[] equipment = new ItemStack[4];
/*     */   
/*     */   private boolean sendEquip;
/*     */ 
/*     */   
/*     */   public PlayerInventoryPacket(int id, ItemStack[] main, ItemStack[] equipment) {
/*  31 */     this.id = id;
/*  32 */     if (main != null) {
/*  33 */       System.arraycopy(main, 0, this.main, 0, main.length);
/*  34 */       this.sendMain = true;
/*     */     } else {
/*  36 */       this.sendMain = false;
/*     */     } 
/*     */     
/*  39 */     if (equipment != null) {
/*  40 */       System.arraycopy(equipment, 0, this.equipment, 0, equipment.length);
/*  41 */       this.sendEquip = true;
/*     */     } else {
/*  43 */       this.sendEquip = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void toBytes(ByteBuf buffer) {
/*  48 */     buffer.writeInt(this.id);
/*  49 */     buffer.writeBoolean(this.sendMain);
/*  50 */     buffer.writeBoolean(this.sendEquip);
/*     */     
/*  52 */     if (this.sendMain) {
/*  53 */       for (int i = 0; i < this.main.length; i++) {
/*  54 */         ByteBufUtils.writeItemStack(buffer, this.main[i]);
/*     */       }
/*     */     }
/*     */     
/*  58 */     if (this.sendEquip) {
/*  59 */       for (int i = 0; i < this.equipment.length; i++) {
/*  60 */         ByteBufUtils.writeItemStack(buffer, this.equipment[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void fromBytes(ByteBuf buffer) {
/*  66 */     this.id = buffer.readInt();
/*  67 */     this.sendMain = buffer.readBoolean();
/*  68 */     this.sendEquip = buffer.readBoolean();
/*     */     
/*  70 */     if (this.sendMain) {
/*  71 */       for (int i = 0; i < this.main.length; i++) {
/*  72 */         this.main[i] = ByteBufUtils.readItemStack(buffer);
/*     */       }
/*     */     }
/*     */     
/*  76 */     if (this.sendEquip)
/*  77 */       for (int i = 0; i < this.equipment.length; i++)
/*  78 */         this.equipment[i] = ByteBufUtils.readItemStack(buffer);  
/*     */   }
/*     */   
/*     */   public PlayerInventoryPacket() {}
/*     */   
/*     */   public static class Handler implements IMessageHandler<PlayerInventoryPacket, IMessage> {
/*     */     public IMessage onMessage(PlayerInventoryPacket message, MessageContext ctx) {
/*     */       try {
/*  86 */         if (ctx.side == Side.SERVER) {
/*  87 */           World world = (ctx.getServerHandler()).playerEntity.worldObj;
/*  88 */           EntityPlayer entity = (EntityPlayer)world.getEntityByID(message.id);
/*  89 */           if (entity == null) {
/*  90 */             return null;
/*     */           }
/*     */           int i;
/*  93 */           for (i = 0; i < message.main.length; i++) {
/*  94 */             entity.inventory.setInventorySlotContents(i, message.main[i]);
/*     */           }
/*     */           
/*  97 */           for (i = 0; i < message.equipment.length; i++) {
/*  98 */        entity.inventory.setInventorySlotContents(i + message.main.length, message.equipment[i]);
/*     */           }
/*     */         } 
/* 101 */       } catch (NullPointerException e) {}
/*     */ 
/*     */ 
/*     */       
/* 105 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\PlayerInventoryPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */