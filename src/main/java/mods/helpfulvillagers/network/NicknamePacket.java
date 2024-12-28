/*    */ package mods.helpfulvillagers.network;
/*    */ 
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import mods.helpfulvillagers.entity.AbstractVillager;
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
/*    */ public class NicknamePacket
/*    */   implements IMessage
/*    */ {
/*    */   private int id;
/*    */   private String name;
/*    */   
/*    */   public NicknamePacket() {}
/*    */   
/*    */   public NicknamePacket(int id, String name) {
/* 29 */     this.id = id;
/* 30 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 35 */     buffer.writeInt(this.id);
/* 36 */     ByteBufUtils.writeUTF8String(buffer, this.name);
/*    */   }
/*    */ 
/*    */   
/*    */   public void fromBytes(ByteBuf buffer) {
/* 41 */     this.id = buffer.readInt();
/* 42 */     this.name = ByteBufUtils.readUTF8String(buffer);
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     implements IMessageHandler<NicknamePacket, IMessage> {
/*    */     public IMessage onMessage(NicknamePacket message, MessageContext ctx) {
/*    */       try {
/* 49 */         if (ctx.side == Side.SERVER) {
/* 50 */           EntityPlayerMP player = (ctx.getServerHandler()).playerEntity;
/* 51 */           World world = player.worldObj;
/* 52 */           AbstractVillager entity = (AbstractVillager)world.getEntityByID(message.id);
/* 53 */           entity.setCustomNameTag(message.name);
/*    */         } 
/* 55 */       } catch (NullPointerException e) {}
/*    */ 
/*    */       
/* 58 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\NicknamePacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */