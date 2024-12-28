/*     */ package mods.helpfulvillagers.command;
/*     */ 
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import mods.helpfulvillagers.enums.EnumMessage;
/*     */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*     */ import mods.helpfulvillagers.network.MessageOptionsPacket;
/*     */ import net.minecraft.command.CommandBase;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VillagerMessagesCommand
/*     */   extends CommandBase
/*     */ {
/*     */   private List aliases;
/*     */   
/*     */   public VillagerMessagesCommand() {
/*  28 */     this.aliases = new ArrayList();
/*  29 */     this.aliases.add("villagermessages");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCommandName() {
/*  37 */     return "villagermessages";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender icommandsender) {
/*  45 */     return "/villagermessages <death:birth:construction:all> <on:off:verbose>";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getCommandAliases() {
/*  53 */     return this.aliases;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender icommandsender, String[] astring) {
/*  61 */     ChatComponentText errorText = new ChatComponentText(getCommandUsage(icommandsender));
/*  62 */     errorText.getChatStyle().setColor(EnumChatFormatting.RED);
/*     */     
/*  64 */     if (astring.length == 2) {
/*  65 */       if (astring[0].equals("birth")) {
/*  66 */         if (astring[1].equals("on")) {
/*  67 */           HelpfulVillagers.network.sendTo((IMessage)new MessageOptionsPacket(EnumMessage.BIRTH, 1), (EntityPlayerMP)icommandsender);
/*  68 */           icommandsender.addChatMessage((IChatComponent)new ChatComponentText("Birth messages set: on"));
/*  69 */         } else if (astring[1].equals("off")) {
/*  70 */           HelpfulVillagers.network.sendTo((IMessage)new MessageOptionsPacket(EnumMessage.BIRTH, 0), (EntityPlayerMP)icommandsender);
/*  71 */           icommandsender.addChatMessage((IChatComponent)new ChatComponentText("Birth messages set: off"));
/*  72 */         } else if (astring[1].equals("verbose")) {
/*  73 */           HelpfulVillagers.network.sendTo((IMessage)new MessageOptionsPacket(EnumMessage.BIRTH, 2), (EntityPlayerMP)icommandsender);
/*  74 */           icommandsender.addChatMessage((IChatComponent)new ChatComponentText("Birth messages set: verbose"));
/*     */         } else {
/*  76 */           icommandsender.addChatMessage((IChatComponent)errorText);
/*     */         } 
/*  78 */       } else if (astring[0].equals("death")) {
/*  79 */         if (astring[1].equals("on")) {
/*  80 */           HelpfulVillagers.network.sendTo((IMessage)new MessageOptionsPacket(EnumMessage.DEATH, 1), (EntityPlayerMP)icommandsender);
/*  81 */           icommandsender.addChatMessage((IChatComponent)new ChatComponentText("Death messages set: on"));
/*  82 */         } else if (astring[1].equals("off")) {
/*  83 */           HelpfulVillagers.network.sendTo((IMessage)new MessageOptionsPacket(EnumMessage.DEATH, 0), (EntityPlayerMP)icommandsender);
/*  84 */           icommandsender.addChatMessage((IChatComponent)new ChatComponentText("Death messages set: off"));
/*  85 */         } else if (astring[1].equals("verbose")) {
/*  86 */           HelpfulVillagers.network.sendTo((IMessage)new MessageOptionsPacket(EnumMessage.DEATH, 2), (EntityPlayerMP)icommandsender);
/*  87 */           icommandsender.addChatMessage((IChatComponent)new ChatComponentText("Death messages set: verbose"));
/*     */         } else {
/*  89 */           icommandsender.addChatMessage((IChatComponent)errorText);
/*     */         } 
/*  91 */       } else if (astring[0].equals("construction")) {
/*  92 */         if (astring[1].equals("on")) {
/*  93 */           HelpfulVillagers.network.sendTo((IMessage)new MessageOptionsPacket(EnumMessage.CONSTRUCTION, 1), (EntityPlayerMP)icommandsender);
/*  94 */           icommandsender.addChatMessage((IChatComponent)new ChatComponentText("Construction messages set: on"));
/*  95 */         } else if (astring[1].equals("off")) {
/*  96 */           HelpfulVillagers.network.sendTo((IMessage)new MessageOptionsPacket(EnumMessage.CONSTRUCTION, 0), (EntityPlayerMP)icommandsender);
/*  97 */           icommandsender.addChatMessage((IChatComponent)new ChatComponentText("Construction messages set: off"));
/*  98 */         } else if (astring[1].equals("verbose")) {
/*  99 */           HelpfulVillagers.network.sendTo((IMessage)new MessageOptionsPacket(EnumMessage.CONSTRUCTION, 2), (EntityPlayerMP)icommandsender);
/* 100 */           icommandsender.addChatMessage((IChatComponent)new ChatComponentText("Construction messages set: verbose"));
/*     */         } else {
/* 102 */           icommandsender.addChatMessage((IChatComponent)errorText);
/*     */         } 
/* 104 */       } else if (astring[0].equals("all")) {
/* 105 */         if (astring[1].equals("on")) {
/* 106 */           HelpfulVillagers.network.sendTo((IMessage)new MessageOptionsPacket(EnumMessage.BIRTH, 1), (EntityPlayerMP)icommandsender);
/* 107 */           HelpfulVillagers.network.sendTo((IMessage)new MessageOptionsPacket(EnumMessage.DEATH, 1), (EntityPlayerMP)icommandsender);
/* 108 */           icommandsender.addChatMessage((IChatComponent)new ChatComponentText("All messages set: on"));
/* 109 */         } else if (astring[1].equals("off")) {
/* 110 */           HelpfulVillagers.network.sendTo((IMessage)new MessageOptionsPacket(EnumMessage.BIRTH, 0), (EntityPlayerMP)icommandsender);
/* 111 */           HelpfulVillagers.network.sendTo((IMessage)new MessageOptionsPacket(EnumMessage.DEATH, 0), (EntityPlayerMP)icommandsender);
/* 112 */           icommandsender.addChatMessage((IChatComponent)new ChatComponentText("All messages set: off"));
/* 113 */         } else if (astring[1].equals("verbose")) {
/* 114 */           HelpfulVillagers.network.sendTo((IMessage)new MessageOptionsPacket(EnumMessage.BIRTH, 2), (EntityPlayerMP)icommandsender);
/* 115 */           HelpfulVillagers.network.sendTo((IMessage)new MessageOptionsPacket(EnumMessage.DEATH, 2), (EntityPlayerMP)icommandsender);
/* 116 */           icommandsender.addChatMessage((IChatComponent)new ChatComponentText("All messages set: verbose"));
/*     */         } else {
/* 118 */           icommandsender.addChatMessage((IChatComponent)errorText);
/*     */         } 
/*     */       } else {
/* 121 */         icommandsender.addChatMessage((IChatComponent)errorText);
/*     */       } 
/*     */     } else {
/* 124 */       icommandsender.addChatMessage((IChatComponent)errorText);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
/* 135 */     if (icommandsender instanceof net.minecraft.entity.player.EntityPlayer) {
/* 136 */       return true;
/*     */     }
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */ 	public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring) {
/* 147 */     return (astring.length == 1) ? getListOfStringsMatchingLastWord(astring, new String[] { "death", "birth", "construction", "all" }) : ((astring.length == 2) ? getListOfStringsMatchingLastWord(astring, new String[] { "on", "off", "verbose" }) : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */		public boolean isUsernameIndex(String[] astring, int i) {
/* 155 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   
/*     */
}


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\command\VillagerMessagesCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */