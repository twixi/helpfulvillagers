/*    */ package mods.helpfulvillagers.network;
/*    */ 
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import mods.helpfulvillagers.entity.AbstractVillager;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.WorldClient;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SwingPacket
/*    */   implements IMessage
/*    */ {
/*    */   private int id;
/*    */   
/*    */   public SwingPacket() {}
/*    */   
/*    */   public SwingPacket(int id) {
/* 24 */     this.id = id;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 29 */     buffer.writeInt(this.id);
/*    */   }
/*    */ 
/*    */   
/*    */   public void fromBytes(ByteBuf buffer) {
/* 34 */     this.id = buffer.readInt();
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     implements IMessageHandler<SwingPacket, IMessage> {
/*    */     public IMessage onMessage(SwingPacket message, MessageContext ctx) {
/*    */       try {
/* 41 */         if (ctx.side == Side.CLIENT) {
/* 42 */           Minecraft mc = Minecraft.getMinecraft();
/* 43 */           WorldClient world = mc.theWorld;
/* 44 */           AbstractVillager entity = (AbstractVillager)world.getEntityByID(message.id);
/* 45 */           entity.swingItem();
/*    */         } 
/* 47 */       } catch (NullPointerException e) {}
/*    */ 
/*    */       
/* 50 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\SwingPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */