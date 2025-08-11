package object;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class OBJ_Chest extends Entity{
	GamePanel gp;
	public static final String objName = "Chest";
	
	public OBJ_Chest(GamePanel gp) {
		
		super(gp);
		this.gp = gp;
		
		name = objName;
		type = type_obstacle;
		
		image = setup("/objects/chest", gp.tileSize, gp.tileSize);
		image2 = setup("/objects/chest_opened", gp.tileSize, gp.tileSize);
		
		down1 = image;
		collision = true;
		
		solidArea.x = 4;
		solidArea.y = 16;
		solidArea.width = 40;
		solidArea.height = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
	}
	public void setLoot(Entity loot) {
		this.loot = loot;
	}
	
	public void interact() {
		
		gp.gameState = gp.dialogueState;
		
		if (opened == false) {
			gp.playSE(3);
			
			StringBuilder sb = new StringBuilder();
			sb.append("You opened the chest and found " + loot.name + "!");
			
			if (gp.player.canObtainItem(loot) == false) {
				sb.append("\n...But you cannot carry any more items!");
			}
			else {
				sb.append("\nYou obtained " + loot.name + "!");
				down1 = image2;
				opened = true;
			}
			gp.ui.currentDialogue = sb.toString();
					
		}
		else {
			gp.ui.currentDialogue = "It's empty";
		}
	}
}
