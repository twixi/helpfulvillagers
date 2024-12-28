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
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LeaderPacket
/*    */   implements IMessage
/*    */ {
/*    */   private int id;
/*    */   private int leaderID;
/*    */   
/*    */   public LeaderPacket() {}
/*    */   
/*    */   public LeaderPacket(int id, int leaderID) {
/* 27 */     this.id = id;
/* 28 */     this.leaderID = leaderID;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 33 */     buffer.writeInt(this.id);
/* 34 */     buffer.writeInt(this.leaderID);
/*    */   }
/*    */ 
/*    */   
/*    */   public void fromBytes(ByteBuf buffer) {
/* 39 */     this.id = buffer.readInt();
/* 40 */     this.leaderID = buffer.readInt();
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     implements IMessageHandler<LeaderPacket, IMessage> {
/*    */     public IMessage onMessage(LeaderPacket message, MessageContext ctx) {
/*    */       try {
/* 47 */         if (ctx.side == Side.CLIENT) {
/* 48 */           Minecraft mc = Minecraft.getMinecraft();
/* 49 */           WorldClient world = mc.theWorld;
/* 50 */           AbstractVillager entity = (AbstractVillager)world.getEntityByID(message.id);
/* 51 */           if (message.leaderID < 0) {
/* 52 */             entity.leader = null;
/*    */           } else {
/* 54 */             EntityLivingBase leader = (EntityLivingBase)world.getEntityByID(message.leaderID);
/* 55 */             entity.leader = leader;
/*    */           } 
/*    */         } 
/* 58 */       } catch (NullPointerException e) {}
/*    */ 
/*    */       
/* 61 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\LeaderPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */