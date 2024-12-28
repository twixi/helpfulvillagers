/*     */ package mods.helpfulvillagers.network;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.enums.EnumMessage;
/*     */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*     */ import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.IChatComponent;
		import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
		import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
		import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
		import net.minecraftforge.fml.relauncher.Side;
		import net.minecraftforge.fml.common.network.ByteBufUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerMessagePacket
/*     */   implements IMessage
/*     */ {
/*     */   private String message;
/*     */   private int messageType;
/*     */   private boolean hasEntity;
/*     */   private int senderID;
/*  29 */   private int[] coords = new int[3];
/*     */ 
/*     */ 
/*     */   
/*     */   public PlayerMessagePacket(String message, EnumMessage messageType, int senderID) {
/*  34 */     this.message = message;
/*  35 */     this.senderID = senderID;
/*  36 */     this.hasEntity = true;
/*  37 */     switch (messageType) {
/*     */       case DEATH:
/*  39 */         this.messageType = 0;
/*     */         return;
/*     */       case BIRTH:
/*  42 */         this.messageType = 1;
/*     */         return;
/*     */       case CONSTRUCTION:
/*  45 */         this.messageType = 2;
/*     */         return;
/*     */     } 
/*  48 */     this.messageType = -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerMessagePacket(String message, EnumMessage messageType, BlockPos coords) {
/*  53 */     this.message = message;
/*  54 */     this.coords[0] = coords.getX();
/*  55 */     this.coords[1] = coords.getY();
/*  56 */     this.coords[2] = coords.getZ();
/*  57 */     this.hasEntity = false;
/*  58 */     System.out.println("Type " + messageType);
/*  59 */     switch (messageType) {
/*     */       case DEATH:
/*  61 */         this.messageType = 0;
/*     */         return;
/*     */       case BIRTH:
/*  64 */         this.messageType = 1;
/*     */         return;
/*     */       case CONSTRUCTION:
/*  67 */         this.messageType = 2;
/*     */         return;
/*     */     } 
/*  70 */     this.messageType = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void toBytes(ByteBuf buffer) {
/*  76 */     ByteBufUtils.writeUTF8String(buffer, this.message);
/*  77 */     buffer.writeInt(this.messageType);
/*  78 */     buffer.writeBoolean(this.hasEntity);
/*  79 */     if (this.hasEntity) {
/*  80 */       buffer.writeInt(this.coords[0]);
/*  81 */       buffer.writeInt(this.coords[1]);
/*  82 */       buffer.writeInt(this.coords[2]);
/*     */     } else {
/*  84 */       buffer.writeInt(this.senderID);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void fromBytes(ByteBuf buffer) {
/*  90 */     this.message = ByteBufUtils.readUTF8String(buffer);
/*  91 */     this.messageType = buffer.readInt();
/*  92 */     this.hasEntity = buffer.readBoolean();
/*  93 */     if (this.hasEntity) {
/*  94 */       this.coords[0] = buffer.readInt();
/*  95 */       this.coords[1] = buffer.readInt();
/*  96 */       this.coords[2] = buffer.readInt();
/*     */     } else {
/*  98 */       this.senderID = buffer.readInt();
/*     */     } 
/*     */   }
/*     */   
/*     */   public PlayerMessagePacket() {}
/*     */   
/*     */   public static class Handler implements IMessageHandler<PlayerMessagePacket, IMessage> { public IMessage onMessage(PlayerMessagePacket packet, MessageContext ctx) {
/* 105 */       String message = null;
/*     */       try {
/* 107 */         if (ctx.side == Side.CLIENT) {
/* 108 */           String senderLoc; int option; Minecraft mc = Minecraft.getMinecraft();
/*     */           
/* 110 */           if (packet.hasEntity) {
/* 111 */             AbstractVillager sender = (AbstractVillager)mc.theWorld.getEntityByID(packet.senderID);
/* 112 */             senderLoc = (int)sender.posX + ", " + (int)sender.posY + ", " + (int)sender.posZ;
/*     */           } else {
/* 114 */             senderLoc = packet.coords[0] + ", " + packet.coords[1] + ", " + packet.coords[2];
/*     */           } 
/*     */           
/* 117 */           switch (packet.messageType) {
/*     */             case 0:
/* 119 */               option = HelpfulVillagers.deathMessageOption;
/* 120 */               if (option == 1) {
/* 121 */                 message = packet.message; break;
/* 122 */               }  if (option == 2) {
/* 123 */                 message = packet.message + " at " + senderLoc;
/*     */               }
/*     */               break;
/*     */             case 1:
/* 127 */               option = HelpfulVillagers.birthMessageOption;
/* 128 */               if (option == 1) {
/* 129 */                 message = packet.message; break;
/* 130 */               }  if (option == 2) {
/* 131 */                 message = packet.message + " at " + senderLoc;
/*     */               }
/*     */               break;
/*     */             case 2:
/* 135 */               option = HelpfulVillagers.constructionMessageOption;
/* 136 */               if (option == 1) {
/* 137 */                 message = packet.message; break;
/* 138 */               }  if (option == 2) {
/* 139 */                 message = packet.message + " at " + senderLoc;
/*     */               }
/*     */               break;
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 146 */           if (message != null) {
/* 147 */             mc.thePlayer.addChatMessage((IChatComponent)new ChatComponentText(message));
/*     */           }
/*     */         } 
/* 150 */       } catch (NullPointerException e) {
/* 151 */         e.printStackTrace();
/*     */       } 
/* 153 */       return null;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\PlayerMessagePacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */