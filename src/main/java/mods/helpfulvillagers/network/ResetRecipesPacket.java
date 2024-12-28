/*    */ package mods.helpfulvillagers.network;
/*    */ 
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import mods.helpfulvillagers.entity.AbstractVillager;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResetRecipesPacket
/*    */   implements IMessage
/*    */ {
/*    */   private int id;
/*    */   
/*    */   public ResetRecipesPacket() {}
/*    */   
/*    */   public ResetRecipesPacket(int id) {
/* 34 */     this.id = id;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 39 */     buffer.writeInt(this.id);
/*    */   }
/*    */ 
/*    */   
/*    */   public void fromBytes(ByteBuf buffer) {
/* 44 */     this.id = buffer.readInt();
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     implements IMessageHandler<ResetRecipesPacket, IMessage> {
/*    */     public IMessage onMessage(ResetRecipesPacket message, MessageContext ctx) {
/*    */       try {
/* 51 */         if (ctx.side == Side.SERVER) {
/* 52 */           World world = (ctx.getServerHandler()).playerEntity.worldObj;
/* 53 */           AbstractVillager entity = (AbstractVillager)world.getEntityByID(message.id);
/* 54 */           if (entity == null) {
/* 55 */             return null;
/*    */           }
/*    */           
/* 58 */           entity.resetRecipes();
/*    */         } 
/* 60 */       } catch (NullPointerException e) {}
/*    */ 
/*    */       
/* 63 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\ResetRecipesPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */