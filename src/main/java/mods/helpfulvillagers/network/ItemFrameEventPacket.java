/*    */ package mods.helpfulvillagers.network;
/*    */ 
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.item.EntityItemFrame;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ItemFrameEventPacket
/*    */   implements IMessage
/*    */ {
/*    */   private int id;
/*    */   
/*    */   public ItemFrameEventPacket() {}
/*    */   
/*    */   public ItemFrameEventPacket(int id) {
/* 30 */     this.id = id;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 35 */     buffer.writeInt(this.id);
/*    */   }
/*    */ 
/*    */   
/*    */   public void fromBytes(ByteBuf buffer) {
/* 40 */     this.id = buffer.readInt();
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     implements IMessageHandler<ItemFrameEventPacket, IMessage> {
/*    */     public IMessage onMessage(ItemFrameEventPacket message, MessageContext ctx) {
/*    */       try {
/* 47 */         if (ctx.side == Side.SERVER) {
/* 48 */           EntityPlayerMP player = (ctx.getServerHandler()).playerEntity;
/* 49 */           World world = player.worldObj;
/* 50 */           Entity entity = world.getEntityByID(message.id);
/* 51 */           if (entity instanceof EntityItemFrame) {
/* 52 */             EntityItemFrame frame = (EntityItemFrame)entity;
/* 53 */             if (!HelpfulVillagers.checkedFrames.contains(frame)) {
/* 54 */               HelpfulVillagers.checkedFrames.add(frame);
/*    */             }
/*    */           } 
/*    */         } 
/* 58 */       } catch (NullPointerException e) {}
/*    */ 
/*    */       
/* 61 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\ItemFrameEventPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */