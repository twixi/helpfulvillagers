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
/*    */ public class ProfessionChangePacket
/*    */   implements IMessage
/*    */ {
/*    */   private int id;
/*    */   private int newProf;
/*    */   
/*    */   public ProfessionChangePacket() {}
/*    */   
/*    */   public ProfessionChangePacket(int id, int newProf) {
/* 27 */     this.id = id;
/* 28 */     this.newProf = newProf;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 33 */     buffer.writeInt(this.id);
/* 34 */     buffer.writeInt(this.newProf);
/*    */   }
/*    */ 
/*    */   
/*    */   public void fromBytes(ByteBuf buffer) {
/* 39 */     this.id = buffer.readInt();
/* 40 */     this.newProf = buffer.readInt();
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     implements IMessageHandler<ProfessionChangePacket, IMessage> {
/*    */     public IMessage onMessage(ProfessionChangePacket message, MessageContext ctx) {
/*    */       try {
/* 47 */         if (ctx.side == Side.SERVER) {
/* 48 */           EntityPlayerMP player = (ctx.getServerHandler()).playerEntity;
/* 49 */           World world = player.worldObj;
/* 50 */           AbstractVillager entity = (AbstractVillager)world.getEntityByID(message.id);
/* 51 */           entity.setProfession(message.newProf);
/*    */         } 
/* 53 */       } catch (NullPointerException e) {}
/*    */ 
/*    */       
/* 56 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\ProfessionChangePacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */