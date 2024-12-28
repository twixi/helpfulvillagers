/*    */ package mods.helpfulvillagers.network;
/*    */ 
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import mods.helpfulvillagers.entity.EntityLumberjack;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.WorldClient;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SaplingPacket
/*    */   implements IMessage
/*    */ {
/*    */   private int id;
/*    */   private boolean shouldPlant;
/*    */   
/*    */   public SaplingPacket() {}
/*    */   
/*    */   public SaplingPacket(int id, boolean shouldPlant) {
/* 24 */     this.id = id;
/* 25 */     this.shouldPlant = shouldPlant;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 30 */     buffer.writeInt(this.id);
/* 31 */     buffer.writeBoolean(this.shouldPlant);
/*    */   }
/*    */ 
/*    */   
/*    */   public void fromBytes(ByteBuf buffer) {
/* 36 */     this.id = buffer.readInt();
/* 37 */     this.shouldPlant = buffer.readBoolean();
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     implements IMessageHandler<SaplingPacket, IMessage> {
/*    */     public IMessage onMessage(SaplingPacket message, MessageContext ctx) {
/*    */       try {
/* 44 */         if (ctx.side == Side.CLIENT) {
/* 45 */           Minecraft mc = Minecraft.getMinecraft();
/* 46 */           WorldClient world = mc.theWorld;
/* 47 */           EntityLumberjack entity = (EntityLumberjack)world.getEntityByID(message.id);
/* 48 */           entity.shouldPlant = message.shouldPlant;
/*    */         } 
/* 50 */       } catch (NullPointerException e) {}
/*    */ 
/*    */       
/* 53 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\SaplingPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */