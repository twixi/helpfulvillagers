/*    */ package mods.helpfulvillagers.network;
/*    */ 
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import mods.helpfulvillagers.enums.EnumMessage;
/*    */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MessageOptionsPacket
/*    */   implements IMessage
/*    */ {
/*    */   private int messageType;
/*    */   private int option;
/*    */   
/*    */   public MessageOptionsPacket() {}
/*    */   
/*    */   public MessageOptionsPacket(EnumMessage messageType, int option) {
/* 30 */     this.option = option;
/* 31 */     switch (messageType) {
/*    */       case DEATH:
/* 33 */         this.messageType = 0;
/*    */         return;
/*    */       case BIRTH:
/* 36 */         this.messageType = 1;
/*    */         return;
/*    */       case CONSTRUCTION:
/* 39 */         this.messageType = 2;
/*    */         return;
/*    */     } 
/* 42 */     this.messageType = -1;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 48 */     buffer.writeInt(this.messageType);
/* 49 */     buffer.writeInt(this.option);
/*    */   }
/*    */ 
/*    */   
/*    */   public void fromBytes(ByteBuf buffer) {
/* 54 */     this.messageType = buffer.readInt();
/* 55 */     this.option = buffer.readInt();
/*    */   }
/*    */   
/*    */   public static class Handler implements IMessageHandler<MessageOptionsPacket, IMessage> {
/*    */     public IMessage onMessage(MessageOptionsPacket packet, MessageContext ctx) {
/*    */       try {
/* 61 */         if (ctx.side == Side.CLIENT) {
/* 62 */           switch (packet.messageType) {
/*    */             case 0:
/* 64 */               HelpfulVillagers.deathMessageOption = packet.option;
/*    */               break;
/*    */             
/*    */             case 1:
/* 68 */               HelpfulVillagers.birthMessageOption = packet.option;
/*    */               break;
/*    */             
/*    */             case 2:
/* 72 */               HelpfulVillagers.constructionMessageOption = packet.option;
/*    */               break;
/*    */           } 
/*    */ 
/*    */ 
/*    */         
/*    */         }
/* 79 */       } catch (NullPointerException e) {
/* 80 */         e.printStackTrace();
/*    */       } 
/* 82 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\MessageOptionsPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */