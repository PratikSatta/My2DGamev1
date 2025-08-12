package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import main.GamePanel;

public class SaveLoad {

	GamePanel gp;
	public SaveLoad(GamePanel gp) {
		this.gp = gp;
	}
	public void save() {
		try {
			
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("save.dat"));
			
			DataStorage dataStorage = new DataStorage();
			
			//PLAYER STATS
			dataStorage.level = gp.player.level;
			dataStorage.maxLife = gp.player.maxLife;
			dataStorage.life = gp.player.life;
			dataStorage.maxMana = gp.player.maxMana;
			dataStorage.mana = gp.player.mana;
			dataStorage.strength = gp.player.strength;
			dataStorage.dexterity = gp.player.dexterity;
			dataStorage.exp = gp.player.exp;
			dataStorage.nextLevelExp = gp.player.nextLevelExp;
			dataStorage.coin = gp.player.coin;
			
			//PLAYER INVENTORY
			for(int i = 0 ; i < gp.player.inventory.size(); i++) {
				dataStorage.itemNames.add(gp.player.inventory.get(i).name);
				dataStorage.itemAmounts.add(gp.player.inventory.get(i).amount);
			}
			
			//PLAYER EQUIPMENT
			dataStorage.currentWeaponSlot = gp.player.getCurrentWeaponSlot();
			dataStorage.currentShieldSlot = gp.player.getCurrentShieldSlot();
			
			// OBJECTS ON MAP
			dataStorage.mapObjectNames = new String[gp.maxMap][gp.obj[1].length];
			dataStorage.mapObjectWorldX = new int[gp.maxMap][gp.obj[1].length];
			dataStorage.mapObjectWorldY = new int[gp.maxMap][gp.obj[1].length];
			dataStorage.mapObjectLootNames = new String[gp.maxMap][gp.obj[1].length];
			dataStorage.mapObjectOpened = new boolean[gp.maxMap][gp.obj[1].length];
			
			for(int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
				
				for(int i = 0; i < gp.obj[1].length; i++) {
					
					if (gp.obj[mapNum][i] == null) {
						dataStorage.mapObjectNames[mapNum][i] = "NA";
					}
					else {
						dataStorage.mapObjectNames[mapNum][i] = gp.obj[mapNum][i].name;
						dataStorage.mapObjectWorldX[mapNum][i] = gp.obj[mapNum][i].worldX;
						dataStorage.mapObjectWorldY[mapNum][i] = gp.obj[mapNum][i].worldY;
						if (gp.obj[mapNum][i].loot != null) {
							dataStorage.mapObjectLootNames[mapNum][i] = gp.obj[mapNum][i].loot.name;
						}
						dataStorage.mapObjectOpened[mapNum][i] = gp.obj[mapNum][i].opened;
					}
				}
			}
			
			//Write the DataStorage object
			oos.writeObject(dataStorage);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void load() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("save.dat")));
			
			//Read the DataStorage Object
			DataStorage dataStorage = (DataStorage)ois.readObject();
			
			gp.player.level =dataStorage.level;
			gp.player.maxLife = dataStorage.maxLife;
			gp.player.life = dataStorage.life;
			gp.player.maxMana = dataStorage.maxMana;
			gp.player.mana = dataStorage.mana;
			gp.player.dexterity = dataStorage.dexterity;
			gp.player.strength = dataStorage.strength;
			gp.player.exp = dataStorage.exp;
			gp.player.nextLevelExp = dataStorage.nextLevelExp;
			gp.player.coin = dataStorage.coin;
			
			//PLAYER INVENTORY
			gp.player.inventory.clear();
			for (int i = 0; i < dataStorage.itemNames.size(); i++) {
				gp.player.inventory.add(gp.entityGenerator.getObject(dataStorage.itemNames.get(i)));
				gp.player.inventory.get(i).amount = dataStorage.itemAmounts.get(i);
			}
			//PLAYER INVENTORY
			gp.player.currentWeapon = gp.player.inventory.get(dataStorage.currentWeaponSlot);
			gp.player.currentShield = gp.player.inventory.get(dataStorage.currentShieldSlot);
			gp.player.getAttack();
			gp.player.getDefense();
			gp.player.getAttackImage();

			//OBJECTS ON MAP
			for(int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
				for(int i = 0; i < gp.obj[1].length; i++) {
					if (dataStorage.mapObjectNames[mapNum][i].equals("NA")) {
						gp.obj[mapNum][i] = null;
					}
					else {
						gp.obj[mapNum][i] = gp.entityGenerator.getObject(dataStorage.mapObjectNames[mapNum][i]);
						gp.obj[mapNum][i].worldX = dataStorage.mapObjectWorldX[mapNum][i];
						gp.obj[mapNum][i].worldY = dataStorage.mapObjectWorldY[mapNum][i];
						if (dataStorage.mapObjectLootNames[mapNum][i] != null) {
							gp.obj[mapNum][i].setLoot(gp.entityGenerator.getObject(dataStorage.mapObjectLootNames[mapNum][i]));
						}
						gp.obj[mapNum][i].opened = dataStorage.mapObjectOpened[mapNum][i];
						if (gp.obj[mapNum][i].opened == true) {
							gp.obj[mapNum][i].down1 = gp.obj[mapNum][i].image2;
						}
					}
				}
			}
			ois.close();
		} 
		catch (Exception e) {
			System.out.println("Load Exception");
		}
	}
}
