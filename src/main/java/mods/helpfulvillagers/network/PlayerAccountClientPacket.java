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
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PlayerAccountClientPacket
/*    */   implements IMessage
/*    */ {
/*    */   private int playerID;
/*    */   private int villagerID;
/*    */   private int amount;
/*    */   
/*    */   public PlayerAccountClientPacket() {}
/*    */   
/*    */   public PlayerAccountClientPacket(EntityPlayer player, AbstractVillager villager) {
/* 28 */     this.playerID = player.getEntityId();
/* 29 */     this.villagerID = villager.getEntityId();
/* 30 */     this.amount = villager.homeVillage.economy.getAccount(player);
/*    */   }
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 34 */     buffer.writeInt(this.playerID);
/* 35 */     buffer.writeInt(this.villagerID);
/* 36 */     buffer.writeInt(this.amount);
/*    */   }
/*    */   
/*    */   public void fromBytes(ByteBuf buffer) {
/* 40 */     this.playerID = buffer.readInt();
/* 41 */     this.villagerID = buffer.readInt();
/* 42 */     this.amount = buffer.readInt();
/*    */   }
/*    */   
/*    */   public static class Handler implements IMessageHandler<PlayerAccountClientPacket, IMessage> {
/*    */     public IMessage onMessage(PlayerAccountClientPacket message, MessageContext ctx) {
/*    */       try {
/* 48 */         if (ctx.side == Side.CLIENT) {
/* 49 */           Minecraft mc = Minecraft.getMinecraft();
/* 50 */           WorldClient world = mc.theWorld;
/*    */           
/* 52 */           EntityPlayer player = (EntityPlayer)world.getEntityByID(message.playerID);
/* 53 */           if (player == null) {
/* 54 */             return null;
/*    */           }
/*    */           
/* 57 */           AbstractVillager villager = (AbstractVillager)world.getEntityByID(message.villagerID);
/* 58 */           if (villager == null) {
/* 59 */             return null;
/*    */           }
/*    */           
/* 62 */           villager.homeVillage.economy.setAccount(player, message.amount);
/*    */         } 
/* 64 */       } catch (NullPointerException e) {}
/*    */ 
/*    */ 
/*    */       
/* 68 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\PlayerAccountClientPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */