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
/*    */ public class GUICommandPacket
/*    */   implements IMessage
/*    */ {
/*    */   private int id;
/*    */   private int command;
/*    */   
/*    */   public GUICommandPacket() {}
/*    */   
/*    */   public GUICommandPacket(int id, int command) {
/* 28 */     this.id = id;
/* 29 */     this.command = command;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 34 */     buffer.writeInt(this.id);
/* 35 */     buffer.writeInt(this.command);
/*    */   }
/*    */ 
/*    */   
/*    */   public void fromBytes(ByteBuf buffer) {
/* 40 */     this.id = buffer.readInt();
/* 41 */     this.command = buffer.readInt();
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     implements IMessageHandler<GUICommandPacket, IMessage> {
/*    */     public IMessage onMessage(GUICommandPacket message, MessageContext ctx) {
/*    */       try {
/* 48 */         if (ctx.side == Side.SERVER) {
/* 49 */           EntityPlayerMP player = (ctx.getServerHandler()).playerEntity;
/* 50 */           World world = player.worldObj;
/* 51 */           AbstractVillager entity = (AbstractVillager)world.getEntityByID(message.id);
/* 52 */           entity.guiCommand = message.command;
/*    */         } 
/* 54 */       } catch (NullPointerException e) {}
/*    */ 
/*    */       
/* 57 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\GUICommandPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */