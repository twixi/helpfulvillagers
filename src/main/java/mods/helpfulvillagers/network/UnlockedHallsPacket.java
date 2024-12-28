/*    */ package mods.helpfulvillagers.network;
/*    */ 
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import mods.helpfulvillagers.entity.AbstractVillager;
/*    */ import mods.helpfulvillagers.village.HelpfulVillage;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.WorldClient;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnlockedHallsPacket
/*    */   implements IMessage
/*    */ {
/*    */   private int id;
/* 24 */   private boolean[] unlockedHalls = new boolean[13];
/*    */ 
/*    */ 
/*    */   
/*    */   public UnlockedHallsPacket(int id, boolean[] unlockedHalls) {
/* 29 */     this.id = id;
/* 30 */     System.arraycopy(unlockedHalls, 0, this.unlockedHalls, 0, this.unlockedHalls.length);
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 35 */     buffer.writeInt(this.id);
/* 36 */     for (int i = 0; i < this.unlockedHalls.length; i++) {
/* 37 */       buffer.writeBoolean(this.unlockedHalls[i]);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void fromBytes(ByteBuf buffer) {
/* 43 */     this.id = buffer.readInt();
/* 44 */     for (int i = 0; i < this.unlockedHalls.length; i++)
/* 45 */       this.unlockedHalls[i] = buffer.readBoolean(); 
/*    */   }
/*    */   
/*    */   public UnlockedHallsPacket() {}
/*    */   
/*    */   public static class Handler implements IMessageHandler<UnlockedHallsPacket, IMessage> {
/*    */     public IMessage onMessage(UnlockedHallsPacket message, MessageContext ctx) {
/*    */       try {
/* 53 */         if (ctx.side == Side.CLIENT) {
/* 54 */           Minecraft mc = Minecraft.getMinecraft();
/* 55 */           WorldClient world = mc.theWorld;
/* 56 */           AbstractVillager entity = (AbstractVillager)world.getEntityByID(message.id);
/* 57 */           if (entity == null) {
/* 58 */             return null;
/*    */           }
/* 60 */           entity.homeVillage = new HelpfulVillage();
/* 61 */           System.arraycopy(message.unlockedHalls, 0, entity.homeVillage.unlockedHalls, 0, message.unlockedHalls.length);
/*    */         } 
/* 63 */       } catch (NullPointerException e) {}
/*    */ 
/*    */       
/* 66 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\UnlockedHallsPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */