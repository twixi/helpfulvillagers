/*    */ package mods.helpfulvillagers.network;
/*    */ 
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import mods.helpfulvillagers.entity.AbstractVillager;
/*    */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*    */ import mods.helpfulvillagers.village.HelpfulVillage;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.WorldClient;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillageSyncPacket
/*    */   implements IMessage
/*    */ {
/* 21 */   private int[] coords = new int[3];
/*    */   
/*    */   private int id;
/*    */ 
/*    */   
/*    */   public VillageSyncPacket(HelpfulVillage village, AbstractVillager villager) {
/* 27 */     this.coords[0] = village.initialCenter.getX();
/* 28 */     this.coords[1] = village.initialCenter.getY();
/* 29 */     this.coords[2] = village.initialCenter.getZ();
/* 30 */     this.id = villager.getEntityId();
/*    */   }
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 34 */     for (int i = 0; i < 3; i++) {
/* 35 */       buffer.writeInt(this.coords[i]);
/*    */     }
/*    */     
/* 38 */     buffer.writeInt(this.id);
/*    */   }
/*    */   public VillageSyncPacket() {}
/*    */   public void fromBytes(ByteBuf buffer) {
/* 42 */     for (int i = 0; i < 3; i++) {
/* 43 */       this.coords[i] = buffer.readInt();
/*    */     }
/*    */     
/* 46 */     this.id = buffer.readInt();
/*    */   }
/*    */   
/*    */   public static class Handler implements IMessageHandler<VillageSyncPacket, IMessage> {
/*    */     public IMessage onMessage(VillageSyncPacket message, MessageContext ctx) {
/*    */       try {
/* 52 */         if (ctx.side == Side.CLIENT) {
/* 53 */           Minecraft mc = Minecraft.getMinecraft();
/* 54 */           WorldClient world = mc.theWorld;
/* 55 */           AbstractVillager entity = (AbstractVillager)world.getEntityByID(message.id);
/* 56 */           if (entity == null) {
/* 57 */             return null;
/*    */           }
/*    */           
/* 60 */           BlockPos coords = new BlockPos(message.coords[0], message.coords[1], message.coords[2]);
/*    */           
/* 62 */           for (HelpfulVillage village : HelpfulVillagers.villages) {
/* 63 */             if (village.initialCenter.equals(entity.homeVillage.initialCenter)) {
/* 64 */               village.initialCenter = coords;
/*    */             }
/*    */           } 
/* 67 */           entity.homeVillage.initialCenter = coords;
/* 68 */           entity.villageCenter = coords;
/*    */         } 
/* 70 */       } catch (NullPointerException e) {}
/*    */ 
/*    */ 
/*    */       
/* 74 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\VillageSyncPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */